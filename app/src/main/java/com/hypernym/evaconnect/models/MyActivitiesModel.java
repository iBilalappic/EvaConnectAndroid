package com.hypernym.evaconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class MyActivitiesModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("content")
    @Expose
    private String content;
    @SerializedName("object_id")
    @Expose
    private Integer objectId;
    @SerializedName("object_type")
    @Expose
    private String objectType;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("user")
    @Expose
    private List<User> user = null;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("created_time")
    @Expose
    private String createdTime;
    @SerializedName("created_datetime")
    @Expose
    private String createdDatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<User> getUser() {
        return user;
    }

    public void setUser(List<User> user) {
        this.user = user;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedDatetime() {
        return createdDatetime;
    }

    public void setCreatedDatetime(String createdDatetime) {
        this.createdDatetime = createdDatetime;
    }


    public static Comparator<MyActivitiesModel> DateCreaterComparator = new Comparator<MyActivitiesModel>() {

        public int compare(MyActivitiesModel s1, MyActivitiesModel s2) {
            String one = s1.getCreatedDatetime();
            String two = s2.getCreatedDatetime();

            Timestamp timestamp1 = Timestamp.valueOf(one);
            Timestamp timestamp2 = Timestamp.valueOf(two);

            //ascending order

            return timestamp2.compareTo(timestamp1);

            //descending order
            //return StudentName2.compareTo(StudentName1);
        }
    };

}

/*

@Generated("jsonschema2pojo")
public class User {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("bio_data")
    @Expose
    private String bioData;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("unique_code")
    @Expose
    private String uniqueCode;
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("date_of_birth")
    @Expose
    private String dateOfBirth;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("company_url")
    @Expose
    private Object companyUrl;
    @SerializedName("verification_pin")
    @Expose
    private Integer verificationPin;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("os")
    @Expose
    private String os;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("created_by_id")
    @Expose
    private Object createdById;
    @SerializedName("is_online")
    @Expose
    private Boolean isOnline;
    @SerializedName("last_online_datetime")
    @Expose
    private String lastOnlineDatetime;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("is_linkedin")
    @Expose
    private Integer isLinkedin;
    @SerializedName("linkedin_image_url")
    @Expose
    private Object linkedinImageUrl;
    @SerializedName("address")
    @Expose
    private Object address;
    @SerializedName("facebook_image_url")
    @Expose
    private Object facebookImageUrl;
    @SerializedName("is_facebook")
    @Expose
    private Integer isFacebook;
    @SerializedName("connection_count")
    @Expose
    private Integer connectionCount;
    @SerializedName("company_name")
    @Expose
    private String companyName;
    @SerializedName("field")
    @Expose
    private Object field;
    @SerializedName("designation")
    @Expose
    private String designation;
    @SerializedName("work_aviation")
    @Expose
    private String workAviation;
    @SerializedName("sector")
    @Expose
    private String sector;
    @SerializedName("other_sector")
    @Expose
    private Object otherSector;
    @SerializedName("is_notifications")
    @Expose
    private Integer isNotifications;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBioData() {
        return bioData;
    }

    public void setBioData(String bioData) {
        this.bioData = bioData;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Object getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(Object companyUrl) {
        this.companyUrl = companyUrl;
    }

    public Integer getVerificationPin() {
        return verificationPin;
    }

    public void setVerificationPin(Integer verificationPin) {
        this.verificationPin = verificationPin;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Object createdById) {
        this.createdById = createdById;
    }

    public Boolean getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(Boolean isOnline) {
        this.isOnline = isOnline;
    }

    public String getLastOnlineDatetime() {
        return lastOnlineDatetime;
    }

    public void setLastOnlineDatetime(String lastOnlineDatetime) {
        this.lastOnlineDatetime = lastOnlineDatetime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIsLinkedin() {
        return isLinkedin;
    }

    public void setIsLinkedin(Integer isLinkedin) {
        this.isLinkedin = isLinkedin;
    }

    public Object getLinkedinImageUrl() {
        return linkedinImageUrl;
    }

    public void setLinkedinImageUrl(Object linkedinImageUrl) {
        this.linkedinImageUrl = linkedinImageUrl;
    }

    public Object getAddress() {
        return address;
    }

    public void setAddress(Object address) {
        this.address = address;
    }

    public Object getFacebookImageUrl() {
        return facebookImageUrl;
    }

    public void setFacebookImageUrl(Object facebookImageUrl) {
        this.facebookImageUrl = facebookImageUrl;
    }

    public Integer getIsFacebook() {
        return isFacebook;
    }

    public void setIsFacebook(Integer isFacebook) {
        this.isFacebook = isFacebook;
    }

    public Integer getConnectionCount() {
        return connectionCount;
    }

    public void setConnectionCount(Integer connectionCount) {
        this.connectionCount = connectionCount;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Object getField() {
        return field;
    }

    public void setField(Object field) {
        this.field = field;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getWorkAviation() {
        return workAviation;
    }

    public void setWorkAviation(String workAviation) {
        this.workAviation = workAviation;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public Object getOtherSector() {
        return otherSector;
    }

    public void setOtherSector(Object otherSector) {
        this.otherSector = otherSector;
    }

    public Integer getIsNotifications() {
        return isNotifications;
    }

    public void setIsNotifications(Integer isNotifications) {
        this.isNotifications = isNotifications;
    }

}*/
