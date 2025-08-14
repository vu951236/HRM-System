package com.example.hrm.mapper;

import com.example.hrm.dto.response.ContractHistoryResponse;
import com.example.hrm.entity.ContractHistory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContractHistoryMapper {
    ContractHistoryResponse toResponse(ContractHistory entity);
}
