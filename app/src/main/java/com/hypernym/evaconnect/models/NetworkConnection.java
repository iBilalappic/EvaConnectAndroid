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
    private Integer senderId;
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSenderId() {
        return senderId;
    }

    public void setSenderId(Integer senderId) {
        this.senderId = senderId;
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

}