package com.example.hrm.mapper;

import com.example.hrm.dto.request.WorkScheduleRequest;
import com.example.hrm.dto.response.WorkScheduleResponse;
import com.example.hrm.entity.WorkSchedule;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface WorkScheduleMapper {

    @Mapping(source = "employee.id", target = "employeeId")
    @Mapping(source = "employee.name", target = "employeeName")
    @Mapping(source = "shift.id", target = "shiftId")
    @Mapping(source = "shift.name", target = "shiftName")
    @Mapping(source = "shift.shiftRule.id", target = "shiftRuleId")
    @Mapping(source = "shift.shiftRule.ruleName", target = "shiftRuleName")
    WorkScheduleResponse toResponse(WorkSchedule entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "employee", ignore = true)
    @Mapping(target = "shift", ignore = true)
    WorkSchedule toEntity(WorkScheduleRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(WorkScheduleRequest request, @MappingTarget WorkSchedule entity);
}
