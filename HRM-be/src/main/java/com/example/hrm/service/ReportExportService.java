package com.example.hrm.service;

import com.example.hrm.dto.response.AttendanceChartResponse;
import com.example.hrm.dto.response.PayrollChartResponse;
import com.example.hrm.dto.response.LeaveOvertimeChartResponse;
import com.example.hrm.dto.response.EmployeeContractChartResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportExportService {

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }

    private void autoSizeColumns(Sheet sheet, int columnCount) {
        for (int i = 0; i < columnCount; i++) {
            sheet.autoSizeColumn(i);
        }
    }
    @PreAuthorize("hasAnyRole('admin','hr')")
    public byte[] exportAttendanceReport(List<AttendanceChartResponse> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Báo cáo chấm công");
            String[] headers = {"Tên", "Đi muộn", "Về sớm", "Vắng mặt"};
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = createHeaderStyle(workbook);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            int rowIdx = 1;
            for (AttendanceChartResponse resp : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(resp.getName());
                row.createCell(1).setCellValue(resp.getLateCount());
                row.createCell(2).setCellValue(resp.getEarlyLeaveCount());
                row.createCell(3).setCellValue(resp.getAbsentCount());
            }
            autoSizeColumns(sheet, headers.length);
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        }
    }

    @PreAuthorize("hasAnyRole('admin','hr')")
    public byte[] exportPayrollReport(List<PayrollChartResponse> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Báo cáo lương");
            String[] headers = {"Tên", "Kỳ", "Lương cơ bản", "Thưởng", "Khấu trừ", "Tổng lương"};
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = createHeaderStyle(workbook);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            int rowIdx = 1;
            for (PayrollChartResponse resp : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(resp.getName());
                row.createCell(1).setCellValue(resp.getPeriod());
                row.createCell(2).setCellValue(resp.getBaseSalaryTotal().doubleValue());
                row.createCell(3).setCellValue(resp.getBonusTotal().doubleValue());
                row.createCell(4).setCellValue(resp.getDeductionTotal().doubleValue());
                row.createCell(5).setCellValue(resp.getFinalSalaryTotal().doubleValue());
            }
            autoSizeColumns(sheet, headers.length);
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        }
    }

    @PreAuthorize("hasAnyRole('admin','hr')")
    public byte[] exportLeaveOvertimeReport(List<LeaveOvertimeChartResponse> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Báo cáo nghỉ phép & tăng ca");
            String[] headers = {"Tên", "Số ngày nghỉ", "Số giờ tăng ca"};
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = createHeaderStyle(workbook);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }
            int rowIdx = 1;
            for (LeaveOvertimeChartResponse resp : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(resp.getName());
                row.createCell(1).setCellValue(resp.getLeaveDays());
                row.createCell(2).setCellValue(resp.getOvertimeHours());
            }
            autoSizeColumns(sheet, headers.length);
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        }
    }

    @PreAuthorize("hasAnyRole('admin','hr')")
    public byte[] exportEmployeeCountByDepartment(List<EmployeeContractChartResponse> data) throws IOException {
        return exportSimpleChart("Nhân sự theo phòng ban", "Phòng ban", "Số lượng nhân viên", data);
    }

    @PreAuthorize("hasAnyRole('admin','hr')")
    public byte[] exportContractCountByType(List<EmployeeContractChartResponse> data) throws IOException {
        return exportSimpleChart("Hợp đồng theo loại", "Loại hợp đồng", "Số lượng hợp đồng", data);
    }

    @PreAuthorize("hasAnyRole('admin','hr')")
    public byte[] exportExpiringContracts(List<EmployeeContractChartResponse> data) throws IOException {
        return exportSimpleChart("Hợp đồng sắp hết hạn", "Loại hợp đồng", "Số lượng hợp đồng sắp hết hạn", data);
    }

    private byte[] exportSimpleChart(String sheetName, String col1Header, String col2Header, List<EmployeeContractChartResponse> data) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(sheetName);
            Row headerRow = sheet.createRow(0);
            CellStyle headerStyle = createHeaderStyle(workbook);
            Cell cell1 = headerRow.createCell(0);
            cell1.setCellValue(col1Header);
            cell1.setCellStyle(headerStyle);
            Cell cell2 = headerRow.createCell(1);
            cell2.setCellValue(col2Header);
            cell2.setCellStyle(headerStyle);
            int rowIdx = 1;
            for (EmployeeContractChartResponse resp : data) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(resp.getLabel());
                row.createCell(1).setCellValue(resp.getCount());
            }
            autoSizeColumns(sheet, 2);
            try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        }
    }
}
