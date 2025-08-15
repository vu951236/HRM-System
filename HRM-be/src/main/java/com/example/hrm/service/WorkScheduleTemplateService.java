package com.example.hrm.service;

import com.example.hrm.dto.request.WorkScheduleTemplateRequest;
import com.example.hrm.dto.response.WorkScheduleTemplateResponse;
import com.example.hrm.entity.*;
import com.example.hrm.mapper.WorkScheduleTemplateMapper;
import com.example.hrm.repository.*;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    public WorkScheduleTemplateResponse createTemplate(WorkScheduleTemplateRequest request) {
        WorkScheduleTemplate template = mapper.toEntity(request);

        Department dep = departmentRepository.findById(request.getDepartmentId())
                .orElseThrow(() -> new RuntimeException("Department not found"));
        template.setDepartment(dep);

        User creator = userRepository.findById(request.getCreatedById())
                .orElseThrow(() -> new RuntimeException("User not found"));
        template.setCreatedBy(creator);

        template.setCreatedAt(LocalDateTime.now());

        return mapper.toResponse(templateRepository.save(template));
    }

    public WorkScheduleTemplateResponse updateTemplate(Integer id, WorkScheduleTemplateRequest request) {
        WorkScheduleTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        mapper.updateEntityFromRequest(request, template);

        if (request.getDepartmentId() != null) {
            Department dep = departmentRepository.findById(request.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found"));
            template.setDepartment(dep);
        }

        if (request.getCreatedById() != null) {
            User creator = userRepository.findById(request.getCreatedById())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            template.setCreatedBy(creator);
        }

        return mapper.toResponse(templateRepository.save(template));
    }

    public List<WorkScheduleTemplateResponse> getAllTemplates() {
        return templateRepository.findAll().stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    public WorkScheduleTemplateResponse getTemplateById(Integer id) {
        WorkScheduleTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        return mapper.toResponse(template);
    }

    public void deleteTemplate(Integer id) {
        WorkScheduleTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        template.setIsDelete(true);
        templateRepository.save(template);
    }

    public void restoreTemplate(Integer id) {
        WorkScheduleTemplate template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));
        template.setIsDelete(false);
        templateRepository.save(template);
    }

    public void applyTemplate(Integer templateId, LocalDate startDate, LocalDate endDate, boolean overwrite) {
        WorkScheduleTemplate template = templateRepository.findById(templateId)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        List<EmployeeRecord> employees = employeeRepository.findAllByDepartmentAndIsDeleteFalse(template.getDepartment());
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
                        WorkSchedule existing = scheduleRepository
                                .findByEmployeeAndShiftAndWorkDateAndIsDeleteFalse(emp, shift, date)
                                .orElse(null);

                        if (existing == null) {
                            WorkSchedule ws = WorkSchedule.builder()
                                    .employee(emp)
                                    .shift(shift)
                                    .workDate(date)
                                    .status(WorkSchedule.Status.planned)
                                    .createdAt(LocalDateTime.now())
                                    .build();
                            schedules.add(ws);
                        } else if (overwrite) {
                            existing.setStatus(WorkSchedule.Status.planned);
                            existing.setUpdatedAt(LocalDateTime.now());
                            schedules.add(existing);
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
