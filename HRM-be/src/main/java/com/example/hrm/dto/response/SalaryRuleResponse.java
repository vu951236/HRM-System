package com.example.hrm.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryRuleResponse {
    private Integer id;
    private String ruleName;
    private String description;
    private String formula;
    private String ruleType;
    private Boolean active;
}
