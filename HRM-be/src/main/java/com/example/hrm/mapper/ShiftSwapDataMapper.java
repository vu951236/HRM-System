package com.example.hrm.mapper;

import com.example.hrm.dto.response.EmployeeRecordDataResponse;
import com.example.hrm.dto.response.WorkScheduleDataResponse;
import com.example.hrm.entity.EmployeeRecord;
import com.example.hrm.entity.WorkSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ShiftSwapDataMapper {

    ShiftSwapDataMapper INSTANCE = Mappers.getMapper(ShiftSwapDataMapper.class);

    @Mapping(target = "id", source = "user.id")
    @Mapping(target = "employeeCode", source = "employeeCode")
    @Mapping(target = "fullName", source = "profile.fullName")
    EmployeeRecordDataResponse toEmployeeRecordDataResponse(EmployeeRecord er);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "shiftName", source = "shift.name")
    @Mapping(target = "workDate", source = "workDate")
    WorkScheduleDataResponse toWorkScheduleDataResponse(WorkSchedule ws);
}
