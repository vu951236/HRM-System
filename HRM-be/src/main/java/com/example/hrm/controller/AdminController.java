package com.example.hrm.controller;

import com.example.hrm.dto.request.UserLockRequest;
import com.example.hrm.dto.request.UserCreateRequest;
import com.example.hrm.dto.request.UserUpdateRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.UserLockResponse;
import com.example.hrm.dto.response.UserResponse;
import com.example.hrm.service.UserService;
import com.example.hrm.systemlog.LoggableAction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @LoggableAction(action = "CREATE_USER", description = "Tạo mới người dùng")
    @PostMapping("/createUser")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserCreateRequest request) {
        UserResponse result = userService.createUser(request);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .data(result)
                .message("Tạo người dùng thành công")
                .build());
    }

    @LoggableAction(action = "UPDATE_USER", description = "Cập nhật thông tin người dùng")
    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Integer userId,
                                                                @RequestBody UserUpdateRequest request) {
        UserResponse result = userService.updateUser(userId, request);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .data(result)
                .message("Cập nhật người dùng thành công")
                .build());
    }

    @GetMapping("/allUser")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> result = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                .data(result)
                .message("Lấy danh sách người dùng thành công")
                .build());
    }

    @LoggableAction(action = "LOCK_UNLOCK_USER", description = "Khóa/Mở khóa người dùng")
    @PutMapping("/{userId}/lock")
    public ResponseEntity<ApiResponse<UserLockResponse>> lockOrUnlockUser(
            @PathVariable Integer userId,
            @RequestBody UserLockRequest request
    ) {
        UserLockResponse result = userService.lockOrUnlockUser(userId, request);
        return ResponseEntity.ok(ApiResponse.<UserLockResponse>builder()
                .data(result)
                .message("Thao tác khóa/mở khóa người dùng thành công")
                .build());
    }
}
