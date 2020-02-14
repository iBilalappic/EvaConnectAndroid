package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IJobAdRepository;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.DateUtils;

import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobListRepository implements IJobAdRepository {

    private MutableLiveData<BaseModel<List<JobAd>>> MessageMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<CompanyJobAdModel>>> CompanyMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<BaseModel<List<JobAd>>> getjobAd() {
        MessageMutableLiveData = new MutableLiveData<>();
        RestClient.get().appApi().getjobAd().enqueue(new Callback<BaseModel<List<JobAd>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<JobAd>>> call, Response<BaseModel<List<JobAd>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    MessageMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    MessageMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<JobAd>>> call, Throwable t) {
                MessageMutableLiveData.setValue(null);
            }
        });
        return MessageMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<CompanyJobAdModel>>> getCompanyAd(User user) {
        CompanyMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_id",user.getId());
        RestClient.get().appApi().getCompanyAd(body).enqueue(new Callback<BaseModel<List<CompanyJobAdModel>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<CompanyJobAdModel>>> call, Response<BaseModel<List<CompanyJobAdModel>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    CompanyMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    CompanyMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<CompanyJobAdModel>>> call, Throwable t) {
                CompanyMutableLiveData.setValue(null);
            }
        });
        return CompanyMutableLiveData;
    }
}