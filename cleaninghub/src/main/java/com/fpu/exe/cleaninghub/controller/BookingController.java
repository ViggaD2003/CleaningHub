package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.request.CreateBookingRequest;
import com.fpu.exe.cleaninghub.dto.response.CreateBookingResponse;
import com.fpu.exe.cleaninghub.dto.response.CreateServiceResponseDto;
import com.fpu.exe.cleaninghub.dto.response.BookingDetailResponseDto;
import com.fpu.exe.cleaninghub.dto.response.BookingResponseDto;
import com.fpu.exe.cleaninghub.services.interfc.BookingService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @PostMapping
    public ResponseEntity<?> CreateBooking(@RequestBody CreateBookingRequest createBookingRequest){
        try{
            CreateBookingResponse response = bookingService.createBooking(createBookingRequest);
            return ResponseEntity.ok(response);
        }catch (Exception e){
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong", e.getMessage()));
        }
    }
}
