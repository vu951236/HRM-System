package com.example.hrm.repository;

import com.example.hrm.entity.Payroll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {
    @Query("SELECT p FROM Payroll p " +
            "WHERE p.year = :year " +
            "AND (:departmentId IS NULL OR p.employee.department.id = :departmentId)")
    List<Payroll> findByYearAndDepartment(
            @Param("year") Integer year,
            @Param("departmentId") Long departmentId
    );
}
