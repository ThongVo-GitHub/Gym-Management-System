// package com.fitness.gymManagementSystem.service;

// import com.fitness.gymManagementSystem.config.VnpayProperties;
// import com.fitness.gymManagementSystem.dto.VnpayCreateResponse;
// import com.fitness.gymManagementSystem.entity.Invoice;
// import com.fitness.gymManagementSystem.entity.InvoiceStatus;
// import com.fitness.gymManagementSystem.exception.ResourceNotFoundException;
// import com.fitness.gymManagementSystem.repository.InvoiceRepository;
// import com.fitness.gymManagementSystem.repository.UserRepository;

// import org.apache.commons.codec.digest.HmacUtils;
// import org.springframework.stereotype.Service;
// import org.springframework.transaction.annotation.Transactional;

// import java.net.URLEncoder;
// import java.nio.charset.StandardCharsets;
// import java.time.ZoneId;
// import java.time.ZonedDateTime;
// import java.time.format.DateTimeFormatter;
// import java.util.*;

// @Service
// public class VnpayService {

//     private static final DateTimeFormatter VNPAY_DATE_FMT =
//             DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

//     private final VnpayProperties props;
//     private final InvoiceRepository invoiceRepository;
//     private final MembershipService membershipService;

//     public VnpayService(VnpayProperties props,
//                         InvoiceRepository invoiceRepository,
//                         MembershipService membershipService) {
//         this.props = props;
//         this.invoiceRepository = invoiceRepository;
//         this.membershipService = membershipService;
//     }

//     // ── Tạo URL thanh toán ──────────────────────────────────────────────────
//     public VnpayCreateResponse createPaymentUrl(Long invoiceId, String ipAddress) {

//         Invoice invoice = invoiceRepository.findById(invoiceId)
//                 .orElseThrow(() -> new ResourceNotFoundException("Invoice", invoiceId));

//         if (invoice.getStatus() == InvoiceStatus.PAID) {
//             throw new IllegalStateException("Invoice đã được thanh toán");
//         }

//         // Số tiền VNPay tính theo đơn vị VND * 100
//         long amount = invoice.getGymPackage().getPrice()
//                             .multiply(java.math.BigDecimal.valueOf(100))
//                             .longValue();

//         String txnRef = invoiceId + "_" + System.currentTimeMillis();
//         String createDate = ZonedDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh"))
//                                         .format(VNPAY_DATE_FMT);

//         // Tập hợp params theo thứ tự alphabet — bắt buộc để ký đúng
//         Map<String, String> params = new TreeMap<>();
//         params.put("vnp_Version",    "2.1.0");
//         params.put("vnp_Command",    "pay");
//         params.put("vnp_TmnCode",    props.getTmnCode());
//         params.put("vnp_Amount",     String.valueOf(amount));
//         params.put("vnp_CurrCode",   "VND");
//         params.put("vnp_TxnRef",     txnRef);
//         params.put("vnp_OrderInfo",  "Thanh toan goi tap #" + invoiceId);
//         params.put("vnp_OrderType",  "other");
//         params.put("vnp_Locale",     "vn");
//         params.put("vnp_ReturnUrl",  props.getReturnUrl());
//         params.put("vnp_IpAddr",     ipAddress);
//         params.put("vnp_CreateDate", createDate);

//         String queryString = buildQueryString(params);
//         String secureHash  = hmacSHA512(props.getHashSecret(), queryString);

//         String paymentUrl = props.getPaymentUrl()
//                 + "?" + queryString
//                 + "&vnp_SecureHash=" + secureHash;

//         return new VnpayCreateResponse(invoiceId, txnRef, paymentUrl);
//     }

//     // ── Xử lý callback từ VNPay ────────────────────────────────────────────
//     @Transactional
//     public boolean handleCallback(Map<String, String> params) {

//         String receivedHash = params.get("vnp_SecureHash");

//         // Tách hash ra, còn lại đem đi verify
//         Map<String, String> toVerify = new TreeMap<>(params);
//         toVerify.remove("vnp_SecureHash");
//         toVerify.remove("vnp_SecureHashType");

//         String computedHash = hmacSHA512(props.getHashSecret(), buildQueryString(toVerify));

//         // 1. Xác thực chữ ký
//         if (!computedHash.equalsIgnoreCase(receivedHash)) {
//             return false; // chữ ký sai — có thể bị giả mạo
//         }

//         // 2. Kiểm tra kết quả giao dịch
//         String responseCode = params.get("vnp_ResponseCode");
//         if (!"00".equals(responseCode)) {
//             return false; // thanh toán thất bại / bị hủy
//         }

//         // 3. Lấy invoiceId từ txnRef (format: invoiceId_timestamp)
//         String txnRef   = params.get("vnp_TxnRef");
//         Long invoiceId  = Long.parseLong(txnRef.split("_")[0]);

//         // 4. Cập nhật invoice → PAID và user
//         membershipService.confirmPayment(invoiceId);
//         return true;
//     }

//     // ── Helpers ────────────────────────────────────────────────────────────
//     private String buildQueryString(Map<String, String> params) {
//         StringBuilder sb = new StringBuilder();
//         params.forEach((k, v) -> {
//             if (v != null && !v.isEmpty()) {
//                 if (sb.length() > 0) sb.append("&");
//                 sb.append(URLEncoder.encode(k, StandardCharsets.UTF_8))
//                 .append("=")
//                 .append(URLEncoder.encode(v, StandardCharsets.UTF_8));
//             }
//         });
//         return sb.toString();
//     }

//     private String hmacSHA512(String key, String data) {
//         return new HmacUtils(org.apache.commons.codec.digest.HmacAlgorithms.HMAC_SHA_512, key)
//                 .hmacHex(data);
//     }
// }