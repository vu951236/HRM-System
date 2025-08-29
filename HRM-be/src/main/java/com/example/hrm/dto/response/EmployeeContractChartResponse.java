package com.example.hrm.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeContractChartResponse {
    private String label;
    private Long count;

}
