package com.fpu.exe.cleaninghub.enums.Booking;

public enum BookingStatus {
    PENDING,            // Initial status when the booking is created and awaiting staff confirmation.
    CONFIRMED,          // Staff has confirmed the booking.
    IN_PROGRESS,        // Staff has started the service.
    COMPLETED,          // The service has been completed by staff.
    CANCELLED        // The booking has been canceled (either by the user or staff
 }
