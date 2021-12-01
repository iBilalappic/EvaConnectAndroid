package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.Meeting;
import com.hypernym.evaconnect.repositories.IMeetingRepository;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingRepository implements IMeetingRepository {
    private MutableLiveData<BaseModel<List<Event>>> eventMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Meeting>>> meetingMutableLiveData = new MutableLiveData<>();
    @Override
    public LiveData<BaseModel<List<Event>>> getMeetingDetails(int meeting_id) {
        eventMutableLiveData =new MutableLiveData<>();
        HashMap<String,Integer> meetingDetails=new HashMap<>();
        meetingDetails.put("meeting_id",meeting_id);
        meetingDetails.put("user_id", LoginUtils.getLoggedinUser().getId());

        RestClient.get().appApi().getMeetingDetails(meetingDetails).enqueue(new Callback<BaseModel<List<Event>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Event>>> call, Response<BaseModel<List<Event>>> response) {
                if (response.body()!=null){
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
    public LiveData<BaseModel<List<Meeting>>> updateMeetingAttendance(Meeting meeting) {
        meetingMutableLiveData =new MutableLiveData<>();


        RestClient.get().appApi().updateMeetingAttendence(meeting).enqueue(new Callback<BaseModel<List<Meeting>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Meeting>>> call, Response<BaseModel<List<Meeting>>> response) {
                if (response.body()!=null){
                    meetingMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Meeting>>> call, Throwable t) {
                meetingMutableLiveData.setValue(null);
            }
        });
        return meetingMutableLiveData;
    }
}
