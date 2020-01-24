package com.hypernym.evaconnect.models;

import java.io.Serializable;

public class ChatMessage {
    private String Message;
    private int type;
    private String image;

    public String getChattime() {
        return chattime;
    }

    public void setChattime(String chattime) {
        this.chattime = chattime;
    }

    private String chattime;

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
