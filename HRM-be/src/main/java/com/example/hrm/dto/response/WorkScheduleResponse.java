package com.example.hrm.dto.response;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class WorkScheduleResponse {
    private Integer id;
    private Integer employeeId;
    private String employeeName;
    private Integer shiftId;
    private String shiftName;
    private Integer shiftRuleId;
    private String shiftRuleName;
    private LocalDate workDate;
    private String status;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
