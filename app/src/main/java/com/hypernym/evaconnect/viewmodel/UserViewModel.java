package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.AccountCheck;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IUserRespository;
import com.hypernym.evaconnect.repositories.impl.UserRepository;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;

public class UserViewModel extends AndroidViewModel {
    private IUserRespository iUserRespository =new UserRepository();

    public UserViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseModel<List<User>>> signUp(User user, MultipartBody.Part image)
    {
        return iUserRespository.signup(user,image);
    }

    public LiveData<BaseModel<List<User>>> login(User user)
    {
        return iUserRespository.login(user);
    }

    public LiveData<BaseModel<List<User>>> forgotPassword(String email)
    {
        return iUserRespository.forgotPassword(email);
    }

    public LiveData<BaseModel<List<User>>> isEmailExist(String email)
    {
        return iUserRespository.isEmailExist(email);
    }
    public LiveData<BaseModel<List<AccountCheck>>> isEmailExist_linkedin(String email)
    {
        return iUserRespository.isEmailExist_linkedin(email);
    }
    public LiveData<BaseModel<List<User>>> linkedin_login(String email)
    {
        return iUserRespository.linkedin_login(email);
    }

    public LiveData<BaseModel<List<User>>> facebookLogin(String user_email){
        return iUserRespository.facebookLogin(user_email);
    }

    public LiveData<BaseModel<List<Object>>> profile_update(int id, String designation,String companyname, String firstname, MultipartBody.Part partImage)
    {
        return iUserRespository.profile_update(id,designation,companyname,firstname,partImage);
    }

    public LiveData<BaseModel<List<User>>> getuser_details(Integer id)
    {
        return iUserRespository.getuser_details(id);
    }
    public LiveData<BaseModel<List<String>>> getSector(String name)
    {
        return iUserRespository.getSector(name);
    }
}
