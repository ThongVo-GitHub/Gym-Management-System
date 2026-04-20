package com.fitness.gymManagementSystem.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fitness.gymManagementSystem.entity.ClassStatus;

import com.fitness.gymManagementSystem.repository.GymClassRepository;

import java.time.LocalDate;


@Service
public class SchedulerService {

    private static final Logger log = LoggerFactory.getLogger(SchedulerService.class);

    private final GymClassRepository gymClassRepository;

    public SchedulerService(GymClassRepository gymClassRepository) {
        this.gymClassRepository = gymClassRepository;
    }

    // Chạy mỗi ngày lúc 00:05 — đánh dấu lớp đã qua là CANCELLED nếu vẫn OPEN
    @Scheduled(cron = "0 5 0 * * *")
    @Transactional
    public void cancelPastOpenClasses() {
        LocalDate yesterday = LocalDate.now().minusDays(1);
        gymClassRepository.findByDate(yesterday).forEach(gymClass -> {
            if (gymClass.getStatus() == ClassStatus.OPEN) {
                gymClass.setStatus(ClassStatus.CANCELLED);
                gymClassRepository.save(gymClass);
            }
        });
        log.info("[Scheduler] Đã cập nhật trạng thái lớp học ngày {}", yesterday);
    }

}