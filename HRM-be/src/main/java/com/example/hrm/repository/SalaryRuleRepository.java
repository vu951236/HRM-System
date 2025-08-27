package com.example.hrm.repository;

import com.example.hrm.entity.SalaryRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SalaryRuleRepository extends JpaRepository<SalaryRule, Long> {
    List<SalaryRule> findByActiveTrueAndIsDeleteFalse();

    List<SalaryRule> findAllByIsDeleteFalse();

    Optional<SalaryRule> findByRuleType(SalaryRule.RuleType ruleType);
}
