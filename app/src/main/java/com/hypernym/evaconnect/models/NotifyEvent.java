package com.hypernym.evaconnect.models;

/**
 * Created by Metis on 18-May-18.
 */

public class NotifyEvent {
    private String data;

    public NotifyEvent(String data){
        this.data = data;
    }

    public String getData(){
        return data;
    }
}