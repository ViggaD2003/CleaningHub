package com.fpu.exe.cleaninghub.exception;

public class AccountNotAvailableToForgotPassword extends RuntimeException {
    public AccountNotAvailableToForgotPassword(String message) {
        super(message);
    }
}
