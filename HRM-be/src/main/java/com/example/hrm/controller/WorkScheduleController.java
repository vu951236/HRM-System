package com.example.hrm.controller;

import com.example.hrm.dto.request.WorkScheduleRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.WorkScheduleResponse;
import com.example.hrm.service.WorkScheduleService;
import com.example.hrm.systemlog.LoggableAction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/work-schedules")
@RequiredArgsConstructor
public class WorkScheduleController {

    private final WorkScheduleService service;

    @LoggableAction(action = "CREATE_WORK_SCHEDULE", description = "Tạo mới lịch làm việc")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<WorkScheduleResponse>> create(@RequestBody WorkScheduleRequest request) {
        WorkScheduleResponse response = service.createWorkSchedule(request);
        return ResponseEntity.ok(ApiResponse.<WorkScheduleResponse>builder()
                .data(response)
                .message("Tạo lịch làm việc thành công")
                .build());
    }

    @LoggableAction(action = "UPDATE_WORK_SCHEDULE", description = "Cập nhật lịch làm việc")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<WorkScheduleResponse>> update(@PathVariable Integer id,
                                                                    @RequestBody WorkScheduleRequest request) {
        WorkScheduleResponse response = service.updateWorkSchedule(id, request);
        return ResponseEntity.ok(ApiResponse.<WorkScheduleResponse>builder()
                .data(response)
                .message("Cập nhật lịch làm việc thành công")
                .build());
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<WorkScheduleResponse>>> getAll() {
        List<WorkScheduleResponse> list = service.getAllSchedules();
        return ResponseEntity.ok(ApiResponse.<List<WorkScheduleResponse>>builder()
                .data(list)
                .message("Lấy danh sách lịch làm việc thành công")
                .build());
    }

    @GetMapping("/getSchedule/{id}")
    public ResponseEntity<ApiResponse<WorkScheduleResponse>> getById(@PathVariable Integer id) {
        WorkScheduleResponse response = service.getScheduleById(id);
        return ResponseEntity.ok(ApiResponse.<WorkScheduleResponse>builder()
                .data(response)
                .message("Lấy thông tin lịch làm việc thành công")
                .build());
    }

    @LoggableAction(action = "DELETE_WORK_SCHEDULE", description = "Xóa lịch làm việc")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.deleteWorkSchedule(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Xóa lịch làm việc thành công")
                .build());
    }

    @LoggableAction(action = "RESTORE_WORK_SCHEDULE", description = "Khôi phục lịch làm việc")
    @PostMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<Void>> restoreWorkSchedule(@PathVariable Integer id) {
        service.restoreWorkSchedule(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Khôi phục lịch làm việc thành công")
                .build());
    }
}
