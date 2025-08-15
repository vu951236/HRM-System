package com.example.hrm.repository;

import com.example.hrm.entity.ShiftSwapRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftSwapRequestRepository extends JpaRepository<ShiftSwapRequest, Integer> {
    List<ShiftSwapRequest> findAllByStatus(ShiftSwapRequest.Status status);

    List<ShiftSwapRequest> findAllByStatusAndIsDeleteFalse(ShiftSwapRequest.Status status);

    List<ShiftSwapRequest> findAllByIsDeleteFalse();
}
