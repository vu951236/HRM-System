package com.example.hrm.controller;

import com.example.hrm.dto.request.UserLockRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.request.UserCreateRequest;
import com.example.hrm.dto.request.UserUpdateRequest;
import com.example.hrm.dto.response.UserLockResponse;
import com.example.hrm.dto.response.UserResponse;
import com.example.hrm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {


    private final UserService userService;

    @PostMapping("/createUser")
    ApiResponse<UserResponse> createUser(@RequestBody UserCreateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.createUser(request))
                .build();
    }

    @PutMapping("update/{userId}")
    public ApiResponse<UserResponse> updateUser(@PathVariable Integer userId, @RequestBody UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.updateUser(userId, request))
                .build();
    }


    @GetMapping("/allUser")
    ApiResponse<List<UserResponse>> getAllUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .data(userService.getAllUsers())
                .build();
    }

    @PutMapping("/{userId}/lock")
    public ApiResponse<UserLockResponse> lockOrUnlockUser(
            @PathVariable Integer userId,
            @RequestBody UserLockRequest request
    ) {
        return ApiResponse.<UserLockResponse>builder()
                .data(userService.lockOrUnlockUser(userId, request))
                .build();
    }

}
