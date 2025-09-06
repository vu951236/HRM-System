package com.example.hrm.service;

import com.example.hrm.entity.*;
import com.example.hrm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Order(1)
public class DefaultDataSeeder implements CommandLineRunner {

    private final DepartmentRepository departmentRepository;
    private final PositionRepository positionRepository;
    private final DeviceRepository deviceRepository;
    private final ContractTypeRepository contractTypeRepository;
    private final EmployeeTypeRepository employeeTypeRepository;
    private final RoleRepository roleRepository;
    private final SalaryRuleRepository salaryRuleRepository;

    @Override
    public void run(String... args) {
        seedDepartments();
        seedPositions();
        seedDevices();
        seedContractTypes();
        seedEmployeeTypes();
        seedRoles();
        seedSalaryRules();
    }

    private void seedDepartments() {
        if (departmentRepository.count() == 0) {
            List<String> names = List.of(
                    "Technical Department",
                    "Software Development Department",
                    "Quality Assurance Department",
                    "Technical Support Department",
                    "IT Infrastructure Department",
                    "Data & AI Department",
                    "Information Security Department",
                    "Human Resources Department",
                    "Sales Department",
                    "Marketing Department",
                    "Accounting Department"
            );
            names.forEach(name -> departmentRepository.save(new Department(null, name)));
        }
    }

    private void seedPositions() {
        if (positionRepository.count() == 0) {
            List<String> names = List.of("Head of Department", "Staff");
            names.forEach(name -> positionRepository.save(new Position(null, name)));
        }
    }

    private void seedDevices() {
        if (deviceRepository.count() == 0) {
            deviceRepository.save(new Device(null, "Máy chấm công 123", "127.0.0.1", 9999, "Văn phòng A", true));
        }
    }

    private void seedContractTypes() {
        if (contractTypeRepository.count() == 0) {
            List<String> names = List.of("Probationary contract", "Definite term contract", "Indefinite term contract", "Seasonal contract");
            names.forEach(name -> contractTypeRepository.save(new ContractType(null, name)));
        }
    }

    private void seedEmployeeTypes() {
        if (employeeTypeRepository.count() == 0) {
            List<String> names = List.of("Full-time", "Part-time", "Intern");
            names.forEach(name -> employeeTypeRepository.save(new EmployeeType(null, name)));
        }
    }

    private void seedRoles() {
        if (roleRepository.count() == 0) {
            List<String> names = List.of("admin", "staff", "hr");
            names.forEach(name -> roleRepository.save(new Role(null, name, null)));
        }
    }

    private void seedSalaryRules() {
        if (salaryRuleRepository.count() == 0) {
            List<SalaryRule> rules = List.of(
                    SalaryRule.builder().ruleName("Base Salary").formula("value * 50000").ruleType(SalaryRule.RuleType.BASE).active(true).isDelete(false).build(),
                    SalaryRule.builder().ruleName("Bonus Salary").formula("value >= 21 ? 1000000 : 0").ruleType(SalaryRule.RuleType.BONUS).active(true).isDelete(false).build(),
                    SalaryRule.builder().ruleName("Deduction").formula("value * 0.08").ruleType(SalaryRule.RuleType.DEDUCTION).active(true).isDelete(false).build(),
                    SalaryRule.builder().ruleName("Overtime Salary").formula("value * 75000").ruleType(SalaryRule.RuleType.OVERTIME).active(true).isDelete(false).build(),
                    SalaryRule.builder().ruleName("Late").formula("value * 2000").ruleType(SalaryRule.RuleType.LATE).active(true).isDelete(false).build(),
                    SalaryRule.builder().ruleName("Leave").formula("value * 300000").ruleType(SalaryRule.RuleType.LEAVE).active(true).isDelete(false).build()
            );
            salaryRuleRepository.saveAll(rules);
        }
    }
}
