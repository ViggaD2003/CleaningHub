package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.entity.Booking;
import com.fpu.exe.cleaninghub.entity.Payments;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.repository.BookingRepository;
import com.fpu.exe.cleaninghub.repository.TokenRepository;
import com.fpu.exe.cleaninghub.repository.UserRepository;
import com.fpu.exe.cleaninghub.services.interfc.JWTService;
import com.fpu.exe.cleaninghub.services.interfc.PayOsService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.ItemData;
import vn.payos.type.PaymentData;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PayOsServiceImpl implements PayOsService {
    private final PayOS payOS;
    private final BookingRepository bookingRepository;
    private final TokenRepository tokenRepository;
    private final JWTService jwtService;
    private final UserRepository userRepository;
    @Override
    public CheckoutResponseData createCheckoutUrl(HttpServletRequest request, Integer bookingId) throws Exception {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Booking not found"));
        User currentUser = getCurrentUser(request);
        if (!Objects.equals(booking.getUser().getId(), currentUser.getId())){
            throw new IllegalArgumentException("You not assign this booking!!!!!");
        }
        Payments payments = booking.getBookingDetail().getPayment();
        String productName = booking.getService().getName();
        String description = "Thanh toan don hang";
        String baseUrl = "http://localhost:8080/api/v1/payOS";
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .queryParam("bookingId", bookingId).toUriString();
        BigDecimal price = payments.getFinalPrice();
        String currentTimeString = String.valueOf(new Date().getTime());
        Long orderCodee = Long.parseLong(currentTimeString.substring(currentTimeString.length() - 6));
        ItemData item = ItemData.builder().name(productName).quantity(1).price(price.intValue()).build();
        PaymentData paymentData = PaymentData.builder().orderCode(orderCodee).amount(price.intValue()).description(description)
                .returnUrl(url).cancelUrl(url).item(item).build();
        return payOS.createPaymentLink(paymentData);
    }
    private User getCurrentUser(HttpServletRequest request) {
        String token = extractTokenFromHeader(request);
        if (token == null) {
            throw new IllegalArgumentException("No JWT token found in the request header");
        }
        final var accessToken = tokenRepository.findByToken(token).orElse(null);
        if (accessToken == null) {
            throw new IllegalArgumentException("No JWT token is valid!");
        }
        String email = jwtService.extractUsername(token);
        var user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("Not Found User"));
        if (!jwtService.isTokenValid(token, user) || accessToken.isRevoked() || accessToken.isExpired()) {
            throw new IllegalArgumentException("JWT token has expired or been revoked");
        }
        return user;
    }
    private String extractTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Remove "Bearer " prefix
        }
        return null;
    }
}
