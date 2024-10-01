package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.request.CreateBookingRequestDTO;
import com.fpu.exe.cleaninghub.dto.response.CreateBookingResponseDTO;
import com.fpu.exe.cleaninghub.dto.response.BookingDetailResponseDto;
import com.fpu.exe.cleaninghub.dto.response.BookingResponseDto;
import com.fpu.exe.cleaninghub.dto.response.ListBookingResponseDTO;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.enums.Booking.BookingStatus;
import com.fpu.exe.cleaninghub.enums.Payment.PaymentStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface BookingService {
    Page<BookingResponseDto> searchBookings(HttpServletRequest request, String searchTerm, int pageIndex, int pageSize);
    BookingDetailResponseDto getBookingDetail(HttpServletRequest request, Integer bookingId);
    CreateBookingResponseDTO createBooking(CreateBookingRequestDTO createBookingRequestDTO);
    Page<ListBookingResponseDTO> getAllStaffBookings(HttpServletRequest request, BookingStatus bookingStatus, Pageable pageable);
    void ChangeBookingStatus(BookingStatus bookingStatus, Integer id, HttpServletRequest request);
    User findAvailableStaff(List<User> availableStaffs);
    void changePaymentStatusOfBooking(Long orderCode, Integer bookingId, PaymentStatus paymentStatus);
}
