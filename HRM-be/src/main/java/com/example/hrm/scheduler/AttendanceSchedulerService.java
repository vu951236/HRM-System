package com.example.hrm.scheduler;

import com.example.hrm.entity.EmployeeRecord;
import com.example.hrm.entity.User;
import com.example.hrm.entity.WorkSchedule;
import com.example.hrm.repository.EmployeeRecordRepository;
import com.example.hrm.repository.UserRepository;
import com.example.hrm.repository.WorkScheduleRepository;
import com.example.hrm.service.AttendanceLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceSchedulerService {

    private final AttendanceLogService attendanceLogService;
    private final WorkScheduleRepository workScheduleRepository;
    private final UserRepository userRepository;
    private final EmployeeRecordRepository employeeRecordRepository;

    @Scheduled(cron = "0 5 9 * * ?")
    @Transactional
    public void markAbsentAutomatically() {
        LocalDate today = LocalDate.now();

        List<WorkSchedule> schedulesToday = workScheduleRepository.findByWorkDate(today);

        for (WorkSchedule schedule : schedulesToday) {
            List<User> users = userRepository.findAll();
            for (User user : users) {
                EmployeeRecord employee = employeeRecordRepository.findByUser(user)
                        .orElse(null);

                if (employee != null) {
                    attendanceLogService.markAbsentForMissedCheckIn(schedule, employee);
                }
            }
        }

        System.out.println("Đã tự động đánh dấu ABSENT cho tất cả nhân viên chưa check-in hôm nay.");
    }

}
