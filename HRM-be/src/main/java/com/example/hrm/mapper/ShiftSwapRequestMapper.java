package com.example.hrm.mapper;

import com.example.hrm.dto.response.ShiftSwapRequestResponse;
import com.example.hrm.entity.ShiftSwapRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShiftSwapRequestMapper {

    @Mapping(target = "requesterId", source = "requester.id")
    @Mapping(target = "userId", source = "requester.user.id")
    @Mapping(target = "requesterPositionName", source = "requester.position.name")
    @Mapping(target = "requesterCode", source = "requester.employeeCode")
    @Mapping(target = "requesterFullName", source = "requester.profile.fullName")
    @Mapping(target = "requestedShiftId", source = "requestedShift.id")
    @Mapping(target = "requestedShiftName", source = "requestedShift.shift.name")
    @Mapping(target = "requestedShiftTime", source = "requestedShift.workDate")
    @Mapping(target = "targetEmployeeId", source = "targetEmployee.id")
    @Mapping(target = "targetEmployeeCode", source = "targetEmployee.employeeCode")
    @Mapping(target = "targetEmployeeFullName", source = "targetEmployee.profile.fullName")
    @Mapping(target = "targetShiftId", source = "targetShift.id")
    @Mapping(target = "targetShiftName", source = "targetShift.shift.name")
    @Mapping(target = "targetShiftTime", source = "targetShift.workDate")
    @Mapping(target = "approvedById", source = "approvedBy.id")
    @Mapping(target = "approvedByFullName", source = "approvedBy.profile.fullName")
    ShiftSwapRequestResponse toResponse(ShiftSwapRequest entity);
}

