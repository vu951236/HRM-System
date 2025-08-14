package com.example.hrm.repository;

import com.example.hrm.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Integer> {

    @Query("SELECT c FROM Contract c WHERE c.isDelete = false")
    List<Contract> findAllActive();

    @Query("SELECT c FROM Contract c WHERE c.endDate < :today AND c.status = 'ACTIVE'")
    List<Contract> findExpiredContracts(LocalDate today);
}
