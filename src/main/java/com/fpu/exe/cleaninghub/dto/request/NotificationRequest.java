package com.fpu.exe.cleaninghub.dto.request;

import jakarta.persistence.Column;

public record NotificationRequest (
    String email,
    Integer bookingId,
    String message,
    String type,
    String status
){}
