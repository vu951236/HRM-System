package com.example.hrm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "contract_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    @Column(name = "changed_at", nullable = false)
    private LocalDateTime changedAt;

    @Column(name = "old_contract_type")
    private String oldContractType;

    @Column(name = "new_contract_type")
    private String newContractType;

    @Column(name = "old_start_date")
    private LocalDate oldStartDate;

    @Column(name = "new_start_date")
    private LocalDate newStartDate;

    @Column(name = "old_end_date")
    private LocalDate oldEndDate;

    @Column(name = "new_end_date")
    private LocalDate newEndDate;

    @Column(name = "old_salary")
    private BigDecimal oldSalary;

    @Column(name = "new_salary")
    private BigDecimal newSalary;

    @Column(name = "old_status")
    private String oldStatus;

    @Column(name = "new_status")
    private String newStatus;

    @Column(name = "note")
    private String note;
}
