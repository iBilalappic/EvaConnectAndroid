package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IApplicaitonSubmitRepository;
import com.hypernym.evaconnect.repositories.ICreateJobAdRepository;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.DateUtils;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicationSubmitRepository implements IApplicaitonSubmitRepository {

    private MutableLiveData<BaseModel<List<Object>>> MessageMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<BaseModel<List<Object>>> submitApplication(User user, int job_id, String content, MultipartBody.Part partImage) {
        MessageMutableLiveData = new MutableLiveData<>();
//        HashMap<String, Object> body = new HashMap<>();
//        body.put("user_id",user_id);
        RestClient.get().appApi().SubmitAppicationForm(user.getId(), job_id, user.getId(),
                RequestBody.create(MediaType.parse("text/plain"), content),
                RequestBody.create(MediaType.parse("text/plain"), user.getStatus())
                , partImage).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    MessageMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    MessageMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                MessageMutableLiveData.setValue(null);
            }
        });
        return MessageMutableLiveData;
    }
}