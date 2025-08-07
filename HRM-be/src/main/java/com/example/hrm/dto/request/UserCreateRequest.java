package com.example.hrm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class UserCreateRequest {
    private String username;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private String roleName;
}

