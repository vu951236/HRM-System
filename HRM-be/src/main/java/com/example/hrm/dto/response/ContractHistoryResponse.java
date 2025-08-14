package com.example.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractHistoryResponse {
    private Integer id;
    private LocalDateTime changedAt;
    private String oldContractType;
    private String newContractType;
    private LocalDate oldStartDate;
    private LocalDate newStartDate;
    private LocalDate oldEndDate;
    private LocalDate newEndDate;
    private BigDecimal oldSalary;
    private BigDecimal newSalary;
    private String oldStatus;
    private String newStatus;
    private String note;
}
