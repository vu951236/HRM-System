package com.example.hrm.dto.request;

import lombok.*;
import java.math.BigDecimal;
import com.example.hrm.entity.PayrollDetail.DetailType;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayrollDetailRequest {
    private DetailType detailType;
    private String description;
    private BigDecimal amount;
}
