package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.Meeting;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IEventRepository;
import com.hypernym.evaconnect.repositories.impl.EventRepository;

import java.util.List;

import okhttp3.MultipartBody;

public class EventViewModel extends AndroidViewModel {
    private IEventRepository iEventRepository =new EventRepository();

    public EventViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseModel<List<Event>>> createEvent(Event event, MultipartBody.Part image)
    {
        return iEventRepository.createEvent(event,image);
    }

    public LiveData<BaseModel<List<Meeting>>> createMeeting(Meeting meeting)
    {
        return iEventRepository.createMeeting(meeting);
    }
    public LiveData<BaseModel<List<Meeting>>> updateMeeting(Meeting meeting)
    {
        return iEventRepository.updateMeeting(meeting);
    }

    public LiveData<BaseModel<List<Event>>> getCalendarEvents(int user_id, String month,String year)
    {
        return iEventRepository.getCalendarEvents(user_id,month,year);
    }

    public LiveData<BaseModel<List<Event>>> getEventDetails(int event_id)
    {
        return iEventRepository.getEventDetails(event_id);
    }

    public LiveData<BaseModel<List<Comment>>> getEventComments(int event_id)
    {
        return iEventRepository.getEventComments(event_id);
    }

    public LiveData<BaseModel<List<Comment>>> addComment(Comment comment)
    {
        return iEventRepository.addComment(comment);
    }

    public LiveData<BaseModel<List<Comment>>> editComment(Comment comment)
    {
        return iEventRepository.editComment(comment);
    }


    public LiveData<BaseModel<List<Event>>> addEventAttendance(Event event)
    {
        return iEventRepository.addEventAttendance(event);
    }

    public LiveData<BaseModel<List<Event>>> updateEventAttendance(Event event)
    {
        return iEventRepository.updateEventAttendance(event);
    }
    public LiveData<BaseModel<List<Event>>> likeEvent(Event event)
    {
        return iEventRepository.likeEvent(event);
    }

    public LiveData<BaseModel<List<Post>>> getEvent(User user, int totalpages, int currentPage)
    {
        return iEventRepository.getEvent(user,totalpages,currentPage);
    }

    public LiveData<BaseModel<List<Event>>> updateEvent(Event event,MultipartBody.Part event_image)
    {
        return iEventRepository.updateEvent(event,event_image);
    }

    public LiveData<BaseModel<List<Comment>>> deleteComment(Integer id)
    {
        return iEventRepository.deleteComment(id);
    }


}