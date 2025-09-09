package com.example.hrm.controller;

import com.example.hrm.dto.request.LeavePolicyRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.LeavePolicyResponse;
import com.example.hrm.service.LeavePolicyService;
import com.example.hrm.systemlog.LoggableAction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave-policy")
@RequiredArgsConstructor
public class LeavePolicyController {

    private final LeavePolicyService service;

    @LoggableAction(action = "CREATE_LEAVE_POLICY", description = "Tạo chính sách nghỉ phép mới")
    @PostMapping("/create")
    public ResponseEntity<ApiResponse<LeavePolicyResponse>> create(@RequestBody LeavePolicyRequest request) {
        LeavePolicyResponse response = service.create(request);
        return ResponseEntity.ok(ApiResponse.<LeavePolicyResponse>builder()
                .data(response)
                .message("Tạo chính sách nghỉ phép thành công.")
                .build());
    }

    @LoggableAction(action = "UPDATE_LEAVE_POLICY", description = "Cập nhật chính sách nghỉ phép")
    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<LeavePolicyResponse>> update(
            @PathVariable Integer id,
            @RequestBody LeavePolicyRequest request) {
        LeavePolicyResponse response = service.update(id, request);
        return ResponseEntity.ok(ApiResponse.<LeavePolicyResponse>builder()
                .data(response)
                .message("Cập nhật chính sách nghỉ phép với id " + id + " thành công.")
                .build());
    }

    @LoggableAction(action = "DELETE_LEAVE_POLICY", description = "Xóa chính sách nghỉ phép")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .data("Chính sách nghỉ phép với id " + id + " đã bị xóa.")
                .message("Xóa chính sách nghỉ phép thành công.")
                .build());
    }

    @LoggableAction(action = "RESTORE_LEAVE_POLICY", description = "Khôi phục chính sách nghỉ phép")
    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<String>> restore(@PathVariable Integer id) {
        service.restore(id);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .data("Chính sách nghỉ phép với id " + id + " đã được khôi phục.")
                .message("Khôi phục chính sách nghỉ phép thành công.")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LeavePolicyResponse>> getById(@PathVariable Integer id) {
        LeavePolicyResponse response = service.getById(id);
        return ResponseEntity.ok(ApiResponse.<LeavePolicyResponse>builder()
                .data(response)
                .message("Lấy thông tin chính sách nghỉ phép với id " + id + " thành công.")
                .build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<LeavePolicyResponse>>> getAll() {
        List<LeavePolicyResponse> responses = service.getAll();
        return ResponseEntity.ok(ApiResponse.<List<LeavePolicyResponse>>builder()
                .data(responses)
                .message("Lấy danh sách tất cả chính sách nghỉ phép thành công.")
                .build());
    }
}

