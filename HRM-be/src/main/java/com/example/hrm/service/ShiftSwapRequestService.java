package com.example.hrm.service;

import com.example.hrm.dto.request.ShiftSwapRequestRequest;
import com.example.hrm.dto.response.ShiftSwapRequestResponse;
import com.example.hrm.entity.EmployeeRecord;
import com.example.hrm.entity.Shift;
import com.example.hrm.entity.ShiftSwapRequest;
import com.example.hrm.entity.User;
import com.example.hrm.mapper.ShiftSwapRequestMapper;
import com.example.hrm.repository.EmployeeRecordRepository;
import com.example.hrm.repository.ShiftRepository;
import com.example.hrm.repository.ShiftSwapRequestRepository;
import com.example.hrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftSwapRequestService {

    private final ShiftSwapRequestRepository repository;
    private final EmployeeRecordRepository employeeRepository;
    private final ShiftRepository shiftRepository;
    private final UserRepository userRepository;
    private final ShiftSwapRequestMapper mapper;

    public ShiftSwapRequestResponse createRequest(ShiftSwapRequestRequest requestDto) {
        EmployeeRecord requester = employeeRepository.findById(requestDto.getRequesterId())
                .orElseThrow(() -> new RuntimeException("Requester not found"));
        EmployeeRecord targetEmployee = employeeRepository.findById(requestDto.getTargetEmployeeId())
                .orElseThrow(() -> new RuntimeException("Target employee not found"));
        Shift requestedShift = shiftRepository.findById(requestDto.getRequestedShiftId())
                .orElseThrow(() -> new RuntimeException("Requested shift not found"));
        Shift targetShift = shiftRepository.findById(requestDto.getTargetShiftId())
                .orElseThrow(() -> new RuntimeException("Target shift not found"));

        ShiftSwapRequest request = ShiftSwapRequest.builder()
                .requester(requester)
                .requestedShift(requestedShift)
                .targetEmployee(targetEmployee)
                .targetShift(targetShift)
                .reason(requestDto.getReason())
                .status(ShiftSwapRequest.Status.pending)
                .createdAt(LocalDateTime.now())
                .isDelete(false)
                .build();

        return mapper.toResponse(repository.save(request));
    }

    public ShiftSwapRequestResponse approveRequest(Integer id, Integer approverId) {
        ShiftSwapRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        request.setStatus(ShiftSwapRequest.Status.approved);
        request.setApprovedBy(approver);
        request.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(repository.save(request));
    }

    public ShiftSwapRequestResponse rejectRequest(Integer id, Integer approverId) {
        ShiftSwapRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        User approver = userRepository.findById(approverId)
                .orElseThrow(() -> new RuntimeException("Approver not found"));

        request.setStatus(ShiftSwapRequest.Status.rejected);
        request.setApprovedBy(approver);
        request.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(repository.save(request));
    }

    public void deleteRequest(Integer id) {
        ShiftSwapRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setIsDelete(true);
        repository.save(request);
    }

    public void restoreRequest(Integer id) {
        ShiftSwapRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setIsDelete(false);
        repository.save(request);
    }

    public List<ShiftSwapRequestResponse> getAll() {
        return repository.findAllByIsDeleteFalse()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    public List<ShiftSwapRequestResponse> getAllByStatus(ShiftSwapRequest.Status status) {
        return repository.findAllByStatusAndIsDeleteFalse(status)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
