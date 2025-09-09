package com.example.hrm.service;

import com.example.hrm.dto.response.LeaveRequestResponse;
import com.example.hrm.entity.EmployeeRecord;
import com.example.hrm.entity.LeavePolicy;
import com.example.hrm.entity.LeaveRequest;
import com.example.hrm.entity.User;
import com.example.hrm.mapper.LeaveRequestMapper;
import com.example.hrm.repository.EmployeeRecordRepository;
import com.example.hrm.repository.LeavePolicyRepository;
import com.example.hrm.repository.LeaveRequestRepository;
import com.example.hrm.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LeaveRequestService {

    private final LeaveRequestRepository repository;
    private final EmployeeRecordRepository employeeRepository;
    private final LeavePolicyRepository policyRepository;
    private final LeaveRequestMapper mapper;
    private final PermissionChecker permissionChecker;

    public LeaveRequestResponse createLeave(Integer policyId, LocalDate startDate, LocalDate endDate, String reason) {
        User currentUser = permissionChecker.getCurrentUser();

        String roleName = currentUser.getRole().getName();
        if (!("staff".equalsIgnoreCase(roleName) || "hr".equalsIgnoreCase(roleName))) {
            throw new RuntimeException("Chỉ staff và hr được tạo đơn nghỉ");
        }

        EmployeeRecord emp = employeeRepository.findByUser_IdAndIsDeleteFalse(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        LeavePolicy policy = policyRepository.findById(Long.valueOf(policyId))
                .orElseThrow(() -> new RuntimeException("Leave policy not found"));

        if (policy.getRole() != null && !policy.getRole().getId().equals(currentUser.getRole().getId())) {
            throw new RuntimeException("Chính sách nghỉ này không áp dụng cho role của bạn");
        }
        if (policy.getPosition() != null && !policy.getPosition().getId().equals(emp.getPosition().getId())) {
            throw new RuntimeException("Chính sách nghỉ này không áp dụng cho vị trí của bạn");
        }

        int requestedDays = (int) (endDate.toEpochDay() - startDate.toEpochDay() + 1);
        if (requestedDays > policy.getMaxDays()) {
            throw new RuntimeException("Số ngày nghỉ vượt quá giới hạn chính sách");
        }

        LeaveRequest leave = LeaveRequest.builder()
                .employee(emp)
                .leavePolicy(policy)
                .startDate(startDate)
                .endDate(endDate)
                .reason(reason)
                .status(LeaveRequest.Status.pending)
                .isDelete(false)
                .createdAt(LocalDateTime.now())
                .build();

        return mapper.toResponse(repository.save(leave));
    }

    public LeaveRequestResponse approveLeave(Integer id) {
        LeaveRequest leave = repository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        permissionChecker.checkRequestPermission(leave);

        leave.setStatus(LeaveRequest.Status.approved);
        leave.setApprovedBy(permissionChecker.getCurrentUser());

        return mapper.toResponse(repository.save(leave));
    }

    public LeaveRequestResponse rejectLeave(Integer id) {
        LeaveRequest leave = repository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Leave request not found"));

        permissionChecker.checkRequestPermission(leave);

        leave.setStatus(LeaveRequest.Status.rejected);
        leave.setApprovedBy(permissionChecker.getCurrentUser());

        return mapper.toResponse(repository.save(leave));
    }


    @Transactional
    public void deleteLeave(Integer id) {
        User currentUser = permissionChecker.getCurrentUser();
        if (!"admin".equalsIgnoreCase(currentUser.getRole().getName())) {
            throw new RuntimeException("Chỉ admin mới được xoá đơn nghỉ");
        }

        LeaveRequest leave = repository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nghỉ với id: " + id));

        leave.setIsDelete(true);
        repository.save(leave);
    }

    @Transactional
    public LeaveRequestResponse restoreLeave(Integer id) {
        User currentUser = permissionChecker.getCurrentUser();
        if (!"admin".equalsIgnoreCase(currentUser.getRole().getName())) {
            throw new RuntimeException("Chỉ admin mới được khôi phục đơn nghỉ");
        }

        LeaveRequest leave = repository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn nghỉ với id: " + id));

        if (!Boolean.TRUE.equals(leave.getIsDelete())) {
            throw new RuntimeException("Đơn nghỉ này chưa bị xoá, không cần khôi phục");
        }

        leave.setIsDelete(false);
        return mapper.toResponse(repository.save(leave));
    }

    public List<LeaveRequestResponse> getAllLeaveRequests() {
        User currentUser = permissionChecker.getCurrentUser();
        String role = currentUser.getRole().getName();

        EmployeeRecord currentRecord = null;
        if (!"admin".equalsIgnoreCase(role)) {
            currentRecord = employeeRepository.findByUser_IdAndIsDeleteFalse(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("EmployeeRecord not found"));
        }

        List<LeaveRequest> allRequests = repository.findAll()
                .stream()
                .filter(r -> !Boolean.TRUE.equals(r.getIsDelete()))
                .toList();

        List<LeaveRequest> filtered = permissionChecker.filterRecordsByPermission(
                allRequests,
                currentUser,
                currentRecord
        );

        return filtered.stream()
                .map(mapper::toResponse)
                .toList();
    }


    public List<LeaveRequestResponse> getLeaveByDate(LocalDate date) {
        return repository.findAll()
                .stream()
                .filter(r -> !date.isBefore(r.getStartDate()) && !date.isAfter(r.getEndDate()))
                .map(mapper::toResponse)
                .toList();
    }
}
