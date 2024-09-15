package com.fpu.exe.cleaninghub.enums.Payment;

public enum PaymentStatus {
    SUCCESS,   // Payment completed successfully
    PENDING,   // Payment is awaiting completion (e.g., online banking in progress)
    FAILED     // Payment could not be completed
}
