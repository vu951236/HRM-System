package com.example.hrm.repository;

import com.example.hrm.entity.Position;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PositionRepository extends JpaRepository<Position, Integer> {
    Optional<Position> findByName(String name);

    Optional<Position> findByNameIgnoreCase(String name);

}
