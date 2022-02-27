package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.ICreateJobAdRepository;
import com.hypernym.evaconnect.utils.DateUtils;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateJobRepository implements ICreateJobAdRepository {

    private MutableLiveData<BaseModel<List<Object>>> MessageMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> UpdateMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<String>>> JobTypeMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<BaseModel<List<Object>>> createJobAd(User user, MultipartBody.Part partImage,
                                                         String jobSector, int amount,
                                                         String companyName, String jobDescription,
                                                         String Location, String jobtitle, String postion, String jobtype, String jobduration) {
        MessageMutableLiveData = new MutableLiveData<>();
//        HashMap<String, Object> body = new HashMap<>();
//        body.put("user_id",user_id);
        RestClient.get().appApi().createJobAd(
                user.getId(),
                RequestBody.create(MediaType.parse("text/plain"), user.getStatus()),
                RequestBody.create(MediaType.parse("text/plain"), jobtitle),
                RequestBody.create(MediaType.parse("text/plain"), jobSector),
                RequestBody.create(MediaType.parse("text/plain"), postion),
                RequestBody.create(MediaType.parse("text/plain"), jobDescription),
                RequestBody.create(MediaType.parse("text/plain"), Location),
                amount,
                user.getId(),
                RequestBody.create(MediaType.parse("text/plain"), DateUtils.GetCurrentdate()),
                RequestBody.create(MediaType.parse("text/plain"), jobtype),Integer.parseInt(jobduration),
                partImage,
                RequestBody.create(MediaType.parse("text/plain"),companyName)).enqueue(new Callback<BaseModel<List<Object>>>() {
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

    @Override
    public LiveData<BaseModel<List<Object>>> UpdateJobAd(int job_id, User user, MultipartBody.Part partImage,
                                                         String jobSector, int amount,
                                                         String companyName, String jobDescription,
                                                         String Location, String jobtitle, String postion, String jobtype, String jobDuration) {
        UpdateMutableLiveData = new MutableLiveData<>();
//        HashMap<String, Object> body = new HashMap<>();
//        body.put("user_id",user_id);
        RestClient.get().appApi().UpdateJobAd(job_id,
                RequestBody.create(MediaType.parse("text/plain"), user.getStatus()),
                RequestBody.create(MediaType.parse("text/plain"), jobtitle),
                RequestBody.create(MediaType.parse("text/plain"), jobSector),
                RequestBody.create(MediaType.parse("text/plain"), postion),
                RequestBody.create(MediaType.parse("text/plain"), jobDescription),
                RequestBody.create(MediaType.parse("text/plain"), Location), amount, user.getId(),
                RequestBody.create(MediaType.parse("text/plain"), DateUtils.GetCurrentdatetime()),
                RequestBody.create(MediaType.parse("text/plain"), jobtype),Integer.parseInt(jobDuration),
                partImage).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    UpdateMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    UpdateMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                UpdateMutableLiveData.setValue(null);
            }
        });
        return UpdateMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<String>>> getJobType() {
        JobTypeMutableLiveData=new MutableLiveData<>();
        RestClient.get().appApi().getJobType().enqueue(new Callback<BaseModel<List<String>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<String>>> call, Response<BaseModel<List<String>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    JobTypeMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    JobTypeMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<String>>> call, Throwable t) {
                JobTypeMutableLiveData.setValue(null);
            }
        });
        return JobTypeMutableLiveData;
    }
}