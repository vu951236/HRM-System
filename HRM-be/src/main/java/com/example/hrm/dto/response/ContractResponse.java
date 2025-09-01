package com.example.hrm.dto.response;

import com.example.hrm.entity.Contract;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContractResponse {
    private Integer id;
    private Integer userId;
    private String fullName;
    private String contractTypeName;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal salary;
    private Contract.ContractStatus status;
    private Boolean isDelete;
}

