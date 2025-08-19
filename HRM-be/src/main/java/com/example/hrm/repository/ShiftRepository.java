package com.example.hrm.repository;

import com.example.hrm.entity.Shift;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShiftRepository extends JpaRepository<Shift, Integer> {
    List<Shift> findAllByIsDeleteFalse();
}