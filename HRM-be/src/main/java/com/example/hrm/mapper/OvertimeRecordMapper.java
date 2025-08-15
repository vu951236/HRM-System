package com.example.hrm.mapper;

import com.example.hrm.dto.response.OvertimeRecordResponse;
import com.example.hrm.entity.OvertimeRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OvertimeRecordMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.name", target = "employeeName")
    @Mapping(source = "approvedBy.id", target = "approvedById")
    @Mapping(source = "approvedBy.name", target = "approvedByName")
    OvertimeRecordResponse toResponse(OvertimeRecord record);
}
