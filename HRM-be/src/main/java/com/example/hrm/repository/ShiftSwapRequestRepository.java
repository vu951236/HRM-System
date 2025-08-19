package com.example.hrm.repository;

import com.example.hrm.entity.ShiftSwapRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface ShiftSwapRequestRepository extends JpaRepository<ShiftSwapRequest, Integer> {
    List<ShiftSwapRequest> findAllByStatus(ShiftSwapRequest.Status status);

    List<ShiftSwapRequest> findAllByRequester_User_IdAndIsDeleteFalse(Integer userId);

    List<ShiftSwapRequest> findAllByRequester_User_Role_NameAndIsDeleteFalse(String roleName);

    List<ShiftSwapRequest> findAllByStatusAndIsDeleteFalseAndRequester_Department_Id(ShiftSwapRequest.Status status, Long aLong);

    List<ShiftSwapRequest> findAllByStatusAndIsDeleteFalseAndRequester_User_Id(ShiftSwapRequest.Status status, Integer id);
}
