package com.example.hrm.mapper;

import com.example.hrm.dto.response.OvertimeRecordResponse;
import com.example.hrm.entity.OvertimeRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OvertimeRecordMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.user.id", target = "userId")
    @Mapping(source = "employee.employeeCode", target = "employeeCode")
    @Mapping(source = "employee.position.name", target = "employeePositionName")
    @Mapping(source = "employee.profile.fullName", target = "employeeFullName")
    @Mapping(source = "approvedBy.id", target = "approvedById")
    @Mapping(source = "approvedBy.profile.fullName", target = "approvedByFullName")
    OvertimeRecordResponse toResponse(OvertimeRecord record);
}
