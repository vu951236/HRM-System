package com.example.hrm.controller;

import com.example.hrm.entity.ContractType;
import com.example.hrm.entity.Department;
import com.example.hrm.entity.EmployeeType;
import com.example.hrm.entity.Position;
import com.example.hrm.repository.ContractTypeRepository;
import com.example.hrm.repository.DepartmentRepository;
import com.example.hrm.repository.EmployeeTypeRepository;
import com.example.hrm.repository.PositionRepository;
import com.example.hrm.dto.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<Department>>> getDepartments() {
        List<Department> departments = departmentRepository.findAll();
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
}
