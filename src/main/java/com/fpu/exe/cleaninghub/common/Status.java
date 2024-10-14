package com.fpu.exe.cleaninghub.common;

public enum Status {
    SUCCESS(1),
    ERROR(-1);

    private final int value;

    Status(int value) {
        this.value = value;
    }

    public int value() {
        return value;
    }
}