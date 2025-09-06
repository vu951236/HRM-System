package com.example.hrm.service;

import com.example.hrm.dto.request.AttendanceChartRequest;
import com.example.hrm.dto.response.AttendanceChartResponse;
import com.example.hrm.entity.AttendanceLog;
import com.example.hrm.repository.AttendanceLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import com.example.hrm.dto.request.PayrollChartRequest;
import com.example.hrm.dto.response.PayrollChartResponse;
import com.example.hrm.entity.Payroll;
import com.example.hrm.repository.PayrollRepository;
import com.example.hrm.dto.request.LeaveOvertimeChartRequest;
import com.example.hrm.dto.response.LeaveOvertimeChartResponse;
import com.example.hrm.entity.LeaveRequest;
import com.example.hrm.entity.OvertimeRecord;
import com.example.hrm.repository.LeaveRequestRepository;
import com.example.hrm.repository.OvertimeRecordRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import com.example.hrm.dto.request.EmployeeContractChartRequest;
import com.example.hrm.dto.response.EmployeeContractChartResponse;
import com.example.hrm.repository.EmployeeRecordRepository;
import com.example.hrm.repository.ContractRepository;

import java.util.*;
import java.math.BigDecimal;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChartService {

    private final AttendanceLogRepository repository;
    private final PayrollRepository payrollRepository;
    private final LeaveRequestRepository leaveRepo;
    private final OvertimeRecordRepository overtimeRepo;
    private final EmployeeRecordRepository employeeRecordRepository;
    private final ContractRepository contractRepository;

    @PreAuthorize("hasAnyRole('admin','hr')")
    public List<EmployeeContractChartResponse> getEmployeeCountByDepartment(EmployeeContractChartRequest request) {
        return employeeRecordRepository.countEmployeeByDepartment(request.getDepartmentId());
    }

    @PreAuthorize("hasAnyRole('admin','hr')")
    public List<EmployeeContractChartResponse> getContractCountByType(EmployeeContractChartRequest request) {
        return contractRepository.countContractByType(request.getContractTypeId());
    }

    @PreAuthorize("hasAnyRole('admin','hr')")
    public List<EmployeeContractChartResponse> getExpiringContracts(EmployeeContractChartRequest request) {
        LocalDate expiryDate = LocalDate.now().plusDays(request.getDaysUntilExpiry() != null ? request.getDaysUntilExpiry() : 30);
        return contractRepository.countContractExpiringBefore(expiryDate);
    }

    @PreAuthorize("hasAnyRole('admin','hr')")
    public List<LeaveOvertimeChartResponse> getLeaveOvertimeChart(LeaveOvertimeChartRequest request) {

        Map<String, LeaveOvertimeChartResponse> resultMap = new HashMap<>();

        List<LeaveRequest> leaves = leaveRepo.findByDateRangeAndDepartment(
                request.getStartDate(), request.getEndDate(), request.getDepartmentId());

        for (LeaveRequest leave : leaves) {
            String key = request.getDepartmentId() != null ?
                    leave.getEmployee().getDepartment().getName() :
                    leave.getEmployee().getProfile().getFullName();

            LeaveOvertimeChartResponse resp = resultMap.getOrDefault(key,
                    LeaveOvertimeChartResponse.builder()
                            .name(key)
                            .leaveDays(0.0)
                            .overtimeHours(0.0)
                            .build());

            double days = (double) ChronoUnit.DAYS.between(
                    leave.getStartDate().isBefore(request.getStartDate()) ? request.getStartDate() : leave.getStartDate(),
                    leave.getEndDate().isAfter(request.getEndDate()) ? request.getEndDate() : leave.getEndDate()
            ) + 1;

            resp.setLeaveDays(resp.getLeaveDays() + days);
            resultMap.put(key, resp);
        }

        List<OvertimeRecord> overtimes = overtimeRepo.findByDateRangeAndDepartment(
                request.getStartDate(), request.getEndDate(), request.getDepartmentId());

        for (OvertimeRecord overtime : overtimes) {
            String key = request.getDepartmentId() != null ?
                    overtime.getEmployee().getDepartment().getName() :
                    overtime.getEmployee().getProfile().getFullName();

            LeaveOvertimeChartResponse resp = resultMap.getOrDefault(key,
                    LeaveOvertimeChartResponse.builder()
                            .name(key)
                            .leaveDays(0.0)
                            .overtimeHours(0.0)
                            .build());

            double hours = ChronoUnit.MINUTES.between(overtime.getStartTime(), overtime.getEndTime()) / 60.0;
            resp.setOvertimeHours(resp.getOvertimeHours() + hours);
            resultMap.put(key, resp);
        }

        return new ArrayList<>(resultMap.values());
    }

    @PreAuthorize("hasAnyRole('admin','hr')")
    public List<PayrollChartResponse> getPayrollChart(PayrollChartRequest request) {
        List<Payroll> payrolls = payrollRepository.findByYearAndDepartment(
                request.getYear(), request.getDepartmentId());

        Map<String, PayrollChartResponse> resultMap = new HashMap<>();

        for (Payroll payroll : payrolls) {
            String key;
            if (request.getDepartmentId() != null) {
                key = payroll.getEmployee().getDepartment().getName();
            } else {
                key = "Tổng";
            }

            String period;
            if (request.getQuarter() != null) {
                int quarter = request.getQuarter();
                int month = payroll.getMonth();
                int payrollQuarter = ((month - 1) / 3) + 1;
                if (payrollQuarter != quarter) continue;

                period = "Q" + quarter;
            } else {
                period = payroll.getYear() + "-" + String.format("%02d", payroll.getMonth());
            }


            String mapKey = key + "-" + period;

            PayrollChartResponse resp = resultMap.getOrDefault(mapKey,
                    PayrollChartResponse.builder()
                            .name(key)
                            .period(period)
                            .baseSalaryTotal(BigDecimal.ZERO)
                            .bonusTotal(BigDecimal.ZERO)
                            .allowanceTotal(BigDecimal.ZERO)
                            .deductionTotal(BigDecimal.ZERO)
                            .finalSalaryTotal(BigDecimal.ZERO)
                            .build());

            resp.setBaseSalaryTotal(resp.getBaseSalaryTotal().add(payroll.getBaseSalary()));
            resp.setBonusTotal(resp.getBonusTotal().add(payroll.getBonus() != null ? payroll.getBonus() : BigDecimal.ZERO));
            resp.setDeductionTotal(resp.getDeductionTotal().add(payroll.getDeduction() != null ? payroll.getDeduction() : BigDecimal.ZERO));
            resp.setFinalSalaryTotal(resp.getFinalSalaryTotal().add(payroll.getFinalSalary() != null ? payroll.getFinalSalary() : BigDecimal.ZERO));


            resultMap.put(mapKey, resp);
        }

        return new ArrayList<>(resultMap.values())
                .stream()
                .sorted(Comparator.comparing(PayrollChartResponse::getPeriod))
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasAnyRole('admin','hr')")
    public List<AttendanceChartResponse> getAttendanceChart(AttendanceChartRequest request) {
        List<AttendanceLog> logs = repository.findByDateRangeAndDepartment(
                request.getStartDate(), request.getEndDate(), request.getDepartmentId());

        Map<String, AttendanceChartResponse> resultMap = new HashMap<>();

        for (AttendanceLog log : logs) {
            String key;
            if (request.getDepartmentId() != null) {
                key = log.getEmployee().getDepartment().getName();
            } else {
                key = log.getEmployee().getUser().getProfile().getFullName();
            }

            AttendanceChartResponse resp = resultMap.getOrDefault(key,
                    AttendanceChartResponse.builder()
                            .name(key)
                            .lateCount(0L)
                            .earlyLeaveCount(0L)
                            .absentCount(0L)
                            .build());

            if (log.getStatus() == AttendanceLog.AttendanceStatus.ABSENT) {
                resp.setAbsentCount(resp.getAbsentCount() + 1);
            } else {
                if (log.getCheckInTime() != null && log.getCheckInTime().isAfter(log.getCheckInTime().toLocalDate().atTime(log.getWorkSchedule().getShift().getStartTime()))) {
                    resp.setLateCount(resp.getLateCount() + 1);
                }
                if (log.getCheckOutTime() != null && log.getCheckOutTime().isBefore(log.getCheckOutTime().toLocalDate().atTime(log.getWorkSchedule().getShift().getEndTime()))) {
                    resp.setEarlyLeaveCount(resp.getEarlyLeaveCount() + 1);
                }
            }

            resultMap.put(key, resp);
        }

        return new ArrayList<>(resultMap.values());
    }

}
