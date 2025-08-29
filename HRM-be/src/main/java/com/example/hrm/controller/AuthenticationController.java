package com.example.hrm.controller;

import com.example.hrm.dto.request.AuthenticationRequest;
import com.example.hrm.dto.request.IntrospectRequest;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.dto.response.AuthenticationResponse;
import com.example.hrm.exception.AppException;
import com.example.hrm.exception.ErrorCode;
import com.example.hrm.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    private void setRefreshTokenCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .secure(false)
                .httpOnly(true)
                .maxAge(30 * 24 * 60 * 60) // 30 ngày
                .path("/")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private void clearRefreshTokenCookie(HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("refresh_token", "")
                .secure(false)
                .httpOnly(true)
                .maxAge(0)
                .path("/")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> login(
            @RequestBody AuthenticationRequest request,
            HttpServletResponse response) {

        AuthenticationResponse authResponse = authenticationService.login(request);
        setRefreshTokenCookie(response, authResponse.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.<AuthenticationResponse>builder()
                .data(authResponse)
                .build());
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @RequestBody IntrospectRequest request,
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response) throws ParseException, JOSEException {

        authenticationService.logout(request, refreshToken);
        clearRefreshTokenCookie(response);

        return ResponseEntity.ok(ApiResponse.<Void>builder().build());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> refreshToken(
            @CookieValue(name = "refresh_token", required = false) String refreshToken,
            HttpServletResponse response) throws ParseException, JOSEException {

        if (refreshToken == null) {
            throw new AppException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        AuthenticationResponse newAccessToken = authenticationService.refreshAccessToken(refreshToken);
        setRefreshTokenCookie(response, newAccessToken.getRefreshToken());

        return ResponseEntity.ok(ApiResponse.<AuthenticationResponse>builder()
                .data(newAccessToken)
                .build());
    }
}
