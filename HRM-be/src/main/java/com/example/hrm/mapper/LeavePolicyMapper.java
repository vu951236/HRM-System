package com.example.hrm.mapper;

import com.example.hrm.dto.request.LeavePolicyRequest;
import com.example.hrm.dto.response.LeavePolicyResponse;
import com.example.hrm.entity.LeavePolicy;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface LeavePolicyMapper {

    @Mapping(source = "position.name", target = "positionName")
    @Mapping(source = "role.name", target = "roleName")
    @Mapping(source = "isDelete", target = "isDelete")
    LeavePolicyResponse toResponse(LeavePolicy entity);

    LeavePolicy toEntity(LeavePolicyRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "position", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateEntityFromRequest(LeavePolicyRequest request, @MappingTarget LeavePolicy entity);
}
