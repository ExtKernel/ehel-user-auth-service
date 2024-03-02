package com.tes.ebayuserauthservice.exception;

public class ExpiredRefreshTokenException extends RuntimeException {
    public ExpiredRefreshTokenException(String message) {
        super(message);
    }

    public ExpiredRefreshTokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpiredRefreshTokenException(Throwable cause) {
        super(cause);
    }
}
