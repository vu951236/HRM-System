package com.example.hrm.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeavePolicyResponse {
    private Integer id;
    private String policyName;
    private Integer maxDays;
    private String description;
    private String positionName;
    private String roleName;
    private Boolean isDelete;
}
