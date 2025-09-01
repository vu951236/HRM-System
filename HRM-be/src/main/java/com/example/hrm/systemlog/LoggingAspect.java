package com.example.hrm.systemlog;

import com.example.hrm.entity.SystemLog;
import com.example.hrm.entity.User;
import com.example.hrm.repository.SystemLogRepository;
import com.example.hrm.service.PermissionChecker;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Aspect
@Component
@RequiredArgsConstructor
public class LoggingAspect {

    private final SystemLogRepository logRepository;
    private final HttpServletRequest request;
    private final PermissionChecker permissionChecker;

    @Around("@annotation(loggableAction)")
    public Object logMethod(ProceedingJoinPoint joinPoint, LoggableAction loggableAction) throws Throwable {
        Object result = joinPoint.proceed();

        User user = permissionChecker.getCurrentUser();

        SystemLog log = SystemLog.builder()
                .user(user)
                .action(loggableAction.action())
                .description(loggableAction.description())
                .ipAddress(request.getRemoteAddr())
                .logTime(LocalDateTime.now())
                .build();

        logRepository.save(log);

        return result;
    }
}
