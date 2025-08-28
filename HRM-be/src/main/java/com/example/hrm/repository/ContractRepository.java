package com.example.hrm.repository;

import com.example.hrm.dto.response.EmployeeContractChartResponse;
import com.example.hrm.entity.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ContractRepository extends JpaRepository<Contract, Integer> {

    @Query("SELECT c FROM Contract c WHERE c.isDelete = false")
    List<Contract> findAllActive();

    @Query("SELECT c FROM Contract c WHERE c.endDate < :today AND c.status = 'ACTIVE'")
    List<Contract> findExpiredContracts(LocalDate today);

    @Query("SELECT new com.example.hrm.dto.response.EmployeeContractChartResponse(ct.name, COUNT(c)) " +
            "FROM Contract c " +
            "JOIN c.contractType ct " +
            "WHERE (:contractTypeId IS NULL OR ct.id = :contractTypeId) " +
            "GROUP BY ct.name")
    List<EmployeeContractChartResponse> countContractByType(@Param("contractTypeId") Integer contractTypeId);

    @Query("SELECT new com.example.hrm.dto.response.EmployeeContractChartResponse(ct.name, COUNT(c)) " +
            "FROM Contract c " +
            "JOIN c.contractType ct " +
            "WHERE c.endDate <= :expiryDate " +
            "GROUP BY ct.name")
    List<EmployeeContractChartResponse> countContractExpiringBefore(@Param("expiryDate") LocalDate expiryDate);
}
