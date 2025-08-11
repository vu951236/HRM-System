package com.example.hrm.repository;

import com.example.hrm.entity.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Integer> {
    Optional<EmployeeProfile> findByUserId(Integer userId);

}
