package com.hypernym.evaconnect.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConnectionModel implements Serializable,Parcelable {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("sender_id")
    @Expose
    public Integer senderId;
    @SerializedName("sender")
    @Expose
    public Sender sender;
    @SerializedName("receiver_id")
    @Expose
    public Integer receiverId;
    @SerializedName("receiver")
    @Expose
    public Receiver receiver;
    @SerializedName("created_datetime")
    @Expose
    public String createdDatetime;
    @SerializedName("modified_by_id")
    @Expose
    public Integer modifiedById;
    @SerializedName("modified_datetime")
    @Expose
    public String modifiedDatetime;
    @SerializedName("os")
    @Expose
    public String os;
    @SerializedName("status")
    @Expose
    public String status;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
