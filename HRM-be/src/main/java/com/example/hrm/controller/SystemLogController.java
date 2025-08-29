package com.example.hrm.controller;

import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.SystemLogResponse;
import com.example.hrm.service.SystemLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class SystemLogController {

    private final SystemLogService systemLogService;

    @GetMapping("/system-logs")
    public ResponseEntity<ApiResponse<List<SystemLogResponse>>> getAllSystemLogs() {
        List<SystemLogResponse> logs = systemLogService.getAllLogs();
        return ResponseEntity.ok(ApiResponse.<List<SystemLogResponse>>builder()
                .data(logs)
                .build());
    }
}
