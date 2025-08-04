package com.example.hrm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
    @JoinColumn(name = "user_id")
    private User user;

    private Integer month;
    private Integer year;

    @Column(name = "base_salary")
    private BigDecimal baseSalary;

    private BigDecimal bonus;
    private BigDecimal deduction;

    @Column(name = "final_salary")
    private BigDecimal finalSalary;

    private String status;

    @Column(name = "generated_at")
    private LocalDateTime generatedAt;
}
