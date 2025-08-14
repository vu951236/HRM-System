package com.example.hrm.controller;

import com.example.hrm.dto.request.UserLockRequest;
import com.example.hrm.dto.request.UserCreateRequest;
import com.example.hrm.dto.request.UserUpdateRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.UserLockResponse;
import com.example.hrm.dto.response.UserResponse;
import com.example.hrm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PostMapping("/createUser")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@RequestBody UserCreateRequest request) {
        UserResponse result = userService.createUser(request);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .data(result)
                .build());
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(@PathVariable Integer userId,
                                                                @RequestBody UserUpdateRequest request) {
        UserResponse result = userService.updateUser(userId, request);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .data(result)
                .build());
    }

    @GetMapping("/allUser")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {
        List<UserResponse> result = userService.getAllUsers();
        return ResponseEntity.ok(ApiResponse.<List<UserResponse>>builder()
                .data(result)
                .build());
    }

    @PutMapping("/{userId}/lock")
    public ResponseEntity<ApiResponse<UserLockResponse>> lockOrUnlockUser(
            @PathVariable Integer userId,
            @RequestBody UserLockRequest request
    ) {
        UserLockResponse result = userService.lockOrUnlockUser(userId, request);
        return ResponseEntity.ok(ApiResponse.<UserLockResponse>builder()
                .data(result)
                .build());
    }

}
