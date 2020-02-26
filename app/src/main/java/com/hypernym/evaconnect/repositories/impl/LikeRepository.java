package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.ILikeRepository;
import com.hypernym.evaconnect.repositories.IMessageRepository;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LikeRepository implements ILikeRepository {

    private MutableLiveData<BaseModel<List<MyLikesModel>>> MessageMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<BaseModel<List<MyLikesModel>>> getLikes(int user_id,int totalpages,int currentPage) {
        MessageMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_id",user_id);
        RestClient.get().appApi().getLikes(body,totalpages,currentPage).enqueue(new Callback<BaseModel<List<MyLikesModel>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<MyLikesModel>>> call, Response<BaseModel<List<MyLikesModel>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                 MessageMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    MessageMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<MyLikesModel>>> call, Throwable t) {
                MessageMutableLiveData.setValue(null);
            }
        });
        return MessageMutableLiveData;
    }
}
