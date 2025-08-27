package com.example.hrm.controller;

import com.example.hrm.dto.request.LeavePolicyRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.LeavePolicyResponse;
import com.example.hrm.service.LeavePolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/leave-policy")
@RequiredArgsConstructor
public class LeavePolicyController {

    private final LeavePolicyService service;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<LeavePolicyResponse>> create(@RequestBody LeavePolicyRequest request) {
        LeavePolicyResponse response = service.create(request);
        return ResponseEntity.ok(ApiResponse.<LeavePolicyResponse>builder()
                .data(response)
                .build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<LeavePolicyResponse>> update(
            @PathVariable Integer id,
            @RequestBody LeavePolicyRequest request) {
        LeavePolicyResponse response = service.update(id, request);
        return ResponseEntity.ok(ApiResponse.<LeavePolicyResponse>builder()
                .data(response)
                .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .data("Leave policy with id " + id + " has been deleted.")
                .build());
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<String>> restore(@PathVariable Integer id) {
        service.restore(id);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .data("Leave policy with id " + id + " has been restored.")
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<LeavePolicyResponse>> getById(@PathVariable Integer id) {
        LeavePolicyResponse response = service.getById(id);
        return ResponseEntity.ok(ApiResponse.<LeavePolicyResponse>builder()
                .data(response)
                .build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<LeavePolicyResponse>>> getAll() {
        List<LeavePolicyResponse> responses = service.getAll();
        return ResponseEntity.ok(ApiResponse.<List<LeavePolicyResponse>>builder()
                .data(responses)
                .build());
    }
}
