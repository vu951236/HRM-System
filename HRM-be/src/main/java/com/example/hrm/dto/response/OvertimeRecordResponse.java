package com.example.hrm.dto.response;

import com.example.hrm.entity.OvertimeRecord;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;

@Data
public class OvertimeRecordResponse {
    private Integer id;
    private Integer employeeId;
    private String employeeName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
    private OvertimeRecord.Status status;
    private Integer approvedById;
    private String approvedByName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
