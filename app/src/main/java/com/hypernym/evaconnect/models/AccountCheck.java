package com.hypernym.evaconnect.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AccountCheck  {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("is_linkedin")
    @Expose
    private Integer isLinkedin;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIsLinkedin() {
        return isLinkedin;
    }

    public void setIsLinkedin(Integer isLinkedin) {
        this.isLinkedin = isLinkedin;
    }

}