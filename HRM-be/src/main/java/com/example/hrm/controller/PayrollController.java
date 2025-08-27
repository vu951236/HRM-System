package com.example.hrm.controller;

import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.PayrollResponse;
import com.example.hrm.service.PayrollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payroll")
@RequiredArgsConstructor
public class PayrollController {

    private final PayrollService payrollService;

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/calculate")
    public ResponseEntity<ApiResponse<PayrollResponse>> calculatePayroll(
            @RequestParam Integer employeeId,
            @RequestParam Integer month,
            @RequestParam Integer year) {
        PayrollResponse payroll = payrollService.calculatePayroll(employeeId, month, year);
        return ResponseEntity.ok(ApiResponse.<PayrollResponse>builder()
                .data(payroll)
                .build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<PayrollResponse>>> getAllPayrolls() {
        List<PayrollResponse> list = payrollService.getAllPayrolls();
        return ResponseEntity.ok(ApiResponse.<List<PayrollResponse>>builder()
                .data(list)
                .build());
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<ApiResponse<PayrollResponse>> approvePayroll(@PathVariable Integer id) {
        PayrollResponse payroll = payrollService.approvePayroll(id);
        return ResponseEntity.ok(ApiResponse.<PayrollResponse>builder()
                .data(payroll)
                .build());
    }

}
