
package com.hypernym.evaconnect.models;

import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class GetBlockedData {

    @SerializedName("created_datetime")
    private String mCreatedDatetime;
    @SerializedName("id")
    private Long mId;
    @SerializedName("modified_by_id")
    private Long mModifiedById;
    @SerializedName("modified_datetime")
    private String mModifiedDatetime;
    @SerializedName("os")
    private String mOs;
    @SerializedName("receiver")
    private Receiver mReceiver;
    @SerializedName("receiver_id")
    private int mReceiverId;
    @SerializedName("sender")
    private Sender mSender;
    @SerializedName("sender_id")
    private Integer mSenderId;
    @SerializedName("status")
    private String mStatus;

    public String getCreatedDatetime() {
        return mCreatedDatetime;
    }

    public void setCreatedDatetime(String createdDatetime) {
        mCreatedDatetime = createdDatetime;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public Long getModifiedById() {
        return mModifiedById;
    }

    public void setModifiedById(Long modifiedById) {
        mModifiedById = modifiedById;
    }

    public String getModifiedDatetime() {
        return mModifiedDatetime;
    }

    public void setModifiedDatetime(String modifiedDatetime) {
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

    public int getReceiverId() {
        return mReceiverId;
    }

    public void setReceiverId(int receiverId) {
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
