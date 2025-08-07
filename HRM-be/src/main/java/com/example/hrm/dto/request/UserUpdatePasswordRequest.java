package com.example.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class UserUpdatePasswordRequest {
    private String oldPassword;
    private String newPassword;
}
