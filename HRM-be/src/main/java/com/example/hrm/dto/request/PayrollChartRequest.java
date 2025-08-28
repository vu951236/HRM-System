package com.example.hrm.dto.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollChartRequest {
    private Integer year;
    private Integer quarter;
    private Long departmentId;
}
