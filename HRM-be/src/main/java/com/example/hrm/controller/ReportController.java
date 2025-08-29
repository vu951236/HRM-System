package com.example.hrm.controller;

import com.example.hrm.dto.request.AttendanceChartRequest;
import com.example.hrm.dto.request.EmployeeContractChartRequest;
import com.example.hrm.dto.request.LeaveOvertimeChartRequest;
import com.example.hrm.dto.request.PayrollChartRequest;
import com.example.hrm.dto.response.EmployeeContractChartResponse;
import com.example.hrm.service.ChartService;
import com.example.hrm.service.ReportExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ChartService chartService;
    private final ReportExportService reportExportService;

    @PostMapping("/attendance")
    public ResponseEntity<byte[]> exportAttendance(@RequestBody AttendanceChartRequest request) throws Exception {
        byte[] file = reportExportService.exportAttendanceReport(chartService.getAttendanceChart(request));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=attendance_report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @PostMapping("/payroll")
    public ResponseEntity<byte[]> exportPayroll(@RequestBody PayrollChartRequest request) throws Exception {
        byte[] file = reportExportService.exportPayrollReport(chartService.getPayrollChart(request));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=payroll_report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @PostMapping("/leave-overtime")
    public ResponseEntity<byte[]> exportLeaveOvertime(@RequestBody LeaveOvertimeChartRequest request) throws Exception {
        byte[] file = reportExportService.exportLeaveOvertimeReport(chartService.getLeaveOvertimeChart(request));
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=leave_overtime_report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @PostMapping("/employee-count")
    public ResponseEntity<byte[]> exportEmployeeCount(@RequestBody EmployeeContractChartRequest request) throws Exception {
        List<EmployeeContractChartResponse> data = chartService.getEmployeeCountByDepartment(request);
        byte[] file = reportExportService.exportEmployeeCountByDepartment(data);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=employee_count_report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @PostMapping("/contract-count")
    public ResponseEntity<byte[]> exportContractCount(@RequestBody EmployeeContractChartRequest request) throws Exception {
        List<EmployeeContractChartResponse> data = chartService.getContractCountByType(request);
        byte[] file = reportExportService.exportContractCountByType(data);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contract_count_report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }

    @PostMapping("/expiring-contracts")
    public ResponseEntity<byte[]> exportExpiringContracts(@RequestBody EmployeeContractChartRequest request) throws Exception {
        List<EmployeeContractChartResponse> data = chartService.getExpiringContracts(request);
        byte[] file = reportExportService.exportExpiringContracts(data);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=expiring_contracts_report.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }
}
