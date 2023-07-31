package com.rudykart.limbah.exceptions;

public class CashNotEnoughtException extends RuntimeException {
    public CashNotEnoughtException(String message) {
        super(message);
    }

    public CashNotEnoughtException(String message, Throwable cause) {
        super(message, cause);
    }
}
