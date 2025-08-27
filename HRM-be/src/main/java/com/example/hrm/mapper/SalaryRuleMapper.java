package com.example.hrm.mapper;

import com.example.hrm.dto.request.SalaryRuleRequest;
import com.example.hrm.dto.response.SalaryRuleResponse;
import com.example.hrm.entity.SalaryRule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SalaryRuleMapper {

    SalaryRuleResponse toResponse(SalaryRule entity);

    List<SalaryRuleResponse> toResponseList(List<SalaryRule> entities);

    @Mapping(target = "id", ignore = true)
    SalaryRule toEntity(SalaryRuleRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    void updateEntity(@MappingTarget SalaryRule entity, SalaryRuleRequest request);
}
