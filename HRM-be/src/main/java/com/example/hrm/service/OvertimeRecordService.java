package com.example.hrm.service;

import com.example.hrm.dto.response.OvertimeRecordResponse;
import com.example.hrm.entity.EmployeeRecord;
import com.example.hrm.entity.OvertimeRecord;
import com.example.hrm.entity.User;
import com.example.hrm.mapper.OvertimeRecordMapper;
import com.example.hrm.repository.EmployeeRecordRepository;
import com.example.hrm.repository.OvertimeRecordRepository;
import com.example.hrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OvertimeRecordService {

    private final OvertimeRecordRepository repository;
    private final EmployeeRecordRepository employeeRepository;
    private final UserRepository userRepository;
    private final OvertimeRecordMapper mapper;
    private final PermissionChecker permissionChecker;

    public OvertimeRecordResponse createOvertime(LocalDate date, LocalTime start, LocalTime end, String reason) {
        User currentUser = permissionChecker.getCurrentUser();
        EmployeeRecord emp = employeeRepository.findByUser_IdAndIsDeleteFalse(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (!"Staff".equalsIgnoreCase(emp.getPosition().getName())) {
            throw new RuntimeException("Chỉ nhân viên Staff mới được tạo yêu cầu OT");
        }

        OvertimeRecord record = OvertimeRecord.builder()
                .employee(emp)
                .date(date)
                .startTime(start)
                .endTime(end)
                .reason(reason)
                .status(OvertimeRecord.Status.pending)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .isDelete(false)
                .build();

        return mapper.toResponse(repository.save(record));
    }

    public OvertimeRecordResponse approveOvertime(Integer id) {
        OvertimeRecord record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Overtime not found"));

        User currentUser = permissionChecker.getCurrentUser();
        String role = currentUser.getRole().getName();

        if ("admin".equalsIgnoreCase(role)) {
        } else if ("hr".equalsIgnoreCase(role)) {
            if ("hr".equalsIgnoreCase(record.getEmployee().getUser().getRole().getName())) {
                throw new RuntimeException("HR không được duyệt OT của HR khác");
            }
        } else if ("staff".equalsIgnoreCase(role)) {
            EmployeeRecord currentRecord = employeeRepository.findByUser_IdAndIsDeleteFalse(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("EmployeeRecord not found"));

            EmployeeRecord requesterRecord = record.getEmployee();
            if (!"Head of Department".equalsIgnoreCase(currentRecord.getPosition().getName())
                    || !requesterRecord.getDepartment().getId().equals(currentRecord.getDepartment().getId())
                    || !"Staff".equalsIgnoreCase(requesterRecord.getPosition().getName())) {
                throw new RuntimeException("Bạn không có quyền duyệt yêu cầu OT này");
            }
        } else {
            throw new RuntimeException("Bạn không có quyền duyệt yêu cầu OT này");
        }

        record.setStatus(OvertimeRecord.Status.approved);
        record.setApprovedBy(currentUser);
        record.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(repository.save(record));
    }

    public OvertimeRecordResponse rejectOvertime(Integer id) {
        OvertimeRecord record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Overtime not found"));

        User currentUser = permissionChecker.getCurrentUser();
        String role = currentUser.getRole().getName();

        if ("admin".equalsIgnoreCase(role)) {
        } else if ("hr".equalsIgnoreCase(role)) {
            if ("hr".equalsIgnoreCase(record.getEmployee().getUser().getRole().getName())) {
                throw new RuntimeException("HR không được từ chối OT của HR khác");
            }
        } else if ("staff".equalsIgnoreCase(role)) {
            EmployeeRecord currentRecord = employeeRepository.findByUser_IdAndIsDeleteFalse(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("EmployeeRecord not found"));

            EmployeeRecord requesterRecord = record.getEmployee();
            if (!"Head of Department".equalsIgnoreCase(currentRecord.getPosition().getName())
                    || !requesterRecord.getDepartment().getId().equals(currentRecord.getDepartment().getId())
                    || !"Staff".equalsIgnoreCase(requesterRecord.getPosition().getName())) {
                throw new RuntimeException("Bạn không có quyền từ chối OT này");
            }
        } else {
            throw new RuntimeException("Bạn không có quyền từ chối OT này");
        }

        record.setStatus(OvertimeRecord.Status.rejected);
        record.setApprovedBy(currentUser);
        record.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(repository.save(record));
    }

    public void deleteOvertime(Integer id) {
        User currentUser = permissionChecker.getCurrentUser();
        if (!"admin".equalsIgnoreCase(currentUser.getRole().getName())) {
            throw new RuntimeException("Chỉ admin mới được xoá OT");
        }
        OvertimeRecord record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Overtime not found"));
        record.setIsDelete(true);
        record.setUpdatedAt(LocalDateTime.now());
        repository.save(record);
    }

    public void restoreOvertime(Integer id) {
        User currentUser = permissionChecker.getCurrentUser();
        if (!"admin".equalsIgnoreCase(currentUser.getRole().getName())) {
            throw new RuntimeException("Chỉ admin mới được khôi phục OT");
        }
        OvertimeRecord record = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Overtime not found"));
        record.setIsDelete(false);
        record.setUpdatedAt(LocalDateTime.now());
        repository.save(record);
    }

    public List<OvertimeRecordResponse> getAllOvertime() {
        User currentUser = permissionChecker.getCurrentUser();
        String role = currentUser.getRole().getName();

        if ("admin".equalsIgnoreCase(role)) {
            return repository.findAll()
                    .stream()
                    .map(mapper::toResponse)
                    .toList();
        }

        EmployeeRecord currentRecord = employeeRepository.findByUser_IdAndIsDeleteFalse(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("EmployeeRecord not found"));
        String currentPosition = currentRecord.getPosition().getName();

        if ("hr".equalsIgnoreCase(role)) {
            List<OvertimeRecord> hrOwn = repository.findAllByEmployee_User_IdAndIsDeleteFalse(currentUser.getId());
            List<OvertimeRecord> staffRecords = repository.findAllByIsDeleteFalse()
                    .stream()
                    .filter(r -> "staff".equalsIgnoreCase(r.getEmployee().getUser().getRole().getName()))
                    .toList();

            List<OvertimeRecord> combined = new ArrayList<>();
            combined.addAll(hrOwn);
            combined.addAll(staffRecords);

            return combined.stream().map(mapper::toResponse).toList();
        }

        List<OvertimeRecord> ownRecords = repository.findAllByEmployee_User_IdAndIsDeleteFalse(currentUser.getId());

        if ("Head of Department".equalsIgnoreCase(currentPosition)) {
            List<OvertimeRecord> deptStaffRecords = repository.findAllByIsDeleteFalse()
                    .stream()
                    .filter(r -> {
                        EmployeeRecord er = employeeRepository.findByUser_IdAndIsDeleteFalse(r.getEmployee().getUser().getId())
                                .orElse(null);
                        return er != null
                                && er.getDepartment().getId().equals(currentRecord.getDepartment().getId())
                                && "Staff".equalsIgnoreCase(er.getPosition().getName());
                    })
                    .toList();

            ownRecords.addAll(deptStaffRecords);
        }

        return ownRecords.stream().map(mapper::toResponse).toList();
    }

    public List<OvertimeRecordResponse> getOvertimeByDate(LocalDate date) {
        User currentUser = permissionChecker.getCurrentUser();
        String role = currentUser.getRole().getName();

        if ("admin".equalsIgnoreCase(role)) {
            return repository.findAllByDateAndIsDeleteFalse(date)
                    .stream()
                    .map(mapper::toResponse)
                    .toList();
        }

        EmployeeRecord currentRecord = employeeRepository.findByUser_IdAndIsDeleteFalse(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("EmployeeRecord not found"));
        String currentPosition = currentRecord.getPosition().getName();

        if ("hr".equalsIgnoreCase(role)) {
            List<OvertimeRecord> hrOwn = repository.findAllByDateAndEmployee_User_IdAndIsDeleteFalse(date, currentUser.getId());
            List<OvertimeRecord> staffRecords = repository.findAllByDateAndIsDeleteFalse(date)
                    .stream()
                    .filter(r -> "staff".equalsIgnoreCase(r.getEmployee().getUser().getRole().getName()))
                    .toList();

            List<OvertimeRecord> combined = new ArrayList<>();
            combined.addAll(hrOwn);
            combined.addAll(staffRecords);

            return combined.stream().map(mapper::toResponse).toList();
        }

        List<OvertimeRecord> ownRecords = repository.findAllByDateAndEmployee_User_IdAndIsDeleteFalse(date, currentUser.getId());

        if ("Head of Department".equalsIgnoreCase(currentPosition)) {
            List<OvertimeRecord> deptStaffRecords = repository.findAllByDateAndIsDeleteFalse(date)
                    .stream()
                    .filter(r -> {
                        EmployeeRecord er = employeeRepository.findByUser_IdAndIsDeleteFalse(r.getEmployee().getUser().getId())
                                .orElse(null);
                        return er != null
                                && er.getDepartment().getId().equals(currentRecord.getDepartment().getId())
                                && "Staff".equalsIgnoreCase(er.getPosition().getName());
                    })
                    .toList();

            ownRecords.addAll(deptStaffRecords);
        }

        return ownRecords.stream().map(mapper::toResponse).toList();
    }
}
