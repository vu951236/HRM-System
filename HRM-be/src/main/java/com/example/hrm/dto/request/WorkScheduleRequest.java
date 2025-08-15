package com.example.hrm.dto.request;

import lombok.Data;
import java.time.LocalDate;

@Data
public class WorkScheduleRequest {
    private Integer employeeId;
    private Integer shiftId;
    private LocalDate workDate;
    private String status;
    private String note;
}
