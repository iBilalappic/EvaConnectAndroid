package com.hypernym.evaconnect.models;

public class ChatMessage {
    private String Message;
    private int type;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
