package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IConnectionRespository;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConnectionRepository implements IConnectionRespository {

    private MutableLiveData<BaseModel<List<Connection>>> connectionMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<User>>> userMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<BaseModel<List<Connection>>> connect(Connection connection) {
        connectionMutableLiveData=new MutableLiveData<>();
        if(connection.getId()==null)
        {
            RestClient.get().appApi().connect(connection).enqueue(new Callback<BaseModel<List<Connection>>>() {
                @Override
                public void onResponse(Call<BaseModel<List<Connection>>> call, Response<BaseModel<List<Connection>>> response) {
                    connectionMutableLiveData.setValue(response.body());
                }

                @Override
                public void onFailure(Call<BaseModel<List<Connection>>> call, Throwable t) {
                    connectionMutableLiveData.setValue(null);
                }
            });
        }
        else
        {
            RestClient.get().appApi().updateConnection(connection,connection.getId()).enqueue(new Callback<BaseModel<List<Connection>>>() {
                @Override
                public void onResponse(Call<BaseModel<List<Connection>>> call, Response<BaseModel<List<Connection>>> response) {
                    connectionMutableLiveData.setValue(response.body());
                }

                @Override
                public void onFailure(Call<BaseModel<List<Connection>>> call, Throwable t) {
                    connectionMutableLiveData.setValue(null);
                }
            });
        }

        return connectionMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<User>>> getAllConnections() {
        userMutableLiveData=new MutableLiveData<>();
        User user=LoginUtils.getLoggedinUser();

        RestClient.get().appApi().getAllConnections(user).enqueue(new Callback<BaseModel<List<User>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {
                userMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {
                userMutableLiveData.setValue(null);
            }
        });
        return userMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<User>>> getConnectionByFilter(String type) {
        userMutableLiveData=new MutableLiveData<>();
        User userData=new User();
        User user=LoginUtils.getLoggedinUser();
        userData.setType(type);
        userData.setUser_id(user.getId());
        RestClient.get().appApi().getConnectionByFilter(userData).enqueue(new Callback<BaseModel<List<User>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {
                userMutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {
                userMutableLiveData.setValue(null);
            }
        });
        return userMutableLiveData;
    }
}
