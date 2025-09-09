package com.example.hrm.controller;

import com.example.hrm.dto.request.LeaveRequestRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.LeaveRequestResponse;
import com.example.hrm.service.LeaveRequestService;
import com.example.hrm.systemlog.LoggableAction;
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
                .message("Yêu cầu nghỉ phép đã được tạo thành công.")
                .build());
    }

    @LoggableAction(action = "APPROVE_LEAVE_REQUEST", description = "Phê duyệt yêu cầu nghỉ phép")
    @PostMapping("/approve/{id}")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> approveLeave(@PathVariable Integer id) {
        LeaveRequestResponse leave = leaveService.approveLeave(id);
        return ResponseEntity.ok(ApiResponse.<LeaveRequestResponse>builder()
                .data(leave)
                .message("Yêu cầu nghỉ phép với id " + id + " đã được phê duyệt.")
                .build());
    }

    @LoggableAction(action = "REJECT_LEAVE_REQUEST", description = "Từ chối yêu cầu nghỉ phép")
    @PostMapping("/reject/{id}")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> rejectLeave(@PathVariable Integer id) {
        LeaveRequestResponse leave = leaveService.rejectLeave(id);
        return ResponseEntity.ok(ApiResponse.<LeaveRequestResponse>builder()
                .data(leave)
                .message("Yêu cầu nghỉ phép với id " + id + " đã bị từ chối.")
                .build());
    }

    @LoggableAction(action = "DELETE_LEAVE_REQUEST", description = "Xóa yêu cầu nghỉ phép")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteLeave(@PathVariable Integer id) {
        leaveService.deleteLeave(id);
        return ResponseEntity.ok(ApiResponse.<Void>builder()
                .message("Yêu cầu nghỉ phép với id " + id + " đã được xóa.")
                .build());
    }

    @LoggableAction(action = "RESTORE_LEAVE_REQUEST", description = "Khôi phục yêu cầu nghỉ phép")
    @PutMapping("/restore/{id}")
    public ResponseEntity<ApiResponse<LeaveRequestResponse>> restoreLeave(@PathVariable Integer id) {
        LeaveRequestResponse leave = leaveService.restoreLeave(id);
        return ResponseEntity.ok(ApiResponse.<LeaveRequestResponse>builder()
                .data(leave)
                .message("Yêu cầu nghỉ phép với id " + id + " đã được khôi phục.")
                .build());
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<LeaveRequestResponse>>> getAllLeaves() {
        List<LeaveRequestResponse> list = leaveService.getAllLeaveRequests();
        return ResponseEntity.ok(ApiResponse.<List<LeaveRequestResponse>>builder()
                .data(list)
                .message("Danh sách tất cả yêu cầu nghỉ phép đã được lấy thành công.")
                .build());
    }

    @GetMapping("/by-date")
    public ResponseEntity<ApiResponse<List<LeaveRequestResponse>>> getLeavesByDate(
            @RequestParam LocalDate date) {
        List<LeaveRequestResponse> list = leaveService.getLeaveByDate(date);
        return ResponseEntity.ok(ApiResponse.<List<LeaveRequestResponse>>builder()
                .data(list)
                .message("Danh sách yêu cầu nghỉ phép vào ngày " + date + " đã được lấy thành công.")
                .build());
    }
}
