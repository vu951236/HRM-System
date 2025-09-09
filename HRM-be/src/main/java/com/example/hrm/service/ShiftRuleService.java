package com.example.hrm.service;

import com.example.hrm.dto.request.ShiftRuleRequest;
import com.example.hrm.dto.response.ShiftRuleResponse;
import com.example.hrm.entity.Role;
import com.example.hrm.entity.ShiftRule;
import com.example.hrm.mapper.ShiftRuleMapper;
import com.example.hrm.repository.ShiftRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.security.access.prepost.PreAuthorize;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftRuleService {

    private final ShiftRuleRepository shiftRuleRepository;
    private final ShiftRuleMapper shiftRuleMapper;
    private final PermissionChecker permissionChecker;

    @PostConstruct
    public void initDefaultRules() {
        if (shiftRuleRepository.count() == 0) {
            ShiftRule defaultRule = ShiftRule.builder()
                    .ruleName("Standard Shift")
                    .description("Standard 8 hour shift, no overtime")
                    .maxHoursPerDay(8)
                    .isDelete(false)
                    .build();
            shiftRuleRepository.save(defaultRule);
        }
    }

    public ShiftRuleResponse createShiftRule(ShiftRuleRequest request) {
        permissionChecker.checkAdminOrHrRole();
        validateRequest(request);
        ShiftRule rule = shiftRuleMapper.toEntity(request);
        return shiftRuleMapper.toResponse(shiftRuleRepository.save(rule));
    }

    public List<ShiftRuleResponse> getAllShiftRules() {
        Role currentRole = permissionChecker.getCurrentUserRole();
        List<ShiftRule> rules;

        if ("admin".equalsIgnoreCase(currentRole.getName())) {
            rules = shiftRuleRepository.findAll();
        } else if ("hr".equalsIgnoreCase(currentRole.getName())) {
            rules = shiftRuleRepository.findAllByIsDeleteFalse();
        } else {
            throw new RuntimeException("Bạn không có quyền xem quy tắc ca");
        }

        return rules.stream()
                .map(shiftRuleMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ShiftRuleResponse getShiftRuleById(Integer id) {
        ShiftRule rule = shiftRuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShiftRule not found"));

        Role currentRole = permissionChecker.getCurrentUserRole();
        if (!rule.getIsDelete() || "admin".equalsIgnoreCase(currentRole.getName())) {
            return shiftRuleMapper.toResponse(rule);
        } else {
            throw new RuntimeException("Bạn không có quyền xem quy tắc ca đã bị xóa");
        }
    }

    public ShiftRuleResponse updateShiftRule(Integer id, ShiftRuleRequest request) {
        permissionChecker.checkAdminOrHrRole();
        validateRequest(request);

        ShiftRule rule = shiftRuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShiftRule not found"));
        shiftRuleMapper.updateEntityFromRequest(request, rule);
        return shiftRuleMapper.toResponse(shiftRuleRepository.save(rule));
    }

    @PreAuthorize("hasRole('admin')")
    public void deleteShiftRule(Integer id) {
        ShiftRule rule = shiftRuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShiftRule not found"));
        rule.setIsDelete(true);
        shiftRuleRepository.save(rule);
    }

    @PreAuthorize("hasRole('admin')")
    public void restoreShiftRule(Integer id) {
        ShiftRule rule = shiftRuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShiftRule not found"));
        rule.setIsDelete(false);
        shiftRuleRepository.save(rule);
    }

    private void validateRequest(ShiftRuleRequest request) {
        if (request.getMaxHoursPerDay() != null && request.getMaxHoursPerDay() <= 0)
            throw new RuntimeException("Max hours per day must be positive");
        if (request.getRuleName() == null || request.getRuleName().isBlank())
            throw new RuntimeException("Rule name is required");
    }
}
