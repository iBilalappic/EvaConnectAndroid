package com.hypernym.evaconnect.communication.api;

import com.hypernym.evaconnect.constants.APIConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Dashboard;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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
    Call<BaseModel<List<User>>> forgotPassword(@Body User username);

    @GET(APIConstants.GET_POSTS)
    Call<BaseModel<List<Post>>> getPosts();

    @Multipart
    @POST(APIConstants.GET_POSTS)
    Call<BaseModel<List<Post>>> createPost(@Part("user_id") int user_id,@Part("content") RequestBody content,
                                           @Part("created_by_id") int created_by_id,@Part("status") RequestBody status,@Part List<MultipartBody.Part> post_image);

    @POST(APIConstants.CHECK_EMAIL_EXIST)
    Call<BaseModel<List<User>>> isEmailExist(@Body User user);

    @POST(APIConstants.DASHBOARD)
    Call<BaseModel<List<Post>>> getDashboard(@Body User user);

    @POST(APIConstants.ADD_COMMENT)
    Call<BaseModel<List<Comment>>> addComment(@Body Comment comment);

    @POST(APIConstants.LIKE_POST)
    Call<BaseModel<List<Post>>> likePost(@Body Post post);

    @POST(APIConstants.GET_POST_COMMENTS)
    Call<BaseModel<List<Comment>>> getPostComments(@Body Post post);
}