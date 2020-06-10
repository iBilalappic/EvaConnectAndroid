package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.ShareConnection;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IConnectionRespository;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConnectionRepository implements IConnectionRespository {

    private MutableLiveData<BaseModel<List<Connection>>> connectionMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<User>>> userMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<BaseModel<User>> connectionCountMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> remove_userMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> block_userMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> share_connection = new MutableLiveData<>();

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
    public LiveData<BaseModel<List<User>>> getAllConnections(int total,int current) {
        userMutableLiveData=new MutableLiveData<>();
        User user=LoginUtils.getLoggedinUser();

        RestClient.get().appApi().getAllConnections(user,total,current).enqueue(new Callback<BaseModel<List<User>>>() {
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
    public LiveData<BaseModel<List<User>>> getConnectionByFilter(User userData,int total,int current) {
        userMutableLiveData=new MutableLiveData<>();

        RestClient.get().appApi().getConnectionByFilter(userData,total,current).enqueue(new Callback<BaseModel<List<User>>>() {
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
    public LiveData<BaseModel<List<User>>> getConnectionByRecommendedUser(User userData,int total,int current) {
        userMutableLiveData=new MutableLiveData<>();

        RestClient.get().appApi().getConnectionByRecommendedUser(userData,total,current).enqueue(new Callback<BaseModel<List<User>>>() {
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
    public LiveData<BaseModel<User>> getConnectionCount(User userData) {
        connectionCountMutableLiveData=new MutableLiveData<>();

        RestClient.get().appApi().getConnectionCount(userData.getId()).enqueue(new Callback<BaseModel<User>>() {
            @Override
            public void onResponse(Call<BaseModel<User>> call, Response<BaseModel<User>> response) {
                connectionCountMutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<User>> call, Throwable t) {
                connectionCountMutableLiveData.setValue(null);
            }
        });
        return connectionCountMutableLiveData;
    }
    @Override
    public LiveData<BaseModel<List<Object>>> remove_user(Integer connection_id) {
        remove_userMutableLiveData=new MutableLiveData<>();

        RestClient.get().appApi().remove_user(connection_id).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                remove_userMutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                remove_userMutableLiveData.setValue(null);
            }
        });
        return remove_userMutableLiveData;
    }
    @Override
    public LiveData<BaseModel<List<Object>>> block_user(Integer connection_id,User user) {
        HashMap<String, Object> body = new HashMap<>();
        body.put("modified_by_id",user.getId());
        body.put("modified_datetime", DateUtils.GetCurrentdatetime());
        body.put("status", AppConstants.DELETED);
        block_userMutableLiveData=new MutableLiveData<>();

        RestClient.get().appApi().block_user(connection_id,body).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                block_userMutableLiveData.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                block_userMutableLiveData.setValue(null);
            }
        });
        return block_userMutableLiveData;
    }
    @Override
    public LiveData<BaseModel<List<Object>>> share_connection(ShareConnection shareConnection) {

        share_connection=new MutableLiveData<>();

        RestClient.get().appApi().share_connection(shareConnection).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                share_connection.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                share_connection.setValue(null);
            }
        });
        return share_connection;
    }
    @Override
    public LiveData<BaseModel<List<Object>>> share_connection_post(ShareConnection shareConnection) {

        share_connection=new MutableLiveData<>();

        RestClient.get().appApi().share_connection_post(shareConnection).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                share_connection.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                share_connection.setValue(null);
            }
        });
        return share_connection;
    }
    @Override
    public LiveData<BaseModel<List<Object>>> share_connection_event(ShareConnection shareConnection) {

        share_connection=new MutableLiveData<>();

        RestClient.get().appApi().share_connection_event(shareConnection).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                share_connection.setValue(response.body());
            }
            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                share_connection.setValue(null);
            }
        });
        return share_connection;
    }
}
