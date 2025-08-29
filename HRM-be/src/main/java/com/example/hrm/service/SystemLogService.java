package com.example.hrm.service;

import com.example.hrm.dto.response.SystemLogResponse;
import com.example.hrm.entity.SystemLog;
import com.example.hrm.mapper.SystemLogMapper;
import com.example.hrm.repository.SystemLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SystemLogService {

    private final SystemLogRepository systemLogRepository;
    private final SystemLogMapper systemLogMapper;

    @PreAuthorize("hasRole('admin')")
    public List<SystemLogResponse> getAllLogs() {
        List<SystemLog> logs = systemLogRepository.findAll();
        return systemLogMapper.toResponseList(logs);
    }
}
