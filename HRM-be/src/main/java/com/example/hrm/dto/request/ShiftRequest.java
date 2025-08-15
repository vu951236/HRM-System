package com.example.hrm.dto.request;

import lombok.*;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftRequest {
    private String name;
    private Integer shiftRuleId;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakTime;
    private String description;
}
