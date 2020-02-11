package com.hypernym.evaconnect.models;

/**
 * Created by Metis on 18-May-18.
 */

public class NotifyEvent {
    public String mMessage;

    public NotifyEvent(String message) {
        mMessage = message;
    }

    public String getMessage() {
        return mMessage;
    }
}