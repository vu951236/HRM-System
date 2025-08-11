package com.example.hrm.dto.request;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRecordRequest {

    private Integer userId;

    private String departmentName;
    private String positionName;
    private String employmentTypeName;

    private Integer supervisorId;

    private LocalDate hireDate;
    private LocalDate terminationDate;
    private String workLocation;
    private String note;
}
