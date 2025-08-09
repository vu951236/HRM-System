package com.example.hrm.dto.request;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileUpdateRequest {
    private String email;
    private String password;
    private String fullName;
    private String gender;
    private LocalDate dob;
    private String phone;
    private String address;
    private String imageUrl;
}
