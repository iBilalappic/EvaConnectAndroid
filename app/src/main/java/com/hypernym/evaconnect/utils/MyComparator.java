package com.hypernym.evaconnect.utils;

import com.hypernym.evaconnect.models.MyActivitiesModel;

import java.util.Comparator;

public class MyComparator implements Comparator<MyActivitiesModel> {


    @Override
    public int compare(MyActivitiesModel o1, MyActivitiesModel o2) {
        if (Integer.getInteger(o1.getCreatedDate()) > Integer.getInteger(o2.getCreatedDate())) {
            return 1;
        } else if (Integer.getInteger(o1.getCreatedDate()) < Integer.getInteger(o2.getCreatedDate())) {
            return -1;
        }
        return 0;
    }

}
