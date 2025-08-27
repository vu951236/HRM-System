package com.example.hrm.controller;

import com.example.hrm.dto.request.SalaryRuleRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.SalaryRuleResponse;
import com.example.hrm.service.SalaryRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salaryRules")
@RequiredArgsConstructor
public class SalaryRuleController {

    private final SalaryRuleService service;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<SalaryRuleResponse>> create(@RequestBody SalaryRuleRequest request) {
        return ResponseEntity.ok(ApiResponse.<SalaryRuleResponse>builder()
                .data(service.create(request))
                .message("Rule created successfully")
                .build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<SalaryRuleResponse>> update(
            @PathVariable Integer id,
            @RequestBody SalaryRuleRequest request) {
        return ResponseEntity.ok(ApiResponse.<SalaryRuleResponse>builder()
                .data(service.update(id, request))
                .message("Rule updated successfully")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SalaryRuleResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.<SalaryRuleResponse>builder()
                .data(service.getById(id))
                .build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<SalaryRuleResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<SalaryRuleResponse>>builder()
                .data(service.getAll())
                .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Rule deleted successfully (soft delete)")
                .build());
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable Integer id) {
        service.restore(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Rule restored successfully")
                .build());
    }
}
