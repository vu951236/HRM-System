package com.example.hrm.controller;

import com.example.hrm.entity.Department;
import com.example.hrm.entity.EmployeeType;
import com.example.hrm.entity.Position;
import com.example.hrm.repository.DepartmentRepository;
import com.example.hrm.repository.EmployeeTypeRepository;
import com.example.hrm.repository.PositionRepository;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DataController {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private PositionRepository positionRepository;

    @Autowired
    private EmployeeTypeRepository employeeTypeRepository;

    @GetMapping("/departments")
    public List<Department> getDepartments() {
        return departmentRepository.findAll();
    }

    @GetMapping("/positions")
    public List<Position> getPositions() {
        return positionRepository.findAll();
    }

    @GetMapping("/employeetypes")
    public List<EmployeeType> getEmployeeTypes() {
        return employeeTypeRepository.findAll();
    }
}
