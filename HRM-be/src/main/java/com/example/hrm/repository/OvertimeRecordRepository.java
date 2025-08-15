package com.example.hrm.repository;

import com.example.hrm.entity.EmployeeRecord;
import com.example.hrm.entity.OvertimeRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface OvertimeRecordRepository extends JpaRepository<OvertimeRecord, Integer> {
    List<OvertimeRecord> findAllByIsDeleteFalse();
    List<OvertimeRecord> findAllByEmployeeAndIsDeleteFalse(EmployeeRecord employee);

    List<OvertimeRecord> findAllByDateAndIsDeleteFalse(LocalDate date);
}
