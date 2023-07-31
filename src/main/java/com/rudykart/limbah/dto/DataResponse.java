package com.rudykart.limbah.dto;

public class DataResponse<T> {
    private int status;
    private T payload;

    public DataResponse() {
    }

    public DataResponse(int status, T payload) {
        this.status = status;
        this.payload = payload;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

}
