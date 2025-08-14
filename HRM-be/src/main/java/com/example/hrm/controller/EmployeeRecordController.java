package com.example.hrm.controller;

import com.example.hrm.dto.request.EmployeeRecordRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.EmployeeRecordResponse;
import com.example.hrm.service.EmployeeRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee-records")
@RequiredArgsConstructor
public class EmployeeRecordController {

    private final EmployeeRecordService employeeRecordService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<EmployeeRecordResponse>> createEmployeeRecord(
            @RequestBody EmployeeRecordRequest request) {
        EmployeeRecordResponse response = employeeRecordService.createEmployeeRecord(request);
        return ResponseEntity.ok(ApiResponse.<EmployeeRecordResponse>builder()
                .data(response)
                .build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<EmployeeRecordResponse>> updateEmployeeRecord(
            @PathVariable Integer id,
            @RequestBody EmployeeRecordRequest request) {
        EmployeeRecordResponse response = employeeRecordService.updateEmployeeRecord(id, request);
        return ResponseEntity.ok(ApiResponse.<EmployeeRecordResponse>builder()
                .data(response)
                .build());
    }

    @GetMapping("/getAllRecords")
    public ResponseEntity<ApiResponse<List<EmployeeRecordResponse>>> getAllRecordsByRole() {
        List<EmployeeRecordResponse> responses = employeeRecordService.getAllRecordsByRole();
        return ResponseEntity.ok(ApiResponse.<List<EmployeeRecordResponse>>builder()
                .data(responses)
                .build());
    }

    @PutMapping("/soft-delete/{id}")
    public ResponseEntity<ApiResponse<Void>> softDeleteRecord(@PathVariable Integer id) {
        employeeRecordService.softDeleteRecord(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<Void>> restoreRecord(@PathVariable Integer id) {
        employeeRecordService.restoreRecord(id);
        return ResponseEntity.noContent().build();
    }
}
