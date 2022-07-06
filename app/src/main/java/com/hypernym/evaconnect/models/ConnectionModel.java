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
    @SerializedName("first_name")
    @Expose
    public String firstName;
    @SerializedName("last_name")
    @Expose
    public Object lastName;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("unique_code")
    @Expose
    public String uniqueCode;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("date_of_birth")
    @Expose
    public Object dateOfBirth;
    @SerializedName("verification_pin")
    @Expose
    public Integer verificationPin;
    @SerializedName("user_image")
    @Expose
    public String userImage;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("bio_data")
    @Expose
    public String bioData;
    @SerializedName("os")
    @Expose
    public String os;
    @SerializedName("is_online")
    @Expose
    public Boolean isOnline;
    @SerializedName("last_online_datetime")
    @Expose
    public String lastOnlineDatetime;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("created_by_id")
    @Expose
    public  Object createdById;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("is_linkedin")
    @Expose
    public Integer isLinkedin;
    @SerializedName("linkedin_image_url")
    @Expose
    public Object linkedinImageUrl;
    @SerializedName("facebook_image_url")
    @Expose
    public  Object facebookImageUrl;
    @SerializedName("is_facebook")
    @Expose
    public  Integer isFacebook;
    @SerializedName("address")
    @Expose
    public Object address;
    @SerializedName("company_name")
    @Expose
    public String companyName;
    @SerializedName("field")
    @Expose
    public Object field;
    @SerializedName("designation")
    @Expose
    public  Object designation;
    @SerializedName("work_aviation")
    @Expose
    public Integer workAviation;
    @SerializedName("sector")
    @Expose
    public Integer sector;
    @SerializedName("is_connected")
    @Expose
    public  String isConnected = "";
    @SerializedName("is_receiver")
    @Expose
    public Boolean isReceiver = false;
    @SerializedName("connection_id")
    @Expose
    public  Integer connectionId;
    @SerializedName("is_notifications")
    @Expose
    public Integer isNotifications;

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
