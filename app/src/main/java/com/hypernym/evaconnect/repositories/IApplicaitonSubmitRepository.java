package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

public interface IApplicaitonSubmitRepository {
    LiveData<BaseModel<List<Object>>> submitApplication(User user, int job_id,String content, @Part MultipartBody.Part user_image);
}
