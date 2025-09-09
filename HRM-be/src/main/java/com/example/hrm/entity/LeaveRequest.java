package com.example.hrm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "leave_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeRecord employee;

    @ManyToOne
    @JoinColumn(name = "leave_policy_id")
    private LeavePolicy leavePolicy;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    private String reason;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @Enumerated(EnumType.STRING)
    private Status status = Status.pending;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    public enum Status {
        pending, approved, rejected
    }
}
