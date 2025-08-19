package com.example.hrm.service;

import com.example.hrm.dto.request.ShiftSwapRequestRequest;
import com.example.hrm.dto.response.EmployeeRecordDataResponse;
import com.example.hrm.dto.response.ShiftSwapOptionsDataResponse;
import com.example.hrm.dto.response.ShiftSwapRequestResponse;
import com.example.hrm.dto.response.WorkScheduleDataResponse;
import com.example.hrm.entity.*;
import com.example.hrm.mapper.ShiftSwapDataMapper;
import com.example.hrm.mapper.ShiftSwapRequestMapper;
import com.example.hrm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ShiftSwapRequestService {

    private final ShiftSwapRequestRepository repository;
    private final EmployeeRecordRepository employeeRepository;
    private final WorkScheduleRepository workScheduleRepository;
    private final UserRepository userRepository;
    private final EmployeeRecordRepository employeeRecordRepository;
    private final PermissionChecker permissionChecker;
    private final ShiftSwapRequestMapper mapper;
    private final ShiftSwapDataMapper shiftSwapDataMapper;

    public ShiftSwapRequestResponse createRequest(ShiftSwapRequestRequest requestDto) {
        EmployeeRecord requester = employeeRepository.findByUser_IdAndIsDeleteFalse(requestDto.getRequesterId())
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        if (!"Staff".equalsIgnoreCase(requester.getPosition().getName())) {
            throw new RuntimeException("Only staff can create shift swap requests");
        }

        EmployeeRecord targetEmployee = employeeRepository.findByUser_IdAndIsDeleteFalse(requestDto.getTargetEmployeeId())
                .orElseThrow(() -> new RuntimeException("Target employee not found"));

        if (!requester.getDepartment().getId().equals(targetEmployee.getDepartment().getId())) {
            throw new RuntimeException("Target employee must be in the same department as requester");
        }

        if ("Head of Department".equalsIgnoreCase(targetEmployee.getPosition().getName())) {
            throw new RuntimeException("Staff cannot swap shifts with Head of Department");
        }

        WorkSchedule requestedSchedule = (WorkSchedule) workScheduleRepository
                .findByIdAndEmployeeId(requestDto.getRequestedShiftId(), requester.getId())
                .orElseThrow(() -> new RuntimeException("Requested shift not found for requester"));

        WorkSchedule targetSchedule = (WorkSchedule) workScheduleRepository
                .findByIdAndEmployeeId(requestDto.getTargetShiftId(), targetEmployee.getId())
                .orElseThrow(() -> new RuntimeException("Target shift not found for target employee"));

        ShiftSwapRequest request = ShiftSwapRequest.builder()
                .requester(requester)
                .requestedShift(requestedSchedule)
                .targetEmployee(targetEmployee)
                .targetShift(targetSchedule)
                .reason(requestDto.getReason())
                .status(ShiftSwapRequest.Status.pending)
                .createdAt(LocalDateTime.now())
                .isDelete(false)
                .build();

        return mapper.toResponse(repository.save(request));
    }

    public List<ShiftSwapRequestResponse> getAll() {
        User currentUser = permissionChecker.getCurrentUser();
        String role = currentUser.getRole().getName();

        if ("admin".equalsIgnoreCase(role)) {
            return repository.findAll()
                    .stream()
                    .map(mapper::toResponse)
                    .toList();
        }

        EmployeeRecord currentRecord = employeeRecordRepository
                .findByUser_IdAndIsDeleteFalse(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("EmployeeRecord not found"));

        String currentPosition = currentRecord.getPosition().getName();

        if ("hr".equalsIgnoreCase(role)) {
            List<ShiftSwapRequest> hrOwnRequests = repository.findAllByRequester_User_IdAndIsDeleteFalse(currentUser.getId());
            List<ShiftSwapRequest> staffRequests = repository.findAllByRequester_User_Role_NameAndIsDeleteFalse("staff");
            List<ShiftSwapRequest> combined = new ArrayList<>();
            combined.addAll(hrOwnRequests);
            combined.addAll(staffRequests);
            return combined.stream().map(mapper::toResponse).toList();
        } else {
            List<ShiftSwapRequest> ownRequests = repository.findAllByRequester_User_IdAndIsDeleteFalse(currentUser.getId());

            if ("Head of Department".equalsIgnoreCase(currentPosition)) {
                List<ShiftSwapRequest> deptStaffRequests = repository.findAll().stream()
                        .filter(r -> {
                            EmployeeRecord er = employeeRecordRepository
                                    .findByUser_IdAndIsDeleteFalse(r.getRequester().getUser().getId())
                                    .orElse(null);
                            return er != null
                                    && er.getDepartment().getId().equals(currentRecord.getDepartment().getId())
                                    && "Staff".equalsIgnoreCase(er.getPosition().getName());
                        })
                        .toList();
                ownRequests.addAll(deptStaffRequests);
            }

            return ownRequests.stream().map(mapper::toResponse).toList();
        }
    }

    public ShiftSwapRequestResponse approveRequest(Integer id) {
        ShiftSwapRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        User currentUser = permissionChecker.getCurrentUser();
        String currentRole = currentUser.getRole().getName();

        if ("admin".equalsIgnoreCase(currentRole)) {
        } else if ("hr".equalsIgnoreCase(currentRole)) {
            if ("hr".equalsIgnoreCase(request.getRequester().getUser().getRole().getName())) {
                throw new RuntimeException("HR không được duyệt request của HR khác");
            }
        } else if ("staff".equalsIgnoreCase(currentRole)) {
            EmployeeRecord currentRecord = employeeRecordRepository
                    .findByUser_IdAndIsDeleteFalse(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("EmployeeRecord not found"));
            String currentPosition = currentRecord.getPosition().getName();

            EmployeeRecord requesterRecord = employeeRecordRepository
                    .findByUser_IdAndIsDeleteFalse(request.getRequester().getUser().getId())
                    .orElseThrow(() -> new RuntimeException("Requester EmployeeRecord not found"));

            if (!"Head of Department".equalsIgnoreCase(currentPosition)
                    || !requesterRecord.getDepartment().getId().equals(currentRecord.getDepartment().getId())
                    || !"Staff".equalsIgnoreCase(requesterRecord.getPosition().getName())) {
                throw new RuntimeException("Bạn không có quyền duyệt request này");
            }
        } else {
            throw new RuntimeException("Bạn không có quyền duyệt request này");
        }

        WorkSchedule requesterShift = request.getRequestedShift();
        WorkSchedule targetShift = request.getTargetShift();

        if (requesterShift == null || targetShift == null) {
            throw new RuntimeException("Request chưa có ca làm việc để hoán đổi");
        }

        EmployeeRecord tempEmployee = requesterShift.getEmployee();
        requesterShift.setEmployee(targetShift.getEmployee());
        targetShift.setEmployee(tempEmployee);

        workScheduleRepository.save(requesterShift);
        workScheduleRepository.save(targetShift);

        request.setStatus(ShiftSwapRequest.Status.approved);
        request.setApprovedBy(currentUser);
        request.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(repository.save(request));
    }

    public ShiftSwapRequestResponse rejectRequest(Integer id) {
        ShiftSwapRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        User currentUser = permissionChecker.getCurrentUser();
        String currentRole = currentUser.getRole().getName();

        if ("admin".equalsIgnoreCase(currentRole)) {
        } else if ("hr".equalsIgnoreCase(currentRole)) {
            if ("hr".equalsIgnoreCase(request.getRequester().getUser().getRole().getName())) {
                throw new RuntimeException("HR không được từ chối request của HR khác");
            }
        } else if ("staff".equalsIgnoreCase(currentRole)) {
            EmployeeRecord currentRecord = employeeRecordRepository
                    .findByUser_IdAndIsDeleteFalse(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("EmployeeRecord not found"));
            String currentPosition = currentRecord.getPosition().getName();

            EmployeeRecord requesterRecord = employeeRecordRepository
                    .findByUser_IdAndIsDeleteFalse(request.getRequester().getUser().getId())
                    .orElseThrow(() -> new RuntimeException("Requester EmployeeRecord not found"));

            if (!"Head of Department".equalsIgnoreCase(currentPosition)
                    || !requesterRecord.getDepartment().getId().equals(currentRecord.getDepartment().getId())
                    || !"Staff".equalsIgnoreCase(requesterRecord.getPosition().getName())) {
                throw new RuntimeException("Bạn không có quyền từ chối request này");
            }
        } else {
            throw new RuntimeException("Bạn không có quyền từ chối request này");
        }

        request.setStatus(ShiftSwapRequest.Status.rejected);
        request.setApprovedBy(currentUser);
        request.setUpdatedAt(LocalDateTime.now());

        return mapper.toResponse(repository.save(request));
    }

    public void deleteRequest(Integer id) {
        User currentUser = permissionChecker.getCurrentUser();
        if (!"admin".equalsIgnoreCase(currentUser.getRole().getName())) {
            throw new RuntimeException("Chỉ admin mới được xóa request");
        }
        ShiftSwapRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setIsDelete(true);
        repository.save(request);
    }

    public void restoreRequest(Integer id) {
        User currentUser = permissionChecker.getCurrentUser();
        if (!"admin".equalsIgnoreCase(currentUser.getRole().getName())) {
            throw new RuntimeException("Chỉ admin mới được khôi phục request");
        }
        ShiftSwapRequest request = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setIsDelete(false);
        repository.save(request);
    }


    public List<ShiftSwapRequestResponse> getAllByStatus(ShiftSwapRequest.Status status) {
        User currentUser = permissionChecker.getCurrentUser();
        String role = currentUser.getRole().getName();

        if ("admin".equalsIgnoreCase(role)) {
            return repository.findAllByStatus(status)
                    .stream()
                    .map(mapper::toResponse)
                    .toList();
        } else if ("hr".equalsIgnoreCase(role)) {
            EmployeeRecord currentEmployee = employeeRecordRepository
                    .findByUser_IdAndIsDeleteFalse(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("EmployeeRecord not found"));
            Integer deptId = Math.toIntExact(currentEmployee.getDepartment().getId());

            return repository.findAllByStatusAndIsDeleteFalseAndRequester_Department_Id(status, Long.valueOf(deptId))
                    .stream()
                    .map(mapper::toResponse)
                    .toList();
        } else {
            return repository.findAllByStatusAndIsDeleteFalseAndRequester_User_Id(status, currentUser.getId())
                    .stream()
                    .map(mapper::toResponse)
                    .toList();
        }
    }

    public ShiftSwapOptionsDataResponse getShiftSwapOptions(Integer userId, Integer targetUserId) {
        EmployeeRecord requester = employeeRecordRepository.findByUser_IdAndIsDeleteFalse(userId)
                .orElseThrow(() -> new RuntimeException("Requester not found"));

        if (!"Staff".equalsIgnoreCase(requester.getPosition().getName())) {
            throw new RuntimeException("Only Staff can access this API");
        }

        List<EmployeeRecordDataResponse> targetEmployees = employeeRecordRepository
                .findByDepartmentIdAndIdNot(requester.getDepartment().getId(), requester.getId())
                .stream()
                .filter(e -> e.getPosition() != null && "Staff".equalsIgnoreCase(e.getPosition().getName()))
                .map(shiftSwapDataMapper::toEmployeeRecordDataResponse)
                .toList();

        List<WorkScheduleDataResponse> requesterShifts = workScheduleRepository
                .findByEmployeeIdAndIsDeleteFalse(requester.getId())
                .stream()
                .map(shiftSwapDataMapper::toWorkScheduleDataResponse)
                .toList();

        List<WorkScheduleDataResponse> targetShifts = new ArrayList<>();
        if (targetUserId != null) {
            EmployeeRecord targetEmployee = employeeRecordRepository.findByUser_IdAndIsDeleteFalse(targetUserId)
                    .orElseThrow(() -> new RuntimeException("Target employee not found"));

            if (!"Staff".equalsIgnoreCase(targetEmployee.getPosition().getName())) {
                throw new RuntimeException("Target employee must be Staff");
            }

            targetShifts = workScheduleRepository
                    .findByEmployeeIdAndIsDeleteFalse(targetEmployee.getId())
                    .stream()
                    .map(shiftSwapDataMapper::toWorkScheduleDataResponse)
                    .toList();
        }

        return ShiftSwapOptionsDataResponse.builder()
                .requesterShifts(requesterShifts)
                .targetEmployees(targetEmployees)
                .targetShifts(targetShifts)
                .build();
    }
}
