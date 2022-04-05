package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.AccountCheck;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.GetBlockedData;
import com.hypernym.evaconnect.models.Stats;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IUserRespository;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

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
    private MutableLiveData<BaseModel<List<User>>> facebookLoginMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> ProfileUpdateMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> EditProfileMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> EmailVerificationCode = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<String>>> SectorMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Stats>>> statMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<GetBlockedData>>> getBlockedUsers = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> resetPasswordMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> deleteUserMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> verifyEmailMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<BaseModel<List<User>>> signup(User user, MultipartBody.Part partImage) {
        userMutableLiveData = new MutableLiveData<>();

        RestClient.get().appApi().signup(RequestBody.create(MediaType.parse("text/plain"),
                user.getStatus()), RequestBody.create(MediaType.parse("text/plain"), user.getFirst_name()),
                RequestBody.create(MediaType.parse("text/plain"), user.getEmail()),
                user.getIsLinkedin(),
                user.getIs_facebook(),
                RequestBody.create(MediaType.parse("text/plain"), user.getLinkedin_image_url()),
                RequestBody.create(MediaType.parse("text/plain"), user.getFacebook_image_url()),
                RequestBody.create(MediaType.parse("text/plain"), user.getPassword()),
                RequestBody.create(MediaType.parse("text/plain"), user.getType()),
                RequestBody.create(MediaType.parse("text/plain"), user.getSector()),
                RequestBody.create(MediaType.parse("text/plain"), user.getOther_sector()),
                RequestBody.create(MediaType.parse("text/plain"), user.getCompany_name()),
                RequestBody.create(MediaType.parse("text/plain"), user.getDesignation()),
                RequestBody.create(MediaType.parse("text/plain"), user.getWork_aviation()),
                RequestBody.create(MediaType.parse("text/plain"), user.getCountry()),
                RequestBody.create(MediaType.parse("text/plain"), user.getCity()),
                RequestBody.create(MediaType.parse("text/plain"), user.getLast_name()),
                RequestBody.create(MediaType.parse("text/plain"), user.getLanguage()),
              /*  RequestBody.create(MediaType.parse("text/plain"), user.getCompany_url()),*/
                partImage).enqueue(new Callback<BaseModel<List<User>>>() {
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
    public LiveData<BaseModel<List<Object>>> getEmailVerificationCode(String email, int verification_code) {
        EmailVerificationCode = new MutableLiveData<>();
        RestClient.get().appApi().getEmailVerificationCode(email,verification_code).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                if (response.body() != null) {
                    EmailVerificationCode.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                EmailVerificationCode.setValue(null);
            }
        });
        return EmailVerificationCode;
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
    public LiveData<BaseModel<List<User>>> setting_update(Integer notification, Integer id) {
        userMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("is_notifications", notification);
        body.put("modified_by_id", id);
        body.put("modified_datetime", DateUtils.GetCurrentdatetime());
        RestClient.get().appApi().setting_update(body,id).enqueue(new Callback<BaseModel<List<User>>>() {
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
    public LiveData<BaseModel<List<User>>> update_password(String Oldpassword, String Newpassword) {
        userMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("user_id", LoginUtils.getUser().getId());
        body.put("old_password", Oldpassword);
        body.put("new_password", Newpassword);
        RestClient.get().appApi().update_password(body).enqueue(new Callback<BaseModel<List<User>>>() {
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
    public LiveData<BaseModel<List<User>>> userOnline(boolean is_active) {
        userMutableLiveData=new MutableLiveData<>();
        HashMap<String,Object> body=new HashMap<>();
        body.put("is_online",is_active);
        body.put("modified_by_id",LoginUtils.getLoggedinUser().getId());
        body.put("last_online_datetime",DateUtils.GetCurrentdatetime());
        RestClient.get().appApi().userOnline(LoginUtils.getLoggedinUser().getId(),body).enqueue(new Callback<BaseModel<List<User>>>() {
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
    public LiveData<BaseModel<List<Stats>>> getUserStats() {
        statMutableLiveData=new MutableLiveData<>();

        RestClient.get().appApi().getUserStats(LoginUtils.getLoggedinUser().getId()).enqueue(new Callback<BaseModel<List<Stats>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Stats>>> call, Response<BaseModel<List<Stats>>> response) {
                if (response.body() != null) {
                    statMutableLiveData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<BaseModel<List<Stats>>> call, Throwable t) {
                statMutableLiveData.setValue(null);
            }
        });
        return statMutableLiveData;
    }


    @Override
    public LiveData<BaseModel<List<User>>> forgotPassword(String email) {
        userMutableLiveData = new MutableLiveData<>();
        User user = new User();
        user.setUsername(email);
        RestClient.get().appApi().forgotPassword(email).enqueue(new Callback<BaseModel<List<User>>>() {
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
    public LiveData<BaseModel<List<Object>>> resetPassword(String email, String code, String password ) {
        resetPasswordMutableLiveData = new MutableLiveData<>();

        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("email", email);
        body.put("verification_code", code);
        body.put("new_password", password);


        RestClient.get().appApi().getResetPassword(body).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                if (response.body() != null) {
                    resetPasswordMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                resetPasswordMutableLiveData.setValue(null);
            }
        });
        return resetPasswordMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Object>>> editProfile (Object user, int id) {
        resetPasswordMutableLiveData = new MutableLiveData<>();

       /* HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("email", email);
        body.put("verification_code", code);
        body.put("new_password", password);*/


        RestClient.get().appApi().Edit_Profile(id, user).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                if (response.body() != null) {
                    resetPasswordMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                resetPasswordMutableLiveData.setValue(null);
            }
        });
        return resetPasswordMutableLiveData;
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
    public LiveData<BaseModel<List<User>>> linkedin_login(String email, String linkedinType) {
        LinkedinLoginMutableLiveData = new MutableLiveData<>();
        User user = new User();
        user.setUsername(email);
        user.setLogin_type(linkedinType);
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
    public LiveData<BaseModel<List<User>>> facebookLogin(String email, String facebookType) {
        facebookLoginMutableLiveData = new MutableLiveData<>();
        User user = new User();
        user.setUsername(email);
        user.setLogin_type(facebookType);
        RestClient.get().appApi().facebookLogin(user).enqueue(new Callback<BaseModel<List<User>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {
                if (response.body() != null && response.isSuccessful()) {
                    facebookLoginMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {
                facebookLoginMutableLiveData.setValue(null);
                t.printStackTrace();
            }
        });

        return facebookLoginMutableLiveData;
    }


    @Override
    public LiveData<BaseModel<List<Object>>> profile_update(int id, String designation,String companyname, String firstname, MultipartBody.Part partImage) {
        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("designation", designation);
        body.put("first_name", designation);
        body.put("modified_by_id", id);
        body.put("modified_datetime", DateUtils.GetCurrentdatetime());
        ProfileUpdateMutableLiveData = new MutableLiveData<>();


        RestClient.get().appApi().profile_update(
                id,
                RequestBody.create(MediaType.parse("text/plain"), designation),
                RequestBody.create(MediaType.parse("text/plain"), companyname),
                RequestBody.create(MediaType.parse("text/plain"), firstname),
                id, RequestBody.create(MediaType.parse("text/plain"), DateUtils.GetCurrentdatetime()), partImage).enqueue(new Callback<BaseModel<List<Object>>>() {
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

        RestClient.get().appApi().getuser_details(id,true).enqueue(new Callback<BaseModel<List<User>>>() {
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
    public LiveData<BaseModel<List<String>>> getSector(String name) {
        SectorMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("work_aviation", name);
        RestClient.get().appApi().getSector(body).enqueue(new Callback<BaseModel<List<String>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<String>>> call, Response<BaseModel<List<String>>> response) {
                if (response.body() != null) {
                    SectorMutableLiveData.postValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<String>>> call, Throwable t) {
                SectorMutableLiveData.postValue(null);
            }
        });
        return SectorMutableLiveData;

    }

    @Override
    public LiveData<BaseModel<List<Object>>> deleteUser(Integer id) {
        RestClient.get().appApi().deleteUser(id).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                if (response.body()!=null){
                    deleteUserMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                deleteUserMutableLiveData.setValue(null);
                t.printStackTrace();
            }
        });

        return deleteUserMutableLiveData;
    }
    @Override
    public LiveData<BaseModel<List<Object>>> verify_email(String email) {
        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("email", email);
        RestClient.get().appApi().verify_email_token(body).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                if (response.body()!=null){
                    deleteUserMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                deleteUserMutableLiveData.setValue(null);
                t.printStackTrace();
            }
        });

        return deleteUserMutableLiveData;
    }
}
