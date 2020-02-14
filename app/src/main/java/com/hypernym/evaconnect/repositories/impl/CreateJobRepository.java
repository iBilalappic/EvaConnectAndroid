package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.ICreateJobAdRepository;
import com.hypernym.evaconnect.repositories.ILikeRepository;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.DateUtils;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateJobRepository implements ICreateJobAdRepository {

    private MutableLiveData<BaseModel<List<Object>>> MessageMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<BaseModel<List<Object>>> createJobAd(User user, MultipartBody.Part partImage,
                                                         String jobSector, String WeeklyHour, int amount,
                                                         String companyName, String jobDescription, String Location,String jobtitle,String postion) {
        MessageMutableLiveData = new MutableLiveData<>();
//        HashMap<String, Object> body = new HashMap<>();
//        body.put("user_id",user_id);
        RestClient.get().appApi().createJobAd(
                user.getId(),
                RequestBody.create(MediaType.parse("text/plain"), user.getStatus()),
                RequestBody.create(MediaType.parse("text/plain"), companyName+" for "+jobtitle),
                RequestBody.create(MediaType.parse("text/plain"), Constants.ANNUAL),
                RequestBody.create(MediaType.parse("text/plain"), jobSector),
                RequestBody.create(MediaType.parse("text/plain"), postion),
                RequestBody.create(MediaType.parse("text/plain"), jobDescription),
                RequestBody.create(MediaType.parse("text/plain"), WeeklyHour),
                RequestBody.create(MediaType.parse("text/plain"), Location), amount, user.getId(),
                RequestBody.create(MediaType.parse("text/plain"), DateUtils.GetCurrentdate()),
                partImage).enqueue(new Callback<BaseModel<List<Object>>>() {
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