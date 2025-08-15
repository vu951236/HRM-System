package com.example.hrm.dto.request;

import lombok.Data;

@Data
public class ShiftSwapRequestRequest {
    private Integer requesterId;
    private Integer requestedShiftId;
    private Integer targetEmployeeId;
    private Integer targetShiftId;
    private String reason;
}