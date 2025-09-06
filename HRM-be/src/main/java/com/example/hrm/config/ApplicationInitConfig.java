package com.example.hrm.config;

import com.example.hrm.entity.Role;
import com.example.hrm.entity.User;
import com.example.hrm.entity.EmployeeProfile;
import com.example.hrm.repository.RoleRepository;
import com.example.hrm.repository.UserRepository;
import com.example.hrm.repository.EmployeeProfileRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    EmployeeProfileRepository employeeProfileRepository;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";
    @NonFinal
    static final String ADMIN_PASSWORD = "admin";
    @NonFinal
    static final String ADMIN_EMAIL = "admin@example.com";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring.datasource",
            name = "driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver"
    )
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        log.info("Initializing application.....");
        return args -> {
            if (userRepository.findByUsername(ADMIN_USER_NAME).isEmpty()) {
                Role adminRole = roleRepository.findByName("admin")
                        .orElseThrow(() -> new RuntimeException("Role 'admin' not found in database"));

                User user = User.builder()
                        .username(ADMIN_USER_NAME)
                        .email(ADMIN_EMAIL)
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .role(adminRole)
                        .isActive(true)
                        .build();

                userRepository.save(user);

                EmployeeProfile profile = EmployeeProfile.builder()
                        .user(user)
                        .fullName("ADMIN")
                        .gender("Nam")
                        .dob(LocalDate.of(2000, 1, 1))
                        .phone("0123456789")
                        .address("System Default")
                        .imageUrl("https://static.vecteezy.com/system/resources/previews/009/398/577/original/man-avatar-clipart-illustration-free-png.png")
                        .joinedDate(LocalDate.now())
                        .build();

                employeeProfileRepository.save(profile);

                log.warn("Admin user has been created with default profile & password: admin, please change it!");
            }
            log.info("Application initialization completed.");
        };
    }
}
