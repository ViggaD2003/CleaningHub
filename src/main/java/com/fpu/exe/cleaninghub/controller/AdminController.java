package com.fpu.exe.cleaninghub.controller;

import com.fpu.exe.cleaninghub.services.interfc.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/total-bookings")
    public ResponseEntity<Long> getTotalBookings() {
        return ResponseEntity.ok(adminService.getTotalBookings());
    }

    @GetMapping("/total-revenue")
    public ResponseEntity<Double> getTotalRevenue() {
        return ResponseEntity.ok(adminService.getTotalRevenue());
    }

    @GetMapping("/average-rating")
    public ResponseEntity<Double> getAverageRating() {
        return ResponseEntity.ok(adminService.getAverageRating());
    }
}
