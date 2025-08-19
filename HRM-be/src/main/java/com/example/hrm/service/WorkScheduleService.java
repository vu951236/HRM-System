package com.example.hrm.service;

import com.example.hrm.dto.request.WorkScheduleRequest;
import com.example.hrm.dto.response.WorkScheduleResponse;
import com.example.hrm.entity.*;
import com.example.hrm.mapper.WorkScheduleMapper;
import com.example.hrm.repository.EmployeeRecordRepository;
import com.example.hrm.repository.ShiftRepository;
import com.example.hrm.repository.WorkScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkScheduleService {

    private final WorkScheduleRepository workScheduleRepository;
    private final EmployeeRecordRepository employeeRepository;
    private final ShiftRepository shiftRepository;
    private final WorkScheduleMapper mapper;
    private final PermissionChecker permissionChecker;

    public WorkScheduleResponse createWorkSchedule(WorkScheduleRequest request) {
        permissionChecker.checkAdminOrHrRole();

        EmployeeRecord emp = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        permissionChecker.checkEmployeeRecordPermission(emp.getUser().getId());

        if (emp.getUser() == null || Boolean.FALSE.equals(emp.getUser().getIsActive())) {
            throw new RuntimeException("Employee is inactive, cannot create schedule");
        }

        if (Boolean.TRUE.equals(emp.getIsDelete())) {
            throw new RuntimeException("Employee record is deleted, cannot create schedule");
        }

        WorkSchedule ws = mapper.toEntity(request);
        ws.setEmployee(emp);

        Shift shift = shiftRepository.findById(request.getShiftId())
                .orElseThrow(() -> new RuntimeException("Shift not found"));
        ws.setShift(shift);

        ws.setCreatedAt(LocalDateTime.now());
        ws.setUpdatedAt(LocalDateTime.now());

        WorkSchedule saved = workScheduleRepository.save(ws);
        return mapper.toResponse(saved);
    }

    public WorkScheduleResponse updateWorkSchedule(Integer id, WorkScheduleRequest request) {
        permissionChecker.checkAdminOrHrRole();

        WorkSchedule ws = workScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkSchedule not found"));

        if (ws.getEmployee() != null && ws.getEmployee().getUser() != null) {
            permissionChecker.checkEmployeeRecordPermission(ws.getEmployee().getUser().getId());
        }

        if (request.getEmployeeId() != null) {
            EmployeeRecord emp = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));

            permissionChecker.checkEmployeeRecordPermission(emp.getUser().getId());
            ws.setEmployee(emp);
        }

        if (request.getShiftId() != null) {
            Shift shift = shiftRepository.findById(request.getShiftId())
                    .orElseThrow(() -> new RuntimeException("Shift not found"));
            ws.setShift(shift);
        }

        mapper.updateEntityFromRequest(request, ws);
        ws.setUpdatedAt(LocalDateTime.now());

        WorkSchedule updated = workScheduleRepository.save(ws);
        return mapper.toResponse(updated);
    }

    public List<WorkScheduleResponse> getAllSchedules() {
        Role currentRole = permissionChecker.getCurrentUserRole();
        User currentUser = permissionChecker.getCurrentUser();
        List<WorkSchedule> schedules;

        if ("admin".equalsIgnoreCase(currentRole.getName())) {
            schedules = workScheduleRepository.findAll();
        } else if ("hr".equalsIgnoreCase(currentRole.getName())) {
            schedules = workScheduleRepository.findAll()
                    .stream()
                    .filter(ws -> !ws.getIsDelete())
                    .filter(ws -> {
                        User empUser = ws.getEmployee().getUser();
                        if (empUser == null) return false;
                        String empRole = empUser.getRole().getName();
                        return empUser.getId().equals(currentUser.getId()) || "staff".equalsIgnoreCase(empRole);
                    })
                    .toList();
        } else {
            throw new RuntimeException("Bạn không có quyền xem lịch làm việc");
        }

        return schedules.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public WorkScheduleResponse getScheduleById(Integer id) {
        WorkSchedule ws = workScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkSchedule not found"));

        Role currentRole = permissionChecker.getCurrentUserRole();
        User currentUser = permissionChecker.getCurrentUser();

        if ("admin".equalsIgnoreCase(currentRole.getName())) {
            return mapper.toResponse(ws);
        } else if ("hr".equalsIgnoreCase(currentRole.getName())) {
            User empUser = ws.getEmployee().getUser();
            if (empUser != null && !ws.getIsDelete() &&
                    (empUser.getId().equals(currentUser.getId()) || "staff".equalsIgnoreCase(empUser.getRole().getName()))) {
                return mapper.toResponse(ws);
            } else {
                throw new RuntimeException("Bạn không có quyền xem lịch làm việc này");
            }
        } else {
            throw new RuntimeException("Bạn không có quyền xem lịch làm việc");
        }
    }

    @PreAuthorize("hasRole('admin')")
    public void deleteWorkSchedule(Integer id) {
        WorkSchedule ws = workScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkSchedule not found"));
        ws.setIsDelete(true);
        workScheduleRepository.save(ws);
    }

    @PreAuthorize("hasRole('admin')")
    public void restoreWorkSchedule(Integer id) {
        WorkSchedule ws = workScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkSchedule not found"));
        ws.setIsDelete(false);
        ws.setUpdatedAt(LocalDateTime.now());
        workScheduleRepository.save(ws);
    }
}
