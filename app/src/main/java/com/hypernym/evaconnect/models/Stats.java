package com.hypernym.evaconnect.models;

import java.io.Serializable;

public class Stats implements Serializable {
    private int connection_count;
    private int notification_count;

    public int getConnection_count() {
        return connection_count;
    }

    public void setConnection_count(int connection_count) {
        this.connection_count = connection_count;
    }

    public int getNotification_count() {
        return notification_count;
    }

    public void setNotification_count(int notification_count) {
        this.notification_count = notification_count;
    }
}
