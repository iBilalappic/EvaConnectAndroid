package com.hypernym.evaconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class NetworkConnection implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("sender_id")
    @Expose
    private String senderId;
    @SerializedName("sender")
    @Expose
    private Sender sender;
    @SerializedName("receiver_id")
    @Expose
    private Integer receiverId;
    @SerializedName("receiver")
    @Expose
    private Receiver receiver;
    @SerializedName("created_datetime")
    @Expose
    private String createdDatetime;
    @SerializedName("modified_by_id")
    @Expose
    private Object modifiedById;
    @SerializedName("modified_datetime")
    @Expose
    private Object modifiedDatetime;
    @SerializedName("os")
    @Expose
    private String os;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    private String chatID;

    private String name;

    private String message_key;

    private int unread_msg_count;
    private String userImage;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Integer getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(Integer receiverId) {
        this.receiverId = receiverId;
    }

    public Receiver getReceiver() {
        return receiver;
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public String getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(String createdDatetime) {
        this.createdDatetime = createdDatetime;
    }

    public Object getModifiedById() {
        return modifiedById;
    }

    public void setModifiedById(Object modifiedById) {
        this.modifiedById = modifiedById;
    }

    public Object getModifiedDatetime() {
        return modifiedDatetime;
    }

    public void setModifiedDatetime(Object modifiedDatetime) {
        this.modifiedDatetime = modifiedDatetime;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getUnread_msg_count() {
        return unread_msg_count;
    }

    public void setUnread_msg_count(int unread_msg_count) {
        this.unread_msg_count = unread_msg_count;
    }

    public String getMessage_key() {
        return message_key;
    }

    public void setMessage_key(String message_key) {
        this.message_key = message_key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}