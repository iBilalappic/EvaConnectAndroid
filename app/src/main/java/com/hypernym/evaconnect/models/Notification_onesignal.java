package com.hypernym.evaconnect.models;

import java.util.List;

public class Notification_onesignal {
    public String app_id;
    public List<Filter> filters;
    public List<String> included_segments;
    public Data data;
    public Contents contents;
}
