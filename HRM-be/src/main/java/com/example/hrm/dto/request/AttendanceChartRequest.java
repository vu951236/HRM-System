package com.example.hrm.dto.request;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceChartRequest {
    private LocalDate startDate;
    private LocalDate endDate;
    private Long departmentId;
}
