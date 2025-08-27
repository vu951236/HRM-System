package com.example.hrm.dto.response;

import lombok.*;
import java.math.BigDecimal;
import com.example.hrm.entity.PayrollDetail.DetailType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollDetailResponse {
    private Integer id;
    private DetailType detailType;
    private String description;
    private BigDecimal amount;
}
