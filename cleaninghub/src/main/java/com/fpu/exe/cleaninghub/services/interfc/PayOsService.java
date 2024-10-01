package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.entity.Booking;
import jakarta.servlet.http.HttpServletRequest;
import vn.payos.type.CheckoutResponseData;

public interface PayOsService {
    CheckoutResponseData createCheckoutUrl(HttpServletRequest request, Integer bookingId) throws Exception;
}
