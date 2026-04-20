// package com.fitness.gymManagementSystem.config;

// import org.springframework.boot.context.properties.ConfigurationProperties;
// import org.springframework.context.annotation.Configuration;
// import jakarta.annotation.PostConstruct;

// @Configuration
// @ConfigurationProperties(prefix = "vnpay")
// public class VnpayProperties {

//     private String tmnCode;
//     private String hashSecret;
//     private String paymentUrl;
//     private String returnUrl;
//     private String apiUrl;

//     @PostConstruct
//     public void validate() {
//         if (tmnCode == null || hashSecret == null) {
//             throw new IllegalStateException("VNPay tmnCode và hashSecret phải được cấu hình");
//         }
//     }

//     public String getTmnCode() { return tmnCode; }
//     public void setTmnCode(String tmnCode) { this.tmnCode = tmnCode; }
//     public String getHashSecret() { return hashSecret; }
//     public void setHashSecret(String hashSecret) { this.hashSecret = hashSecret; }
//     public String getPaymentUrl() { return paymentUrl; }
//     public void setPaymentUrl(String paymentUrl) { this.paymentUrl = paymentUrl; }
//     public String getReturnUrl() { return returnUrl; }
//     public void setReturnUrl(String returnUrl) { this.returnUrl = returnUrl; }
//     public String getApiUrl() { return apiUrl; }
//     public void setApiUrl(String apiUrl) { this.apiUrl = apiUrl; }
// }