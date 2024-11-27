package com.HaiDang.model;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDetails {
    String partnerCode;          // Mã đối tác của MoMo
    String accessKey;            // Khóa truy cập
    String secretKey;            // Khóa bí mật
    String amount;               // Số tiền cần thanh toán
    String orderInfo;            // Thông tin đơn hàng
    String redirectUrl;          // URL chuyển hướng sau khi thanh toán
    String ipnUrl;               // URL thông báo cho hệ thống
    String lang;                 // Ngôn ngữ sử dụng (vd: "vi" cho tiếng Việt)
    String momoPaymentLinkId;    // ID liên kết thanh toán từ MoMo (nếu có)
    String transactionId;        // ID giao dịch từ MoMo (sau khi thanh toán)
    String status;               // Trạng thái giao dịch (thành công, thất bại, đang xử lý)
}
