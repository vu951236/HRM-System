package com.example.hrm.service;

import com.example.hrm.dto.request.WorkScheduleTemplateRequest;
import com.example.hrm.dto.response.ShiftPatternResponse;
import com.example.hrm.dto.response.WorkScheduleTemplateResponse;
import com.example.hrm.entity.*;
import com.example.hrm.mapper.WorkScheduleTemplateMapper;
import com.example.hrm.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkScheduleTemplateService {

    private final WorkScheduleTemplateRepository templateRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final EmployeeRecordRepository employeeRepository;
    private final ShiftRepository shiftRepository;
    private final WorkScheduleRepository scheduleRepository;
    private final WorkScheduleTemplateMapper mapper;
    private final PermissionChecker permissionChecker;


    private String validateAndCleanShiftPattern(String rawPatternJson) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<ShiftPatternItem> items = mapper.readValue(rawPatternJson, new TypeReference<>() {});

            List<ShiftPatternItem> cleaned = items.stream()
                    .collect(Collectors.collectingAndThen(
                            Collectors.toMap(
                                    item -> item.getDay() + "-" + item.getShiftId(),
                                    item -> item,
                                    (existing, replacement) -> existing
                            ),
                            map -> new ArrayList<>(map.values())
                    ));

            return mapper.writeValueAsString(cleaned);
        } catch (Exception e) {
            throw new RuntimeException("Invalid shiftPattern JSON");
        }
    }

    public WorkScheduleTemplateResponse createTemplate(WorkScheduleTemplateRequest request) {
        permissionChecker.checkAdminOrHrRole();

        Department dep = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));

        if (dep.getName().equalsIgnoreCase("Human Resources Department")
                && permissionChecker.isCurrentUserHr()) {
            throw new RuntimeException("HR không được tạo template cho phòng Human Resources Department");
        }

        WorkScheduleTemplate template = mapper.toEntity(request);
        template.setDepartment(dep);

        User creator = userRepository.findById(request.getCreatedById())
                .orElseThrow(() -> new RuntimeException("User not found"));
        template.setCreatedBy(creator);

        template.setShiftPattern(validateAndCleanShiftPattern(request.getShiftPattern()));
        template.setCreatedAt(LocalDateTime.now());

        return mapper.toResponse(templateRepository.save(template));
    }


    public WorkScheduleTemplateResponse updateTemplate(Integer id, WorkScheduleTemplateRequest request) {
        permissionChecker.checkAdminOrHrRole();

        WorkScheduleTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        if (template.getDepartment().getName().equalsIgnoreCase("Human Resources Department")
                && permissionChecker.isCurrentUserHr()) {
            throw new RuntimeException("HR không được cập nhật template của phòng Human Resources Department");
        }

        mapper.updateEntityFromRequest(request, template);

        if (request.getDepartmentId() != null) {
            Department dep = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));

            if (dep.getName().equalsIgnoreCase("Human Resources")
                    && permissionChecker.isCurrentUserHr()) {
                throw new RuntimeException("HR không được cập nhật template sang phòng Human Resources");
            }

            template.setDepartment(dep);
        }

        if (request.getCreatedById() != null) {
            User creator = userRepository.findById(request.getCreatedById())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            template.setCreatedBy(creator);
        }

        if (request.getShiftPattern() != null) {
            template.setShiftPattern(validateAndCleanShiftPattern(request.getShiftPattern()));
        }

        return mapper.toResponse(templateRepository.save(template));
    }

    public List<WorkScheduleTemplateResponse> getAllTemplates() {
        Role currentRole = permissionChecker.getCurrentUserRole();
        List<WorkScheduleTemplate> templates;

        if ("admin".equalsIgnoreCase(currentRole.getName())) {
            templates = templateRepository.findAll();
        } else if ("hr".equalsIgnoreCase(currentRole.getName())) {
            templates = templateRepository.findAll().stream()
                    .filter(t -> !t.getIsDelete()
                            && !t.getDepartment().getName().equalsIgnoreCase("Human Resources Department"))
                    .toList();
        } else {
            throw new RuntimeException("Bạn không có quyền xem template lịch làm việc");
        }

        return templates.stream()
                .map(template -> {
                    WorkScheduleTemplateResponse res = mapper.toResponse(template);

                    try {
                        ObjectMapper mapperJson = new ObjectMapper();
                        List<ShiftPatternItem> pattern = mapperJson.readValue(
                                template.getShiftPattern(), new TypeReference<>() {}
                        );

                        List<ShiftPatternResponse> detail = pattern.stream()
                                .map(item -> {
                                    String shiftName = shiftRepository.findById(item.getShiftId())
                                            .map(Shift::getName)
                                            .orElse("N/A");
                                    return new ShiftPatternResponse(item.getDay(), item.getShiftId(), shiftName);
                                })
                                .toList();

                        res.setShiftPatternDetail(detail);
                    } catch (Exception e) {
                        res.setShiftPatternDetail(new ArrayList<>());
                    }

                    return res;
                })
                .toList();
    }

    public WorkScheduleTemplateResponse getTemplateById(Integer id) {
        WorkScheduleTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        Role currentRole = permissionChecker.getCurrentUserRole();

        if ("hr".equalsIgnoreCase(currentRole.getName())
                && template.getDepartment().getName().equalsIgnoreCase("Human Resources Department")) {
            throw new RuntimeException("HR không có quyền xem template của phòng Human Resources Department");
        }

        if (!template.getIsDelete() || "admin".equalsIgnoreCase(currentRole.getName())) {
            WorkScheduleTemplateResponse res = mapper.toResponse(template);

            try {
                ObjectMapper mapperJson = new ObjectMapper();
                List<ShiftPatternItem> pattern = mapperJson.readValue(
                        template.getShiftPattern(), new TypeReference<>() {}
                );

                List<ShiftPatternResponse> detail = pattern.stream()
                        .map(item -> {
                            String shiftName = shiftRepository.findById(item.getShiftId())
                                    .map(Shift::getName)
                                    .orElse("N/A");
                            return new ShiftPatternResponse(item.getDay(), item.getShiftId(), shiftName);
                        })
                        .toList();

                res.setShiftPatternDetail(detail);
            } catch (Exception e) {
                res.setShiftPatternDetail(new ArrayList<>());
            }

            return res;
        } else {
            throw new RuntimeException("Bạn không có quyền xem template đã bị xóa");
        }
    }


    @PreAuthorize("hasRole('admin')")
    public void deleteTemplate(Integer id) {
        WorkScheduleTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        template.setIsDelete(true);
        templateRepository.save(template);
    }

    @PreAuthorize("hasRole('admin')")
    public void restoreTemplate(Integer id) {
        WorkScheduleTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        template.setIsDelete(false);
        templateRepository.save(template);
    }

    public void applyTemplate(Integer templateId, LocalDate startDate, LocalDate endDate, boolean overwrite) {
        permissionChecker.checkAdminOrHrRole();
        WorkScheduleTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        if (permissionChecker.isCurrentUserHr()
                && template.getDepartment().getName().equalsIgnoreCase("Human Resources Department")) {
            throw new RuntimeException("HR không được áp dụng template cho phòng Human Resources Department");
        }

        List<EmployeeRecord> employees = employeeRepository.findActiveByDepartmentId(Math.toIntExact(template.getDepartment().getId()));
        if (employees.isEmpty()) return;

        ObjectMapper mapperJson = new ObjectMapper();
        List<ShiftPatternItem> pattern;
        try {
            pattern = mapperJson.readValue(template.getShiftPattern(), new TypeReference<>() {});
        } catch (Exception e) {
            throw new RuntimeException("Invalid shiftPattern JSON");
        }

        List<WorkSchedule> schedules = new ArrayList<>();

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            int dayOfWeek = date.getDayOfWeek().getValue();

            for (ShiftPatternItem item : pattern) {
                if (item.getDay() == dayOfWeek) {
                    Shift shift = shiftRepository.findById(item.getShiftId())
                            .orElseThrow(() -> new RuntimeException("Shift not found"));

                    for (EmployeeRecord emp : employees) {
                        Optional<WorkSchedule> existingOpt = scheduleRepository
                                .findByEmployeeAndShiftAndWorkDateAndIsDeleteFalse(emp, shift, date);

                        if (existingOpt.isPresent()) {
                            WorkSchedule existing = existingOpt.get();
                            if (overwrite) {
                                existing.setStatus(WorkSchedule.Status.planned);
                                existing.setUpdatedAt(LocalDateTime.now());
                            }
                        } else {
                            WorkSchedule ws = WorkSchedule.builder()
                                    .employee(emp)
                                    .shift(shift)
                                    .workDate(date)
                                    .status(WorkSchedule.Status.planned)
                                    .createdAt(LocalDateTime.now())
                                    .isDelete(false)
                                    .build();
                            schedules.add(ws);
                        }
                    }
                }
            }
        }
        scheduleRepository.saveAll(schedules);
    }

    @Setter
    @Getter
    public static class ShiftPatternItem {
        private int day;
        private Integer shiftId;

    }
}
