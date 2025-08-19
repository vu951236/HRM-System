package com.example.hrm.mapper;

import com.example.hrm.dto.request.WorkScheduleTemplateRequest;
import com.example.hrm.dto.response.WorkScheduleTemplateResponse;
import com.example.hrm.entity.WorkScheduleTemplate;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WorkScheduleTemplateMapper {

    @Mapping(source = "department.id", target = "departmentId")
    @Mapping(source = "department.name", target = "departmentName")
    @Mapping(source = "createdBy.id", target = "createdById")
    @Mapping(source = "createdBy.username", target = "createdByName")
    WorkScheduleTemplateResponse toResponse(WorkScheduleTemplate entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "department", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "isDelete", constant = "false")
    WorkScheduleTemplate toEntity(WorkScheduleTemplateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(WorkScheduleTemplateRequest request, @MappingTarget WorkScheduleTemplate entity);
}
