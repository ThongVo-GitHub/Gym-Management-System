// package com.fitness.gymManagementSystem.service;

// import java.util.List;

// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import com.fitness.gymManagementSystem.dto.CreateClassRequest;
// import com.fitness.gymManagementSystem.dto.GymClassResponse;
// import com.fitness.gymManagementSystem.entity.ClassBooking;
// import com.fitness.gymManagementSystem.entity.ClassStatus;
// import com.fitness.gymManagementSystem.entity.GymClass;
// import com.fitness.gymManagementSystem.entity.Role;
// import com.fitness.gymManagementSystem.entity.User;
// import com.fitness.gymManagementSystem.entity.UserStatus;
// import com.fitness.gymManagementSystem.exception.DuplicateResourceException;
// import com.fitness.gymManagementSystem.exception.ResourceNotFoundException;
// import com.fitness.gymManagementSystem.repository.ClassBookingRepository;
// import com.fitness.gymManagementSystem.repository.GymClassRepository;
// import com.fitness.gymManagementSystem.repository.UserRepository;

// @Service
// public class GymClassService {

//     private final GymClassRepository gymClassRepository;
//     private final UserRepository userRepository;
//     private final ClassBookingRepository bookingRepository;

//     public GymClassService(
//             GymClassRepository gymClassRepository,
//             UserRepository userRepository,
//             ClassBookingRepository bookingRepository) {

//         this.gymClassRepository = gymClassRepository;
//         this.userRepository = userRepository;
//         this.bookingRepository = bookingRepository;
//     }

//     // ================== MAPPER ==================
//     private GymClassResponse mapToResponse(GymClass gymClass) {
//         GymClassResponse res = new GymClassResponse();

//         res.setId(gymClass.getId());
//         res.setName(gymClass.getName());
//         res.setDate(gymClass.getDate());
//         res.setStartTime(gymClass.getStartTime());
//         res.setEndTime(gymClass.getEndTime());
//         res.setStudio(gymClass.getStudio());
//         res.setMaxCapacity(gymClass.getMaxCapacity());
//         res.setCurrentCapacity(gymClass.getCurrentCapacity());
//         res.setStatus(gymClass.getStatus().name());

//         if (gymClass.getTrainer() != null) {
//             res.setTrainerName(gymClass.getTrainer().getUsername());
//         }

//         return res;
//     }

//     // ================== CREATE CLASS ==================
//     @Transactional
//     public GymClassResponse createClass(CreateClassRequest req, String username) {

//         User trainer = userRepository.findByUsername(username)
//                 .orElseThrow(() -> new ResourceNotFoundException("Trainer not found"));

//         if (trainer.getStatus() != UserStatus.ACTIVE) {
//             throw new RuntimeException("User is not active");
//         }

//         if (trainer.getRole() != Role.TRAINER) {
//             throw new RuntimeException("Chỉ TRAINER được tạo lớp");
//         }

//         List<GymClass> conflicts = gymClassRepository.findConflictingClasses(
//                 trainer.getId(),
//                 req.getDate(),
//                 req.getStartTime(),
//                 req.getEndTime()
//         );

//         if (!conflicts.isEmpty()) {
//             throw new DuplicateResourceException("Trùng lịch giảng dạy");
//         }

//         GymClass gymClass = new GymClass();
//         gymClass.setName(req.getName());
//         gymClass.setDate(req.getDate());
//         gymClass.setStartTime(req.getStartTime());
//         gymClass.setEndTime(req.getEndTime());
//         gymClass.setStudio(req.getStudio());
//         gymClass.setMaxCapacity(req.getMaxCapacity());
//         gymClass.setCurrentCapacity(0);
//         gymClass.setTrainer(trainer); // 🔥 QUAN TRỌNG
//         gymClass.setStatus(ClassStatus.OPEN);

//         GymClass saved = gymClassRepository.save(gymClass);

//         return mapToResponse(saved);
//     }

//     // ================== BOOK ==================
//     @Transactional
//     public void book(Long classId, String username) {

//         GymClass gymClass = gymClassRepository.findById(classId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

//         User user = userRepository.findByUsername(username)
//                 .orElseThrow(() -> new ResourceNotFoundException("User not found"));

//         if (user.getStatus() != UserStatus.ACTIVE) {
//             throw new RuntimeException("User is not active");
//         }

//         if (gymClass.getCurrentCapacity() >= gymClass.getMaxCapacity()) {
//             throw new RuntimeException("Lớp đã đầy");
//         }

//         if (bookingRepository.existsByUserAndGymClass(user, gymClass)) {
//             throw new DuplicateResourceException("Đã đăng ký rồi");
//         }

//         List<ClassBooking> conflicts = bookingRepository.findUserConflicts(
//                 user.getId(),
//                 gymClass.getDate(),
//                 gymClass.getStartTime(),
//                 gymClass.getEndTime()
//         );

//         if (!conflicts.isEmpty()) {
//             throw new DuplicateResourceException("Bạn bị trùng lịch");
//         }

//         ClassBooking booking = new ClassBooking();
//         booking.setUser(user);
//         booking.setGymClass(gymClass);

//         int current = gymClass.getCurrentCapacity();
//         int max = gymClass.getMaxCapacity();

//         gymClass.setCurrentCapacity(current + 1);

//         if (current + 1 >= max) {
//             gymClass.setStatus(ClassStatus.FULL);
//         }

//         bookingRepository.save(booking);
//         gymClassRepository.save(gymClass);
//     }

//     // ================== GET ALL ==================
//     public List<GymClassResponse> getAll() {
//         return gymClassRepository.findAll()
//                 .stream()
//                 .map(this::mapToResponse)
//                 .toList();
//     }

//     // ================== GET BY ID ==================
//     public GymClassResponse getById(Long id) {
//         GymClass gymClass = gymClassRepository.findById(id)
//                 .orElseThrow(() -> new ResourceNotFoundException("Class not found"));

//         return mapToResponse(gymClass);
//     }
// }