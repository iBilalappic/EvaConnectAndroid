package com.hypernym.evaconnect.communication.api;

import com.hypernym.evaconnect.constants.APIConstants;
import com.hypernym.evaconnect.models.AccountCheck;
import com.hypernym.evaconnect.models.AppliedApplicants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CalendarModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.Notification_onesignal;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.SpecficJobAd;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.viewmodel.AppliedApplicantViewModel;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
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
    Call<BaseModel<List<User>>> signup(@Part("status") RequestBody status,
                                       @Part("first_name") RequestBody firstname,
                                       @Part("email") RequestBody email,
                                       @Part("is_linkedin") int is_linkedin,
                                       @Part("linkedin_image_url") RequestBody linkedin_image_url,
                                       @Part("password") RequestBody password,
                                       @Part("type") RequestBody type,
                                       @Part("sector") RequestBody sector,
                                       @Part("company_name") RequestBody company_name,
                                       @Part("designation") RequestBody designation,
                                       @Part("work_aviation") RequestBody work_aviation,
                                       @Part("country") RequestBody country,
                                       @Part("city") RequestBody city,
                                       @Part("last_name") RequestBody last_name,
                                       @Part MultipartBody.Part user_image);

    @POST(APIConstants.LOGIN)
    Call<BaseModel<List<User>>> login(@Body User user);

    @POST(APIConstants.FORGOT_PASSWORD)
    Call<BaseModel<List<User>>> forgotPassword(@Body User username);

    @GET(APIConstants.GET_POSTS)
    Call<BaseModel<List<Post>>> getPosts();

    @Multipart
    @POST(APIConstants.GET_POSTS)
    Call<BaseModel<List<Post>>> createPost(@Part("user_id") int user_id, @Part("content") RequestBody content,
                                           @Part("created_by_id") int created_by_id, @Part("status") RequestBody status,@Part("is_url") boolean is_url, @Part List<MultipartBody.Part> post_image, @Part MultipartBody.Part post_video);
    @Multipart
    @POST(APIConstants.ADD_JOB_AD)
    Call<BaseModel<List<Object>>> createJobAd(@Part("user_id") int user_id,
                                              @Part("status") RequestBody status,
                                              @Part("job_title") RequestBody job_title,
                                              @Part("job_nature") RequestBody job_nature,
                                              @Part("job_sector") RequestBody job_sector,
                                              @Part("position") RequestBody position,
                                              @Part("content") RequestBody content,
                                              @Part("weekly_hours") RequestBody weekly_hours,
                                              @Part("location") RequestBody location,
                                              @Part("salary") int salary,
                                              @Part("created_by_id") int created_by_id,
                                              @Part("published_date") RequestBody published_date,
                                              @Part MultipartBody.Part job_image);

    @Multipart
    @PATCH(APIConstants.UPDATE_JOB_AD)
    Call<BaseModel<List<Object>>> UpdateJobAd(
            @Path("job_id") int job_id,
            @Part("status") RequestBody status,
            @Part("job_title") RequestBody job_title,
            @Part("job_nature") RequestBody job_nature,
            @Part("job_sector") RequestBody job_sector,
            @Part("position") RequestBody position,
            @Part("content") RequestBody content,
            @Part("weekly_hours") RequestBody weekly_hours,
            @Part("location") RequestBody location,
            @Part("salary") int salary,
            @Part("modified_by_id") int modified_by_id,
            @Part("modified_datetime") RequestBody modified_datetime,
            @Part MultipartBody.Part job_image);

    @POST(APIConstants.GET_JOB_AD_BY_ID)
    Call<BaseModel<List<SpecficJobAd>>> GetJobAd_ID(@Path("job_id") int job_id,@Body HashMap<String, Object> body);


    @Multipart
    @POST(APIConstants.APPLICATION_SUBMITT)
    Call<BaseModel<List<Object>>> SubmitAppicationForm(@Part("user_id") int user_id,
                                                       @Part("job_id") int job_id,
                                                       @Part("created_by_id") int created_by_id,
                                                       @Part("content") RequestBody content,
                                                       @Part("status") RequestBody status,
                                                       @Part MultipartBody.Part cv);

    @POST(APIConstants.GET_APPLICANTS)
    Call<BaseModel<List<AppliedApplicants>>> getApplicants(@Body HashMap<String, Object> body);

    @POST(APIConstants.JOB_LIKE)
        // Call<BaseModel<List<Object>>> setLikeJob();
    Call<BaseModel<List<Object>>> setLikeJob(@Body HashMap<String, Object> body);

    @POST(APIConstants.APPLY_INTERVIEW)
        // Call<BaseModel<List<Object>>> setLikeJob();
    Call<BaseModel<List<Object>>> apply_interview(@Body HashMap<String, Object> body);


    @POST(APIConstants.CHECK_EMAIL_EXIST)
    Call<BaseModel<List<User>>> isEmailExist(@Body User user);

    @POST(APIConstants.CHECK_EMAIL_EXIST)
    Call<BaseModel<List<AccountCheck>>> isEmailExist_linkedin(@Body User user);

    @POST(APIConstants.GETUSER_FROM_LINKEDIN)
    Call<BaseModel<List<User>>> linkedin_login(@Body User user);



    @POST(APIConstants.DASHBOARD)
    Call<BaseModel<List<Post>>> getDashboard(@Body User user, @Query("limit") int limit, @Query("offset") int offset);

    @GET(APIConstants.FRIENDCONNECTION)
    Call<BaseModel<List<NetworkConnection>>> getFriendDetails(@Path("id") int id);

    @POST(APIConstants.JOB_LIST_AD)
    Call<BaseModel<List<JobAd>>> getjobAd(@Body HashMap<String, Object> body);

    @POST(APIConstants.JOB_FILTER_AD)
    Call<BaseModel<List<CompanyJobAdModel>>> getCompanyAd(@Body HashMap<String, Object> body);

    @POST(APIConstants.GET_MY_LIKES)
    Call<BaseModel<List<MyLikesModel>>> getLikes(@Body HashMap<String, Object> body, @Query("limit") int limit, @Query("offset") int offset);

    @POST(APIConstants.ADD_COMMENT)
    Call<BaseModel<List<Comment>>> addComment(@Body Comment comment);

    @POST(APIConstants.LIKE_POST)
    Call<BaseModel<List<Post>>> likePost(@Body Post post);

    @POST(APIConstants.GET_POST_COMMENTS)
    Call<BaseModel<List<Comment>>> getPostComments(@Body Post post);

    @POST(APIConstants.CONNECT)
    Call<BaseModel<List<Connection>>> connect(@Body Connection connection);

    @PATCH(APIConstants.UPDATE_CONNECTION)
    Call<BaseModel<List<Connection>>> updateConnection(@Body Connection connection, @Path("id") int id);

    @POST(APIConstants.GET_ALL_CONNECTIONS)
    Call<BaseModel<List<User>>> getAllConnections(@Body User user,@Query("limit") int limit, @Query("offset") int offset);

    @POST(APIConstants.GET_CONNECTION_BY_FILTER)
    Call<BaseModel<List<User>>> getConnectionByFilter(@Body User user,@Query("limit") int limit, @Query("offset") int offset);

    @POST(APIConstants.SEND_NOTIFICATION)
    Call<Object> postPackets(@Body Notification_onesignal data);

    @POST(APIConstants.GET_ALL_NOTIFICATIONS)
    Call<BaseModel<List<Post>>> getAllNotifications(@Body Object user,@Query("limit") int limit, @Query("offset") int offset);

    @POST(APIConstants.GET_ALL_NOTIFICATIONS)
    Call<BaseModel<List<Post>>> getAllUnreadNotifications(@Body Object user);

    @PATCH(APIConstants.NOTIFICATION_MARKS_AS_READ)
    Call<BaseModel<List<Post>>> notificationMarkAsRead(@Body User user);

    @POST(APIConstants.GET_POST_BY_ID)
    Call<BaseModel<List<Post>>> getPostById(@Body Object user);

    @GET(APIConstants.GET_CONNECTION_COUNT)
    Call<BaseModel<User>> getConnectionCount(@Path("id") int id);

    @Multipart
    @POST(APIConstants.ADD_EVENT)
    Call<BaseModel<List<Event>>> createEvent(@Part("user_id") int user_id,
                                             @Part("created_by_id") int created_by_id,
                                             @Part("content") RequestBody content,
                                             @Part("status") RequestBody status,
                                             @Part("event_city") RequestBody event_city,
                                             @Part("event_address") RequestBody event_address,
                                             @Part("event_start_date") RequestBody start_date,
                                             @Part("event_end_date") RequestBody end_date,
                                             @Part("start_time") RequestBody start_time,
                                             @Part("end_time") RequestBody end_time,
                                             @Part("event_name") RequestBody event_name,
                                             @Part MultipartBody.Part event_image);


    @POST(APIConstants.GET_EVENT_DETAILS)
    Call<BaseModel<List<Event>>> getEventDetails(@Body Event event);

    @POST(APIConstants.GET_EVENT_COMMENTS)
    Call<BaseModel<List<Comment>>> getEventComments(@Body Event event);

    @POST(APIConstants.ADD_EVENT_COMMENT)
    Call<BaseModel<List<Comment>>> addEventComment(@Body Comment comment);

    @POST(APIConstants.ADD_EVENT_ATTENDANCE)
    Call<BaseModel<List<Event>>> addEventAttendance(@Body Event eventAttendees);

    @PATCH(APIConstants.UPDATE_EVENT_ATTENDANCE)
    Call<BaseModel<List<Event>>> updateEventAttendance(@Body Event eventAttendees);

    @POST(APIConstants.LIKE_EVENT)
    Call<BaseModel<List<Event>>> likeEvent(@Body Event event);

    @POST(APIConstants.GET_CALENDAR_MARKS)
    Call<BaseModel<List<CalendarModel>>> getAllCalendarMarks(@Body CalendarModel calendarModel);

    @POST(APIConstants.GET_CALENDAR_BY_DATE)
    Call<BaseModel<List<CalendarModel>>> getCalendarMarksByDate(@Body CalendarModel calendarModel);

    @POST(APIConstants.CREATE_NOTE)
    Call<BaseModel<List<CalendarModel>>> createNote(@Body CalendarModel calendarModel);

    @PATCH(APIConstants.DECLINE_APPLICATION)
    Call<BaseModel<List<AppliedApplicants>>> declineApplication(@Path("job_application_id") int job_id,@Body AppliedApplicants applicants);

    @DELETE(APIConstants.REMOVE_USER)
    Call<BaseModel<List<Object>>> remove_user(@Path("id") int id);

    @PATCH(APIConstants.BLOCK_USER)
    Call<BaseModel<List<Object>>> block_user(@Path("connection_id") int connection_id,@Body HashMap<String, Object> body);

    @PATCH(APIConstants.PROFILE_UPDATE)
    Call<BaseModel<List<Object>>> profile_update(@Path("userid") int user_id,@Body HashMap<String, Object> body);

    @GET(APIConstants.GET_USER_DETAILS)
    Call<BaseModel<List<User>>> getuser_details(@Path("id") int user_id);

    @POST(APIConstants.GET_SECTOR)
    Call<BaseModel<List<String>>> getSector(@Body HashMap<String, Object> body);



}
