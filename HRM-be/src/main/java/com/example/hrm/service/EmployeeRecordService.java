package com.example.hrm.service;

import com.example.hrm.dto.request.EmployeeRecordRequest;
import com.example.hrm.dto.response.EmployeeRecordResponse;
import com.example.hrm.entity.EmployeeRecord;
import com.example.hrm.entity.User;
import com.example.hrm.mapper.EmployeeRecordMapper;
import com.example.hrm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeRecordService {

    private final EmployeeRecordRepository employeeRecordRepository;
    private final UserRepository userRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeTypeRepository employeeTypeRepository;
    private final EmployeeRecordMapper employeeRecordMapper;
    private final PermissionChecker permissionChecker;

    public EmployeeRecordResponse createEmployeeRecord(EmployeeRecordRequest request) {
        if (request.getUserId() == null) {
            throw new RuntimeException("UserId không được null");
        }
        permissionChecker.checkEmployeeRecordPermission(request.getUserId());

        EmployeeRecord record = new EmployeeRecord();
        setFields(record, request);
        record.setCreatedAt(LocalDateTime.now());
        record.setUpdatedAt(LocalDateTime.now());
        employeeRecordRepository.save(record);
        return employeeRecordMapper.toResponse(record);
    }

    public EmployeeRecordResponse updateEmployeeRecord(Integer id, EmployeeRecordRequest request) {
        if (request.getUserId() == null) {
            throw new RuntimeException("UserId không được null");
        }
        permissionChecker.checkEmployeeRecordPermission(request.getUserId());

        EmployeeRecord record = employeeRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee record not found"));
        setFields(record, request);
        record.setUpdatedAt(LocalDateTime.now());
        employeeRecordRepository.save(record);
        return employeeRecordMapper.toResponse(record);
    }

    private void setFields(EmployeeRecord record, EmployeeRecordRequest request) {
        record.setEmployeeCode(generateEmployeeCode(
                request.getDepartmentName(),
                request.getPositionName(),
                request.getEmploymentTypeName(),
                request.getUserId()
        ));

        record.setUser(request.getUserId() != null
                ? userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"))
                : null);

        if (request.getUserId() != null) {
            record.setProfile(
                    employeeProfileRepository.findByUserId(request.getUserId())
                            .orElseThrow(() -> new RuntimeException("Profile not found for userId: " + request.getUserId()))
            );
        } else {
            record.setProfile(null);
        }

        record.setDepartment(request.getDepartmentName() != null
                ? departmentRepository.findByName(request.getDepartmentName())
                .orElseThrow(() -> new RuntimeException("Department not found"))
                : null);

        record.setPosition(request.getPositionName() != null
                ? positionRepository.findByName(request.getPositionName())
                .orElseThrow(() -> new RuntimeException("Position not found"))
                : null);

        record.setEmploymentType(request.getEmploymentTypeName() != null
                ? employeeTypeRepository.findByName(request.getEmploymentTypeName())
                .orElseThrow(() -> new RuntimeException("Employment type not found"))
                : null);

        record.setSupervisor(request.getSupervisorId() != null
                ? userRepository.findById(request.getSupervisorId())
                .orElseThrow(() -> new RuntimeException("Supervisor not found"))
                : null);

        record.setHireDate(request.getHireDate());
        record.setTerminationDate(request.getTerminationDate());
        record.setWorkLocation(request.getWorkLocation());
        record.setNote(request.getNote());
    }


    private String generateEmployeeCode(String departmentName, String positionName, String employmentTypeName, Integer userId) {
        String deptCode = (departmentName != null && !departmentName.isEmpty()) ? departmentName.substring(0, 3).toUpperCase() : "XXX";
        String posCode = (positionName != null && !positionName.isEmpty()) ? positionName.substring(0, 3).toUpperCase() : "XXX";
        String empTypeCode = (employmentTypeName != null && !employmentTypeName.isEmpty()) ? employmentTypeName.substring(0, 3).toUpperCase() : "XXX";

        String userPart = (userId != null) ? String.format("%04d", userId) : "0000";

        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));

        return deptCode + "-" + posCode + "-" + empTypeCode + "-" + userPart + "-" + timestamp;
    }

    public List<EmployeeRecordResponse> getAllRecordsByRole() {
        User currentUser = permissionChecker.getCurrentUser();
        String currentRole = currentUser.getRole().getName();

        List<EmployeeRecord> records;

        if ("admin".equalsIgnoreCase(currentRole)) {
            records = employeeRecordRepository.findByUserRoles(List.of("hr", "staff"));
        } else if ("HR".equalsIgnoreCase(currentRole)) {
            records = employeeRecordRepository.findByUserRoleAndNotDeleted("staff");
        } else {
            throw new RuntimeException("Bạn không có quyền xem hồ sơ này");
        }

        return records.stream()
                .map(employeeRecordMapper::toResponse)
                .collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('admin')")
    public void softDeleteRecord(Integer id) {
        EmployeeRecord record = employeeRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee record not found"));
        record.setIsDelete(true);
        record.setUpdatedAt(LocalDateTime.now());
        employeeRecordRepository.save(record);
    }

    @PreAuthorize("hasRole('admin')")
    public void restoreRecord(Integer id) {
        EmployeeRecord record = employeeRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee record not found"));
        record.setIsDelete(false);
        record.setUpdatedAt(LocalDateTime.now());
        employeeRecordRepository.save(record);
    }


}
