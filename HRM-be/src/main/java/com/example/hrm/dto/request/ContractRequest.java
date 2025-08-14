package com.example.hrm.dto.request;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractRequest {
    private Integer userId;
    private String contractTypeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal salary;
}

