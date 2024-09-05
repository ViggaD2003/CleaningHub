package com.fpu.exe.cleaninghub.config;

//import com.main.badminton.booking.utils.VNPAY.VNPayUtil;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@Configuration
public class VNPAYConfig {
//    @Getter
//    @Value("${payment.vnPay.url}")
//    private String vnp_PayUrl;
//    @Value("${payment.vnPay.returnUrl}")
//    private String vnp_ReturnUrl;
//    @Value("${payment.vnPay.tmnCode}")
//    private String vnp_TmnCode;
//    @Getter
//    @Value("${payment.vnPay.secretKey}")
//    private String secretKey;
//    @Value("${payment.vnPay.version}")
//    private String vnp_Version;
//    @Value("${payment.vnPay.command}")
//    private String vnp_Command;
//    @Value("${payment.vnPay.orderType}")
//    private String orderType;
//
//    public Map<String, String> getVNPayConfig(String booking_order_code) {
//        Map<String, String> vnpParamsMap = new HashMap<>();
//        vnpParamsMap.put("vnp_Version", this.vnp_Version);
//        vnpParamsMap.put("vnp_Command", this.vnp_Command);
//        vnpParamsMap.put("vnp_TmnCode", this.vnp_TmnCode);
//        vnpParamsMap.put("vnp_CurrCode", "VND");
//        vnpParamsMap.put("vnp_TxnRef", VNPayUtil.getRandomNumber(8));
//        vnpParamsMap.put("vnp_OrderInfo", "Thanh toan don hang:" + VNPayUtil.getRandomNumber(8));
//        vnpParamsMap.put("vnp_OrderType", this.orderType);
//        vnpParamsMap.put("vnp_Locale", "vn");
//        vnpParamsMap.put("vnp_ReturnUrl", this.vnp_ReturnUrl + "?vnp_Data=" + booking_order_code);
//
//        // Thiết lập thời gian tạo và hết hạn giao dịch
//        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        formatter.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh")); // Đảm bảo định dạng sử dụng đúng múi giờ
//
//        // Thời gian tạo giao dịch
//        String vnpCreateDate = formatter.format(calendar.getTime());
//        vnpParamsMap.put("vnp_CreateDate", vnpCreateDate);
//
//        // Thời gian hết hạn giao dịch (thêm 15 phút)
//        calendar.add(Calendar.MINUTE, 15);
//        String vnp_ExpireDate = formatter.format(calendar.getTime());
//        vnpParamsMap.put("vnp_ExpireDate", vnp_ExpireDate);
//
//        return vnpParamsMap;
//    }
}
