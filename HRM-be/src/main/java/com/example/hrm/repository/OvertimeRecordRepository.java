package com.example.hrm.repository;

import com.example.hrm.entity.EmployeeRecord;
import com.example.hrm.entity.OvertimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface OvertimeRecordRepository extends JpaRepository<OvertimeRecord, Integer> {
    List<OvertimeRecord> findAllByDateAndIsDeleteFalse(LocalDate date);

    List<OvertimeRecord> findAllByDateAndEmployee_User_IdAndIsDeleteFalse(LocalDate date, Integer id);

    List<OvertimeRecord> findByEmployeeIdAndDateBetween(Integer employeeId, LocalDate start, LocalDate end);

    @Query("SELECT o FROM OvertimeRecord o " +
            "WHERE o.date BETWEEN :startDate AND :endDate " +
            "AND o.isDelete = false " +
            "AND o.status = com.example.hrm.entity.OvertimeRecord.Status.approved " +
            "AND (:departmentId IS NULL OR o.employee.department.id = :departmentId)")
    List<OvertimeRecord> findByDateRangeAndDepartment(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("departmentId") Long departmentId
    );
}
