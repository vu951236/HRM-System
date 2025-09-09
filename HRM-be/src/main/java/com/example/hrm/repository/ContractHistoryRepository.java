package com.example.hrm.repository;

import com.example.hrm.entity.ContractHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContractHistoryRepository extends JpaRepository<ContractHistory, Integer> {
    List<ContractHistory> findByContract_IdOrderByChangedAtDesc(Integer contractId);

    boolean existsByContractId(Integer contractId);
}
