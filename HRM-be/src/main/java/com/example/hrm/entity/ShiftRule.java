package com.example.hrm.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shift_rules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rule_name", nullable = false)
    private String ruleName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "max_hours_per_day")
    private Integer maxHoursPerDay = 8;

    @Column(name = "allow_overtime")
    private Boolean allowOvertime = false;

    @Column(name = "night_shift_multiplier")
    private Float nightShiftMultiplier = 1.0f;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;
}
