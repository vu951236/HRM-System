package com.example.hrm.dto.request;

import lombok.Data;

@Data
public class WorkScheduleTemplateRequest {
    private String templateName;
    private Integer departmentId;
    private String shiftPattern;
    private Integer createdById;
    private Boolean isDelete;
}
