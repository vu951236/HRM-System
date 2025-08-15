package com.example.hrm.mapper;

import com.example.hrm.dto.response.ShiftSwapRequestResponse;
import com.example.hrm.entity.ShiftSwapRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShiftSwapRequestMapper {

    @Mapping(target = "requesterId", source = "requester.id")
    @Mapping(target = "requestedShiftId", source = "requestedShift.id")
    @Mapping(target = "targetEmployeeId", source = "targetEmployee.id")
    @Mapping(target = "targetShiftId", source = "targetShift.id")
    @Mapping(target = "approvedById", source = "approvedBy.id")
    ShiftSwapRequestResponse toResponse(ShiftSwapRequest entity);
}
