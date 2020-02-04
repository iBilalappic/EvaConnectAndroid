package com.hypernym.evaconnect.communication.api;

import com.hypernym.evaconnect.constants.APIConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Dashboard;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.Notification;
import com.hypernym.evaconnect.models.Notification_onesignal;
import com.hypernym.evaconnect.models.Post;
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
    Call<BaseModel<List<User>>> forgotPassword(@Body User username);

    @GET(APIConstants.GET_POSTS)
    Call<BaseModel<List<Post>>> getPosts();

    @Multipart
    @POST(APIConstants.GET_POSTS)
    Call<BaseModel<List<Post>>> createPost(@Part("user_id") int user_id,@Part("content") RequestBody content,
                                           @Part("created_by_id") int created_by_id,@Part("status") RequestBody status,@Part List<MultipartBody.Part> post_image,@Part MultipartBody.Part post_video);

    @POST(APIConstants.CHECK_EMAIL_EXIST)
    Call<BaseModel<List<User>>> isEmailExist(@Body User user);

    @POST(APIConstants.DASHBOARD)
    Call<BaseModel<List<Post>>> getDashboard(@Body User user, @Query("limit") int limit, @Query("offset") int offset);

    @GET(APIConstants.FRIENDCONNECTION)
    Call<BaseModel<List<NetworkConnection>>> getFriendDetails(@Path("id") int id);

    @POST(APIConstants.ADD_COMMENT)
    Call<BaseModel<List<Comment>>> addComment(@Body Comment comment);

    @POST(APIConstants.LIKE_POST)
    Call<BaseModel<List<Post>>> likePost(@Body Post post);

    @POST(APIConstants.GET_POST_COMMENTS)
    Call<BaseModel<List<Comment>>> getPostComments(@Body Post post);

    @POST(APIConstants.CONNECT)
    Call<BaseModel<List<Connection>>> connect(@Body Connection connection);

    @PATCH(APIConstants.UPDATE_CONNECTION)
    Call<BaseModel<List<Connection>>> updateConnection(@Body Connection connection,@Path("id") int id);

    @POST(APIConstants.GET_ALL_CONNECTIONS)
    Call<BaseModel<List<User>>> getAllConnections(@Body User user);

    @POST(APIConstants.GET_CONNECTION_BY_FILTER)
    Call<BaseModel<List<User>>> getConnectionByFilter(@Body User user);

    @POST("api/v1/notifications")
    Call<Object> postPackets(@Body Notification_onesignal data);

    @POST(APIConstants.GET_ALL_NOTIFICATIONS)
    Call<BaseModel<List<Post>>> getAllNotifications(@Body User user);

    @PATCH(APIConstants.NOTIFICATION_MARKS_AS_READ)
    Call<BaseModel<List<Post>>> notificationMarkAsRead(@Path("id") int id);

    @GET(APIConstants.GET_POST_BY_ID)
    Call<BaseModel<List<Post>>> getPostById(@Path("id") int id);
}
