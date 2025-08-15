package com.example.hrm.controller;

import com.example.hrm.dto.request.ShiftRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.ShiftResponse;
import com.example.hrm.service.ShiftService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shifts")
@RequiredArgsConstructor
public class ShiftController {

    private final ShiftService shiftService;

    @PostMapping
    public ResponseEntity<ApiResponse<ShiftResponse>> createShift(@RequestBody ShiftRequest request) {
        ShiftResponse response = shiftService.createShift(request);
        return ResponseEntity.ok(ApiResponse.<ShiftResponse>builder().data(response).build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ShiftResponse>>> getAllShifts() {
        List<ShiftResponse> responses = shiftService.getAllShifts();
        return ResponseEntity.ok(ApiResponse.<List<ShiftResponse>>builder().data(responses).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShiftResponse>> getShiftById(@PathVariable Integer id) {
        ShiftResponse response = shiftService.getShiftById(id);
        return ResponseEntity.ok(ApiResponse.<ShiftResponse>builder().data(response).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ShiftResponse>> updateShift(@PathVariable Integer id,
                                                                  @RequestBody ShiftRequest request) {
        ShiftResponse response = shiftService.updateShift(id, request);
        return ResponseEntity.ok(ApiResponse.<ShiftResponse>builder().data(response).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteShift(@PathVariable Integer id) {
        shiftService.deleteShift(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<Void>> restoreShift(@PathVariable Integer id) {
        shiftService.restoreShift(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }
}
