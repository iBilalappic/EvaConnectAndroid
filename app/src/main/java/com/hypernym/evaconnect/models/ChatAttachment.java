package com.hypernym.evaconnect.models;

import java.io.Serializable;

import okhttp3.MultipartBody;

public class ChatAttachment implements Serializable {
    private int created_by_id;
    private String status;
    private MultipartBody.Part chat_image;

    public int getCreated_by_id() {
        return created_by_id;
    }

    public void setCreated_by_id(int created_by_id) {
        this.created_by_id = created_by_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public MultipartBody.Part getChat_image() {
        return chat_image;
    }

    public void setChat_image(MultipartBody.Part chat_image) {
        this.chat_image = chat_image;
    }
}
