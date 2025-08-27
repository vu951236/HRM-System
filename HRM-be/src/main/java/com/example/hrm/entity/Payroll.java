package com.example.hrm.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "payrolls")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeRecord employee;

    private Integer month;
    private Integer year;

    @Column(name = "total_work_days")
    private Integer totalWorkDays;

    @Column(name = "total_overtime_hours")
    private BigDecimal totalOvertimeHours;

    @Column(name = "total_leave_days")
    private Integer totalLeaveDays;

    @Column(name = "total_absent_days")
    private Integer totalAbsentDays;

    @Column(name = "total_late_days")
    private Integer totalLateDays;

    @Column(name = "worked_hours")
    private BigDecimal workedHours;

    @Column(name = "base_salary")
    private BigDecimal baseSalary;

    @Column(name = "overtime_salary")
    private BigDecimal overtimeSalary;

    @Column(name = "leave_deduction")
    private BigDecimal leaveDeduction;

    private BigDecimal bonus;
    private BigDecimal deduction;

    @Column(name = "final_salary")
    private BigDecimal finalSalary;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private PayrollStatus status;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;

    @OneToMany(mappedBy = "payroll", cascade = CascadeType.ALL)
    private List<PayrollDetail> details;

    public enum PayrollStatus {
        GENERATED,
        PAID,
        PENDING
    }
}
