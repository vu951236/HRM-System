package com.example.hrm.controller;

import com.example.hrm.dto.request.ForgotPasswordRequest;
import com.example.hrm.dto.request.ResetPasswordRequest;
import com.example.hrm.dto.request.UserUpdatePasswordRequest;
import com.example.hrm.dto.request.VerifyCodeRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.UserResponse;
import com.example.hrm.service.UserService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    final UserService userService;

    @GetMapping("/getinfo")
    ApiResponse<UserResponse> GetMyInfo() {
        return ApiResponse.<UserResponse>builder()
                .data(userService.getMyInfo())
                .build();
    }

    @PutMapping("/updatePass")
    ApiResponse<UserResponse> UpdatePassword(@RequestBody UserUpdatePasswordRequest request) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.updatePassword(request))
                .build();
    }

    @PostMapping("/forgot-password")
    ApiResponse<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) throws IOException, MessagingException {
        userService.generateVerificationCode(request);
        return ApiResponse.<Void>builder().build();
    }


    @PostMapping("/verify-code")
    ApiResponse<Boolean> verifyCode(@RequestBody VerifyCodeRequest request) {
        var verified = userService.verifyCode(request.getEmail(),request.getCode());
        return ApiResponse.<Boolean>builder()
                .data(verified)
                .build();
    }

    @PostMapping("/reset-password")
    ApiResponse<Boolean> resetPassword(@RequestBody ResetPasswordRequest request) {
        var verified = userService.verifyCode(request.getEmail(),request.getCode());
        if (verified) {
            userService.resetPassword(request.getEmail(), request.getNewPassword());
            userService.clearCode(request.getEmail());
        }
        return ApiResponse.<Boolean>builder()
                .data(verified)
                .build();
    }
}
