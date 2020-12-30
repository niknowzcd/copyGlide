package com.architect.library;

import java.io.IOException;

public final class HttpException extends IOException {

    private static final long serialVersionUID = 1L;
    public static final int UNKNOWN = -1;
    private final int statusCode;

    public HttpException(int statusCode) {
        this("Http request failed", statusCode);
    }

    public HttpException(String message) {
        this(message, UNKNOWN);
    }

    public HttpException(String message, int statusCode) {
        this(message, statusCode, null);
    }

    public HttpException(String message, int statusCode, Throwable cause) {
        super(message + " , status code: " + statusCode, cause);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
