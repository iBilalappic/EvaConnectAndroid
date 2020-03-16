package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.AppliedApplicants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.repositories.IApplicantRepository;
import com.hypernym.evaconnect.repositories.ILikeRepository;
import com.hypernym.evaconnect.viewmodel.AppliedApplicantViewModel;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApplicantsRepository implements IApplicantRepository {

    private MutableLiveData<BaseModel<List<AppliedApplicants>>> ApplicantsMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<BaseModel<List<AppliedApplicants>>> getApplicants(int job_id) {
        ApplicantsMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("job_id", job_id);
        RestClient.get().appApi().getApplicants(body).enqueue(new Callback<BaseModel<List<AppliedApplicants>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<AppliedApplicants>>> call, Response<BaseModel<List<AppliedApplicants>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                     ApplicantsMutableLiveData.setValue(response.body());
                    if (response.code() == 500) {
                        ApplicantsMutableLiveData.setValue(null);
                    }
            }

            @Override
            public void onFailure(Call<BaseModel<List<AppliedApplicants>>> call, Throwable t) {
                ApplicantsMutableLiveData.setValue(null);
            }
        });
        return ApplicantsMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<AppliedApplicants>>> declineApplication(int applicant_job_id, AppliedApplicants appliedApplicants) {
        ApplicantsMutableLiveData=new MutableLiveData<>();

        RestClient.get().appApi().declineApplication(applicant_job_id,appliedApplicants).enqueue(new Callback<BaseModel<List<AppliedApplicants>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<AppliedApplicants>>> call, Response<BaseModel<List<AppliedApplicants>>> response) {
                if(response.isSuccessful() && !response.body().isError())
                {
                    ApplicantsMutableLiveData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<BaseModel<List<AppliedApplicants>>> call, Throwable t) {
                ApplicantsMutableLiveData.setValue(null);
            }
        });

        return ApplicantsMutableLiveData;
    }
}
