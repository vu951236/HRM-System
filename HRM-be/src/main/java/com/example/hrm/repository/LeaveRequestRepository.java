package com.example.hrm.repository;

import com.example.hrm.entity.EmployeeRecord;
import com.example.hrm.entity.LeaveRequest;
import com.example.hrm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, Long> {
    List<LeaveRequest> findByEmployee_IdAndStartDateBetween(
            Integer employeeId,
            LocalDate startDate,
            LocalDate endDate
    );

    boolean existsByEmployeeAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
            EmployeeRecord employee,
            String status,
            LocalDate startDate,
            LocalDate endDate
    );

    @Query("SELECT l FROM LeaveRequest l " +
            "WHERE l.startDate <= :endDate AND l.endDate >= :startDate " +
            "AND l.isDelete = false " +
            "AND (:departmentId IS NULL OR l.employee.department.id = :departmentId)")
    List<LeaveRequest> findByDateRangeAndDepartment(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("departmentId") Long departmentId
    );
}
