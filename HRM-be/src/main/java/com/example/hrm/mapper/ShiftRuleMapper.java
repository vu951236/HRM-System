package com.example.hrm.mapper;

import com.example.hrm.dto.request.ShiftRuleRequest;
import com.example.hrm.dto.response.ShiftRuleResponse;
import com.example.hrm.entity.ShiftRule;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ShiftRuleMapper {

    ShiftRuleResponse toResponse(ShiftRule rule);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.LocalDateTime.now())")
    ShiftRule toEntity(ShiftRuleRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ShiftRuleRequest request, @MappingTarget ShiftRule rule);
}
