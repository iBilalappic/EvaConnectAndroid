package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.AccountCheck;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IUserRespository;
import com.hypernym.evaconnect.utils.DateUtils;

import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository implements IUserRespository {
    private MutableLiveData<BaseModel<List<User>>> userMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<AccountCheck>>> LinkedinMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<User>>> LinkedinLoginMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> ProfileUpdateMutableLiveData = new MutableLiveData<>();


    @Override
    public LiveData<BaseModel<List<User>>> signup(User user, MultipartBody.Part partImage) {
        userMutableLiveData = new MutableLiveData<>();

        RestClient.get().appApi().signup(RequestBody.create(MediaType.parse("text/plain"),
                user.getStatus()), RequestBody.create(MediaType.parse("text/plain"), user.getFirst_name()),
                RequestBody.create(MediaType.parse("text/plain"), user.getEmail()),
                user.getIsLinkedin(),
                RequestBody.create(MediaType.parse("text/plain"), user.getLinkedin_image_url()),

                RequestBody.create(MediaType.parse("text/plain"), user.getPassword()),
                RequestBody.create(MediaType.parse("text/plain"), user.getType()),
                RequestBody.create(MediaType.parse("text/plain"), user.getBio_data()), partImage).enqueue(new Callback<BaseModel<List<User>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {
                if (response.body() != null) {
                    userMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {
                userMutableLiveData.postValue(null);
            }
        });
        return userMutableLiveData;
    }


    @Override
    public LiveData<BaseModel<List<User>>> login(User user) {
        userMutableLiveData = new MutableLiveData<>();
        RestClient.get().appApi().login(user).enqueue(new Callback<BaseModel<List<User>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {
                if (response.body() != null) {
                    userMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {
                userMutableLiveData.setValue(null);
            }
        });
        return userMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<User>>> forgotPassword(String email) {
        userMutableLiveData = new MutableLiveData<>();
        User user = new User();
        user.setUsername(email);
        RestClient.get().appApi().forgotPassword(user).enqueue(new Callback<BaseModel<List<User>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {
                if (response.body() != null) {
                    userMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {
                userMutableLiveData.setValue(null);
            }
        });
        return userMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<User>>> isEmailExist(String email) {
        userMutableLiveData = new MutableLiveData<>();
        User user = new User();
        user.setEmail(email);
        RestClient.get().appApi().isEmailExist(user).enqueue(new Callback<BaseModel<List<User>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {
                if (response.body() != null) {
                    userMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {
                userMutableLiveData.setValue(null);
            }
        });
        return userMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<AccountCheck>>> isEmailExist_linkedin(String email) {
        LinkedinMutableLiveData = new MutableLiveData<>();
        User user = new User();
        user.setEmail(email);
        RestClient.get().appApi().isEmailExist_linkedin(user).enqueue(new Callback<BaseModel<List<AccountCheck>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<AccountCheck>>> call, Response<BaseModel<List<AccountCheck>>> response) {
                if (response.body() != null) {
                    LinkedinMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<AccountCheck>>> call, Throwable t) {
                LinkedinMutableLiveData.setValue(null);
            }
        });
        return LinkedinMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<User>>> linkedin_login(String email) {
        LinkedinLoginMutableLiveData = new MutableLiveData<>();
        User user = new User();
        user.setUsername(email);
        RestClient.get().appApi().linkedin_login(user).enqueue(new Callback<BaseModel<List<User>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {
                if (response.body() != null) {
                    LinkedinLoginMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {
                LinkedinLoginMutableLiveData.setValue(null);
            }
        });
        return LinkedinLoginMutableLiveData;
    }


    @Override
    public LiveData<BaseModel<List<Object>>> profile_update(Integer id, String designation, String field, String company_name, String address, String bio_data) {
        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("designation", designation);
        body.put("field", field);
        body.put("company_name", company_name);
        body.put("address", address);
        body.put("bio_data", bio_data);
        body.put("modified_by_id", id);
        body.put("modified_datetime", DateUtils.GetCurrentdatetime());
        ProfileUpdateMutableLiveData = new MutableLiveData<>();

        RestClient.get().appApi().profile_update(id, body).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                if (response.body() != null) {
                    ProfileUpdateMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                ProfileUpdateMutableLiveData.setValue(null);
            }
        });
        return ProfileUpdateMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<User>>> getuser_details(Integer id) {
        userMutableLiveData = new MutableLiveData<>();

        RestClient.get().appApi().getuser_details(id).enqueue(new Callback<BaseModel<List<User>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {
                if (response.body() != null) {
                    userMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {
                userMutableLiveData.postValue(null);
            }
        });
        return userMutableLiveData;

    }
}
