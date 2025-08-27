package com.example.hrm.repository;

import com.example.hrm.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AttendanceLogRepository extends JpaRepository<AttendanceLog, Integer> {

    List<AttendanceLog> findByEmployee_User_IdAndLogDateBetween(
            Integer userId, LocalDate startDate, LocalDate endDate);

    boolean existsByEmployeeAndWorkScheduleAndStatus(EmployeeRecord employee, WorkSchedule schedule, AttendanceLog.AttendanceStatus status);

    Optional<AttendanceLog> findByEmployeeAndWorkScheduleAndStatus(EmployeeRecord employee, WorkSchedule schedule, AttendanceLog.AttendanceStatus status);

    List<AttendanceLog> findByEmployee(EmployeeRecord employee);

    List<AttendanceLog> findByEmployee_IdAndLogDateBetween(Integer employeeId, LocalDate start, LocalDate end);
}
