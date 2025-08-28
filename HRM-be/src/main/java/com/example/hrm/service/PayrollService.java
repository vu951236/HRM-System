package com.example.hrm.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.hrm.dto.response.PayrollResponse;
import com.example.hrm.entity.*;
import com.example.hrm.repository.*;
import com.example.hrm.mapper.PayrollMapper;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayrollService {

    private final AttendanceLogRepository attendanceLogRepository;
    private final OvertimeRecordRepository overtimeRepository;
    private final LeaveRequestRepository leaveRequestRepository;
    private final SalaryRuleRepository salaryRuleRepository;
    private final PayrollRepository payrollRepository;
    private final PayrollMapper payrollMapper;
    private final PermissionChecker permissionChecker;
    private final EmployeeRecordRepository employeeRecordRepository;

    @Transactional
    public PayrollResponse calculatePayroll(Integer employeeId, Integer month, Integer year) {
        EmployeeRecord employee = employeeRecordRepository
                .findByIdAndIsDeleteFalse(employeeId)
                .orElseThrow(() -> new RuntimeException("EmployeeRecord not found for id: " + employeeId));

        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate monthEnd = monthStart.withDayOfMonth(monthStart.lengthOfMonth());

        List<AttendanceLog> logs = attendanceLogRepository
                .findByEmployee_IdAndLogDateBetween(employeeId, monthStart, monthEnd);

        int totalWorkDays = (int) logs.stream()
                .filter(l -> l.getStatus() == AttendanceLog.AttendanceStatus.CHECKED_IN
                        || l.getStatus() == AttendanceLog.AttendanceStatus.CHECKED_OUT)
                .map(AttendanceLog::getLogDate)
                .distinct()
                .count();

        BigDecimal workedHours = logs.stream()
                .filter(l -> l.getStatus() == AttendanceLog.AttendanceStatus.CHECKED_IN
                        || l.getStatus() == AttendanceLog.AttendanceStatus.CHECKED_OUT)
                .map(l -> {
                    if (l.getCheckOutTime() != null && l.getCheckInTime() != null) {
                        return BigDecimal.valueOf(
                                java.time.Duration.between(l.getCheckInTime(), l.getCheckOutTime()).toMinutes() / 60.0
                        );
                    } else return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalLateMinutes = logs.stream()
                .filter(l -> l.getStatus() == AttendanceLog.AttendanceStatus.CHECKED_IN
                        || l.getStatus() == AttendanceLog.AttendanceStatus.CHECKED_OUT)
                .map(l -> {
                    LocalTime shiftStart = l.getWorkSchedule().getShift().getStartTime();
                    if (l.getCheckInTime() != null && l.getCheckInTime().toLocalTime().isAfter(shiftStart)) {
                        long minutesLate = java.time.Duration.between(shiftStart, l.getCheckInTime().toLocalTime()).toMinutes();
                        return BigDecimal.valueOf(minutesLate);
                    }
                    return BigDecimal.ZERO;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalAbsentDays = (int) logs.stream()
                .filter(l -> l.getStatus() == AttendanceLog.AttendanceStatus.ABSENT)
                .map(AttendanceLog::getLogDate)
                .distinct()
                .count();

        int totalLateDays = (int) logs.stream()
                .filter(l -> l.getStatus() == AttendanceLog.AttendanceStatus.CHECKED_IN
                        || l.getStatus() == AttendanceLog.AttendanceStatus.CHECKED_OUT)
                .filter(l -> {
                    LocalTime shiftStart = l.getWorkSchedule().getShift().getStartTime();
                    return l.getCheckInTime() != null && l.getCheckInTime().toLocalTime().isAfter(shiftStart);
                })
                .map(AttendanceLog::getLogDate)
                .distinct()
                .count();

        BigDecimal totalOvertimeHours = overtimeRepository
                .findByEmployeeIdAndDateBetween(employeeId, monthStart, monthEnd)
                .stream()
                .filter(o -> o.getStatus() == OvertimeRecord.Status.approved)
                .map(o -> BigDecimal.valueOf(java.time.Duration.between(o.getStartTime(), o.getEndTime()).toMinutes() / 60.0))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalLeaveDays = leaveRequestRepository.findByEmployee_IdAndStartDateBetween(employeeId, monthStart, monthEnd)
                .stream()
                .filter(l -> l.getStatus().equals("APPROVED"))
                .mapToInt(l -> (int) ChronoUnit.DAYS.between(l.getStartDate(), l.getEndDate()) + 1)
                .sum();

        List<SalaryRule> rules = salaryRuleRepository.findByActiveTrueAndIsDeleteFalse();

        int totalWorkDaysAdjusted = totalWorkDays + totalLeaveDays;

        BigDecimal baseSalary = rules.stream()
                .filter(r -> r.getRuleType() == SalaryRule.RuleType.BASE)
                .map(r -> evaluateFormula(r.getFormula(), workedHours))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal otherDeductions = rules.stream()
                .filter(r -> r.getRuleType() == SalaryRule.RuleType.DEDUCTION)
                .map(r -> evaluateFormula(r.getFormula(), baseSalary))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal lateDeduction = rules.stream()
                .filter(r -> r.getRuleType() == SalaryRule.RuleType.LATE)
                .map(r -> evaluateFormula(r.getFormula(), totalLateMinutes))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal overtimeSalary = rules.stream()
                .filter(r -> r.getRuleType() == SalaryRule.RuleType.OVERTIME)
                .map(r -> evaluateFormula(r.getFormula(), totalOvertimeHours))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal leaveDeduction = rules.stream()
                .filter(r -> r.getRuleType() == SalaryRule.RuleType.LEAVE)
                .map(r -> evaluateFormula(r.getFormula(), totalAbsentDays))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal bonus = rules.stream()
                .filter(r -> r.getRuleType() == SalaryRule.RuleType.BONUS)
                .map(r -> evaluateFormula(r.getFormula(), totalWorkDaysAdjusted))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal finalSalary = baseSalary.add(overtimeSalary).add(bonus)
                .subtract(leaveDeduction)
                .subtract(otherDeductions)
                .subtract(lateDeduction);

        Payroll payroll = Payroll.builder()
                .employee(employee)
                .month(month)
                .year(year)
                .totalWorkDays(totalWorkDays)
                .workedHours(workedHours)
                .totalLeaveDays(totalLeaveDays)
                .totalAbsentDays(totalAbsentDays)
                .baseSalary(baseSalary)
                .overtimeSalary(overtimeSalary)
                .leaveDeduction(leaveDeduction)
                .bonus(bonus)
                .deduction(otherDeductions)
                .finalSalary(finalSalary)
                .totalLateDays(totalLateDays)
                .status(Payroll.PayrollStatus.GENERATED)
                .generatedAt(LocalDateTime.now())
                .build();

        payrollRepository.save(payroll);

        return payrollMapper.toResponse(payroll);
    }

    private BigDecimal evaluateFormula(String formula, Number value) {
        if (formula == null || formula.isEmpty()) return BigDecimal.ZERO;

        try {
            String trimmedFormula = formula.trim();

            if (trimmedFormula.contains("?")) {
                String[] parts = trimmedFormula.split("\\?");
                String condition = parts[0].trim(); // value >= 21
                String[] values = parts[1].split(":"); // [1000000 , 0]

                double trueValue = Double.parseDouble(values[0].trim());
                double falseValue = Double.parseDouble(values[1].trim());

                boolean condResult = false;
                if (condition.contains(">=")) {
                    String[] condParts = condition.split(">=");
                    double left = Double.parseDouble(condParts[0].trim().replace("value", value.toString()));
                    double right = Double.parseDouble(condParts[1].trim());
                    condResult = left >= right;
                } else if (condition.contains("<=")) {
                    String[] condParts = condition.split("<=");
                    double left = Double.parseDouble(condParts[0].trim().replace("value", value.toString()));
                    double right = Double.parseDouble(condParts[1].trim());
                    condResult = left <= right;
                } else if (condition.contains("==")) {
                    String[] condParts = condition.split("==");
                    double left = Double.parseDouble(condParts[0].trim().replace("value", value.toString()));
                    double right = Double.parseDouble(condParts[1].trim());
                    condResult = left == right;
                } else {
                    throw new RuntimeException("Unsupported condition: " + condition);
                }

                return BigDecimal.valueOf(condResult ? trueValue : falseValue);
            }

            Expression e = new ExpressionBuilder(trimmedFormula)
                    .variable("value")
                    .build()
                    .setVariable("value", value.doubleValue());

            double result = e.evaluate();
            return BigDecimal.valueOf(result);

        } catch (Exception ex) {
            throw new RuntimeException("Error evaluating formula: " + formula, ex);
        }
    }

    @Transactional
    public PayrollResponse approvePayroll(Integer id) {
        Payroll payroll = payrollRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Payroll not found with id: " + id));

        permissionChecker.checkRequestPermission(payroll);

        if (payroll.getStatus() != Payroll.PayrollStatus.GENERATED) {
            throw new RuntimeException("Only GENERATED payrolls can be approved");
        }

        payroll.setStatus(Payroll.PayrollStatus.PAID);
        payroll.setApprovedBy(permissionChecker.getCurrentUser());
        payroll.setApprovedAt(LocalDateTime.now());

        return payrollMapper.toResponse(payrollRepository.save(payroll));
    }

    public List<PayrollResponse> getAllPayrolls() {
        User currentUser = permissionChecker.getCurrentUser();
        String role = currentUser.getRole().getName();
        EmployeeRecord currentRecord = null;

        if (!"admin".equalsIgnoreCase(role)) {
            currentRecord = employeeRecordRepository
                    .findByUser_IdAndIsDeleteFalse(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("EmployeeRecord not found"));
        }

        List<Payroll> allPayrolls = payrollRepository.findAll();

        List<Payroll> filteredPayrolls = permissionChecker.filterRecordsByPermission(
                allPayrolls, currentUser, currentRecord
        );

        return filteredPayrolls.stream()
                .map(payrollMapper::toResponse)
                .toList();
    }

}
