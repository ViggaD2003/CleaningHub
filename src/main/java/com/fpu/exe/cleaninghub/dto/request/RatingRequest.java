package com.fpu.exe.cleaninghub.dto.request;

public record RatingRequest(Long bookingId, Integer stars, String comments) {
}
