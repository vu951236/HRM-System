package com.example.hrm.controller;

import com.example.hrm.dto.request.WorkScheduleRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.WorkScheduleResponse;
import com.example.hrm.service.WorkScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/work-schedules")
@RequiredArgsConstructor
public class WorkScheduleController {

    private final WorkScheduleService service;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<WorkScheduleResponse>> create(@RequestBody WorkScheduleRequest request) {
        WorkScheduleResponse response = service.createWorkSchedule(request);
        return ResponseEntity.ok(ApiResponse.<WorkScheduleResponse>builder().data(response).build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<WorkScheduleResponse>> update(@PathVariable Integer id,
                                                                    @RequestBody WorkScheduleRequest request) {
        WorkScheduleResponse response = service.updateWorkSchedule(id, request);
        return ResponseEntity.ok(ApiResponse.<WorkScheduleResponse>builder().data(response).build());
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<WorkScheduleResponse>>> getAll() {
        List<WorkScheduleResponse> list = service.getAllSchedules();
        return ResponseEntity.ok(ApiResponse.<List<WorkScheduleResponse>>builder().data(list).build());
    }

    @GetMapping("/getSchedule/{id}")
    public ResponseEntity<ApiResponse<WorkScheduleResponse>> getById(@PathVariable Integer id) {
        WorkScheduleResponse response = service.getScheduleById(id);
        return ResponseEntity.ok(ApiResponse.<WorkScheduleResponse>builder().data(response).build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.deleteWorkSchedule(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }

    @PostMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<Void>> restoreWorkSchedule(@PathVariable Integer id) {
        service.restoreWorkSchedule(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("WorkSchedule restored successfully")
                .build());
    }
}
