package com.rudykart.limbah.exceptions;

public class CredentialNotMatchException extends RuntimeException {
    public CredentialNotMatchException(String message) {
        super(message);
    }

    public CredentialNotMatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
