
package com.fitness.gymManagementSystem.entity;

public enum CheckInMethod {
    QR_CODE,            // Khách hàng tự quét mã QR trên App
    FACE_RECOGNITION,   // Hệ thống Camera AI nhận diện khuôn mặt
    STAFF_CONFIRM       // Nhân viên lễ tân xác nhận thủ công trên hệ thống
}