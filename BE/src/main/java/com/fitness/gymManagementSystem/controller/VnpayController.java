// package com.fitness.gymManagementSystem.controller;

// import com.fitness.gymManagementSystem.dto.VnpayCallbackResult;
// import com.fitness.gymManagementSystem.dto.VnpayCreateResponse;
// import com.fitness.gymManagementSystem.service.VnpayService;

// import jakarta.servlet.http.HttpServletRequest;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import java.util.Map;
// import java.util.stream.Collectors;

// @RestController
// @RequestMapping("/api/payment/vnpay")
// public class VnpayController {

//     private final VnpayService vnpayService;

//     public VnpayController(VnpayService vnpayService) {
//         this.vnpayService = vnpayService;
//     }

//     // User gọi sau khi tạo invoice để lấy URL thanh toán
//     @PostMapping("/create/{invoiceId}")
//     public ResponseEntity<VnpayCreateResponse> createPayment(
//             @PathVariable Long invoiceId,
//             HttpServletRequest request) {

//         String ip = getClientIp(request);
//         return ResponseEntity.ok(vnpayService.createPaymentUrl(invoiceId, ip));
//     }

//     // VNPay gọi về sau khi user thanh toán xong — phải là public (không cần JWT)
//     @GetMapping("/callback")
//     public ResponseEntity<VnpayCallbackResult> callback(
//             @RequestParam Map<String, String> params) {

//         boolean success = vnpayService.handleCallback(params);

//         if (success) {
//             return ResponseEntity.ok(new VnpayCallbackResult(true, "Thanh toán thành công"));
//         }
//         return ResponseEntity.badRequest()
//                             .body(new VnpayCallbackResult(false, "Thanh toán thất bại hoặc chữ ký không hợp lệ"));
//     }

//     private String getClientIp(HttpServletRequest request) {
//         String ip = request.getHeader("X-Forwarded-For");
//         if (ip == null || ip.isBlank()) {
//             ip = request.getRemoteAddr();
//         }
//         // Lấy IP đầu tiên nếu có nhiều (proxy chain)
//         return ip.split(",")[0].trim();
//     }
// }