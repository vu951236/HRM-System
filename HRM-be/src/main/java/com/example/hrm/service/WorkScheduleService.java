package com.example.hrm.service;

import com.example.hrm.dto.request.WorkScheduleRequest;
import com.example.hrm.dto.response.WorkScheduleResponse;
import com.example.hrm.entity.EmployeeRecord;
import com.example.hrm.entity.Shift;
import com.example.hrm.entity.WorkSchedule;
import com.example.hrm.mapper.WorkScheduleMapper;
import com.example.hrm.repository.EmployeeRecordRepository;
import com.example.hrm.repository.ShiftRepository;
import com.example.hrm.repository.WorkScheduleRepository;
import lombok.RequiredArgsConstructor;
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

    public WorkScheduleResponse createWorkSchedule(WorkScheduleRequest request) {
        WorkSchedule ws = mapper.toEntity(request);

        EmployeeRecord emp = employeeRepository.findById(request.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
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
        WorkSchedule ws = workScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkSchedule not found"));

        mapper.updateEntityFromRequest(request, ws);

        if(request.getEmployeeId() != null) {
            EmployeeRecord emp = employeeRepository.findById(request.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            ws.setEmployee(emp);
        }

        if(request.getShiftId() != null) {
            Shift shift = shiftRepository.findById(request.getShiftId())
                    .orElseThrow(() -> new RuntimeException("Shift not found"));
            ws.setShift(shift);
        }

        ws.setUpdatedAt(LocalDateTime.now());
        WorkSchedule updated = workScheduleRepository.save(ws);
        return mapper.toResponse(updated);
    }

    public List<WorkScheduleResponse> getAllSchedules() {
        return workScheduleRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public WorkScheduleResponse getScheduleById(Integer id) {
        WorkSchedule ws = workScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkSchedule not found"));
        return mapper.toResponse(ws);
    }

    public void deleteWorkSchedule(Integer id) {
        WorkSchedule ws = workScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkSchedule not found"));
        ws.setIsDelete(true);
        workScheduleRepository.save(ws);
    }

    public void restoreWorkSchedule(Integer id) {
        WorkSchedule ws = workScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WorkSchedule not found"));
        ws.setIsDelete(false);
        ws.setUpdatedAt(LocalDateTime.now());
        workScheduleRepository.save(ws);
    }

}
