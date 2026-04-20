package com.fitness.gymManagementSystem.service;
    
import com.fitness.gymManagementSystem.dto.CreateClassRequest;
import com.fitness.gymManagementSystem.dto.GymClassResponse;
import com.fitness.gymManagementSystem.entity.*;
import com.fitness.gymManagementSystem.exception.ResourceNotFoundException;
import com.fitness.gymManagementSystem.repository.*;
    
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
    
import java.util.List;

    
@Service
public class GymClassService {

    private final GymClassRepository gymClassRepository;
    private final UserRepository userRepository;

    public GymClassService(GymClassRepository gymClassRepository,
                        UserRepository userRepository) {
        this.gymClassRepository = gymClassRepository;
        this.userRepository = userRepository;
    }

    // ── CREATE CLASS ─────────────────────────────────
    @Transactional
    public GymClass createClass(CreateClassRequest req, String trainerUsername) {

        if (!req.getEndTime().isAfter(req.getStartTime())) {
            throw new IllegalArgumentException("Giờ kết thúc phải sau giờ bắt đầu");
        }

        User trainer = userRepository.findByUsername(trainerUsername)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer", trainerUsername));

        var conflicts = gymClassRepository.findConflictingClasses(
                trainer.getId(), req.getDate(), req.getStartTime(), req.getEndTime());

        if (!conflicts.isEmpty()) {
            throw new IllegalStateException("Trainer đã có lớp trong khung giờ này");
        }

        GymClass gymClass = new GymClass();
        gymClass.setName(req.getName());
        gymClass.setDate(req.getDate());
        gymClass.setStartTime(req.getStartTime());
        gymClass.setEndTime(req.getEndTime());
        gymClass.setStudio(req.getStudio());
        gymClass.setMaxCapacity(req.getMaxCapacity());
        gymClass.setCurrentCapacity(0);
        gymClass.setTrainer(trainer);
        gymClass.setStatus(ClassStatus.OPEN);

        return gymClassRepository.save(gymClass);
    }

    // ── GET ALL ─────────────────────────────────
    @Transactional(readOnly = true)
    public List<GymClassResponse> getAll() {
        return gymClassRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // ── GET BY ID ───────────────────────────────
    @Transactional(readOnly = true)
    public GymClass getEntityById(Long id) {
        return gymClassRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("GymClass", id));
    }

    @Transactional(readOnly = true)
    public GymClassResponse getById(Long id) {
        return mapToResponse(getEntityById(id));
    }

    // ── CAPACITY LOGIC (dùng cho BookingService) ──
    @Transactional
    public void increaseCapacity(GymClass gymClass) {
        int updated = gymClass.getCurrentCapacity() + 1;
        gymClass.setCurrentCapacity(updated);

        if (updated >= gymClass.getMaxCapacity()) {
            gymClass.setStatus(ClassStatus.FULL);
        }

        gymClassRepository.save(gymClass);
    }

    @Transactional
    public void decreaseCapacity(GymClass gymClass) {
        int current = gymClass.getCurrentCapacity();

        if (current > 0) {
            gymClass.setCurrentCapacity(current - 1);
        }

        if (gymClass.getStatus() == ClassStatus.FULL) {
            gymClass.setStatus(ClassStatus.OPEN);
        }

        gymClassRepository.save(gymClass);
    }

    // ── MAPPER ─────────────────────────────────
    public GymClassResponse mapToResponse(GymClass g) {
        GymClassResponse res = new GymClassResponse();
        res.setId(g.getId());
        res.setName(g.getName());
        res.setDate(g.getDate());
        res.setStartTime(g.getStartTime());
        res.setEndTime(g.getEndTime());
        res.setStudio(g.getStudio());
        res.setMaxCapacity(g.getMaxCapacity());
        res.setCurrentCapacity(g.getCurrentCapacity());
        res.setSpotsLeft(g.getMaxCapacity() - g.getCurrentCapacity());
        res.setStatus(g.getStatus());
        res.setCreatedAt(g.getCreatedAt());

        if (g.getTrainer() != null) {
            res.setTrainerName(g.getTrainer().getUsername());
        }

        return res;
    }
}
    