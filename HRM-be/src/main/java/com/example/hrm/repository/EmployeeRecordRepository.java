package com.example.hrm.repository;

import com.example.hrm.entity.EmployeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRecordRepository extends JpaRepository<EmployeeRecord, Integer> {
    @Query("SELECT er FROM EmployeeRecord er WHERE er.user.role.name = :roleName")
    List<EmployeeRecord> findByUserRole(@Param("roleName") String roleName);

    @Query("SELECT er FROM EmployeeRecord er WHERE er.user.role.name IN :roleNames")
    List<EmployeeRecord> findByUserRoles(@Param("roleNames") List<String> roleNames);
}
