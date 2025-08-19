package com.example.hrm.repository;

import com.example.hrm.entity.EmployeeRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmployeeRecordRepository extends JpaRepository<EmployeeRecord, Integer> {

    @Query("SELECT e FROM EmployeeRecord e JOIN e.user u JOIN u.role r WHERE r.name IN :roles")
    List<EmployeeRecord> findByUserRoles(@Param("roles") List<String> roles);

    @Query("SELECT e FROM EmployeeRecord e JOIN e.user u JOIN u.role r WHERE r.name = :role AND e.isDelete = false")
    List<EmployeeRecord> findByUserRoleAndNotDeleted(@Param("role") String role);


    @Query("SELECT e FROM EmployeeRecord e WHERE e.department.id = :deptId AND e.isDelete = false")
    List<EmployeeRecord> findAllByDepartmentIdAndIsDeleteFalse(@Param("deptId") Long deptId);

    List<EmployeeRecord> findByDepartmentIdAndIdNot(Long id, Integer requesterId);

    @Query("SELECT e FROM EmployeeRecord e WHERE e.department.id = :deptId AND e.isDelete = false AND e.user.isActive = true")
    List<EmployeeRecord> findActiveByDepartmentId(@Param("deptId") Integer deptId);

    Optional<EmployeeRecord> findByUser_IdAndIsDeleteFalse(Integer userId);

    Optional<EmployeeRecord> findByUser_Id(Integer userId);
}
