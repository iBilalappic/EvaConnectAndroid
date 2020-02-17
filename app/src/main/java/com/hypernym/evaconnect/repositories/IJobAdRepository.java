package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

public interface IJobAdRepository {
    LiveData<BaseModel<List<JobAd>>> getjobAd();
    LiveData<BaseModel<List<CompanyJobAdModel>>> getCompanyAd(User user);
    LiveData<BaseModel<List<Object>>> setLike(User user ,int application_id,String action);
}