package com.example.hrm.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class UserResponse {
    private String userId;
    private String username;
    private String email;
    private String role;
    private String fullName;
    private String gender;
    private String dob;
    private String phone;
    private String address;
    private String avatarUrl;
    private Boolean isActive;
    private String positionName;
}


