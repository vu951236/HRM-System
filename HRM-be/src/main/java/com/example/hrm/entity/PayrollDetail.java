package com.example.hrm.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "payroll_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "payroll_id", nullable = false)
    private Payroll payroll;

    @Enumerated(EnumType.STRING)
    @Column(name = "detail_type", nullable = false)
    private DetailType detailType;

    @Column(name = "description")
    private String description;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    public enum DetailType {
        BASE,
        BONUS,
        DEDUCTION,
        OVERTIME,
        LEAVE
    }
}
