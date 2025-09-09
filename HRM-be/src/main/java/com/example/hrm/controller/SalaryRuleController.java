package com.example.hrm.controller;

import com.example.hrm.dto.request.SalaryRuleRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.SalaryRuleResponse;
import com.example.hrm.service.SalaryRuleService;
import com.example.hrm.systemlog.LoggableAction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/salaryRules")
@RequiredArgsConstructor
public class SalaryRuleController {

    private final SalaryRuleService service;

    @LoggableAction(action = "CREATE_SALARY_RULE", description = "Tạo mới rule lương")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<SalaryRuleResponse>> create(@RequestBody SalaryRuleRequest request) {
        return ResponseEntity.ok(ApiResponse.<SalaryRuleResponse>builder()
                .data(service.create(request))
                .message("Tạo rule lương thành công")
                .build());
    }

    @LoggableAction(action = "UPDATE_SALARY_RULE", description = "Cập nhật rule lương")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<SalaryRuleResponse>> update(
            @PathVariable Integer id,
            @RequestBody SalaryRuleRequest request) {
        return ResponseEntity.ok(ApiResponse.<SalaryRuleResponse>builder()
                .data(service.update(id, request))
                .message("Cập nhật rule lương thành công")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SalaryRuleResponse>> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.<SalaryRuleResponse>builder()
                .data(service.getById(id))
                .message("Lấy thông tin rule lương thành công")
                .build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<SalaryRuleResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<SalaryRuleResponse>>builder()
                .data(service.getAll())
                .message("Lấy danh sách tất cả rule lương thành công")
                .build());
    }

    @LoggableAction(action = "DELETE_SALARY_RULE", description = "Xóa mềm rule lương")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Xóa rule lương thành công (xóa mềm)")
                .build());
    }

    @LoggableAction(action = "RESTORE_SALARY_RULE", description = "Khôi phục rule lương")
    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable Integer id) {
        service.restore(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Khôi phục rule lương thành công")
                .build());
    }
}
