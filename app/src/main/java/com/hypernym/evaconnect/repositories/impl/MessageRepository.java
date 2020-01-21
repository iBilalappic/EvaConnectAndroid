package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IMessageRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessageRepository implements IMessageRepository {

    private MutableLiveData<BaseModel<List<NetworkConnection>>> MessageMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<BaseModel<List<NetworkConnection>>> getFriendList(User user) {
        MessageMutableLiveData = new MutableLiveData<>();
        user.setUser_id(user.getId());
        RestClient.get().appApi().getFriendDetails(user.getId()).enqueue(new Callback<BaseModel<List<NetworkConnection>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<NetworkConnection>>> call, Response<BaseModel<List<NetworkConnection>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    MessageMutableLiveData.setValue(response.body());
//                if (response.code() == 500) {
//                    MessageMutableLiveData.setValue(null);
//                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<NetworkConnection>>> call, Throwable t) {
                MessageMutableLiveData.setValue(null);
            }
        });
        return MessageMutableLiveData;
    }
}
