package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.repositories.IMeetingRepository;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingRepository implements IMeetingRepository {
    private MutableLiveData<BaseModel<List<Event>>> meetingMutableLiveData = new MutableLiveData<>();
    @Override
    public LiveData<BaseModel<List<Event>>> getMeetingDetails(int meeting_id) {
        meetingMutableLiveData=new MutableLiveData<>();
        RestClient.get().appApi().getMeetingDetails(meeting_id).enqueue(new Callback<BaseModel<List<Event>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Event>>> call, Response<BaseModel<List<Event>>> response) {
                if (response.body()!=null){
                    meetingMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Event>>> call, Throwable t) {
                    meetingMutableLiveData.setValue(null);
            }
        });
        return meetingMutableLiveData;
    }
}
