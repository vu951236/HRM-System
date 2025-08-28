package com.example.hrm.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveOvertimeChartResponse {
    private String name;
    private Double leaveDays;
    private Double overtimeHours;
}
