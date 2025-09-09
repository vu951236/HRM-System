package com.example.hrm.controller;

import com.example.hrm.dto.request.ShiftRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.ShiftResponse;
import com.example.hrm.service.ShiftService;
import com.example.hrm.systemlog.LoggableAction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @LoggableAction(action = "CREATE_SHIFT", description = "Tạo ca làm việc mới")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ShiftResponse>> createShift(@RequestBody ShiftRequest request) {
        ShiftResponse response = shiftService.createShift(request);
        return ResponseEntity.ok(ApiResponse.<ShiftResponse>builder()
                .data(response)
                .message("Tạo ca làm việc thành công")
                .build());
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<ShiftResponse>>> getAllShifts() {
        List<ShiftResponse> responses = shiftService.getAllShifts();
        return ResponseEntity.ok(ApiResponse.<List<ShiftResponse>>builder()
                .data(responses)
                .message("Lấy danh sách ca làm việc thành công")
                .build());
    }

    @GetMapping("/getShift/{id}")
    public ResponseEntity<ApiResponse<ShiftResponse>> getShiftById(@PathVariable Integer id) {
        ShiftResponse response = shiftService.getShiftById(id);
        return ResponseEntity.ok(ApiResponse.<ShiftResponse>builder()
                .data(response)
                .message("Lấy thông tin ca làm việc thành công")
                .build());
    }

    @LoggableAction(action = "UPDATE_SHIFT", description = "Cập nhật ca làm việc")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<ShiftResponse>> updateShift(@PathVariable Integer id,
                                                                  @RequestBody ShiftRequest request) {
        ShiftResponse response = shiftService.updateShift(id, request);
        return ResponseEntity.ok(ApiResponse.<ShiftResponse>builder()
                .data(response)
                .message("Cập nhật ca làm việc thành công")
                .build());
    }

    @LoggableAction(action = "DELETE_SHIFT", description = "Xóa ca làm việc")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteShift(@PathVariable Integer id) {
        shiftService.deleteShift(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Xóa ca làm việc thành công (soft delete)")
                .build());
    }

    @LoggableAction(action = "RESTORE_SHIFT", description = "Khôi phục ca làm việc")
    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<Void>> restoreShift(@PathVariable Integer id) {
        shiftService.restoreShift(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Khôi phục ca làm việc thành công")
                .build());
    }

}
