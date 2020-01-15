package com.hypernym.evaconnect.models;

import java.io.Serializable;
import java.util.List;

public class Dashboard implements Serializable {
    private List<Post> posts;

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }
}
