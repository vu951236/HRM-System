package com.example.hrm.service;

import com.example.hrm.dto.response.OvertimeRecordResponse;
import com.example.hrm.entity.EmployeeRecord;
import com.example.hrm.entity.OvertimeRecord;
import com.example.hrm.entity.User;
import com.example.hrm.mapper.OvertimeRecordMapper;
import com.example.hrm.repository.EmployeeRecordRepository;
import com.example.hrm.repository.OvertimeRecordRepository;
import com.example.hrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OvertimeRecordService {

    private final OvertimeRecordRepository repository;
    private final EmployeeRecordRepository employeeRepository;
    private final UserRepository userRepository;
    private final OvertimeRecordMapper mapper;

    public OvertimeRecordResponse createOvertime(Integer employeeId, LocalDate date, LocalTime start, LocalTime end, String reason) {
        EmployeeRecord emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        OvertimeRecord record = OvertimeRecord.builder()
                .employee(emp)
                .date(date)
                .startTime(start)
                .endTime(end)
                .reason(reason)
                .status(OvertimeRecord.Status.pending)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDelete(false)
                .build();

        return mapper.toResponse(repository.save(record));
    }

    public OvertimeRecordResponse approveOvertime(Integer id, Integer approverId) {
        OvertimeRecord record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Overtime not found"));
        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        record.setStatus(OvertimeRecord.Status.approved);
        record.setApprovedBy(approver);
        record.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(repository.save(record));
    }

    public OvertimeRecordResponse rejectOvertime(Integer id, Integer approverId) {
        OvertimeRecord record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Overtime not found"));
        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        record.setStatus(OvertimeRecord.Status.rejected);
        record.setApprovedBy(approver);
        record.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(repository.save(record));
    }

    public void deleteOvertime(Integer id) {
        OvertimeRecord record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Overtime not found"));
        record.setIsDelete(true);
        record.setUpdatedAt(LocalDateTime.now());
        repository.save(record);
    }

    public void restoreOvertime(Integer id) {
        OvertimeRecord record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Overtime not found"));
        record.setIsDelete(false);
        record.setUpdatedAt(LocalDateTime.now());
        repository.save(record);
    }

    public List<OvertimeRecordResponse> getAllOvertime() {
        return repository.findAllByIsDeleteFalse()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<OvertimeRecordResponse> getOvertimeByEmployee(Integer employeeId) {
        EmployeeRecord emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        return repository.findAllByEmployeeAndIsDeleteFalse(emp)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<OvertimeRecordResponse> getOvertimeByDate(LocalDate date) {
        return repository.findAllByDateAndIsDeleteFalse(date)
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }
}
