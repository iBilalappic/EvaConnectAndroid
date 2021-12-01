package com.hypernym.evaconnect.models;

import java.io.Serializable;

public class EventAttendees implements Serializable {
  private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
