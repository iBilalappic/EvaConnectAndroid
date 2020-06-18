package com.hypernym.evaconnect.models;

import java.util.List;

public class ChatMessage {
    private String Message;
    private String senderID;
    private String name;
    private String created_datetime;
    private List<String> chatImages;

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public void setCreated_datetime(String created_datetime) {
        this.created_datetime = created_datetime;
    }

    public List<String> getChatImages() {
        return chatImages;
    }

    public void setChatImages(List<String> chatImages) {
        this.chatImages = chatImages;
    }
}
