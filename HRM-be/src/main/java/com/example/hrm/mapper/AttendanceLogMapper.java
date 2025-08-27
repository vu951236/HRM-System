package com.example.hrm.mapper;

import com.example.hrm.dto.request.AttendanceLogRequest;
import com.example.hrm.dto.response.AttendanceLogResponse;
import com.example.hrm.entity.AttendanceLog;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface AttendanceLogMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employee.id", source = "employeeId")
    @Mapping(target = "workSchedule.id", source = "workScheduleId")
    AttendanceLog toEntity(AttendanceLogRequest request);

    @Mapping(target = "employeeId", source = "employee.id")
    @Mapping(target = "employeeCode", source = "employee.employeeCode")
    @Mapping(target = "deviceName", source = "device.deviceName")
    @Mapping(target = "deviceId", source = "device.id")
    @Mapping(target = "userFullName", source = "employee.user.profile.fullName")
    @Mapping(target = "workScheduleId", source = "workSchedule.id")
    @Mapping(target = "shiftName", source = "workSchedule.shift.name")
    @Mapping(target = "checkInMethod", source = "checkInMethod")
    @Mapping(target = "checkOutMethod", source = "checkOutMethod")
    AttendanceLogResponse toResponse(AttendanceLog entity);
}

