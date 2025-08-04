package com.example.hrm.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "salary_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SalaryRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rule_name")
    private String ruleName;

    private String description;
    private String formula;
    private Boolean active = true;
}
