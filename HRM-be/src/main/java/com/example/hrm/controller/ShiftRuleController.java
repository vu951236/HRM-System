package com.example.hrm.controller;

import com.example.hrm.dto.request.ShiftRuleRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.ShiftRuleResponse;
import com.example.hrm.service.ShiftRuleService;
import com.example.hrm.systemlog.LoggableAction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shift-rules")
@RequiredArgsConstructor
public class ShiftRuleController {

    private final ShiftRuleService shiftRuleService;

    @LoggableAction(action = "CREATE_SHIFT_RULE", description = "Tạo quy tắc ca làm việc mới")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ShiftRuleResponse>> createShiftRule(@RequestBody ShiftRuleRequest request) {
        ShiftRuleResponse response = shiftRuleService.createShiftRule(request);
        return ResponseEntity.ok(ApiResponse.<ShiftRuleResponse>builder()
                .data(response)
                .message("Tạo quy tắc ca làm việc thành công")
                .build());
    }

    @GetMapping("/getAll")
    public ResponseEntity<ApiResponse<List<ShiftRuleResponse>>> getAllShiftRules() {
        List<ShiftRuleResponse> responses = shiftRuleService.getAllShiftRules();
        return ResponseEntity.ok(ApiResponse.<List<ShiftRuleResponse>>builder()
                .data(responses)
                .message("Lấy danh sách quy tắc ca làm việc thành công")
                .build());
    }

    @GetMapping("/getShiftRule/{id}")
    public ResponseEntity<ApiResponse<ShiftRuleResponse>> getShiftRuleById(@PathVariable Integer id) {
        ShiftRuleResponse response = shiftRuleService.getShiftRuleById(id);
        return ResponseEntity.ok(ApiResponse.<ShiftRuleResponse>builder()
                .data(response)
                .message("Lấy thông tin quy tắc ca làm việc thành công")
                .build());
    }

    @LoggableAction(action = "UPDATE_SHIFT_RULE", description = "Cập nhật quy tắc ca theo ID")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<ShiftRuleResponse>> updateShiftRule(@PathVariable Integer id,
                                                                          @RequestBody ShiftRuleRequest request) {
        ShiftRuleResponse response = shiftRuleService.updateShiftRule(id, request);
        return ResponseEntity.ok(ApiResponse.<ShiftRuleResponse>builder()
                .data(response)
                .message("Cập nhật quy tắc ca làm việc thành công")
                .build());
    }

    @LoggableAction(action = "DELETE_SHIFT_RULE", description = "Xóa quy tắc ca theo ID")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteShiftRule(@PathVariable Integer id) {
        shiftRuleService.deleteShiftRule(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Xóa quy tắc ca làm việc thành công")
                .build());
    }

    @LoggableAction(action = "RESTORE_SHIFT_RULE", description = "Khôi phục quy tắc ca đã xóa")
    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<Void>> restoreShiftRule(@PathVariable Integer id) {
        shiftRuleService.restoreShiftRule(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Khôi phục quy tắc ca làm việc thành công")
                .build());
    }
}
