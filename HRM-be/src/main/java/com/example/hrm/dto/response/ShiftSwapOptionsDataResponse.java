package com.example.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class ShiftSwapOptionsDataResponse {
    private List<WorkScheduleDataResponse> requesterShifts;
    private List<EmployeeRecordDataResponse> targetEmployees;
    private List<WorkScheduleDataResponse> targetShifts;
}

