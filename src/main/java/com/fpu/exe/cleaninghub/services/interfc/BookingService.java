package com.fpu.exe.cleaninghub.services.interfc;

import com.fpu.exe.cleaninghub.dto.request.CreateBookingRequestDTO;
import com.fpu.exe.cleaninghub.dto.response.*;
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
    CreateBookingResponseDTO createBooking(CreateBookingRequestDTO createBookingRequestDTO) throws Exception;
    Page<ListBookingResponseDTO> getAllStaffBookings(HttpServletRequest request, BookingStatus bookingStatus, Pageable pageable);
    List<ListBookingResponseDTO> getAllStaffBookings(HttpServletRequest request);
    void ChangeBookingStatus(BookingStatus bookingStatus, Integer id, HttpServletRequest request);
//    List<User> findAvailableStaff(List<User> availableStaffs, Integer numberOfWorker);
    void changePaymentStatusOfBooking(Long orderCode, Integer bookingId, PaymentStatus paymentStatus);
    List<User> findAvailableStaff(Double logU, Double latU,List<User> availableStaffs, Integer numberOfWorker) throws Exception;
    BookingDetailStaffResponse getBookingDetailStaff(int bookingId);
}
