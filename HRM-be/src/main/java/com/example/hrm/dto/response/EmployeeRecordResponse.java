package com.example.hrm.dto.response;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeRecordResponse {

    private Integer id;
    private String employeeCode;

    private String userName;
    private String profileName;
    private String departmentName;
    private String positionName;
    private String employmentTypeName;
    private String supervisorName;

    private LocalDate hireDate;
    private LocalDate terminationDate;
    private String workLocation;
    private String note;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
