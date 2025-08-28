package com.example.hrm.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.example.hrm.entity.Payroll.PayrollStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollResponse {
    private Integer id;
    private Integer userId;
    private String employeeName;
    private Integer month;
    private Integer year;
    private Integer totalWorkDays;
    private BigDecimal totalOvertimeHours;
    private Integer totalLeaveDays;
    private BigDecimal baseSalary;
    private BigDecimal overtimeSalary;
    private BigDecimal leaveDeduction;
    private BigDecimal bonus;
    private BigDecimal deduction;
    private BigDecimal finalSalary;
    private Integer totalAbsentDays;
    private Integer totalLateDays;
    private BigDecimal workedHours;
    private PayrollStatus status;
    private LocalDateTime generatedAt;
}
