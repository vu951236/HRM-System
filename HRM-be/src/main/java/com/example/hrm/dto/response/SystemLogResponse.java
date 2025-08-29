package com.example.hrm.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SystemLogResponse {
    private Integer id;
    private String username;
    private String action;
    private LocalDateTime logTime;
    private String description;
    private String ipAddress;
}
