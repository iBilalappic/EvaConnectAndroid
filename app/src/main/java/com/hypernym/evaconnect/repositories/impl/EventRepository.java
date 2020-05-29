package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IEventRepository;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRepository implements IEventRepository {
    private MutableLiveData<BaseModel<List<Event>>> eventMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Comment>>> commentMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Post>>> dashboardMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<BaseModel<List<Event>>> createEvent(Event event, MultipartBody.Part user_image) {
        eventMutableLiveData = new MutableLiveData<>();
        RestClient.get().appApi().createEvent(LoginUtils.getLoggedinUser().getId(),LoginUtils.getLoggedinUser().getId(),
                RequestBody.create(MediaType.parse("text/plain"),event.getContent()),
                RequestBody.create(MediaType.parse("text/plain"), AppConstants.ACTIVE),
                RequestBody.create(MediaType.parse("text/plain"), event.getEvent_city()),
                RequestBody.create(MediaType.parse("text/plain"), event.getEvent_start_date()),
                RequestBody.create(MediaType.parse("text/plain"), event.getEvent_end_date()),
                RequestBody.create(MediaType.parse("text/plain"), event.getStart_time()),
                RequestBody.create(MediaType.parse("text/plain"), event.getEnd_time()),
                RequestBody.create(MediaType.parse("text/plain"), event.getEvent_name()),
                RequestBody.create(MediaType.parse("text/plain"), event.getRegistration_link()),
                event.getIs_private(),  event.getEvent_attendees(),user_image)
                .enqueue(new Callback<BaseModel<List<Event>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Event>>> call, Response<BaseModel<List<Event>>> response) {
                if(response.body()!=null)
                {
                    eventMutableLiveData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<BaseModel<List<Event>>> call, Throwable t) {
                eventMutableLiveData.setValue(null);
            }
        });
        return eventMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Event>>> getCalendarEvents(int user_id, String month, String year) {
        return null;
    }

    @Override
    public LiveData<BaseModel<List<Event>>> getEventDetails(int event_id) {
        eventMutableLiveData = new MutableLiveData<>();
        Event event=new Event();
        event.setEvent_id(event_id);
        event.setUser_id(LoginUtils.getLoggedinUser().getId());
        RestClient.get().appApi().getEventDetails(event).enqueue(new Callback<BaseModel<List<Event>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Event>>> call, Response<BaseModel<List<Event>>> response) {
                if(response.body()!=null)
                {
                    eventMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Event>>> call, Throwable t) {
                eventMutableLiveData.setValue(null);
            }
        });
        return eventMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Comment>>> getEventComments(int event_id) {
        commentMutableLiveData=new MutableLiveData<>();
        Event event=new Event();
        event.setEvent_id(event_id);
        RestClient.get().appApi().getEventComments(event).enqueue(new Callback<BaseModel<List<Comment>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Comment>>> call, Response<BaseModel<List<Comment>>> response) {
                if(response.body()!=null)
                {
                    commentMutableLiveData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<BaseModel<List<Comment>>> call, Throwable t) {
                commentMutableLiveData.setValue(null);
            }
        });
        return commentMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Comment>>> addComment(Comment comment) {
        commentMutableLiveData=new MutableLiveData<>();
        RestClient.get().appApi().addEventComment(comment).enqueue(new Callback<BaseModel<List<Comment>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Comment>>> call, Response<BaseModel<List<Comment>>> response) {
                if(response.body()!=null)
                {
                    commentMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Comment>>> call, Throwable t) {
                commentMutableLiveData.setValue(null);
            }
        });
        return commentMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Event>>> addEventAttendance(Event event) {
        eventMutableLiveData=new MutableLiveData<>();
        RestClient.get().appApi().addEventAttendance(event).enqueue(new Callback<BaseModel<List<Event>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Event>>> call, Response<BaseModel<List<Event>>> response) {
                if(response.body()!=null)
                {
                    eventMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Event>>> call, Throwable t) {
                eventMutableLiveData.setValue(null);
            }
        });
        return eventMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Event>>> updateEventAttendance(Event event) {
        eventMutableLiveData=new MutableLiveData<>();
        RestClient.get().appApi().updateEventAttendance(event).enqueue(new Callback<BaseModel<List<Event>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Event>>> call, Response<BaseModel<List<Event>>> response) {
                if(response.body()!=null)
                {
                    eventMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Event>>> call, Throwable t) {
                eventMutableLiveData.setValue(null);
            }
        });
        return eventMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Event>>> likeEvent(Event event) {
        eventMutableLiveData=new MutableLiveData<>();
        RestClient.get().appApi().likeEvent(event).enqueue(new Callback<BaseModel<List<Event>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Event>>> call, Response<BaseModel<List<Event>>> response) {
                if(response.body()!=null)
                {
                    eventMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Event>>> call, Throwable t) {
                eventMutableLiveData.setValue(null);
            }
        });
        return eventMutableLiveData;
    }
    @Override
    public LiveData<BaseModel<List<Post>>> getEvent(User user, int total, int current) {
        dashboardMutableLiveData = new MutableLiveData<>();
        user.setUser_id(user.getId());
        RestClient.get().appApi().getEvent(user, total, current).enqueue(new Callback<BaseModel<List<Post>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Post>>> call, Response<BaseModel<List<Post>>> response) {
                dashboardMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseModel<List<Post>>> call, Throwable t) {
                dashboardMutableLiveData.setValue(null);
            }
        });
        return dashboardMutableLiveData;
    }
}
