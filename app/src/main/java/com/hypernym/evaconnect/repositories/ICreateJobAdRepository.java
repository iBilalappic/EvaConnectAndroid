package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

public interface ICreateJobAdRepository {
    LiveData<BaseModel<List<Object>>> createJobAd(User user, @Part MultipartBody.Part user_image,
    String jobSector,int amount,String companyName,String jobDescription,String Locaiton,String jobtitle,String postion,int duration);

    LiveData<BaseModel<List<Object>>> UpdateJobAd(int job_id,User user, @Part MultipartBody.Part user_image,
                                                  String jobSector,int amount,String companyName,String jobDescription,String Locaiton,String jobtitle,String postion,int duration);
}
