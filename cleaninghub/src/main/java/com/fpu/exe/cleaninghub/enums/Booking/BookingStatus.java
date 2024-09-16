package com.fpu.exe.cleaninghub.enums.Booking;

public enum BookingStatus {
    PENDING,            // Initial status when the booking is created and awaiting staff confirmation.
    AWAITING_CONFIRMATION,  // Staff has been notified and the system is waiting for them to confirm the order.
    CONFIRMED,          // Staff has confirmed the booking.
    IN_PROGRESS,        // Staff has started the service.
    COMPLETED,          // The service has been completed by staff.
    CANCELLED,          // The booking has been canceled (either by the user or staff).
    REJECTED            // Staff has rejected the booking request.
}
