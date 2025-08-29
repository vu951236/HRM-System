package com.example.hrm.controller;

import com.example.hrm.dto.request.AttendanceLogRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.AttendanceLogResponse;
import com.example.hrm.entity.EmployeeRecord;
import com.example.hrm.entity.User;
import com.example.hrm.repository.EmployeeRecordRepository;
import com.example.hrm.repository.UserRepository;
import com.example.hrm.service.AttendanceLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceLogService service;

    @PostMapping("/checkin")
    public ResponseEntity<ApiResponse<AttendanceLogResponse>> checkIn(
            @RequestBody AttendanceLogRequest request) {
        return ResponseEntity.ok(ApiResponse.<AttendanceLogResponse>builder()
                .data(service.checkIn(request))
                .build());
    }

    @PostMapping("/checkout")
    public ResponseEntity<ApiResponse<AttendanceLogResponse>> checkOut(
            @RequestBody AttendanceLogRequest request) {
        return ResponseEntity.ok(ApiResponse.<AttendanceLogResponse>builder()
                .data(service.checkOut(request))
                .build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<AttendanceLogResponse>>> getAll() {
        return ResponseEntity.ok(
                ApiResponse.<List<AttendanceLogResponse>>builder()
                        .message("Lấy danh sách chấm công thành công")
                        .data(service.getAllLogs())
                        .build()
        );
    }

}
