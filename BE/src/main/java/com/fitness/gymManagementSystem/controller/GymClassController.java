// package com.fitness.gymManagementSystem.controller;

// import java.security.Principal;
// import java.util.List;

// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import com.fitness.gymManagementSystem.dto.CreateClassRequest;
// import com.fitness.gymManagementSystem.dto.GymClassResponse;
// import com.fitness.gymManagementSystem.service.GymClassService;

// @RestController
// @RequestMapping("/api/classes")
// public class GymClassController {

//     private final GymClassService gymClassService;

//     public GymClassController(GymClassService gymClassService) {
//         this.gymClassService = gymClassService;
//     }


//     @PostMapping
//     public ResponseEntity<GymClassResponse> createClass(
//             @RequestBody CreateClassRequest request,
//             Principal principal) {

//         String username = principal.getName();

//         return ResponseEntity.ok(
//                 gymClassService.mapToResponse(
//                         gymClassService.createClass(request, username)
//                 )
//         );
//     }

//     // 🔥 USER book lớp
//     @PostMapping("/{id}/book")
//     public ResponseEntity<String> bookClass(
//             @PathVariable Long id,
//             Principal principal) {

//         gymClassService.book(id, principal.getName());

//         return ResponseEntity.ok("Đăng ký lớp thành công");
//     }

//     // 🔥 GET ALL
//     @GetMapping
//     public ResponseEntity<List<GymClassResponse>> getAll() {
//         return ResponseEntity.ok(gymClassService.getAll());
//     }

//     // 🔥 GET DETAIL
//     @GetMapping("/{id}")
//     public ResponseEntity<GymClassResponse> getById(@PathVariable Long id) {
//         return ResponseEntity.ok(gymClassService.getById(id));
//     }
// }