package com.example.hrm.scheduler;

import com.example.hrm.service.EmployeeRecordService;
import com.example.hrm.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PayrollScheduler {

    private final PayrollService payrollService;
    private final EmployeeRecordService employeeService;

    @Scheduled(cron = "0 0 1 1 * ?")
    public void generateMonthlyPayroll() {
        LocalDate now = LocalDate.now();
        int month = now.minusMonths(1).getMonthValue();
        int year = now.minusMonths(1).getYear();

        List<String> employeeCodes = employeeService.getActiveEmployeeCodes();
        for (String employeeCode : employeeCodes) {
            payrollService.calculatePayrollByCode(employeeCode, month, year);
        }

        System.out.println("Payrolls for month " + month + "/" + year + " generated successfully.");
    }
}
