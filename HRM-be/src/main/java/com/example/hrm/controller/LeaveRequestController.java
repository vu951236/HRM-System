package com.example.hrm.controller;

import com.example.hrm.dto.request.LeaveRequestRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.LeaveRequestResponse;
import com.example.hrm.service.LeaveRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/leave")
@RequiredArgsConstructor
public class LeaveRequestController {

    private final LeaveRequestService leaveService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> createLeave(
            @RequestBody LeaveRequestRequest request) {

        LeaveRequestResponse leave = leaveService.createLeave(
                request.getPolicyId(),
                request.getStartDate(),
                request.getEndDate(),
                request.getReason()
        );

        return ResponseEntity.ok(ApiResponse.<LeaveRequestResponse>builder()
                .data(leave)
                .build());
    }

    @PostMapping("/approve/{id}")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> approveLeave(@PathVariable Integer id) {
        LeaveRequestResponse leave = leaveService.approveLeave(id);
        return ResponseEntity.ok(ApiResponse.<LeaveRequestResponse>builder()
                .data(leave)
                .build());
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> rejectLeave(@PathVariable Integer id) {
        LeaveRequestResponse leave = leaveService.rejectLeave(id);
        return ResponseEntity.ok(ApiResponse.<LeaveRequestResponse>builder()
                .data(leave)
                .build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLeave(@PathVariable Integer id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> restoreLeave(@PathVariable Integer id) {
        LeaveRequestResponse leave = leaveService.restoreLeave(id);
        return ResponseEntity.ok(ApiResponse.<LeaveRequestResponse>builder()
                .data(leave)
                .build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<LeaveRequestResponse>>> getAllLeaves() {
        List<LeaveRequestResponse> list = leaveService.getAllLeaveRequests();
        return ResponseEntity.ok(ApiResponse.<List<LeaveRequestResponse>>builder()
                .data(list)
                .build());
    }

    @GetMapping("/by-date")
    public ResponseEntity<ApiResponse<List<LeaveRequestResponse>>> getLeavesByDate(
            @RequestParam LocalDate date) {
        List<LeaveRequestResponse> list = leaveService.getLeaveByDate(date);
        return ResponseEntity.ok(ApiResponse.<List<LeaveRequestResponse>>builder()
                .data(list)
                .build());
    }
}
