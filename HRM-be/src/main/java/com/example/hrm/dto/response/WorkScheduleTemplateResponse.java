package com.example.hrm.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WorkScheduleTemplateResponse {
    private Integer id;
    private String templateName;
    private Integer departmentId;
    private String departmentName;
    private String shiftPattern;
    private Integer createdById;
    private String createdByName;
    private LocalDateTime createdAt;
}
