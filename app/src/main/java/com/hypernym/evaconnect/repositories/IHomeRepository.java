package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.NewSources;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;

import java.util.List;

public interface IHomeRepository {
    LiveData<BaseModel<List<Post>>> getDashboard(User user,int total,int current);
    LiveData<BaseModel<List<Post>>> getDashboardSearch(User user,int total,int current,String filter);
    LiveData<BaseModel<List<Post>>> getAllNotifications(int totalpages,int currentPage);
    LiveData<BaseModel<List<Post>>> notificationMarkAsRead(int id);
    LiveData<BaseModel<List<Post>>> getAllUnReadNotifications();
    LiveData<BaseModel<List<NewSources>>> getNewSources();
    LiveData<BaseModel<List<NewSources>>> setNewSources(List<Integer> newsSelectedids);

}
