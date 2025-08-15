package com.example.hrm.controller;

import com.example.hrm.dto.request.ShiftRuleRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.ShiftRuleResponse;
import com.example.hrm.service.ShiftRuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shift-rules")
@RequiredArgsConstructor
public class ShiftRuleController {

    private final ShiftRuleService shiftRuleService;

    @PostMapping
    public ResponseEntity<ApiResponse<ShiftRuleResponse>> createShiftRule(@RequestBody ShiftRuleRequest request) {
        ShiftRuleResponse response = shiftRuleService.createShiftRule(request);
        return ResponseEntity.ok(ApiResponse.<ShiftRuleResponse>builder().data(response).build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ShiftRuleResponse>>> getAllShiftRules() {
        List<ShiftRuleResponse> responses = shiftRuleService.getAllShiftRules();
        return ResponseEntity.ok(ApiResponse.<List<ShiftRuleResponse>>builder().data(responses).build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ShiftRuleResponse>> getShiftRuleById(@PathVariable Integer id) {
        ShiftRuleResponse response = shiftRuleService.getShiftRuleById(id);
        return ResponseEntity.ok(ApiResponse.<ShiftRuleResponse>builder().data(response).build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ShiftRuleResponse>> updateShiftRule(@PathVariable Integer id,
                                                                          @RequestBody ShiftRuleRequest request) {
        ShiftRuleResponse response = shiftRuleService.updateShiftRule(id, request);
        return ResponseEntity.ok(ApiResponse.<ShiftRuleResponse>builder().data(response).build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteShiftRule(@PathVariable Integer id) {
        shiftRuleService.deleteShiftRule(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<Void>> restoreShiftRule(@PathVariable Integer id) {
        shiftRuleService.restoreShiftRule(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }
}
