package com.example.hrm.dto.response;

import com.example.hrm.entity.ShiftSwapRequest;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShiftSwapRequestResponse {
    private Integer id;
    private Integer requesterId;
    private Integer requestedShiftId;
    private Integer targetEmployeeId;
    private Integer targetShiftId;
    private String reason;
    private ShiftSwapRequest.Status status;
    private Integer approvedById;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}