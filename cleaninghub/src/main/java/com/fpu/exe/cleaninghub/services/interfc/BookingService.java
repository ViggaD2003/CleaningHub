package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.response.BookingDetailResponseDto;
import com.fpu.exe.cleaninghub.dto.response.BookingResponseDto;
import com.sun.security.auth.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

public interface BookingService {
    Page<BookingResponseDto> searchBookings(HttpServletRequest request, String searchTerm, int pageIndex, int pageSize);
    BookingDetailResponseDto getBookingDetail(HttpServletRequest request, Integer bookingId);
}
