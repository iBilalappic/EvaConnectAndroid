package com.hypernym.evaconnect.constants;

public interface APIConstants {
   //public static String BASE_SERVER_URL="http:/67.205.178.219:8000/eva/"; //Staging
//   public static String BASE_SERVER_URL="http://168.63.140.202:8003/eva/";
    public static String ONESINGAL_BASE_URL ="https://onesignal.com/";
    public static String SIGNUP="user/signup/";
    public static String LOGIN="user/login/";
    public static String FORGOT_PASSWORD="user/password/verification/";
    public static String GET_POSTS="post/";
    public static String GET_BLOCKED_USERS="user/connection/block/";
    public static String CHECK_EMAIL_EXIST="user/account/check/";
    public static String DASHBOARD="dashboard/android/?";
    public static String DASHBOARD_SEARCH="dashboard/search/?";
    public static String POST="dashboard/post/?";
   // public static String POST="post/filter/";
    public static String EVENT="dashboard/event/?";
    public static String USER_EDIT="user/details/";
    public static String NEWS="dashboard/news/?";
    public static String JOB="dashboard/job/?";
    public static String FRIENDCONNECTION="user/network/connection/details/{id}/";
    public static String ADD_COMMENT="post/comment/";
    public static String EDIT_COMMENT="post/comment/details/{id}/";
    public static String EDIT_POST="post/details/{id}/";
    public static String LIKE_POST="post/like/";
    public static String GET_POST_COMMENTS="post/comment/filter/";
    public static String CONNECT="user/connection/";
    public static String UPDATE_CONNECTION="user/connection/details/{id}/";
    public static String GET_ALL_CONNECTIONS="user/network/connection/?";
    public static String GET_CONNECTION_BY_FILTER="user/network/connection/filter/";
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
    public static String JOB_COMMENT ="job/comment/";
    public static String GET_JOB_COMMENTS ="job/comment/filter/";
    public static String EDIT_JOB_COMMENTS ="job/comment/details/{id}/";

    public static String UPDATE_JOB_AD="job/details/{job_id}/";
    public static String SEND_NOTIFICATION="api/v1/notifications";
    public static String GET_JOB_AD_BY_ID="job/show/details/{job_id}/";
    public static String GET_APPLICANTS="job/application/filter/";
    public static String ADD_EVENT="event/";
    public static String ADD_MEETING="meeting/";
    public static String GET_EVENT_INTERESTED="event/interested/";
    public static String UPDATE_MEETING="meeting/details/{id}/";
    public static String GET_CALENDAR_MARKS="user/calendar/";
    public static String GET_EVENT_DETAILS="event/details/";
    public static String GET_EVENT_COMMENTS="event/comment/filter/";
    public static String ADD_EVENT_COMMENT="event/comment/";
    public static String EDIT_EVENT_COMMENT="event/comment/details/{id}/";
    public static String ADD_EVENT_ATTENDANCE="event/attendee/";
    public static String UPDATE_EVENT_ATTENDANCE=" event/attendee/detail/";
    public static String LIKE_EVENT="event/like/";
    public static String GET_CALENDAR_BY_DATE="user/calendar/day/";
    public static String CREATE_NOTE="user/notes/";
    public static String APPLY_INTERVIEW="job/interview/";
    public static String DECLINE_APPLICATION="job/application/details/{job_application_id}/";
    public static String GETUSER_FROM_LINKEDIN="user/linkedin/login/";
    public static String FACEBOOK_LOGIN="/eva/user/facebook/login/";
    public static String REMOVE_USER="user/connection/delete/{id}/";
    public static String BLOCK_USER="user/connection/block/";
    public static String PROFILE_UPDATE="user/details/{userid}/";
    public static String GET_USER_DETAILS="user/details/{id}/";
    public static String GET_SECTOR="user/sector/";
    public static String Get_NEWS_SOURCES="news/sources/";
    public static String Get_SELECTED_NEWS_SOURCES="news/selection/user/";
    public static String SET_NEWS_SOURCES="news/selection/";
    public static String SETTING_UPDATE="user/details/{user_id}/";
    public static String UPDATE_PASSWORD="user/changepassword/";
    public static String GET_MEETING_DETAILS="meeting/details/";
    public static String SHARE_JOB="job/share/";
    public static String SHARE_EVENT="event/share/";
    public static String SHARE_POST="post/share/";
    public static String GET_CONNECTION_BY_RECOMMENDED_USER="user/network/connection/recommendations/?";
    public static String CHAT_IMAGE="chat/";
    public static String UPDATE_MEETING_ATTENDENCE="meeting/attendee/";
    public static String UPDATE_EVENT="event/details/{id}/";
    public static String LIKE_NEWS="news/like/";
    public static String GET_NEWS_COMMENTS="news/comment/filter/";
    public static String ADD_NEWS_COMMENT="news/comment/";
    public static String EDIT_NEWS_COMMENT="news/comment/details/{id}/";
    public static String GET_NEWS_BY_ID="news/rss/";
    public static String GET_JOB_TYPE="job/type/";
    public static String USER_ONLINE="user/details/{id}/";
    public static String USER_STATS="user/stats/{id}/";
    public static String GET_ALL_PENDING = "user/pending/connection/";
    public static String EMAIL_VERIFICATION_CODE = "user/verification/";
    public static String RESET_PASSWORD = "user/forgotpassword/";
}
