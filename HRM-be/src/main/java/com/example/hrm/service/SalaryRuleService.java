package com.example.hrm.service;

import com.example.hrm.dto.request.SalaryRuleRequest;
import com.example.hrm.dto.response.SalaryRuleResponse;
import com.example.hrm.entity.Role;
import com.example.hrm.entity.SalaryRule;
import com.example.hrm.mapper.SalaryRuleMapper;
import com.example.hrm.repository.SalaryRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryRuleService {

    private final SalaryRuleRepository repository;
    private final SalaryRuleMapper mapper;
    private final PermissionChecker permissionChecker;

    @PreAuthorize("hasRole('admin')")
    public SalaryRuleResponse create(SalaryRuleRequest request) {
        permissionChecker.checkAdminOrHrRole();

        SalaryRule rule = mapper.toEntity(request);
        rule.setIsDelete(false);

        return mapper.toResponse(repository.save(rule));
    }

    public List<SalaryRuleResponse> getAll() {
        Role currentRole = permissionChecker.getCurrentUserRole();
        List<SalaryRule> rules;

        if ("admin".equalsIgnoreCase(currentRole.getName())) {
            rules = repository.findAll();
        } else if ("hr".equalsIgnoreCase(currentRole.getName())) {
            rules = repository.findAllByIsDeleteFalse();
        } else {
            throw new RuntimeException("Bạn không có quyền xem quy tắc lương");
        }

        return rules.stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public SalaryRuleResponse getById(Integer id) {
        SalaryRule rule = repository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Rule not found with id " + id));

        Role currentRole = permissionChecker.getCurrentUserRole();
        if (!rule.getIsDelete() || "admin".equalsIgnoreCase(currentRole.getName())) {
            return mapper.toResponse(rule);
        } else {
            throw new RuntimeException("Bạn không có quyền xem rule đã bị xóa");
        }
    }

    @PreAuthorize("hasRole('admin')")
    public SalaryRuleResponse update(Integer id, SalaryRuleRequest request) {
        permissionChecker.checkAdminOrHrRole();

        SalaryRule rule = repository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Rule not found with id " + id));

        mapper.updateEntity(rule, request);

        return mapper.toResponse(repository.save(rule));
    }

    @PreAuthorize("hasRole('admin')")
    @Transactional
    public void delete(Integer id) {
        SalaryRule rule = repository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi với id: " + id));
        rule.setIsDelete(true);
        repository.save(rule);
    }

    @PreAuthorize("hasRole('admin')")
    @Transactional
    public void restore(Integer id) {
        SalaryRule rule = repository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Không tìm thấy bản ghi với id: " + id));
        rule.setIsDelete(false);
        repository.save(rule);
    }
}
