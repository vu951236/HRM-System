package com.example.hrm.repository;

import com.example.hrm.entity.EmployeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRecordRepository extends JpaRepository<EmployeeRecord, Integer> {
    @Query("SELECT er FROM EmployeeRecord er WHERE er.user.role.name = :roleName")
    List<EmployeeRecord> findByUserRole(@Param("roleName") String roleName);

    // Tất cả record của user có role trong danh sách (kể cả xóa)
    @Query("SELECT e FROM EmployeeRecord e JOIN e.user u JOIN u.role r WHERE r.name IN :roles")
    List<EmployeeRecord> findByUserRoles(@Param("roles") List<String> roles);

    // Các record chưa bị xóa của user có role
    @Query("SELECT e FROM EmployeeRecord e JOIN e.user u JOIN u.role r WHERE r.name = :role AND e.isDelete = false")
    List<EmployeeRecord> findByUserRoleAndNotDeleted(@Param("role") String role);
}
