package com.example.hrm.dto.response;

import com.example.hrm.entity.OvertimeRecord;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class OvertimeRecordResponse {
    private Integer id;
    private Integer userId;
    private Integer employeeId;
    private String employeeCode;
    private String employeePositionName;
    private String employeeFullName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String reason;
    private OvertimeRecord.Status status;
    private Integer approvedById;
    private String approvedByFullName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDelete;
}
