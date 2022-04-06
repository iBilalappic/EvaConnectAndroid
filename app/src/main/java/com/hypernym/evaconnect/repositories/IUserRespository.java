package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.AccountCheck;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Stats;
import com.hypernym.evaconnect.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

public interface IUserRespository {
    LiveData<BaseModel<List<User>>> signup(User user, @Part MultipartBody.Part user_image);

    LiveData<BaseModel<List<Object>>> getEmailVerificationCode(String email, int verification_code);

    LiveData<BaseModel<List<User>>> login(User user);

    LiveData<BaseModel<List<User>>> forgotPassword(String email);

    LiveData<BaseModel<List<Object>>> resetPassword(String email, String code, String password);

    LiveData<BaseModel<List<Object>>> editProfile(Object user, int id);

    LiveData<BaseModel<List<User>>> isEmailExist(String email);

    LiveData<BaseModel<List<AccountCheck>>> isEmailExist_linkedin(String email);

    LiveData<BaseModel<List<User>>> linkedin_login(String email, String linkedinType);

    LiveData<BaseModel<List<User>>> facebookLogin(String email, String facebookType);

    LiveData<BaseModel<List<Object>>> profile_update(int id, String designation, String companyname, String firstname, MultipartBody.Part partImage);

    LiveData<BaseModel<List<User>>> getuser_details(Integer id);

    LiveData<BaseModel<List<String>>> getSector(String name);

    LiveData<BaseModel<List<User>>> setting_update(Integer notification, Integer id);

    LiveData<BaseModel<List<User>>> update_password(String Oldpassword, String Newpassword);

    LiveData<BaseModel<List<User>>> userOnline(boolean is_active);

    LiveData<BaseModel<List<Stats>>> getUserStats();

    LiveData<BaseModel<List<Object>>> deleteUser(Integer id);

    LiveData<BaseModel<List<Object>>> updateUserLocation(Integer id, User userData);

    LiveData<BaseModel<List<Object>>> verify_email(String email);

}
