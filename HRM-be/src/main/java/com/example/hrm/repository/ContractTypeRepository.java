package com.example.hrm.repository;

import com.example.hrm.entity.ContractType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ContractTypeRepository extends JpaRepository<ContractType, Integer> {
    Optional<ContractType> findByName(String name);
}
