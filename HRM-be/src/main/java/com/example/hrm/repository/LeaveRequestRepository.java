package com.example.hrm.repository;

import com.example.hrm.entity.EmployeeRecord;
import com.example.hrm.entity.LeaveRequest;
import com.example.hrm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

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


    List<LeaveRequest> findAllByEmployee_User_IdAndIsDeleteFalse(Integer userId);

    List<LeaveRequest> findAllByIsDeleteFalse();
}
