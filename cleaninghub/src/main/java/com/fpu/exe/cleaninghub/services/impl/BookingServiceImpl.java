package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.dto.response.BookingDetailResponseDto;
import com.fpu.exe.cleaninghub.dto.response.BookingResponseDto;
import com.fpu.exe.cleaninghub.dto.response.PaymentResponseDto;
import com.fpu.exe.cleaninghub.dto.response.VoucherResponseDto;
import com.fpu.exe.cleaninghub.entity.Booking;
import com.fpu.exe.cleaninghub.entity.BookingDetail;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.entity.Voucher;
import com.fpu.exe.cleaninghub.repository.BookingRepository;
import com.fpu.exe.cleaninghub.repository.TokenRepository;
import com.fpu.exe.cleaninghub.repository.UserRepository;
import com.fpu.exe.cleaninghub.services.interfc.BookingService;
import com.fpu.exe.cleaninghub.services.interfc.JWTService;
import com.sun.security.auth.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private UserRepository userRepository;
    @Override
    public Page<BookingResponseDto> searchBookings(HttpServletRequest request, String searchTerm, int pageIndex, int pageSize) {
        User currentUser = getCurrentUser(request);
        Integer userId = currentUser.getId();
        Pageable pageable = PageRequest.of(pageIndex, pageSize);
        Page<Booking> bookingsPage = bookingRepository.findByUserId(userId, searchTerm, pageable);
        return bookingsPage.map(this::convertToDto);
    }

    @Override
    public BookingDetailResponseDto getBookingDetail(HttpServletRequest request, Integer bookingId) {
        User currentUser = getCurrentUser(request);
        Integer userId = currentUser.getId();
        Booking booking = bookingRepository.findByIdAndUserId(bookingId, userId).orElseThrow(() -> new IllegalArgumentException("Booking not found or you do not have access to this booking"));
        BookingDetailResponseDto dto = new BookingDetailResponseDto();
        dto.setId(booking.getId());
        dto.setCreateDate(booking.getBookingDate());
        dto.setUpdateDate(booking.getUpdateDate());
        if(booking.getBookingDetails() != null && !booking.getBookingDetails().isEmpty()){
            BookingDetail bookingDetail = booking.getBookingDetails().get(0);
            Voucher voucher = bookingDetail.getVoucher();
            if (voucher != null){
                VoucherResponseDto voucherDto = new VoucherResponseDto();
                voucherDto.setId(voucher.getId());
                voucherDto.setAmount(voucher.getAmount());
                voucherDto.setPercentage(voucher.getPercentage());
                voucherDto.setCreateDate(voucher.getCreateDate());
                voucherDto.setUpdateDate(voucher.getUpdateDate());
                voucherDto.setExpiredDate(voucher.getExpiredDate());
                dto.setVoucher(voucherDto);
            }
            List<PaymentResponseDto> paymentDtos = bookingDetail.getPayments().stream().map(payments ->{
                PaymentResponseDto paymentDto = new PaymentResponseDto();
                paymentDto.setId(payments.getId());
                paymentDto.setCreateDate(payments.getCreateDate());
                paymentDto.setFinalPrice(payments.getFinalPrice());
                return paymentDto;
            }).toList();
            dto.setPayments(paymentDtos);
        }
        return dto;
    }

    private BookingResponseDto convertToDto(Booking booking) {
        BookingResponseDto dto = new BookingResponseDto();
        dto.setId(booking.getId());
        dto.setStatus(booking.getStatus());
        dto.setBookingDate(booking.getBookingDate());
        dto.setAddress(booking.getAddress());
        dto.setServiceName(booking.getService().getName());
        dto.setStaffName(booking.getStaff() != null ? booking.getStaff().getFullName() : null);
        return dto;
    }
    public User getCurrentUser(HttpServletRequest request) {
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
