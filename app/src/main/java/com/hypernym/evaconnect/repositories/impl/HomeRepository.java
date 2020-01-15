package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Dashboard;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IHomeRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeRepository implements IHomeRepository {

    private MutableLiveData<BaseModel<List<Post>>> dashboardMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<BaseModel<List<Post>>> getDashboard(User user) {
        dashboardMutableLiveData=new MutableLiveData<>();
        user.setUser_id(user.getId());
        RestClient.get().appApi().getDashboard(user).enqueue(new Callback<BaseModel<List<Post>>>() {
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
}
