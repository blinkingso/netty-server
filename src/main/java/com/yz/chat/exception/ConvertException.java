package com.yz.chat.exception;

public class ConvertException extends RuntimeException {
    public ConvertException() {
        super("Failed to decode message");
    }

    public ConvertException(String message) {
        super(message);
    }

    public ConvertException(String message, Throwable cause) {
        super(message, cause);
    }
}
