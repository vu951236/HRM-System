package com.example.hrm.controller;

import com.example.hrm.dto.response.EmployeeRecordDataResponse;
import com.example.hrm.dto.response.ShiftSwapOptionsDataResponse;
import com.example.hrm.entity.*;
import com.example.hrm.repository.*;
import com.example.hrm.dto.response.ApiResponse;
import com.example.hrm.service.ShiftSwapRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DataController {

    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final EmployeeTypeRepository employeeTypeRepository;
    private final ContractTypeRepository contractTypeRepository;
    private final ShiftRuleRepository shiftRuleRepository;
    private final EmployeeRecordRepository employeeRecordRepository;
    private final ShiftRepository shiftRepository;
    private final ShiftSwapRequestService shiftSwapRequestService;
    private final UserRepository userRepository;
    private final LeavePolicyRepository leavePolicyRepository;

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<Department>>> getDepartments() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String role = currentUser.getRole().getName();

        List<Department> departments = departmentRepository.findAll();

        if ("hr".equalsIgnoreCase(role)) {
            departments = departments.stream()
                    .filter(dept -> !"Human Resources Department".equalsIgnoreCase(dept.getName()))
                    .toList();
        }

        return ResponseEntity.ok(ApiResponse.<List<Department>>builder()
                .data(departments)
                .build());
    }

    @GetMapping("/positions")
    public ResponseEntity<ApiResponse<List<Position>>> getPositions() {
        List<Position> positions = positionRepository.findAll();
        return ResponseEntity.ok(ApiResponse.<List<Position>>builder()
                .data(positions)
                .build());
    }

    @GetMapping("/employeetypes")
    public ResponseEntity<ApiResponse<List<EmployeeType>>> getEmployeeTypes() {
        List<EmployeeType> employeeTypes = employeeTypeRepository.findAll();
        return ResponseEntity.ok(ApiResponse.<List<EmployeeType>>builder()
                .data(employeeTypes)
                .build());
    }

    @GetMapping("/contracttypes")
    public ResponseEntity<ApiResponse<List<ContractType>>> getContractTypes() {
        List<ContractType> contractTypes = contractTypeRepository.findAll();
        return ResponseEntity.ok(ApiResponse.<List<ContractType>>builder()
                .data(contractTypes)
                .build());
    }

    @GetMapping("/shiftrules")
    public ResponseEntity<ApiResponse<List<ShiftRule>>> getShiftRules() {
        List<ShiftRule> shiftRules = shiftRuleRepository.findAll();
        return ResponseEntity.ok(ApiResponse.<List<ShiftRule>>builder()
                .data(shiftRules)
                .build());
    }

    @GetMapping("/employees")
    public ResponseEntity<ApiResponse<List<EmployeeRecordDataResponse>>> getEmployees() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String role = currentUser.getRole().getName();

        List<EmployeeRecordDataResponse> employees;

        if ("admin".equalsIgnoreCase(role)) {
            employees = employeeRecordRepository.findAll().stream()
                    .filter(er -> Boolean.FALSE.equals(er.getIsDelete()))
                    .map(er -> EmployeeRecordDataResponse.builder()
                            .id(er.getId())
                            .employeeCode(er.getEmployeeCode())
                            .fullName(er.getProfile() != null ? er.getProfile().getFullName() : null)
                            .build())
                    .toList();
        } else if ("hr".equalsIgnoreCase(role)) {
            employees = employeeRecordRepository.findAll().stream()
                    .filter(er -> Boolean.FALSE.equals(er.getIsDelete()))
                    .filter(er -> er.getUser().getRole() != null && "staff".equalsIgnoreCase(er.getUser().getRole().getName()))
                    .map(er -> EmployeeRecordDataResponse.builder()
                            .id(er.getId())
                            .employeeCode(er.getEmployeeCode())
                            .fullName(er.getProfile() != null ? er.getProfile().getFullName() : null)
                            .build())
                    .toList();
        } else {
            employees = List.of();
        }

        return ResponseEntity.ok(ApiResponse.<List<EmployeeRecordDataResponse>>builder()
                .data(employees)
                .build());
    }

    @GetMapping("/shifts")
    public ResponseEntity<ApiResponse<List<Shift>>> getShifts() {
        List<Shift> shifts = shiftRepository.findAll();
        return ResponseEntity.ok(ApiResponse.<List<Shift>>builder()
                .data(shifts)
                .build());
    }

    @GetMapping("/shift-swap-options/{requesterId}")
    public ResponseEntity<ApiResponse<ShiftSwapOptionsDataResponse>> getShiftSwapOptions(
            @PathVariable Integer requesterId,
            @RequestParam(required = false) Integer targetEmployeeId) {

        ShiftSwapOptionsDataResponse options = shiftSwapRequestService.getShiftSwapOptions(requesterId, targetEmployeeId);

        return ResponseEntity.ok(ApiResponse.<ShiftSwapOptionsDataResponse>builder()
                .data(options)
                .build());
    }

    @GetMapping("/leave-policies/me")
    public ResponseEntity<ApiResponse<List<LeavePolicy>>> getMyLeavePolicies() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        EmployeeRecord emp = employeeRecordRepository.findByUser_IdAndIsDeleteFalse(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Employee record not found"));

        Integer roleId = currentUser.getRole() != null ? currentUser.getRole().getId() : null;
        Long positionId = emp.getPosition() != null ? emp.getPosition().getId() : null;

        List<LeavePolicy> policies;

        if (roleId != null && positionId != null) {
            policies = leavePolicyRepository.findByRole_IdAndPosition_Id(roleId, positionId);
        } else {
            policies = List.of();
        }

        return ResponseEntity.ok(ApiResponse.<List<LeavePolicy>>builder()
                .data(policies)
                .build());
    }

}
