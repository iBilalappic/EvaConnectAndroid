package com.hypernym.evaconnect.communication.api;

import com.hypernym.evaconnect.constants.APIConstants;
import com.hypernym.evaconnect.models.AccountCheck;
import com.hypernym.evaconnect.models.AppliedApplicants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CalendarModel;
import com.hypernym.evaconnect.models.ChatAttachment;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.CompanyJobAdModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.GetBlockedData;
import com.hypernym.evaconnect.models.GetEventInterestedUsers;
import com.hypernym.evaconnect.models.GetPendingData;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.Meeting;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.NewSources;
import com.hypernym.evaconnect.models.Notification_onesignal;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.ShareConnection;
import com.hypernym.evaconnect.models.SpecficJobAd;
import com.hypernym.evaconnect.models.Stats;
import com.hypernym.evaconnect.models.User;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
                                       @Part("is_facebook") int is_facebook,
                                       @Part("linkedin_image_url") RequestBody linkedin_image_url,
                                       @Part("facebook_image_url") RequestBody facebook_image_url,
                                       @Part("password") RequestBody password,
                                       @Part("type") RequestBody type,
                                       @Part("sector") RequestBody sector,
                                       @Part("other_sector") RequestBody other_sector,
                                       @Part("company_name") RequestBody company_name,
                                       @Part("designation") RequestBody designation,
                                       @Part("work_aviation") RequestBody work_aviation,
                                       @Part("country") RequestBody country,
                                       @Part("city") RequestBody city,
                                       @Part("last_name") RequestBody last_name,
                                       @Part("language") RequestBody language,
                                       @Part MultipartBody.Part user_image);

    @POST(APIConstants.LOGIN)
    Call<BaseModel<List<User>>> login(@Body User user);

    @POST(APIConstants.BLOCK_USER)
    Call<BaseModel<List<Object>>> block(@Body Connection connection);

    @POST(APIConstants.FORGOT_PASSWORD)
    @FormUrlEncoded
    Call<BaseModel<List<User>>> forgotPassword(@Field("email") String email);

    @GET(APIConstants.GET_POSTS)
    Call<BaseModel<List<Post>>> getPosts();

    @GET(APIConstants.GET_BLOCKED_USERS)
    Call<BaseModel<List<GetBlockedData>>> getBlockedUsers();

    @Multipart
    @POST(APIConstants.GET_POSTS)
    Call<BaseModel<List<Post>>> createPost(@Part("user_id") int user_id, @Part("content") RequestBody content,
                                           @Part("created_by_id") int created_by_id, @Part("status") RequestBody status,@Part("is_url") boolean is_url, @Part List<MultipartBody.Part> post_image, @Part MultipartBody.Part post_video,@Part MultipartBody.Part post_document);

    @Multipart
    @PATCH(APIConstants.EDIT_POST)
    Call<BaseModel<List<Post>>> editPost(@Part("modified_by_id") int modified_by_id, @Part("content") RequestBody content,
                                         @Part("modified_datetime") RequestBody modified_datetime, @Part("is_url") boolean is_url, @Part List<MultipartBody.Part> post_image, @Part MultipartBody.Part post_video,@Part MultipartBody.Part post_document,@Path("id") int id);

    @PATCH(APIConstants.EDIT_POST)
    Call<BaseModel<List<Post>>> deletePost(@Body Post post,@Path("id") int id);

    @Multipart
    @POST(APIConstants.ADD_JOB_AD)
    Call<BaseModel<List<Object>>> createJobAd(@Part("user_id") int user_id,
                                              @Part("status") RequestBody status,
                                              @Part("job_title") RequestBody job_title,
                                              @Part("job_sector") RequestBody job_sector,
                                              @Part("position") RequestBody position,
                                              @Part("content") RequestBody content,
                                              @Part("location") RequestBody location,
                                              @Part("salary") int salary,
                                              @Part("created_by_id") int created_by_id,
                                              @Part("published_date") RequestBody published_date,
                                              @Part("job_type ") RequestBody job_type ,@Part("active_hours ") int active_hours,
                                              @Part MultipartBody.Part job_image,@Part("job_nature ") RequestBody job_nature);

    @Multipart
    @PATCH(APIConstants.UPDATE_JOB_AD)
    Call<BaseModel<List<Object>>> UpdateJobAd(
            @Path("job_id") int job_id,
            @Part("status") RequestBody status,
            @Part("job_title") RequestBody job_title,
            @Part("job_sector") RequestBody job_sector,
            @Part("position") RequestBody position,
            @Part("content") RequestBody content,
            @Part("location") RequestBody location,
            @Part("salary") int salary,
            @Part("modified_by_id") int modified_by_id,
            @Part("modified_datetime") RequestBody modified_datetime,
            @Part("job_type ") RequestBody job_type ,
            @Part("active_hours ") int active_hours,
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

    @POST(APIConstants.JOB_COMMENT)
        // Call<BaseModel<List<Object>>> setLikeJob();
    Call<BaseModel<List<Object>>> setJobComment(@Body HashMap<String, Object> body);

    @POST(APIConstants.GET_JOB_COMMENTS)
        // Call<BaseModel<List<Object>>> setLikeJob();
    Call<BaseModel<List<Comment>>> getJobComments(@Body HashMap<String, Object> body);

    @PATCH(APIConstants.EDIT_JOB_COMMENTS)
        // Call<BaseModel<List<Object>>> setLikeJob();
    Call<BaseModel<List<Comment>>> editJobComment(@Body Comment body,@Path("id") int id);


    @POST(APIConstants.APPLY_INTERVIEW)
        // Call<BaseModel<List<Object>>> setLikeJob();
    Call<BaseModel<List<Object>>> apply_interview(@Body HashMap<String, Object> body);


    @POST(APIConstants.CHECK_EMAIL_EXIST)
    Call<BaseModel<List<User>>> isEmailExist(@Body User user);

    @POST(APIConstants.CHECK_EMAIL_EXIST)
    Call<BaseModel<List<AccountCheck>>> isEmailExist_linkedin(@Body User user);

    @POST(APIConstants.LOGIN)
    Call<BaseModel<List<User>>> linkedin_login(@Body User user);

    @POST(APIConstants.LOGIN)
    Call<BaseModel<List<User>>> facebookLogin(@Body User user);

    @POST(APIConstants.DASHBOARD)
    Call<BaseModel<List<Post>>> getDashboard(@Body User user, @Query("limit") int limit, @Query("offset") int offset);

    @POST(APIConstants.DASHBOARD_SEARCH)
    Call<BaseModel<List<Post>>> getDashboardSearch(@Body Object user, @Query("limit") int limit, @Query("offset") int offset);

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

    @PATCH(APIConstants.EDIT_COMMENT)
    Call<BaseModel<List<Comment>>> editComment(@Body Comment comment,@Path("id") int id);

    @DELETE(APIConstants.EDIT_COMMENT)
    Call<BaseModel<List<Comment>>> deleteComment(@Path("id") int id);

    @DELETE(APIConstants.EDIT_EVENT_COMMENT)
    Call<BaseModel<List<Comment>>> deleteEventComment(@Path("id") int id);

    @DELETE(APIConstants.EDIT_NEWS_COMMENT)
    Call<BaseModel<List<Comment>>> deleteNewsComment(@Path("id") int id);

    @DELETE(APIConstants.EDIT_JOB_COMMENTS)
    Call<BaseModel<List<Comment>>> deleteJobComment(@Path("id") int id);

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

    @GET(APIConstants.GET_ALL_PENDING)
    Call<BaseModel<List<GetPendingData>>> getAllPending(/*@Body User user, @Query("limit") int limit, @Query("offset") int offset*/);

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

    @POST(APIConstants.EMAIL_VERIFICATION_CODE)
    @FormUrlEncoded
    Call<BaseModel<List<Object>>> getEmailVerificationCode(@Field("email")String email,
                                                           @Field("verification_code")int verification_code);

    @POST(APIConstants.RESET_PASSWORD)
    Call<BaseModel<List<Object>>> getResetPassword(@Body Object object);

    @GET(APIConstants.GET_CONNECTION_COUNT)
    Call<BaseModel<User>> getConnectionCount(@Path("id") int id);

    @Multipart
    @POST(APIConstants.ADD_EVENT)
    Call<BaseModel<List<Event>>> createEvent(@Part("user_id") int user_id,
                                             @Part("created_by_id") int created_by_id,
                                             @Part("content") RequestBody content,
                                             @Part("status") RequestBody status,
                                             @Part("address") RequestBody event_city,
                                             @Part("start_date") RequestBody start_date,
                                             @Part("end_date") RequestBody end_date,
                                             @Part("start_time") RequestBody start_time,
                                             @Part("end_time") RequestBody end_time,
                                             @Part("name") RequestBody event_name,
                                             @Part("registration_link") RequestBody registration_link,
                                             @Part("is_private") int is_private,
                                             @Part("attendees") List<Integer> event_attendees,
                                             @Part MultipartBody.Part event_image);

    @POST(APIConstants.ADD_MEETING)
    Call<BaseModel<List<Meeting>>> createMeeting(@Body Meeting meeting);

    @GET(APIConstants.GET_EVENT_INTERESTED)
    Call<BaseModel<List<GetEventInterestedUsers>>> getEventInterested(@Query("event_id") int event_id);

    @PATCH(APIConstants.UPDATE_MEETING)
    Call<BaseModel<List<Meeting>>> updateMeeting(@Body Meeting meeting,@Path("id") int id);

    @PATCH(APIConstants.UPDATE_MEETING_ATTENDENCE)
    Call<BaseModel<List<Meeting>>> updateMeetingAttendence(@Body Meeting meeting);

    @POST(APIConstants.GET_EVENT_DETAILS)
    Call<BaseModel<List<Event>>> getEventDetails(@Body Event event);

    @POST(APIConstants.GET_EVENT_COMMENTS)
    Call<BaseModel<List<Comment>>> getEventComments(@Body Event event);

    @POST(APIConstants.ADD_EVENT_COMMENT)
    Call<BaseModel<List<Comment>>> addEventComment(@Body Comment comment);

    @PATCH(APIConstants.EDIT_EVENT_COMMENT)
    Call<BaseModel<List<Comment>>> editEventComment(@Body Comment comment,@Path("id") int id);

    @POST(APIConstants.ADD_EVENT_ATTENDANCE)
    Call<BaseModel<List<Event>>> addEventAttendance(@Body Event eventAttendees);

    @PATCH(APIConstants.UPDATE_EVENT_ATTENDANCE)
    Call<BaseModel<List<Event>>> updateEventAttendance(@Body Event eventAttendees);

    @POST(APIConstants.LIKE_EVENT)
    Call<BaseModel<List<Event>>> likeEvent(@Body Event event);

    @DELETE(APIConstants.UPDATE_EVENT)
    Call<BaseModel<List<Event>>> deleteEvent(@Path("id") int id);

    @DELETE(APIConstants.UPDATE_JOB_AD)
    Call<BaseModel<List<Post>>> deleteJob(@Path("job_id") int id);

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

    @POST(APIConstants.BLOCK_USER)
    Call<BaseModel<List<Object>>> block_user(@Body HashMap<String, Object> body);

    @Multipart
    @PATCH(APIConstants.PROFILE_UPDATE)
    Call<BaseModel<List<Object>>> profile_update(
            @Path("userid") int user_id,
            @Part("designation") RequestBody designation,
            @Part("company_name") RequestBody companyname,
            @Part("first_name") RequestBody firstname,
            @Part("modified_by_id") int modify_by_id,
            @Part("modified_datetime") RequestBody modify_datetime,
            @Part MultipartBody.Part event_image);

    @PATCH(APIConstants.SETTING_UPDATE)
    Call<BaseModel<List<User>>> setting_update(@Body HashMap<String, Object> body,@Path("user_id") int user_id);

    @POST(APIConstants.UPDATE_PASSWORD)
    Call<BaseModel<List<User>>> update_password(@Body HashMap<String, Object> body);

    @GET(APIConstants.GET_USER_DETAILS)
    Call<BaseModel<List<User>>> getuser_details(@Path("id") int user_id);

    @POST(APIConstants.GET_SECTOR)
    Call<BaseModel<List<String>>> getSector(@Body HashMap<String, Object> body);

    @GET(APIConstants.Get_NEWS_SOURCES)
    Call<BaseModel<List<NewSources>>> getNewSources();

    @POST(APIConstants.Get_SELECTED_NEWS_SOURCES)
    @FormUrlEncoded
    Call<BaseModel<List<NewSources>>> getSelectedNewSources(@Field("user_id") Integer user_id);

    @POST(APIConstants.SET_NEWS_SOURCES)
    Call<BaseModel<List<NewSources>>> setNewSources(@Body HashMap<String, Object> body);

    @POST(APIConstants.POST)
    Call<BaseModel<List<Post>>> getPost(@Body User user, @Query("limit") int limit, @Query("offset") int offset);

    @POST(APIConstants.EVENT)
    Call<BaseModel<List<Post>>> getEvent(@Body User user, @Query("limit") int limit, @Query("offset") int offset);

    @POST(APIConstants.JOB)
    Call<BaseModel<List<Post>>> getJob(@Body User user, @Query("limit") int limit, @Query("offset") int offset);

    @POST(APIConstants.NEWS)
    Call<BaseModel<List<Post>>> getNews(@Body User user, @Query("limit") int limit, @Query("offset") int offset);

    @POST(APIConstants.GET_MEETING_DETAILS)
    Call<BaseModel<List<Event>>> getMeetingDetails(@Body HashMap<String,Integer> user);
    @GET(APIConstants.DECLINE_APPLICATION)
    Call<BaseModel<List<AppliedApplicants>>> getApplicant(@Path("job_application_id") int job_id);

    @POST(APIConstants.SHARE_JOB)
    Call<BaseModel<List<Object>>> share_connection(@Body ShareConnection shareConnection);

    @POST(APIConstants.SHARE_EVENT)
    Call<BaseModel<List<Object>>> share_connection_event(@Body ShareConnection shareConnection);

    @POST(APIConstants.SHARE_POST)
    Call<BaseModel<List<Object>>> share_connection_post(@Body ShareConnection shareConnection);

    @POST(APIConstants.GET_CONNECTION_BY_RECOMMENDED_USER)
    Call<BaseModel<List<User>>> getConnectionByRecommendedUser(@Body User user,@Query("limit") int limit, @Query("offset") int offset);

    @Multipart
    @POST(APIConstants.CHAT_IMAGE)
    Call<BaseModel<ChatAttachment>> uploadAttachment(@Part("created_by_id") int user_id,
                                                           @Part("status") RequestBody status ,
                                                           @Part List<MultipartBody.Part> Chat_image);

    @Multipart
    @PATCH(APIConstants.UPDATE_EVENT)
    Call<BaseModel<List<Event>>> updateEvent(@Path("id") int id,@Part("user_id") int user_id,
                                             @Part("created_by_id") int created_by_id,
                                             @Part("content") RequestBody content,
                                             @Part("status") RequestBody status,
                                             @Part("address") RequestBody event_city,
                                             @Part("start_date") RequestBody start_date,
                                             @Part("end_date") RequestBody end_date,
                                             @Part("start_time") RequestBody start_time,
                                             @Part("end_time") RequestBody end_time,
                                             @Part("name") RequestBody event_name,
                                             @Part("registration_link") RequestBody registration_link,
                                             @Part("is_private") int is_private,
                                             @Part("attendees") List<Integer> event_attendees,
                                             @Part MultipartBody.Part event_image,
                                             @Part("modified_by_id") int modify_by_id,
                                             @Part("modified_datetime") RequestBody modify_datetime);

    @POST(APIConstants.GET_NEWS_COMMENTS)
    Call<BaseModel<List<Comment>>> getNewsComments(@Body Post news);

    @POST(APIConstants.ADD_NEWS_COMMENT)
    Call<BaseModel<List<Comment>>> addNewsComment(@Body Comment comment);

    @PATCH(APIConstants.EDIT_NEWS_COMMENT)
    Call<BaseModel<List<Comment>>> editNewsComment(@Body Comment comment,@Path("id") int id);

    @POST(APIConstants.LIKE_NEWS)
    Call<BaseModel<List<Post>>> likeNews(@Body Post post);

    @POST(APIConstants.GET_NEWS_BY_ID)
    Call<BaseModel<List<Post>>> getNewsByID(@Body Object user);

    @GET(APIConstants.GET_JOB_TYPE)
    Call<BaseModel<List<String>>> getJobType();

    @PATCH(APIConstants.USER_ONLINE)
    Call<BaseModel<List<User>>> userOnline(@Path("id") int id,@Body HashMap<String,Object> user);

    @GET(APIConstants.USER_STATS)
    Call<BaseModel<List<Stats>>> getUserStats(@Path("id") int id);
}
