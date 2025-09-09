package com.example.hrm.dto.response;

import com.example.hrm.entity.AttendanceLog.AttendanceMethod;
import com.example.hrm.entity.AttendanceLog.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceLogResponse {
    private Integer id;
    private Integer employeeId;
    private String employeeCode;
    private String userFullName;
    private Integer workScheduleId;
    private String shiftName;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
    private AttendanceStatus status;
    private String deviceId;
    private String deviceName;
    private AttendanceMethod checkInMethod;
    private AttendanceMethod checkOutMethod;
    private LocalDate logDate;
    private String error;

}
