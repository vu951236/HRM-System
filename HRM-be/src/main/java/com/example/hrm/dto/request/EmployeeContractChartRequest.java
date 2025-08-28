package com.example.hrm.dto.request;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeContractChartRequest {
    private Integer departmentId;
    private Integer contractTypeId;
    private Integer daysUntilExpiry;
    private LocalDate startDate;
    private LocalDate endDate;
}

