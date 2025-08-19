package com.example.hrm.repository;

import com.example.hrm.entity.ShiftRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShiftRuleRepository extends JpaRepository<ShiftRule, Integer> {
    List<ShiftRule> findAllByIsDeleteFalse();
}