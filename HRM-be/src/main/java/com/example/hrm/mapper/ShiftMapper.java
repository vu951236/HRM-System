package com.example.hrm.mapper;

import com.example.hrm.dto.request.ShiftRequest;
import com.example.hrm.entity.Shift;
import com.example.hrm.dto.response.ShiftResponse;
import com.example.hrm.entity.ShiftRule;
import com.example.hrm.dto.response.ShiftRuleResponse;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ShiftMapper {

    @Mapping(source = "shiftRule", target = "shiftRule")
    ShiftResponse toResponse(Shift shift);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shiftRule", ignore = true)
    Shift toEntity(ShiftRequest request);

    // ShiftRule mapping
    ShiftRuleResponse toResponse(ShiftRule rule);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(ShiftRequest request, @MappingTarget Shift shift);
}
