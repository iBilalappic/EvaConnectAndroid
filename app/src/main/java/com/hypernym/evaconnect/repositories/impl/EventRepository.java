package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.EventStaus;
import com.hypernym.evaconnect.models.GetEventInterestedUsers;
import com.hypernym.evaconnect.models.Meeting;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.SaveEventData;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.IEventRepository;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventRepository implements IEventRepository {
    private MutableLiveData<BaseModel<List<Event>>> eventMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Meeting>>> meetingMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Comment>>> commentMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Post>>> dashboardMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<GetEventInterestedUsers>>> interestedMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<SaveEventData>> saveEventMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<SaveEventData>>> GetsaveEventMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> InterestedEventMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<EventStaus>>> eventStatusMutableLiveData = new MutableLiveData<>();


    @Override
    public LiveData<BaseModel<List<GetEventInterestedUsers>>> getEventInterested(int event_id) {
        interestedMutableLiveData=new MutableLiveData<>();
        Event event=new Event();
        event.setEvent_id(event_id);
        RestClient.get().appApi().getEventInterested(event_id).enqueue(new Callback<BaseModel<List<GetEventInterestedUsers>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<GetEventInterestedUsers>>> call, Response<BaseModel<List<GetEventInterestedUsers>>> response) {
                if(response.body()!=null)
                {
                    interestedMutableLiveData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<BaseModel<List<GetEventInterestedUsers>>> call, Throwable t) {
                interestedMutableLiveData.setValue(null);
            }
        });
        return interestedMutableLiveData;
    }
    @Override
    public LiveData<BaseModel<List<Meeting>>> createMeeting(Meeting meeting) {
        meetingMutableLiveData = new MutableLiveData<>();

        RestClient.get().appApi().createMeeting(meeting).enqueue(new Callback<BaseModel<List<Meeting>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Meeting>>> call, Response<BaseModel<List<Meeting>>> response) {
                if (response.body()!=null){
                    meetingMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Meeting>>> call, Throwable t) {
                meetingMutableLiveData.setValue(null);
                t.printStackTrace();
            }
        });

        return meetingMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Meeting>>> updateMeeting(Meeting meeting) {
        meetingMutableLiveData = new MutableLiveData<>();

        RestClient.get().appApi().updateMeeting(meeting,meeting.getId()).enqueue(new Callback<BaseModel<List<Meeting>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Meeting>>> call, Response<BaseModel<List<Meeting>>> response) {
                if (response.body()!=null){
                    meetingMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Meeting>>> call, Throwable t) {
                meetingMutableLiveData.setValue(null);
                t.printStackTrace();
            }
        });

        return meetingMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Event>>> createEvent(Event event, MultipartBody.Part user_image) {
        eventMutableLiveData = new MutableLiveData<>();
        RestClient.get().appApi().createEvent(LoginUtils.getLoggedinUser().getId(),LoginUtils.getLoggedinUser().getId(),
                RequestBody.create(MediaType.parse("text/plain"),event.getContent()),
                RequestBody.create(MediaType.parse("text/plain"), AppConstants.ACTIVE),
                RequestBody.create(MediaType.parse("text/plain"), event.getEvent_city()),
                RequestBody.create(MediaType.parse("text/plain"), event.getStart_date()),
                RequestBody.create(MediaType.parse("text/plain"), event.getEnd_date()),
                RequestBody.create(MediaType.parse("text/plain"), event.getStart_time()),
                RequestBody.create(MediaType.parse("text/plain"), event.getEnd_time()),
                RequestBody.create(MediaType.parse("text/plain"), event.getName()),
                RequestBody.create(MediaType.parse("text/plain"), event.getRegistration_link()),
                event.getIs_private(),  event.getEvent_attendeeIDs(),user_image)
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
    public LiveData<BaseModel<List<Event>>> getEventDetails(int event_id, int user_id) {
        eventMutableLiveData = new MutableLiveData<>();
        Event event=new Event();
        event.setEvent_id(event_id);
        event.setUser_id(user_id);
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
    public LiveData<BaseModel<List<Comment>>> editComment(Comment comment) {
        commentMutableLiveData=new MutableLiveData<>();
        RestClient.get().appApi().editEventComment(comment,comment.getId()).enqueue(new Callback<BaseModel<List<Comment>>>() {
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

        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("event_id", event.getEvent_id());
        data.put("user_id", event.getUser_id());
        data.put("status", event.getStatus());
        data.put("attending_date", DateUtils.GetCurrentdate());
        data.put("attendance_status", event.getAttendance_status());

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
    public LiveData<BaseModel<List<Event>>> deleteEvent(Post post) {
        eventMutableLiveData=new MutableLiveData<>();

        RestClient.get().appApi().deleteEvent(post.getId()).enqueue(new Callback<BaseModel<List<Event>>>() {
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
    public LiveData<BaseModel<List<Post>>> getEvent(User user/*, int total, int current*/) {
        dashboardMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("user_id", user.getId());
        data.put("filter", user.getFilter());
       /* data.put("search_key", user.getSearch_key());
        user.setUser_id(user.getId());*/
        RestClient.get().appApi().getEvent(data/*, total, current*/).enqueue(new Callback<BaseModel<List<Post>>>() {
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

    @Override
    public LiveData<BaseModel<List<Event>>> updateEvent(Event event,MultipartBody.Part user_image) {
        eventMutableLiveData=new MutableLiveData<>();
        RestClient.get().appApi().updateEvent(event.getId(),LoginUtils.getLoggedinUser().getId(),LoginUtils.getLoggedinUser().getId(),
                RequestBody.create(MediaType.parse("text/plain"),event.getContent()),
                RequestBody.create(MediaType.parse("text/plain"), AppConstants.ACTIVE),
                RequestBody.create(MediaType.parse("text/plain"), event.getEvent_city()),
                RequestBody.create(MediaType.parse("text/plain"), event.getStart_date()),
                RequestBody.create(MediaType.parse("text/plain"), event.getEnd_date()),
                RequestBody.create(MediaType.parse("text/plain"), event.getStart_time()),
                RequestBody.create(MediaType.parse("text/plain"), event.getEnd_time()),
                RequestBody.create(MediaType.parse("text/plain"), event.getName()),
                RequestBody.create(MediaType.parse("text/plain"), event.getRegistration_link()),
                event.getIs_private(),  event.getEvent_attendeeIDs(),user_image,event.getModified_by_id(),
                RequestBody.create(MediaType.parse("text/plain"), event.getModified_datetime())).enqueue(new Callback<BaseModel<List<Event>>>() {
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
    public LiveData<BaseModel<List<Comment>>> deleteComment(Integer id) {
        commentMutableLiveData=new MutableLiveData<>();

        RestClient.get().appApi().deleteEventComment(id).enqueue(new Callback<BaseModel<List<Comment>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Comment>>> call, Response<BaseModel<List<Comment>>> response) {
                commentMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<BaseModel<List<Comment>>> call, Throwable t) {
                commentMutableLiveData.setValue(null);
            }
        });
        return commentMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<SaveEventData>> saveEvent(int event_id, Boolean is_favourite_event) {
        saveEventMutableLiveData=new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("event_id", event_id);
        body.put("user_id", LoginUtils.getUser().getId());
        body.put("status", "active");
        body.put("is_favourite", is_favourite_event);
        RestClient.get().appApi().save_event(body).enqueue(new Callback<BaseModel<SaveEventData>>() {
            @Override
            public void onResponse(Call<BaseModel<SaveEventData>> call, Response<BaseModel<SaveEventData>> response) {
                if(response.body()!=null)
                {
                    saveEventMutableLiveData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<BaseModel<SaveEventData>> call, Throwable t) {
                saveEventMutableLiveData.setValue(null);
            }
        });
        return saveEventMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<Object>>> showInterestEvent(int event_id, int user_id,String status,String attendance_status) {
        InterestedEventMutableLiveData=new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("event_id", event_id);
        body.put("user_id", LoginUtils.getUser().getId());
        body.put("status", status);
        body.put("attendance_status", attendance_status);
        RestClient.get().appApi().save_event_interested(body).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                if(response.body()!=null)
                {
                    InterestedEventMutableLiveData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                InterestedEventMutableLiveData.setValue(null);
            }
        });
        return InterestedEventMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<EventStaus>>> getEventStatus(int event_id, int user_id) {
        eventStatusMutableLiveData = new MutableLiveData<>();
        Event event=new Event();
        event.setEvent_id(event_id);
        event.setUser_id(user_id);
        RestClient.get().appApi().get_EventStatus(user_id,event_id).enqueue(new Callback<BaseModel<List<EventStaus>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<EventStaus>>> call, Response<BaseModel<List<EventStaus>>> response) {
                if(response.body()!=null)
                {
                    eventStatusMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<EventStaus>>> call, Throwable t) {
                eventStatusMutableLiveData.setValue(null);
            }
        });
        return eventStatusMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<SaveEventData>>> GetSaveEvent(int event_id, int user_id) {
        GetsaveEventMutableLiveData =new MutableLiveData<>();

        RestClient.get().appApi().get_save_event(LoginUtils.getLoggedinUser().getId(),event_id,true).enqueue(new Callback<BaseModel<List<SaveEventData>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<SaveEventData>>> call, Response<BaseModel<List<SaveEventData>>> response) {
                if(response.body()!=null)
                {
                    GetsaveEventMutableLiveData .setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<BaseModel<List<SaveEventData>>> call, Throwable t) {
                GetsaveEventMutableLiveData .setValue(null);
            }
        });
        return GetsaveEventMutableLiveData ;
    }

}
