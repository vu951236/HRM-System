package com.example.hrm.dto.response;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftPatternResponse {
    private int day;
    private Integer shiftId;
    private String shiftName;
}

