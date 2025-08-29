package com.example.hrm.controller;

import com.example.hrm.dto.request.OvertimeRecordRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.OvertimeRecordResponse;
import com.example.hrm.service.OvertimeRecordService;
import com.example.hrm.systemlog.LoggableAction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/overtime")
@RequiredArgsConstructor
public class OvertimeRecordController {

    private final OvertimeRecordService service;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<OvertimeRecordResponse>> create(@RequestBody OvertimeRecordRequest request) {
        OvertimeRecordResponse response = service.createOvertime(
                request.getDate(),
                request.getStartTime(),
                request.getEndTime(),
                request.getReason()
        );
        return ResponseEntity.ok(ApiResponse.<OvertimeRecordResponse>builder().data(response).build());
    }

    @LoggableAction(action = "APPROVE_OVERTIME", description = "Phê duyệt làm thêm")
    @PostMapping("/approve/{id}")
    public ResponseEntity<ApiResponse<OvertimeRecordResponse>> approve(@PathVariable Integer id) {
        OvertimeRecordResponse response = service.approveOvertime(id);
        return ResponseEntity.ok(ApiResponse.<OvertimeRecordResponse>builder().data(response).build());
    }

    @LoggableAction(action = "REJECT_OVERTIME", description = "Từ chối làm thêm")
    @PostMapping("/reject/{id}")
    public ResponseEntity<ApiResponse<OvertimeRecordResponse>> reject(@PathVariable Integer id) {
        OvertimeRecordResponse response = service.rejectOvertime(id);
        return ResponseEntity.ok(ApiResponse.<OvertimeRecordResponse>builder().data(response).build());
    }

    @LoggableAction(action = "DELETE_OVERTIME", description = "Xóa bản ghi làm thêm")
    @PostMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.deleteOvertime(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }

    @LoggableAction(action = "RESTORE_OVERTIME", description = "Khôi phục bản ghi làm thêm")
    @PostMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable Integer id) {
        service.restoreOvertime(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<OvertimeRecordResponse>>> getAll() {
        List<OvertimeRecordResponse> responses = service.getAllOvertime();
        return ResponseEntity.ok(ApiResponse.<List<OvertimeRecordResponse>>builder().data(responses).build());
    }

    @GetMapping("/date")
    public ResponseEntity<ApiResponse<List<OvertimeRecordResponse>>> getByDate(@RequestParam String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<OvertimeRecordResponse> responses = service.getOvertimeByDate(localDate);
        return ResponseEntity.ok(ApiResponse.<List<OvertimeRecordResponse>>builder().data(responses).build());
    }
}
