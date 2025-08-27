package com.example.hrm.repository;

import com.example.hrm.entity.EmployeeRecord;
import com.example.hrm.entity.OvertimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface OvertimeRecordRepository extends JpaRepository<OvertimeRecord, Integer> {
    List<OvertimeRecord> findAllByIsDeleteFalse();
    List<OvertimeRecord> findAllByEmployeeAndIsDeleteFalse(EmployeeRecord employee);

    List<OvertimeRecord> findAllByDateAndIsDeleteFalse(LocalDate date);

    List<OvertimeRecord> findAllByEmployee_User_IdAndIsDeleteFalse(Integer id);

    List<OvertimeRecord> findAllByDateAndEmployee_User_IdAndIsDeleteFalse(LocalDate date, Integer id);

    List<OvertimeRecord> findByEmployeeUserIdAndDateBetween(Integer userId, LocalDate start, LocalDate end);

    List<OvertimeRecord> findByEmployeeIdAndDateBetween(Integer employeeId, LocalDate start, LocalDate end);
}
