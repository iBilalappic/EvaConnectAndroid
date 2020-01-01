package com.hypernym.evaconnect.models;

import java.io.Serializable;

public class Posts implements Serializable {

    private String authorName;
    private String postDescription;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }
}
