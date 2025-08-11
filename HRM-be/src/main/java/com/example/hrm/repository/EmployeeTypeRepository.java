package com.example.hrm.repository;

import com.example.hrm.entity.EmployeeType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeTypeRepository extends JpaRepository<EmployeeType, Integer> {
    Optional<EmployeeType> findByName(String name);
}
