package com.example.hrm.controller;

import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.request.AuthenticationRequest;
import com.example.hrm.dto.request.IntrospectRequest;
import com.example.hrm.dto.response.AuthenticationResponse;
import com.example.hrm.exception.AppException;
import com.example.hrm.exception.ErrorCode;
import com.example.hrm.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        AuthenticationResponse authenticationResponse = authenticationService.login(request);
        ResponseCookie refreshCookie = ResponseCookie.from("refresh_token",authenticationResponse.getRefreshToken())
                .secure(false)
                .httpOnly(true)
                .maxAge(30*24*60*60)
                .path("/")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, refreshCookie.toString());

        return ApiResponse.<AuthenticationResponse>builder()
                .data(authenticationResponse)
                .build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody IntrospectRequest request,
                             @CookieValue(name = "refresh_token", required = false) String refreshToken,
                             HttpServletResponse response) throws ParseException, JOSEException {

        authenticationService.logout(request, refreshToken);

        ResponseCookie clearCookie = ResponseCookie.from("refresh_token", "")
                .secure(false)
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, clearCookie.toString());

        return ApiResponse.<Void>builder().build();
    }


    @PostMapping("/refresh-token")
    public ApiResponse<AuthenticationResponse> refreshToken(
            @CookieValue(name = "refresh_token", required = false) String refreshToken) throws ParseException, JOSEException {
        if (refreshToken == null) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
        var newAccessToken = authenticationService.refreshAccessToken(refreshToken);
        return ApiResponse.<AuthenticationResponse>builder()
                .data(newAccessToken)
                .build();

    }


}
