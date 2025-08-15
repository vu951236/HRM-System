package com.example.hrm.service;

import com.example.hrm.dto.request.ShiftRuleRequest;
import com.example.hrm.dto.response.ShiftRuleResponse;
import com.example.hrm.entity.ShiftRule;
import com.example.hrm.mapper.ShiftRuleMapper;
import com.example.hrm.repository.ShiftRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftRuleService {

    private final ShiftRuleRepository shiftRuleRepository;
    private final ShiftRuleMapper shiftRuleMapper;

    @PostConstruct
    public void initDefaultRules() {
        if (shiftRuleRepository.count() == 0) {
            ShiftRule defaultRule = ShiftRule.builder()
                    .ruleName("Standard Shift")
                    .description("Ca chuẩn 8 tiếng, không làm thêm")
                    .maxHoursPerDay(8)
                    .allowOvertime(false)
                    .nightShiftMultiplier(1.0)
                    .build();
            shiftRuleRepository.save(defaultRule);
        }
    }

    public ShiftRuleResponse createShiftRule(ShiftRuleRequest request) {
        validateRequest(request);
        ShiftRule rule = shiftRuleMapper.toEntity(request);
        return shiftRuleMapper.toResponse(shiftRuleRepository.save(rule));
    }

    public List<ShiftRuleResponse> getAllShiftRules() {
        return shiftRuleRepository.findAll().stream()
                .map(shiftRuleMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ShiftRuleResponse getShiftRuleById(Integer id) {
        ShiftRule rule = shiftRuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShiftRule not found"));
        return shiftRuleMapper.toResponse(rule);
    }

    public ShiftRuleResponse updateShiftRule(Integer id, ShiftRuleRequest request) {
        validateRequest(request);
        ShiftRule rule = shiftRuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShiftRule not found"));
        shiftRuleMapper.updateEntityFromRequest(request, rule);
        return shiftRuleMapper.toResponse(shiftRuleRepository.save(rule));
    }

    public void deleteShiftRule(Integer id) {
        ShiftRule rule = shiftRuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShiftRule not found"));
        rule.setIsDelete(true);
        shiftRuleRepository.save(rule);
    }

    public void restoreShiftRule(Integer id) {
        ShiftRule rule = shiftRuleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ShiftRule not found"));
        rule.setIsDelete(false);
        shiftRuleRepository.save(rule);
    }

    private void validateRequest(ShiftRuleRequest request) {
        if (request.getMaxHoursPerDay() != null && request.getMaxHoursPerDay() <= 0)
            throw new RuntimeException("Max hours per day must be positive");
        if (request.getNightShiftMultiplier() != null && request.getNightShiftMultiplier() <= 0)
            throw new RuntimeException("Night shift multiplier must be positive");
        if (request.getRuleName() == null || request.getRuleName().isBlank())
            throw new RuntimeException("Rule name is required");
    }
}
