package com.example.hrm.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "leave_policies")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeavePolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "policy_name")
    private String policyName;

    @Column(name = "max_days")
    private Integer maxDays;

    private String description;
}
