package com.fpu.exe.cleaninghub.services.impl;

import com.fpu.exe.cleaninghub.repository.BookingRepository;
import com.fpu.exe.cleaninghub.repository.UserRepository;
import com.fpu.exe.cleaninghub.services.interfc.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    @Override
    public Long getTotalBookings() {
        return bookingRepository.countTotalBookings();
    }

    @Override
    public Double getTotalRevenue() {
        return bookingRepository.calculateTotalRevenue();
    }

    @Override
    public Double getAverageRating() {
        return bookingRepository.calculateAverageRating();
    }
}
