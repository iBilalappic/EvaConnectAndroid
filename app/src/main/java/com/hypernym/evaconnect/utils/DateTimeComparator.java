package com.hypernym.evaconnect.utils;

import com.hypernym.evaconnect.models.NetworkConnection;

import java.util.Comparator;

public class DateTimeComparator implements Comparator<NetworkConnection>
{
    public int compare(NetworkConnection left, NetworkConnection right) {
        return left.getCreatedDatetime().compareTo(right.getCreatedDatetime());
    }
}