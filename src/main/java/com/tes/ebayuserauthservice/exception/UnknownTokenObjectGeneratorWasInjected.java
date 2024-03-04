package com.tes.ebayuserauthservice.exception;

public class UnknownTokenObjectGeneratorWasInjected extends RuntimeException {
    public UnknownTokenObjectGeneratorWasInjected(String message) {
        super(message);
    }

    public UnknownTokenObjectGeneratorWasInjected(String message, Throwable cause) {
        super(message, cause);
    }

    public UnknownTokenObjectGeneratorWasInjected(Throwable cause) {
        super(cause);
    }
}
