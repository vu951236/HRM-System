package com.example.hrm.dto.response;

import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestResponse {
    private Integer id;

    private Integer userId;
    private Integer employeeId;
    private String employeeCode;
    private String employeeFullName;

    private Integer leavePolicyId;
    private String leavePolicyName;

    private String employeePositionName;

    private LocalDate startDate;
    private LocalDate endDate;

    private String reason;
    private String status;

    private LocalDateTime createdAt;

    private Integer approvedById;
    private String approvedByName;
    private Boolean isDelete;
}
