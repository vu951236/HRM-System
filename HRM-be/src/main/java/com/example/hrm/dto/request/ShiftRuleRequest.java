package com.example.hrm.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftRuleRequest {
    private String ruleName;
    private String description;
    private Integer maxHoursPerDay;
    private Boolean allowOvertime;
    private Float nightShiftMultiplier;
}
