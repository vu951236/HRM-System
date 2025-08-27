package com.example.hrm.mapper;

import com.example.hrm.dto.response.LeaveRequestResponse;
import com.example.hrm.entity.LeaveRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LeaveRequestMapper {

    LeaveRequestMapper INSTANCE = Mappers.getMapper(LeaveRequestMapper.class);

    @Mapping(source = "employee.user.id", target = "userId")
    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.position.name", target = "employeePositionName")
    @Mapping(source = "employee.employeeCode", target = "employeeCode")
    @Mapping(source = "employee.profile.fullName", target = "employeeFullName")
    @Mapping(source = "leavePolicy.id", target = "leavePolicyId")
    @Mapping(source = "leavePolicy.policyName", target = "leavePolicyName")
    @Mapping(source = "approvedBy.id", target = "approvedById")
    @Mapping(source = "approvedBy.profile.fullName", target = "approvedByName")
    @Mapping(source = "isDelete", target = "isDelete")
    LeaveRequestResponse toResponse(LeaveRequest entity);
}

