package com.example.hrm.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "shift_swap_requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShiftSwapRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private EmployeeRecord requester;

    @ManyToOne
    @JoinColumn(name = "requested_shift_id", nullable = false)
    private WorkSchedule requestedShift;

    @ManyToOne
    @JoinColumn(name = "target_employee_id", nullable = false)
    private EmployeeRecord targetEmployee;

    @ManyToOne
    @JoinColumn(name = "target_shift_id", nullable = false)
    private WorkSchedule targetShift;

    @Enumerated(EnumType.STRING)
    private Status status = Status.pending;

    private String reason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_delete", nullable = false)
    private Boolean isDelete = false;

    @ManyToOne
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    public enum Status {
        pending, approved, rejected
    }

}
