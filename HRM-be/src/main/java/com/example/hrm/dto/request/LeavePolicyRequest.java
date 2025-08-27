package com.example.hrm.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeavePolicyRequest {
    private String policyName;
    private Integer maxDays;
    private String description;
    private String positionName;
    private String roleName;
}
