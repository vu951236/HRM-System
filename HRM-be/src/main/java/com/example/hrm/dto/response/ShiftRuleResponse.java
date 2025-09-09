package com.example.hrm.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftRuleResponse {
    private Integer id;
    private String ruleName;
    private String description;
    private Integer maxHoursPerDay;
    private LocalDateTime createdAt;
    private Boolean isDelete;
}
