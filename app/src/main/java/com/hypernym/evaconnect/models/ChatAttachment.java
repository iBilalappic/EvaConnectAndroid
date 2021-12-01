package com.hypernym.evaconnect.models;

import java.io.Serializable;
import java.util.List;

import okhttp3.MultipartBody;

public class ChatAttachment implements Serializable {
    private int created_by_id;
    private String status;
    private List<MultipartBody.Part> chat_image;
    private List<String> images;
    private List<String> documents;

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

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<String> getDocuments() {
        return documents;
    }

    public void setDocuments(List<String> documents) {
        this.documents = documents;
    }

    public List<MultipartBody.Part> getChat_image() {
        return chat_image;
    }

    public void setChat_image(List<MultipartBody.Part> chat_image) {
        this.chat_image = chat_image;
    }
}
