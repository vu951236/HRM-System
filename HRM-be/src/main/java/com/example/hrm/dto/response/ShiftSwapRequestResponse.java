package com.example.hrm.dto.response;

import com.example.hrm.entity.ShiftSwapRequest;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ShiftSwapRequestResponse {
    private Integer id;
    private Integer userId;
    private Integer requesterId;
    private String requesterCode;
    private String requesterPositionName;
    private String requesterFullName;
    private Integer requestedShiftId;
    private String requestedShiftName;
    private String requestedShiftTime;
    private Integer targetEmployeeId;
    private String targetEmployeeCode;
    private String targetEmployeeFullName;
    private Integer targetShiftId;
    private String targetShiftName;
    private String targetShiftTime;
    private Integer approvedById;
    private String approvedByFullName;
    private String reason;
    private ShiftSwapRequest.Status status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isDelete;

}
