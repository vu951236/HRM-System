package com.example.hrm.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_schedule_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkScheduleTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "template_name", nullable = false)
    private String templateName;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @Column(name = "shift_pattern", columnDefinition = "json")
    private String shiftPattern;

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

}
