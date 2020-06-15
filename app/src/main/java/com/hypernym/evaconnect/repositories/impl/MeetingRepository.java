package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.repositories.IMeetingRepository;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MeetingRepository implements IMeetingRepository {
    private MutableLiveData<BaseModel<List<Event>>> meetingMutableLiveData = new MutableLiveData<>();
    @Override
    public LiveData<BaseModel<List<Event>>> getMeetingDetails(int meeting_id) {
        meetingMutableLiveData=new MutableLiveData<>();
        HashMap<String,Integer> meetingDetails=new HashMap<>();
        meetingDetails.put("meeting_id",meeting_id);
        meetingDetails.put("user_id", LoginUtils.getLoggedinUser().getUser_id());

        RestClient.get().appApi().getMeetingDetails(meetingDetails).enqueue(new Callback<BaseModel<List<Event>>>() {
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
