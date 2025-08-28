package com.example.hrm.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceChartResponse {
    private String name;
    private Long lateCount;
    private Long earlyLeaveCount;
    private Long absentCount;
}
