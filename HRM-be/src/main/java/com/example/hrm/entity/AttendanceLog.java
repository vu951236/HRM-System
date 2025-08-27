package com.example.hrm.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendance_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private EmployeeRecord employee;

    @ManyToOne
    @JoinColumn(name = "work_schedule_id", nullable = false)
    private WorkSchedule workSchedule;

    @Column(name = "check_in_time")
    private LocalDateTime checkInTime;

    @Column(name = "check_out_time")
    private LocalDateTime checkOutTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AttendanceStatus status;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_in_method")
    private AttendanceMethod checkInMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "check_out_method")
    private AttendanceMethod checkOutMethod;


    public enum AttendanceStatus {
        CHECKED_IN,
        CHECKED_OUT,
        ABSENT
    }

    public enum AttendanceMethod {
        FINGERPRINT,
        CARD,
        QR
    }
}
