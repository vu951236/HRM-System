package com.example.hrm.controller;

import com.example.hrm.dto.request.AttendanceChartRequest;
import com.example.hrm.dto.request.EmployeeContractChartRequest;
import com.example.hrm.dto.request.PayrollChartRequest;
import com.example.hrm.dto.request.LeaveOvertimeChartRequest;
import com.example.hrm.dto.response.*;
import com.example.hrm.service.ChartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/charts")
@RequiredArgsConstructor
public class ChartController {

    private final ChartService chartService;

    @PostMapping("/leave-overtime")
    public ResponseEntity<ApiResponse<List<LeaveOvertimeChartResponse>>> getLeaveOvertimeChart(
            @RequestBody LeaveOvertimeChartRequest request) {
        List<LeaveOvertimeChartResponse> responses = chartService.getLeaveOvertimeChart(request);
        return ResponseEntity.ok(ApiResponse.<List<LeaveOvertimeChartResponse>>builder()
                .data(responses)
                .message("Lấy dữ liệu Leave/Overtime thành công")
                .build());
    }

    @PostMapping("/payroll")
    public ResponseEntity<ApiResponse<List<PayrollChartResponse>>> getPayrollChart(
            @RequestBody PayrollChartRequest request) {
        List<PayrollChartResponse> responses = chartService.getPayrollChart(request);
        return ResponseEntity.ok(ApiResponse.<List<PayrollChartResponse>>builder()
                .data(responses)
                .message("Lấy dữ liệu Payroll thành công")
                .build());
    }

    @PostMapping("/attendance")
    public ResponseEntity<ApiResponse<List<AttendanceChartResponse>>> getAttendanceChart(
            @RequestBody AttendanceChartRequest request) {
        List<AttendanceChartResponse> responses = chartService.getAttendanceChart(request);
        return ResponseEntity.ok(ApiResponse.<List<AttendanceChartResponse>>builder()
                .data(responses)
                .message("Lấy dữ liệu Attendance thành công")
                .build());
    }

    @PostMapping("/employee-department")
    public ResponseEntity<ApiResponse<List<EmployeeContractChartResponse>>> employeeByDepartment(
            @RequestBody EmployeeContractChartRequest request) {
        List<EmployeeContractChartResponse> responses = chartService.getEmployeeCountByDepartment(request);
        return ResponseEntity.ok(ApiResponse.<List<EmployeeContractChartResponse>>builder()
                .data(responses)
                .message("Lấy dữ liệu Employee theo Department thành công")
                .build());
    }

    @PostMapping("/contract-type")
    public ResponseEntity<ApiResponse<List<EmployeeContractChartResponse>>> contractByType(
            @RequestBody EmployeeContractChartRequest request) {
        List<EmployeeContractChartResponse> responses = chartService.getContractCountByType(request);
        return ResponseEntity.ok(ApiResponse.<List<EmployeeContractChartResponse>>builder()
                .data(responses)
                .message("Lấy dữ liệu Contract theo Type thành công")
                .build());
    }

    @PostMapping("/contract-expiring")
    public ResponseEntity<ApiResponse<List<EmployeeContractChartResponse>>> contractExpiring(
            @RequestBody EmployeeContractChartRequest request) {
        List<EmployeeContractChartResponse> responses = chartService.getExpiringContracts(request);
        return ResponseEntity.ok(ApiResponse.<List<EmployeeContractChartResponse>>builder()
                .data(responses)
                .message("Lấy dữ liệu Contract sắp hết hạn thành công")
                .build());
    }
}