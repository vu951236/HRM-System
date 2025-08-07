package com.example.hrm.service;

import com.example.hrm.dto.request.ForgotPasswordRequest;
import com.example.hrm.dto.request.UserUpdatePasswordRequest;
import com.example.hrm.dto.response.UserResponse;
import com.example.hrm.dto.request.UserCreateRequest;
import com.example.hrm.dto.request.UserUpdateRequest;
import com.example.hrm.entity.EmployeeProfile;
import com.example.hrm.entity.Role;
import com.example.hrm.entity.User;
import com.example.hrm.exception.AppException;
import com.example.hrm.exception.ErrorCode;
import com.example.hrm.mapper.UserMapper;
import com.example.hrm.repository.UserRepository;
import com.example.hrm.repository.EmployeeProfileRepository;
import com.example.hrm.repository.RoleRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.AbstractMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final ConcurrentHashMap<String, AbstractMap.SimpleEntry<String, LocalDateTime>> verificationCodes = new ConcurrentHashMap<>();

    public UserResponse getMyInfo() {
        var context = SecurityContextHolder.getContext();
        var username = context.getAuthentication().getName();

        User user = userRepository.findByUsernameWithProfile(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("hasRole('admin')")
    public UserResponse createUser(UserCreateRequest request) {
        Role role = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        User user = userMapper.toUser(request);
        user.setRole(role);

        user.setPassword(passwordEncoder.encode("12345678@aA"));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        try {
            System.out.println("User to save: " + user);

            user = userRepository.save(user);

            EmployeeProfile profile = userMapper.toProfile(request, user);
            employeeProfileRepository.save(profile);
            user.setProfile(profile);
        } catch (Exception e) {
            e.printStackTrace(); // xem lỗi thực tế
            throw new AppException(ErrorCode.USER_EXISTS);
        }

        return userMapper.toUserResponse(user);
    }


    @PreAuthorize("hasRole('admin')")
    public UserResponse updateUser(Integer userId, UserUpdateRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        userMapper.updateUser(user, request);

        if (request.getRoleName() != null) {
            Role role = roleRepository.findByName(request.getRoleName())
                    .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
            user.setRole(role);
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        user.setUpdatedAt(LocalDateTime.now());

        EmployeeProfile profile = user.getProfile();
        if (profile != null) {
            profile.setFullName(request.getFullName());
            profile.setPhone(request.getPhone());
            profile.setAddress(request.getAddress());
            employeeProfileRepository.save(profile);
        }

        User updatedUser = userRepository.save(user);

        return userMapper.toUserResponse(updatedUser);
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse updatePassword(UserUpdatePasswordRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new AppException(ErrorCode.OLD_PASSWORD_INCORRECT);
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());

        return userMapper.toUserResponse(userRepository.save(user));
    }


    @PreAuthorize("hasRole('admin')")
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }


    private String generateVerificationCode() {
        int length = 6;
        String characters = "0123456789";
        Random random = new Random();
        StringBuilder code = new StringBuilder();

        for (int i = 0; i < length; i++) {
            code.append(characters.charAt(random.nextInt(characters.length())));
        }

        return code.toString();
    }

    public void generateVerificationCode(ForgotPasswordRequest request) throws IOException, MessagingException {
        userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        String code = generateVerificationCode();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(10);

        emailService.sendResetCode(request.getEmail(), code);
        verificationCodes.put(request.getEmail(), new AbstractMap.SimpleEntry<>(code, expiry));
    }

    public void resetPassword(String email, String newPassword) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);
        }
    }

    public boolean verifyCode(String email, String code) {
        AbstractMap.SimpleEntry<String, LocalDateTime> entry = verificationCodes.get(email);
        if (entry == null) throw new AppException(ErrorCode.INVALID_CONFIRMATION_CODE);
        if (LocalDateTime.now().isAfter(entry.getValue())) {
            verificationCodes.remove(email);
            throw new AppException(ErrorCode.INVALID_CONFIRMATION_CODE);
        }
        if (!entry.getKey().equals(code)) {
            throw new AppException(ErrorCode.INVALID_CONFIRMATION_CODE);
        }
        return true;
    }

    public void clearCode(String email) {
        verificationCodes.remove(email);
    }
}
