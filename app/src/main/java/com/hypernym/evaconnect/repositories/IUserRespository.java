package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.AccountCheck;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.view.ui.activities.ForgotPassword;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Part;

public interface IUserRespository {

    LiveData<BaseModel<List<User>>> signup(User user, @Part MultipartBody.Part user_image);

    LiveData<BaseModel<List<User>>> login(User user);

    LiveData<BaseModel<List<User>>> forgotPassword(String email);

    LiveData<BaseModel<List<User>>> isEmailExist(String email);
    LiveData<BaseModel<List<AccountCheck>>> isEmailExist_linkedin(String email);
    LiveData<BaseModel<List<User>>> linkedin_login(String email);
    LiveData<BaseModel<List<Object>>> profile_update(Integer id, String designation, String field, String company_name, String address, String bio_data);
    LiveData<BaseModel<List<User>>> getuser_details(Integer id);
    LiveData<BaseModel<List<String>>> getSector(String name);
}
