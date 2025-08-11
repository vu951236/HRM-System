package com.example.hrm.controller;

import com.example.hrm.dto.request.EmployeeRecordRequest;
import com.example.hrm.dto.response.EmployeeRecordResponse;
import com.example.hrm.service.EmployeeRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee-records")
@RequiredArgsConstructor
public class EmployeeRecordController {

    private final EmployeeRecordService employeeRecordService;

    @PostMapping("/create")
    public EmployeeRecordResponse createEmployeeRecord(@RequestBody EmployeeRecordRequest request) {
        return employeeRecordService.createEmployeeRecord(request);
    }

    @PutMapping("/update/{id}")
    public EmployeeRecordResponse updateEmployeeRecord(@PathVariable Integer id,
                                                       @RequestBody EmployeeRecordRequest request) {
        return employeeRecordService.updateEmployeeRecord(id, request);
    }

    @GetMapping("/getAllRecords")
    public List<EmployeeRecordResponse> getAllRecordsByRole() {
        return employeeRecordService.getAllRecordsByRole();
    }
}
