package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.EventStaus;
import com.hypernym.evaconnect.models.GetEventInterestedUsers;
import com.hypernym.evaconnect.models.Meeting;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.SaveEventData;
import com.hypernym.evaconnect.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

public interface IEventRepository {
    LiveData<BaseModel<List<Event>>> createEvent(Event event, @Part MultipartBody.Part user_image);

    LiveData<BaseModel<List<Meeting>>> createMeeting(Meeting meeting);

    LiveData<BaseModel<List<Meeting>>> updateMeeting(Meeting meeting);

    LiveData<BaseModel<List<Event>>> getCalendarEvents(int user_id, String month,String year);

    LiveData<BaseModel<List<Event>>> getEventDetails(int event_id, int user_id);

    LiveData<BaseModel<List<Comment>>> getEventComments(int event_id);

    LiveData<BaseModel<List<GetEventInterestedUsers>>> getEventInterested(int event_id);

    LiveData<BaseModel<List<Comment>>> addComment(Comment comment);

    LiveData<BaseModel<List<Comment>>> editComment(Comment comment);

    LiveData<BaseModel<List<Event>>> addEventAttendance(Event event);

    LiveData<BaseModel<List<Event>>> updateEventAttendance(Event event);

    LiveData<BaseModel<List<Event>>> likeEvent(Event event);

    LiveData<BaseModel<List<Event>>> deleteEvent(Post post);

    LiveData<BaseModel<List<Post>>> getEvent(User user, int total, int current);

    LiveData<BaseModel<List<Event>>> updateEvent(Event event,@Part MultipartBody.Part user_image);

    LiveData<BaseModel<List<Comment>>> deleteComment(Integer id);

    LiveData<BaseModel<SaveEventData>> saveEvent(int event_id, Boolean is_favourite_event);

    LiveData<BaseModel<List<Object>>> showInterestEvent(int event_id, int user_id,String status,String attendance_status);

    LiveData<BaseModel<List<EventStaus>>> getEventStatus(int event_id, int user_id);

    LiveData<BaseModel<List<SaveEventData>>> GetSaveEvent(int event_id, int user_id);

    LiveData<BaseModel<List<Object>>> save_event_false(int event_id, Boolean is_favourite_event);

}
