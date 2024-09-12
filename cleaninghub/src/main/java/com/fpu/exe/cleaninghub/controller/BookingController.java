package com.fpu.exe.cleaninghub.controller;


import com.fpu.exe.cleaninghub.dto.response.BookingDetailResponseDto;
import com.fpu.exe.cleaninghub.dto.response.BookingResponseDto;
import com.fpu.exe.cleaninghub.entity.Booking;
import com.fpu.exe.cleaninghub.services.interfc.BookingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/bookings")

public class BookingController {
    @Autowired
    private BookingService bookingService;

    @GetMapping("/history")
    public ResponseEntity<?> bookingsHistory(
            HttpServletRequest request,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            Page<BookingResponseDto> bookingsPage = bookingService.searchBookings(request, searchTerm, pageIndex, pageSize);
            return ResponseEntity.ok(bookingsPage);
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingDetail(
            HttpServletRequest request,
            @PathVariable("id") Integer bookingId) {

        try {
            BookingDetailResponseDto bookingDetail = bookingService.getBookingDetail(request, bookingId);
            return ResponseEntity.ok(bookingDetail);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

}

