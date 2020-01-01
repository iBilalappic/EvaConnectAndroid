package com.hypernym.evaconnect.communication.api;

import com.hypernym.evaconnect.constants.APIConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AppApi {

    @Multipart
    @POST(APIConstants.SIGNUP)
    Call<BaseModel<List<User>>> signup(@Part("status") RequestBody status, @Part("first_name")RequestBody firstname,
                                   @Part("email") RequestBody email,
                                   @Part("password") RequestBody password,@Part("type") RequestBody type,
                                   @Part("bio_data") RequestBody biodata, @Part MultipartBody.Part user_image);

    @POST(APIConstants.LOGIN)
    Call<BaseModel<List<User>>> login(@Body User user);

    @POST(APIConstants.FORGOT_PASSWORD)
    Call<BaseModel<List<User>>> forgotPassword(@Body String username);


}
