package com.example.hrm.repository;

import com.example.hrm.entity.LeavePolicy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeavePolicyRepository extends JpaRepository<LeavePolicy, Long> {
    List<LeavePolicy> findByRole_IdAndPosition_Id(Integer roleId, Long positionId);

    List<LeavePolicy> findAllByIsDeleteFalse();
}
