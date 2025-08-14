package com.example.hrm.mapper;

import com.example.hrm.dto.request.ContractRequest;
import com.example.hrm.dto.response.ContractResponse;
import com.example.hrm.entity.Contract;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface ContractMapper {
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.username", target = "userName")
    @Mapping(source = "contractType.name", target = "contractTypeName")
    ContractResponse toResponse(Contract entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contractType", ignore = true)
    @Mapping(target = "isDelete", constant = "false")
    @Mapping(target = "status", constant = "ACTIVE")
    Contract toEntity(ContractRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "contractType", ignore = true)
    @Mapping(target = "isDelete", ignore = true)
    @Mapping(target = "status", ignore = true)
    void updateEntity(@MappingTarget Contract entity, ContractRequest request);
}

