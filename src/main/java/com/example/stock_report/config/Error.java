package com.example.stock_report.config;

public enum Error {
    //@formatter:off

    SYSTEM_ERROR(1000, "SYSTEM_ERROR"),

    REPORT_NOT_FOUND(1001, "REPORT_NOT_FOUND");

    //@formatter:on

    private final int code;
    private final String message;

    Error(int code, String message) {
        this.code = code;
        this.message = message;
    }
    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
