package com.fpu.exe.cleaninghub.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpu.exe.cleaninghub.dto.request.CreateBookingRequestDTO;
import com.fpu.exe.cleaninghub.dto.response.*;
import com.fpu.exe.cleaninghub.entity.User;
import com.fpu.exe.cleaninghub.enums.Booking.BookingStatus;
import com.fpu.exe.cleaninghub.services.interfc.BookingService;
import com.fpu.exe.cleaninghub.services.interfc.MapBoxService;
import com.fpu.exe.cleaninghub.utils.wapper.API;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/bookings")
public class BookingController {
    @Autowired
    private BookingService bookingService;

    @Autowired
    private MapBoxService mapBoxService;

    private final WebClient webClient;
    public BookingController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/history")
    public ResponseEntity<?> bookingsHistory(
            HttpServletRequest request,
            @RequestParam(value = "searchTerm", required = false) String searchTerm,
            @RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize) {
        try {
            Page<BookingResponseDto> bookingsPage = bookingService.searchBookings(request, searchTerm, pageIndex, pageSize);
            return ResponseEntity.ok(bookingsPage);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
    @GetMapping("/get-all")
    public ResponseEntity<?> getAllBookings(@RequestParam(value = "pageIndex", defaultValue = "0") int pageIndex,
                                            @RequestParam(value = "pageSize", defaultValue = "10") int pageSize){
        try {
            Page<ListBookingResponseDTO> bookingsPage = bookingService.getAllBookings(pageIndex, pageSize);
            return ResponseEntity.ok(bookingsPage);
        }catch (Exception e) {
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong", e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
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
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> CreateBooking(@RequestBody @Valid CreateBookingRequestDTO createBookingRequestDTO) {
        try {
            CreateBookingResponseDTO response = bookingService.createBooking(createBookingRequestDTO);
            return ResponseEntity.ok(API.Response.success(response));
        } catch (Exception e) {
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong", e.getMessage()));
        }
    }

    @GetMapping("/get-by-current-staff")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<?> GetAllBookingByStaffId(HttpServletRequest request, @RequestParam BookingStatus bookingStatus, @RequestParam(defaultValue = "0") Integer page,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ListBookingResponseDTO> bookings = bookingService.getAllStaffBookings(request, bookingStatus, pageable);
            return ResponseEntity.ok((API.Response.success(bookings)));
        } catch (Exception e) {
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong", e.getMessage()));
        }
    }


    @GetMapping("/get-by-current-staff-pending")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<?> GetAllBookingByStaffPending(HttpServletRequest request) {
        try {
            List<?> bookings = bookingService.getAllStaffBookings(request);
            return ResponseEntity.ok((API.Response.success(bookings)));
        } catch (Exception e) {
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong", e.getMessage()));
        }
    }

    @PutMapping("/chang-booking-status/{id}/{bookingStatus}")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<?> ChangeBookingStatus(HttpServletRequest request, @PathVariable BookingStatus bookingStatus, @PathVariable Integer id) {
        try {
            bookingService.ChangeBookingStatus(bookingStatus, id, request);
            return ResponseEntity.ok(API.Response.success("Changed success"));
        } catch (Exception e) {
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST, "Something went wrong!!", e.getMessage()));
        }
    }

    @GetMapping("/staff/{id}")
    @PreAuthorize("hasRole('ROLE_STAFF')")
    public ResponseEntity<?> getBookingDetailStaff(@PathVariable Integer id){
        try{
            BookingDetailStaffResponse bookingResponseDTO = bookingService.getBookingDetailStaff(id);
            return ResponseEntity.ok(API.Response.success(bookingResponseDTO));
        }catch (Exception e){
            return ResponseEntity.ok(API.Response.error(HttpStatus.BAD_REQUEST, "ss", e.getMessage()));
        }
    }
}