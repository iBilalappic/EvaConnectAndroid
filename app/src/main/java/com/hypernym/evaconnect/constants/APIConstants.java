package com.hypernym.evaconnect.constants;

public interface APIConstants {
    public static String BASE_SERVER_URL="http://168.63.140.202:8003/eva/";
    public static String ONESINGAL_BASE_URL ="https://onesignal.com/";
    public static String SIGNUP="user/signup/";
    public static String LOGIN="user/login/";
    public static String FORGOT_PASSWORD="user/forgotpassword/";
    public static String GET_POSTS="post/";
    public static String CHECK_EMAIL_EXIST="user/account/check/";
    public static String DASHBOARD="dashboard/android/?";
    public static String FRIENDCONNECTION="user/network/connection/details/{id}/";
    public static String ADD_COMMENT="post/comment/";
    public static String LIKE_POST="post/like/";
    public static String GET_POST_COMMENTS="post/comment/filter/";
    public static String CONNECT="user/connection/";
    public static String UPDATE_CONNECTION="user/connection/details/{id}/";
    public static String GET_ALL_CONNECTIONS="user/network/connection/?";
    public static String GET_CONNECTION_BY_FILTER="user/network/connection/filter/?";
    public static String GET_ALL_NOTIFICATIONS="user/notifications/filter/?";
    public static String NOTIFICATION_MARKS_AS_READ="user/notifications/details/?";
    public static String GET_POST_BY_ID="post/details/";
    public static String GET_CONNECTION_COUNT="user/connection/count/{id}/";
    public static String GET_MY_LIKES="user/mylikes/?";
    public static String ADD_JOB_AD="job/";
    public static String JOB_LIST_AD ="job/show/";
    public static String JOB_FILTER_AD ="job/filter/";
    public static String APPLICATION_SUBMITT ="job/application/";
    public static String JOB_LIKE =" job/like/";
    public static String UPDATE_JOB_AD="job/details/{job_id}/";
    public static String SEND_NOTIFICATION="api/v1/notifications";
    public static String GET_JOB_AD_BY_ID="job/show/details/{job_id}/";
    public static String GET_APPLICANTS="job/application/filter/";
    public static String ADD_EVENT="event/";
    public static String GET_CALENDAR_MARKS="user/calendar/";
    public static String GET_EVENT_DETAILS="event/details/";
    public static String GET_EVENT_COMMENTS="event/comment/filter/";
    public static String ADD_EVENT_COMMENT="event/comment/";
    public static String ADD_EVENT_ATTENDANCE="event/attendee/";
    public static String UPDATE_EVENT_ATTENDANCE=" event/attendee/detail/";
    public static String LIKE_EVENT="event/like/";
    public static String GET_CALENDAR_BY_DATE="user/calendar/day/";
    public static String CREATE_NOTE="user/notes/";
    public static String APPLY_INTERVIEW="job/interview/";
    public static String DECLINE_APPLICATION="job/application/details/{job_application_id}/";
    public static String GETUSER_FROM_LINKEDIN="user/linkedin/login/";
    public static String REMOVE_USER="user/connection/delete/{id}/";
    public static String BLOCK_USER="user/connection/details/{connection_id}/";
    public static String PROFILE_UPDATE="user/details/{userid}/";
    public static String GET_USER_DETAILS="user/details/{id}/";
    public static String GET_SECTOR="user/sector/";
    public static String Get_NEWS_SOURCES="news/sources/";

}
