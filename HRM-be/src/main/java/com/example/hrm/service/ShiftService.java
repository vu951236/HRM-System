package com.example.hrm.service;

import com.example.hrm.dto.request.ShiftRequest;
import com.example.hrm.dto.response.ShiftResponse;
import com.example.hrm.entity.Role;
import com.example.hrm.entity.Shift;
import com.example.hrm.entity.ShiftRule;
import com.example.hrm.mapper.ShiftMapper;
import com.example.hrm.repository.ShiftRepository;
import com.example.hrm.repository.ShiftRuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShiftService {

    private final ShiftRepository shiftRepository;
    private final ShiftRuleRepository shiftRuleRepository;
    private final ShiftMapper shiftMapper;
    private final PermissionChecker permissionChecker;

    @PostConstruct
    public void initDefaultShifts() {
        if (shiftRepository.count() == 0) {
            ShiftRule defaultRule = shiftRuleRepository.findAll().stream().findFirst().orElse(null);
            if (defaultRule != null) {
                Shift shift = Shift.builder()
                        .name("Morning Shift")
                        .startTime(java.time.LocalTime.of(8, 0))
                        .endTime(java.time.LocalTime.of(16, 0))
                        .breakTime(java.time.LocalTime.of(12, 0))
                        .description("8 hour morning shift")
                        .shiftRule(defaultRule)
                        .isDelete(false)
                        .build();
                shiftRepository.save(shift);
            }
        }
    }

    public ShiftResponse createShift(ShiftRequest request) {
        permissionChecker.checkAdminOrHrRole();
        validateShiftRequest(request);

        Shift shift = shiftMapper.toEntity(request);

        if (request.getShiftRuleId() != null) {
            ShiftRule rule = shiftRuleRepository.findById(request.getShiftRuleId())
                    .orElseThrow(() -> new RuntimeException("ShiftRule not found"));
            shift.setShiftRule(rule);
        }

        return shiftMapper.toResponse(shiftRepository.save(shift));
    }

    public List<ShiftResponse> getAllShifts() {
        Role role = permissionChecker.getCurrentUserRole();

        List<Shift> shifts;
        String roleName = role.getName();

        if ("admin".equalsIgnoreCase(roleName)) {
            shifts = shiftRepository.findAll();
        } else if ("hr".equalsIgnoreCase(roleName)) {
            shifts = shiftRepository.findAllByIsDeleteFalse();
        } else {
            throw new RuntimeException("Bạn không có quyền xem ca làm việc");
        }

        return shifts.stream()
                .map(shiftMapper::toResponse)
                .collect(Collectors.toList());
    }


    public ShiftResponse getShiftById(Integer id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        permissionChecker.checkAdminOrHrRole();

        String currentRoleName = permissionChecker.getCurrentUserRole().getName();

        if (!shift.getIsDelete() || "admin".equalsIgnoreCase(currentRoleName)) {
            return shiftMapper.toResponse(shift);
        } else {
            throw new RuntimeException("Bạn không có quyền xem ca đã bị xóa");
        }
    }


    public ShiftResponse updateShift(Integer id, ShiftRequest request) {
        permissionChecker.checkAdminOrHrRole();
        validateShiftRequest(request);

        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found"));

        shiftMapper.updateEntityFromRequest(request, shift);

        if (request.getShiftRuleId() != null) {
            ShiftRule rule = shiftRuleRepository.findById(request.getShiftRuleId())
                    .orElseThrow(() -> new RuntimeException("ShiftRule not found"));
            shift.setShiftRule(rule);
        }

        return shiftMapper.toResponse(shiftRepository.save(shift));
    }

    @PreAuthorize("hasRole('admin')")
    public void deleteShift(Integer id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found"));
        shift.setIsDelete(true);
        shiftRepository.save(shift);
    }

    @PreAuthorize("hasRole('admin')")
    public void restoreShift(Integer id) {
        Shift shift = shiftRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shift not found"));
        shift.setIsDelete(false);
        shiftRepository.save(shift);
    }

    private void validateShiftRequest(ShiftRequest request) {
        if (request.getStartTime() == null || request.getEndTime() == null)
            throw new RuntimeException("Start time and end time are required");
        if (request.getEndTime().isBefore(request.getStartTime()))
            throw new RuntimeException("End time must be after start time");
        if (request.getBreakTime() != null &&
                (request.getBreakTime().isBefore(request.getStartTime()) || request.getBreakTime().isAfter(request.getEndTime())))
            throw new RuntimeException("Break time must be within start and end time");
        if (request.getName() == null || request.getName().isBlank())
            throw new RuntimeException("Shift name is required");
    }
}
