
package com.hypernym.evaconnect.models;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class GetPendingData {

    @SerializedName("created_datetime")
    private String mCreatedDatetime;
    @SerializedName("id")
    private Integer mId;
    @SerializedName("modified_by_id")
    private Object mModifiedById;
    @SerializedName("modified_datetime")
    private Object mModifiedDatetime;
    @SerializedName("os")
    private String mOs;
    @SerializedName("receiver")
    private Receiver mReceiver;
    @SerializedName("receiver_id")
    private Integer mReceiverId;
    @SerializedName("sender")
    private Sender mSender;
    @SerializedName("sender_id")
    private Integer mSenderId;
    @SerializedName("status")
    private String mStatus;
    @SerializedName("is_connected")
    private String is_connected;
    public String getCreatedDatetime() {
        return mCreatedDatetime;
    }

    public void setCreatedDatetime(String createdDatetime) {
        mCreatedDatetime = createdDatetime;
    }

    public Integer getId() {
        return mId;
    }

    public void setId(Integer id) {
        mId = id;
    }

    public Object getModifiedById() {
        return mModifiedById;
    }

    public void setModifiedById(Object modifiedById) {
        mModifiedById = modifiedById;
    }

    public Object getModifiedDatetime() {
        return mModifiedDatetime;
    }

    public void setModifiedDatetime(Object modifiedDatetime) {
        mModifiedDatetime = modifiedDatetime;
    }

    public String getOs() {
        return mOs;
    }

    public void setOs(String os) {
        mOs = os;
    }

    public Receiver getReceiver() {
        return mReceiver;
    }

    public void setReceiver(Receiver receiver) {
        mReceiver = receiver;
    }

    public Integer getReceiverId() {
        return mReceiverId;
    }


    public String getIs_connected() {
        return is_connected;
    }

    public void setIs_connected(String is_connected) {
        this.is_connected = is_connected;
    }
    public void setReceiverId(Integer receiverId) {
        mReceiverId = receiverId;
    }

    public Sender getSender() {
        return mSender;
    }

    public void setSender(Sender sender) {
        mSender = sender;
    }

    public Integer getSenderId() {
        return mSenderId;
    }

    public void setSenderId(Integer senderId) {
        mSenderId = senderId;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

}
