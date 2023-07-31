package com.rudykart.limbah.dto;

import java.util.List;
import java.util.Map;

public class NotValidResponse {

    private String message;
    private int status;
    private Map<String, List<String>> errors;

    public NotValidResponse() {
    }

    public NotValidResponse(String message, int status, Map<String, List<String>> errors) {
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, List<String>> errors) {
        this.errors = errors;
    }

}
