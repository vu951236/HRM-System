package com.example.hrm.mapper;

import com.example.hrm.entity.SystemLog;
import com.example.hrm.dto.response.SystemLogResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SystemLogMapper {

    @Mapping(source = "user.username", target = "username")
    SystemLogResponse toResponse(SystemLog systemLog);

    List<SystemLogResponse> toResponseList(List<SystemLog> systemLogs);
}
