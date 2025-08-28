package com.example.hrm.dto.request;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollRequest {
    private Integer userId;
    private Integer month;
    private Integer year;

    private BigDecimal baseSalary;
    private BigDecimal bonus;
    private BigDecimal deduction;
}
