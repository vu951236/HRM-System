package com.example.hrm.dto.response;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponse {
    private String email;
    private String fullName;
    private String gender;
    private LocalDate dob;
    private String phone;
    private String address;
    private String imageUrl;
}
