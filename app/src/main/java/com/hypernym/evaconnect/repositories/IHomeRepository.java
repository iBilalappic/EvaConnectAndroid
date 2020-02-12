package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Dashboard;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;

import java.util.List;

public interface IHomeRepository {
    LiveData<BaseModel<List<Post>>> getDashboard(User user,int total,int current);
    LiveData<BaseModel<List<Post>>> getAllNotifications();
    LiveData<BaseModel<List<Post>>> notificationMarkAsRead(int id);
    LiveData<BaseModel<List<Post>>> getAllUnReadNotifications();

}
