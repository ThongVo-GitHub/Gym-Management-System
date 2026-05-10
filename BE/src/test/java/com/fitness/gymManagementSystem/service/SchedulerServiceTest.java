package com.fitness.gymManagementSystem.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fitness.gymManagementSystem.entity.ClassStatus;
import com.fitness.gymManagementSystem.entity.GymClass;
import com.fitness.gymManagementSystem.repository.GymClassRepository;

@ExtendWith(MockitoExtension.class)
class SchedulerServiceTest {

    @Mock
    private GymClassRepository gymClassRepository;

    @InjectMocks
    private SchedulerService schedulerService;

    private GymClass openClass;
    private GymClass fullClass;

    @BeforeEach
    void setUp() {
        openClass = new GymClass();
        openClass.setId(1L);
        openClass.setStatus(ClassStatus.OPEN);

        fullClass = new GymClass();
        fullClass.setId(2L);
        fullClass.setStatus(ClassStatus.FULL); // Trạng thái khác OPEN
    }

    @Test
    void cancelPastOpenClasses_UpdatesOpenClassesToCancelled() {
        // Giả lập DB trả về danh sách các lớp học của ngày hôm qua
        LocalDate yesterday = LocalDate.now().minusDays(1);
        when(gymClassRepository.findByDate(yesterday)).thenReturn(List.of(openClass, fullClass));

        // Thực thi Job chạy ngầm
        schedulerService.cancelPastOpenClasses();

        // 1. Kiểm tra lớp OPEN đã bị chuyển thành CANCELLED và lưu xuống DB chưa
        assertEquals(ClassStatus.CANCELLED, openClass.getStatus());
        verify(gymClassRepository, times(1)).save(openClass);

        // 2. Kiểm tra lớp FULL KHÔNG BỊ tác động (không gọi hàm save)
        assertEquals(ClassStatus.FULL, fullClass.getStatus());
        verify(gymClassRepository, never()).save(fullClass);
    }
}