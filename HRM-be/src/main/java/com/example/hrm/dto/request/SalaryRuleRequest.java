package com.example.hrm.dto.request;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryRuleRequest {
    private String ruleName;
    private String description;
    private String formula;
    private String ruleType;
    private Boolean active;
}
