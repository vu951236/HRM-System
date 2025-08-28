package com.example.hrm.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollChartResponse {
    private String name;
    private String period;
    private BigDecimal baseSalaryTotal;
    private BigDecimal bonusTotal;
    private BigDecimal allowanceTotal;
    private BigDecimal deductionTotal;
    private BigDecimal finalSalaryTotal;
}
