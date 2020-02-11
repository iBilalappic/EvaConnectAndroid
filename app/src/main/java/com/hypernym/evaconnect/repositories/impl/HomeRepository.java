package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Dashboard;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IHomeRepository;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepository implements IHomeRepository {

    private MutableLiveData<BaseModel<List<Post>>> dashboardMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Post>>> notificationMutableLiveData = new MutableLiveData<>();


    @Override
    public LiveData<BaseModel<List<Post>>> getDashboard(User user,int total,int current) {
        dashboardMutableLiveData=new MutableLiveData<>();
        user.setUser_id(user.getId());
        RestClient.get().appApi().getDashboard(user,total,current).enqueue(new Callback<BaseModel<List<Post>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Post>>> call, Response<BaseModel<List<Post>>> response) {
                dashboardMutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<List<Post>>> call, Throwable t) {
                dashboardMutableLiveData.setValue(null);
            }
        });
        return dashboardMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Post>>> getAllNotifications() {
       notificationMutableLiveData=new MutableLiveData<>();
        User user=LoginUtils.getLoggedinUser();
        HashMap<String,Object> data=new HashMap<String,Object>();
        data.put("receiver_id",user.getId());
       // data.put("is_read",0);
        RestClient.get().appApi().getAllNotifications(data).enqueue(new Callback<BaseModel<List<Post>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Post>>> call, Response<BaseModel<List<Post>>> response) {
                notificationMutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<List<Post>>> call, Throwable t) {
                notificationMutableLiveData.setValue(null);
            }
        });
        return notificationMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Post>>> notificationMarkAsRead(int id) {
        dashboardMutableLiveData=new MutableLiveData<>();
        User user=new User();
        user.setUser_id(id);

        RestClient.get().appApi().notificationMarkAsRead(user).enqueue(new Callback<BaseModel<List<Post>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Post>>> call, Response<BaseModel<List<Post>>> response) {
                dashboardMutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<List<Post>>> call, Throwable t) {
                dashboardMutableLiveData.setValue(null);
            }
        });
        return dashboardMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Post>>> getAllUnReadNotifications() {
        User user=LoginUtils.getLoggedinUser();
        HashMap<String,Object> data=new HashMap<String,Object>();
        data.put("receiver_id",user.getId());
        data.put("is_read",0);
        RestClient.get().appApi().getAllNotifications(data).enqueue(new Callback<BaseModel<List<Post>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Post>>> call, Response<BaseModel<List<Post>>> response) {
                notificationMutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<List<Post>>> call, Throwable t) {
                notificationMutableLiveData.setValue(null);
            }
        });
        return notificationMutableLiveData;
    }

}
