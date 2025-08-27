package com.example.hrm.repository;

import com.example.hrm.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


public interface WorkScheduleRepository extends JpaRepository<WorkSchedule, Integer> {
    Optional<WorkSchedule> findByEmployeeAndShiftAndWorkDateAndIsDeleteFalse(EmployeeRecord employee, Shift shift, LocalDate workDate);

    Optional<Object> findByIdAndEmployeeId(Integer targetShiftId, Integer id);

    List<WorkSchedule> findByEmployeeIdAndIsDeleteFalse(Integer employeeId);

    List<WorkSchedule> findByEmployeeDepartmentIdAndIsDeleteFalse(Long employee_department_id);

    List<WorkSchedule> findByWorkDate(LocalDate today);
}