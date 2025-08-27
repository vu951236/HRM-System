package com.example.hrm.service;

import com.example.hrm.entity.*;
import com.example.hrm.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PermissionChecker {

    private final UserRepository userRepository;
    private final EmployeeRecordRepository employeeRepository;
    private final ShiftSwapRequestRepository shiftSwapRepository;
    private final OvertimeRecordRepository overtimeRepository;
    private final AttendanceLogRepository attendanceLogRepository;
    private final PayrollRepository payrollRepository;

    private final LeaveRequestRepository leaveRequestRepository;

    public User getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("Unauthorized");
        }
        String username = auth.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Current user not found"));
    }

    public void checkEmployeeRecordPermission(Integer targetUserId) {
        User currentUser = getCurrentUser();
        Role currentRole = currentUser.getRole();

        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new RuntimeException("Target user not found"));
        Role targetRole = targetUser.getRole();

        if ("admin".equalsIgnoreCase(currentRole.getName())) {
            if (!("hr".equalsIgnoreCase(targetRole.getName()) || "staff".equalsIgnoreCase(targetRole.getName()))) {
                throw new RuntimeException("Admin chỉ được thao tác trên HR hoặc Staff");
            }
        } else if ("hr".equalsIgnoreCase(currentRole.getName())) {
            if (!"staff".equalsIgnoreCase(targetRole.getName())) {
                throw new RuntimeException("HR chỉ được thao tác trên Staff");
            }
        } else {
            throw new RuntimeException("Bạn không có quyền tạo/cập nhật hồ sơ");
        }
    }

    public void checkAdminOrHrRole() {
        User currentUser = getCurrentUser();
        String roleName = currentUser.getRole().getName();

        if (!("admin".equalsIgnoreCase(roleName) || "hr".equalsIgnoreCase(roleName))) {
            throw new RuntimeException("Bạn không có quyền truy cập chức năng này");
        }
    }

    public Role getCurrentUserRole() {
        return getCurrentUser().getRole();
    }

    public boolean isCurrentUserHr() {
        Role role = getCurrentUserRole();
        return role != null && "hr".equalsIgnoreCase(role.getName());
    }

    private void checkPermission(EmployeeRecord requesterRecord) {
        User currentUser = getCurrentUser();
        String role = currentUser.getRole().getName();

        EmployeeRecord currentRecord = null;
        if (!"admin".equalsIgnoreCase(role)) {
            currentRecord = employeeRepository.findByUser_IdAndIsDeleteFalse(currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("EmployeeRecord not found"));
        }

        switch (role.toLowerCase()) {
            case "admin":
                break;

            case "hr":
                String requesterRole = requesterRecord.getUser().getRole().getName();

                if ("hr".equalsIgnoreCase(requesterRole)
                        && !"Head of Department".equalsIgnoreCase(currentRecord.getPosition().getName())) {
                    throw new RuntimeException("HR không được duyệt đơn của HR khác");
                }

                if ("Head of Department".equalsIgnoreCase(currentRecord.getPosition().getName())) {
                    if (currentRecord.getId().equals(requesterRecord.getId())) {
                        throw new RuntimeException("Bạn không được duyệt yêu cầu của chính mình");
                    }
                }
                break;

            case "staff":
                if (!"Head of Department".equalsIgnoreCase(currentRecord.getPosition().getName())
                        || !requesterRecord.getDepartment().getId().equals(currentRecord.getDepartment().getId())
                        || !"Staff".equalsIgnoreCase(requesterRecord.getPosition().getName())) {
                    throw new RuntimeException("Bạn không có quyền duyệt yêu cầu này");
                }
                break;

            default:
                throw new RuntimeException("Bạn không có quyền duyệt yêu cầu này");
        }
    }

    public void checkRequestPermission(LeaveRequest leave) {
        EmployeeRecord requesterRecord = employeeRepository.findByUser_IdAndIsDeleteFalse(
                        leave.getEmployee().getUser().getId())
                .orElseThrow(() -> new RuntimeException("Requester EmployeeRecord not found"));

        checkPermission(requesterRecord);
    }

    public void checkRequestPermission(OvertimeRecord overtime) {
        EmployeeRecord requesterRecord = employeeRepository.findByUser_IdAndIsDeleteFalse(
                        overtime.getEmployee().getUser().getId())
                .orElseThrow(() -> new RuntimeException("Requester EmployeeRecord not found"));

        checkPermission(requesterRecord);
    }

    public void checkRequestPermission(ShiftSwapRequest request) {
        EmployeeRecord requesterRecord = employeeRepository.findByUser_IdAndIsDeleteFalse(
                        request.getRequester().getUser().getId())
                .orElseThrow(() -> new RuntimeException("Requester EmployeeRecord not found"));

        checkPermission(requesterRecord);
    }

    public void checkRequestPermission(Payroll payroll) {
        EmployeeRecord requesterRecord = employeeRepository.findByUser_IdAndIsDeleteFalse(
                        payroll.getEmployee().getUser().getId())
                .orElseThrow(() -> new RuntimeException("Requester EmployeeRecord not found"));

        checkPermission(requesterRecord);
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> filterRecordsByPermission(
            List<T> records,
            User currentUser,
            EmployeeRecord currentRecord
    ) {
        String role = currentUser.getRole().getName();

        if ("admin".equalsIgnoreCase(role)) {
            if (records != null && !records.isEmpty()) {
                Object first = records.get(0);
                if (first instanceof LeaveRequest) {
                    return (List<T>) leaveRequestRepository.findAll();
                } else if (first instanceof OvertimeRecord) {
                    return (List<T>) overtimeRepository.findAll();
                } else if (first instanceof ShiftSwapRequest) {
                    return (List<T>) shiftSwapRepository.findAll();
                } else if (first instanceof AttendanceLog) {
                    return (List<T>) attendanceLogRepository.findAll();
                } else if (first instanceof Payroll) {
                    return (List<T>) payrollRepository.findAll();
                }
            }
            return records;
        }

        if (currentRecord == null) {
            throw new RuntimeException("EmployeeRecord not found");
        }

        String position = currentRecord.getPosition().getName();

        return records.stream()
                .filter(r -> {
                    EmployeeRecord er = null;

                    if (r instanceof LeaveRequest lr) {
                        er = employeeRepository.findByUser_IdAndIsDeleteFalse(
                                lr.getEmployee().getUser().getId()
                        ).orElse(null);
                    } else if (r instanceof OvertimeRecord or) {
                        er = employeeRepository.findByUser_IdAndIsDeleteFalse(
                                or.getEmployee().getUser().getId()
                        ).orElse(null);
                    } else if (r instanceof ShiftSwapRequest ssr) {
                        er = employeeRepository.findByUser_IdAndIsDeleteFalse(
                                ssr.getRequester().getUser().getId()
                        ).orElse(null);
                    } else if (r instanceof AttendanceLog al) {
                        er = employeeRepository.findByUser_IdAndIsDeleteFalse(
                                al.getEmployee().getUser().getId()
                        ).orElse(null);
                    } else if (r instanceof Payroll p) {
                        er = employeeRepository.findByUser_IdAndIsDeleteFalse(
                                p.getEmployee().getUser().getId()
                        ).orElse(null);
                    }

                    if (er == null) return false;

                    if ("hr".equalsIgnoreCase(role)) {
                        if (er.getId().equals(currentRecord.getId())) {
                            return true;
                        }
                        if (!"Head of Department".equalsIgnoreCase(position)
                                && "hr".equalsIgnoreCase(er.getUser().getRole().getName())) {
                            return false;
                        }
                        return true;
                    }

                    if ("staff".equalsIgnoreCase(role)) {
                        if ("Head of Department".equalsIgnoreCase(position)) {
                            return "Staff".equalsIgnoreCase(er.getPosition().getName())
                                    && er.getDepartment().getId().equals(currentRecord.getDepartment().getId());
                        } else {
                            return er.getUser().getId().equals(currentRecord.getUser().getId());
                        }
                    }

                    return false;
                })
                .toList();
    }
}
