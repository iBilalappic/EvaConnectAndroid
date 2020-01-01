package com.hypernym.evaconnect.models;

import java.io.Serializable;

public class BaseModel<T> implements Serializable {
    private boolean error;
    private String message;
    private Throwable exception;
    private T data;
    private String status;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public String getMessage() {
        return message;
    }

    public Throwable getException() {
        return exception;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
