package com.example.hrm.dto.request;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequestRequest {
    private Integer policyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private String reason;
}
