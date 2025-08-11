package com.example.hrm.service;

import com.example.hrm.entity.Role;
import com.example.hrm.entity.User;
import com.example.hrm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionChecker {

    private final UserRepository userRepository;

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
}
