package com.example.hrm.controller;

import com.example.hrm.dto.request.ShiftSwapRequestRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.ShiftSwapRequestResponse;
import com.example.hrm.entity.ShiftSwapRequest;
import com.example.hrm.service.ShiftSwapRequestService;
import com.example.hrm.systemlog.LoggableAction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shift-swap")
@RequiredArgsConstructor
public class ShiftSwapRequestController {

    private final ShiftSwapRequestService service;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<ShiftSwapRequestResponse>> create(@RequestBody ShiftSwapRequestRequest request) {
        return ResponseEntity.ok(ApiResponse.<ShiftSwapRequestResponse>builder()
                .data(service.createRequest(request))
                .build());
    }

    @LoggableAction(action = "APPROVE_SHIFT_SWAP", description = "Duyệt yêu cầu đổi ca")
    @PostMapping("/approve/{id}")
    public ResponseEntity<ApiResponse<ShiftSwapRequestResponse>> approve(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.<ShiftSwapRequestResponse>builder()
                .data(service.approveRequest(id))
                .build());
    }

    @LoggableAction(action = "REJECT_SHIFT_SWAP", description = "Từ chối yêu cầu đổi ca")
    @PostMapping("/reject/{id}")
    public ResponseEntity<ApiResponse<ShiftSwapRequestResponse>> reject(@PathVariable Integer id) {
        return ResponseEntity.ok(ApiResponse.<ShiftSwapRequestResponse>builder()
                .data(service.rejectRequest(id))
                .build());
    }

    @LoggableAction(action = "DELETE_SHIFT_SWAP", description = "Xóa yêu cầu đổi ca")
    @PostMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Integer id) {
        service.deleteRequest(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }

    @LoggableAction(action = "RESTORE_SHIFT_SWAP", description = "Khôi phục yêu cầu đổi ca")
    @PostMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<Void>> restore(@PathVariable Integer id) {
        service.restoreRequest(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<ShiftSwapRequestResponse>>> getAll() {
        return ResponseEntity.ok(ApiResponse.<List<ShiftSwapRequestResponse>>builder()
                .data(service.getAll())
                .build());
    }

    @GetMapping("/status")
    public ResponseEntity<ApiResponse<List<ShiftSwapRequestResponse>>> getByStatus(@RequestParam ShiftSwapRequest.Status status) {
        return ResponseEntity.ok(ApiResponse.<List<ShiftSwapRequestResponse>>builder()
                .data(service.getAllByStatus(status))
                .build());
    }
}
