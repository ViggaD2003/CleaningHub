package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.dto.request.CreateBookingRequestDTO;
import com.fpu.exe.cleaninghub.dto.response.CreateBookingResponseDTO;
import com.fpu.exe.cleaninghub.dto.response.BookingDetailResponseDto;
import com.fpu.exe.cleaninghub.dto.response.BookingResponseDto;
import com.fpu.exe.cleaninghub.dto.response.ListBookingResponseDTO;
import com.fpu.exe.cleaninghub.enums.Booking.BookingStatus;
import com.fpu.exe.cleaninghub.services.interfc.BookingService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    @PostMapping
    public ResponseEntity<?> CreateBooking(@RequestBody @Valid CreateBookingRequestDTO createBookingRequestDTO){
        try{
            CreateBookingResponseDTO response = bookingService.createBooking(createBookingRequestDTO);
            return ResponseEntity.ok(API.Response.success(response));
        }catch (Exception e){
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong", e.getMessage()));
        }
    }
    @GetMapping("/get-by-current-staff")
    public ResponseEntity<?> GetAllBookingByStaffId(HttpServletRequest request, @RequestParam BookingStatus bookingStatus,@RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size){

        try{
            Pageable pageable = PageRequest.of(page, size);
            Page<ListBookingResponseDTO> bookings = bookingService.getAllStaffBookings(request, bookingStatus, pageable);
            return ResponseEntity.ok((API.Response.success(bookings)));
        }catch (Exception e){
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong", e.getMessage()));
        }
    }
    @PutMapping("/chang-booking-status/{id}/{bookingStatus}")
    public ResponseEntity<?> ChangeBookingStatus(@PathVariable BookingStatus bookingStatus, @PathVariable Integer id){
        try{
            bookingService.ChangeBookingStatus(bookingStatus, id);
            return ResponseEntity.ok(API.Response.success("Chang status successfully"));
        }catch (Exception e){
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong!!", e.getMessage()));
        }
    }
}
