package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IUserRespository;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository implements IUserRespository {
    private MutableLiveData<BaseModel<List<User>>> userMutableLiveData = new MutableLiveData<>();


    @Override
    public LiveData<BaseModel<List<User>>> signup(User user, MultipartBody.Part partImage) {
        userMutableLiveData = new MutableLiveData<>();

        RestClient.get().appApi().signup(RequestBody.create(MediaType.parse("text/plain"),user.getStatus()), RequestBody.create(MediaType.parse("text/plain"),user.getFirst_name()), RequestBody.create(MediaType.parse("text/plain"),user.getEmail()),
                RequestBody.create(MediaType.parse("text/plain"),user.getPassword()), RequestBody.create(MediaType.parse("text/plain"),user.getType()), RequestBody.create(MediaType.parse("text/plain"),user.getBio_data()),partImage).enqueue(new Callback<BaseModel<List<User>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {
                if (response.body() != null ) {
                    userMutableLiveData.postValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {
                userMutableLiveData.postValue(null);
            }
        });
        return userMutableLiveData;
    }



    @Override
    public LiveData<BaseModel<List<User>>> login(User user) {
        userMutableLiveData = new MutableLiveData<>();
        RestClient.get().appApi().login(user).enqueue(new Callback<BaseModel<List<User>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {
                if (response.body() != null) {
                    userMutableLiveData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {
                userMutableLiveData.setValue(null);
            }
        });
        return userMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<User>>> forgotPassword(String email) {
        userMutableLiveData = new MutableLiveData<>();
        User user=new User();
        user.setUsername(email);
        RestClient.get().appApi().forgotPassword(user).enqueue(new Callback<BaseModel<List<User>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {
                if (response.body() != null) {
                    userMutableLiveData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {
                userMutableLiveData.setValue(null);
            }
        });
        return userMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<User>>> isEmailExist(String email) {
        userMutableLiveData = new MutableLiveData<>();
        User user=new User();
        user.setEmail(email);
        RestClient.get().appApi().isEmailExist(user).enqueue(new Callback<BaseModel<List<User>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {
                if (response.body() != null) {
                    userMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {
                userMutableLiveData.setValue(null);
            }
        });
        return userMutableLiveData;
    }


}
