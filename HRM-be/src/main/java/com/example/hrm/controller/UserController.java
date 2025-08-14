package com.example.hrm.controller;

import com.example.hrm.dto.request.*;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.ProfileResponse;
import com.example.hrm.dto.response.UserResponse;
import com.example.hrm.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/getinfo")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo() {
        UserResponse response = userService.getMyInfo();
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .data(response)
                .build());
    }

    @PutMapping("/updatePass")
    public ResponseEntity<ApiResponse<UserResponse>> updatePassword(@RequestBody UserUpdatePasswordRequest request) {
        UserResponse response = userService.updatePassword(request);
        return ResponseEntity.ok(ApiResponse.<UserResponse>builder()
                .data(response)
                .build());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<Void>> forgotPassword(@RequestBody ForgotPasswordRequest request)
            throws IOException, MessagingException {
        userService.generateVerificationCode(request);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/verify-code")
    public ResponseEntity<ApiResponse<Boolean>> verifyCode(@RequestBody VerifyCodeRequest request) {
        boolean verified = userService.verifyCode(request.getEmail(), request.getCode());
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .data(verified)
                .build());
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<Boolean>> resetPassword(@RequestBody ResetPasswordRequest request) {
        boolean verified = userService.verifyCode(request.getEmail(), request.getCode());
        if (verified) {
            userService.resetPassword(request.getEmail(), request.getNewPassword());
            userService.clearCode(request.getEmail());
        }
        return ResponseEntity.ok(ApiResponse.<Boolean>builder()
                .data(verified)
                .build());
    }

    @PutMapping("/{userId}/update-profile")
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(
            @PathVariable Integer userId,
            @RequestBody ProfileUpdateRequest request) {
        ProfileResponse response = userService.updateProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.<ProfileResponse>builder()
                .data(response)
                .build());
    }

}
