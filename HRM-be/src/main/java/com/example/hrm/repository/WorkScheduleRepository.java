package com.example.hrm.repository;

import com.example.hrm.entity.EmployeeRecord;
import com.example.hrm.entity.Shift;
import com.example.hrm.entity.WorkSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;


public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Integer> {
    Optional<WorkSchedule> findByEmployeeAndShiftAndWorkDateAndIsDeleteFalse(EmployeeRecord employee, Shift shift, LocalDate workDate);
}