package com.example.hrm.repository;

import com.example.hrm.entity.AttendanceErrorLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceErrorLogRepository extends JpaRepository<AttendanceErrorLog, Long> {
}
