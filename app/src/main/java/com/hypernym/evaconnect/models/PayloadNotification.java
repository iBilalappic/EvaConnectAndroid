package com.hypernym.evaconnect.models;

import java.util.List;

/**
 * Created by Metis on 27-Mar-18.
 */

public class PayloadNotification {
    public String app_id;
    public List<Filter> filters;
    public List<String> included_segments;
    public Data data;
    public Contents contents;

}
