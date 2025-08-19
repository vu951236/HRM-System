package com.example.hrm.dto.response;

import lombok.*;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftResponse {
    private Integer id;
    private String name;
    private ShiftRuleResponse shiftRule;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalTime breakTime;
    private String description;
    private Boolean isDelete;
}
