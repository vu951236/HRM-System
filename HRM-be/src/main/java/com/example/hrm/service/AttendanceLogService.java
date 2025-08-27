package com.example.hrm.service;

import com.example.hrm.dto.request.AttendanceLogRequest;
import com.example.hrm.dto.response.AttendanceLogResponse;
import com.example.hrm.entity.*;
import com.example.hrm.entity.AttendanceLog.AttendanceStatus;
import com.example.hrm.entity.AttendanceLog.AttendanceMethod;
import com.example.hrm.mapper.AttendanceLogMapper;
import com.example.hrm.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceLogService {

    private final AttendanceLogRepository repository;
    private final AttendanceLogMapper mapper;
    private final LeaveRequestRepository leaveRequestRepository;
    private final UserDeviceRepository userDeviceRepository;
    private final AttendanceErrorLogRepository errorLogRepository;
    private final EmployeeRecordRepository employeeRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final PermissionChecker permissionChecker;
    private final EmployeeRecordRepository employeeRecordRepository;
    private final DeviceRepository deviceRepository;

    @Transactional
    public AttendanceLogResponse checkIn(AttendanceLogRequest request) {
        Optional<EmployeeRecord> employeeOpt = employeeRepository.findById(request.getEmployeeId());
        Optional<WorkSchedule> scheduleOpt = workScheduleRepository.findById(request.getWorkScheduleId());

        if (employeeOpt.isEmpty() || scheduleOpt.isEmpty()) {
            return buildErrorResponse(request, "Employee or WorkSchedule not found");
        }

        EmployeeRecord employee = employeeOpt.get();

        if (!isDeviceAllowed(employee, request.getDeviceId())) {
            saveErrorLog(employee, request.getDeviceId(), "Invalid device", request.getCheckInTime());
            return buildErrorResponse(request, "Invalid device");
        }

        boolean alreadyCheckedIn = repository.existsByEmployeeAndWorkScheduleAndStatus(
                employee, scheduleOpt.get(), AttendanceStatus.CHECKED_IN
        );

        if (alreadyCheckedIn) {
            saveErrorLog(employee, request.getDeviceId(), "Already checked in", request.getCheckInTime());
            return buildErrorResponse(request, "Already checked in for this schedule");
        }

        Long deviceId = Long.valueOf(request.getDeviceId());
        Device device = deviceRepository.findById(deviceId)
                .orElse(null);

        AttendanceLog entity = AttendanceLog.builder()
                .employee(employee)
                .workSchedule(scheduleOpt.get())
                .checkInTime(request.getCheckInTime())
                .logDate(request.getCheckInTime().toLocalDate())
                .status(AttendanceStatus.CHECKED_IN)
                .device(device)
                .checkInMethod(request.getMethod())
                .build();


        return mapper.toResponse(repository.save(entity));
    }

    @Transactional
    public AttendanceLogResponse checkOut(AttendanceLogRequest request) {
        Optional<EmployeeRecord> employeeOpt = employeeRepository.findById(request.getEmployeeId());
        Optional<WorkSchedule> scheduleOpt = workScheduleRepository.findById(request.getWorkScheduleId());

        if (employeeOpt.isEmpty() || scheduleOpt.isEmpty()) {
            return buildErrorResponse(request, "Employee or WorkSchedule not found");
        }

        EmployeeRecord employee = employeeOpt.get();

        AttendanceLog entity = repository.findByEmployeeAndWorkScheduleAndStatus(
                employee, scheduleOpt.get(), AttendanceStatus.CHECKED_IN
        ).orElse(null);

        if (entity == null) {
            saveErrorLog(employee, request.getDeviceId(), "Cannot check out without prior check-in", request.getCheckOutTime());
            return buildErrorResponse(request, "Cannot check out without prior check-in");
        }

        entity.setCheckOutTime(request.getCheckOutTime());
        entity.setStatus(AttendanceStatus.CHECKED_OUT);
        entity.setCheckOutMethod(request.getMethod());

        WorkSchedule schedule = scheduleOpt.get();
        schedule.setStatus(WorkSchedule.Status.completed);
        schedule.setUpdatedAt(LocalDateTime.now());
        workScheduleRepository.save(schedule);

        return mapper.toResponse(repository.save(entity));
    }

    private boolean isDeviceAllowed(EmployeeRecord employee, String deviceId) {
        return userDeviceRepository.existsByEmployeeAndDeviceId(employee, Long.parseLong(deviceId));
    }

    private void saveErrorLog(EmployeeRecord employee, String deviceId, String reason, LocalDateTime logTime) {
        AttendanceErrorLog error = AttendanceErrorLog.builder()
                .employee(employee)
                .deviceId(Long.parseLong(deviceId))
                .reason(reason)
                .logTime(logTime)
                .build();
        errorLogRepository.save(error);
    }

    private AttendanceLogResponse buildErrorResponse(AttendanceLogRequest req, String reason) {
        AttendanceLogResponse res = new AttendanceLogResponse();
        res.setEmployeeId(req.getEmployeeId());
        res.setDeviceId(req.getDeviceId());
        res.setError(reason);
        return res;
    }

    public List<AttendanceLogResponse> getAllLogs() {
        User currentUser = permissionChecker.getCurrentUser();
        String role = currentUser.getRole().getName();
        EmployeeRecord currentRecord = null;

        if (!"admin".equalsIgnoreCase(role)) {
            currentRecord = employeeRecordRepository
                    .findByUser_IdAndIsDeleteFalse(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("EmployeeRecord not found"));
        }

        List<AttendanceLog> allLogs = repository.findAll();

        List<AttendanceLog> filteredLogs = permissionChecker.filterRecordsByPermission(
                allLogs, currentUser, currentRecord
        );

        return filteredLogs.stream()
                .map(mapper::toResponse)
                .toList();
    }

    public void markAbsentForMissedCheckIn(WorkSchedule schedule, EmployeeRecord employee) {
        boolean hasCheckedIn = repository.existsByEmployeeAndWorkScheduleAndStatus(
                employee, schedule, AttendanceStatus.CHECKED_IN
        );

        boolean isOnApprovedLeave = leaveRequestRepository.existsByEmployeeAndStatusAndStartDateLessThanEqualAndEndDateGreaterThanEqual(
                employee,
                "APPROVED",
                schedule.getWorkDate(),
                schedule.getWorkDate()
        );

        if (!hasCheckedIn && !isOnApprovedLeave) {
            AttendanceLog absentLog = AttendanceLog.builder()
                    .employee(employee)
                    .workSchedule(schedule)
                    .logDate(schedule.getWorkDate())
                    .status(AttendanceStatus.ABSENT)
                    .build();
            repository.save(absentLog);
        }
    }
}
