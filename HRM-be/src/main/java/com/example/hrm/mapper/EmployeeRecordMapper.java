package com.example.hrm.mapper;

import com.example.hrm.dto.request.EmployeeRecordRequest;
import com.example.hrm.dto.response.EmployeeRecordResponse;
import com.example.hrm.entity.EmployeeRecord;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface EmployeeRecordMapper {

    @Mapping(source = "user.username", target = "userName")
    @Mapping(source = "profile.fullName", target = "profileName")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "position.name", target = "positionName")
    @Mapping(source = "employmentType.name", target = "employmentTypeName")
    @Mapping(source = "supervisor.profile.fullName", target = "supervisorName")
    EmployeeRecordResponse toResponse(EmployeeRecord entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    EmployeeRecord toEntity(EmployeeRecordRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", expression = "java(java.time.LocalDateTime.now())")
    void updateEntity(@MappingTarget EmployeeRecord entity, EmployeeRecordRequest request);
}
