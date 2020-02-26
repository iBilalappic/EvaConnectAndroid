package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.SpecficJobAd;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IJobAdRepository;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobListRepository implements IJobAdRepository {

    private MutableLiveData<BaseModel<List<JobAd>>> MessageMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<CompanyJobAdModel>>> CompanyMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> LikeMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<SpecficJobAd>>> JobMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<BaseModel<List<JobAd>>> getjobAd(User user) {
        MessageMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_id", user.getId());
        RestClient.get().appApi().getjobAd(body).enqueue(new Callback<BaseModel<List<JobAd>>>() {
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
        body.put("user_id", user.getId());
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

    @Override
    public LiveData<BaseModel<List<SpecficJobAd>>> getJobId(int job_id) {
        JobMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("user_id", LoginUtils.getUser().getId());
        RestClient.get().appApi().GetJobAd_ID(job_id,body).enqueue(new Callback<BaseModel<List<SpecficJobAd>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<SpecficJobAd>>> call, Response<BaseModel<List<SpecficJobAd>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                   JobMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    JobMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<SpecficJobAd>>> call, Throwable t) {
                JobMutableLiveData.setValue(null);
            }
        });
        return JobMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Object>>> setLike(User user, int application_id, String action) {
        LikeMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("job_id", application_id);
        body.put("created_by_id", user.getId());
        body.put("status", user.getStatus());
        body.put("action", action);
        RestClient.get().appApi().setLikeJob(body).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    LikeMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    LikeMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                LikeMutableLiveData.setValue(null);
            }
        });
        return LikeMutableLiveData;
    }
}