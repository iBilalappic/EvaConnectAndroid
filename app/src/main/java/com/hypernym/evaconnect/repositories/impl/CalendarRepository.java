package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CalendarModel;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.repositories.ICalendarRepository;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarRepository implements ICalendarRepository {
    private MutableLiveData<BaseModel<List<CalendarModel>>> calendarMutableLiveData = new MutableLiveData<>();

    private MutableLiveData<BaseModel<List<Event>>> eventMutableLiveData = new MutableLiveData<>();
    @Override
    public LiveData<BaseModel<List<CalendarModel>>> getAllCalendarMarks(CalendarModel calendarModel) {
        calendarMutableLiveData=new MutableLiveData<>();
        RestClient.get().appApi().getAllCalendarMarks(calendarModel).enqueue(new Callback<BaseModel<List<CalendarModel>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<CalendarModel>>> call, Response<BaseModel<List<CalendarModel>>> response) {
                if(response.body()!=null)
                {
                    calendarMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<CalendarModel>>> call, Throwable t) {
                calendarMutableLiveData.setValue(null);
            }
        });

        return calendarMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<CalendarModel>>> createNote(CalendarModel calendarModel) {

        calendarMutableLiveData=new MutableLiveData<>();
        RestClient.get().appApi().createNote(calendarModel).enqueue(new Callback<BaseModel<List<CalendarModel>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<CalendarModel>>> call, Response<BaseModel<List<CalendarModel>>> response) {
                if(response.body()!=null)
                {
                    calendarMutableLiveData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<CalendarModel>>> call, Throwable t) {
                calendarMutableLiveData.setValue(null);
            }
        });

        return calendarMutableLiveData;
    }

    @Override
    public LiveData<BaseModel<List<CalendarModel>>> getCalendarMarksByDate(CalendarModel calendarModel) {
        calendarMutableLiveData=new MutableLiveData<>();
        RestClient.get().appApi().getCalendarMarksByDate(calendarModel).enqueue(new Callback<BaseModel<List<CalendarModel>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<CalendarModel>>> call, Response<BaseModel<List<CalendarModel>>> response) {
                if(response.body()!=null)
                {
                    calendarMutableLiveData.setValue(response.body());
                }
            }
            @Override
            public void onFailure(Call<BaseModel<List<CalendarModel>>> call, Throwable t) {
                calendarMutableLiveData.setValue(null);
            }
        });

        return calendarMutableLiveData;
    }
}
