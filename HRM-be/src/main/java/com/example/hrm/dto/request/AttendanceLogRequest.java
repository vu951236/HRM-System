package com.example.hrm.dto.request;

import com.example.hrm.entity.AttendanceLog.AttendanceMethod;
import com.example.hrm.entity.AttendanceLog.AttendanceStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceLogRequest {
    private Integer employeeId;
    private Integer workScheduleId;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkInTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime checkOutTime;

    private AttendanceStatus status;
    private String deviceId;
    private AttendanceMethod method;

}
