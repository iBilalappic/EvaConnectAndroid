package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.http.Part;

public interface IEventRepository {
    LiveData<BaseModel<List<Event>>> createEvent(Event event, @Part MultipartBody.Part user_image);

    LiveData<BaseModel<List<Event>>> getCalendarEvents(int user_id, String month,String year);

    LiveData<BaseModel<List<Event>>> getEventDetails(int event_id);

    LiveData<BaseModel<List<Comment>>> getEventComments(int event_id);

    LiveData<BaseModel<List<Comment>>> addComment(Comment comment);

    LiveData<BaseModel<List<Event>>> addEventAttendance(Event event);

    LiveData<BaseModel<List<Event>>> updateEventAttendance(Event event);

    LiveData<BaseModel<List<Event>>> likeEvent(Event event);

}