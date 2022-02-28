package com.hypernym.evaconnect.repositories.impl;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.NotesData;
import com.hypernym.evaconnect.repositories.INotesRepository;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotesRepository implements INotesRepository {

    private MutableLiveData<BaseModel<List<Object>>> CreateNoteMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<Object>>> UpdateNoteMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<BaseModel<List<NotesData>>> GetNoteMutableLiveData = new MutableLiveData<>();

    @Override
    public LiveData<BaseModel<List<Object>>> create_notes(String formattedEventDate, String time_utc, String notes_content, String notes_title, boolean setReminder) {
        CreateNoteMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("occurrence_date",formattedEventDate);
        body.put("occurrence_time",time_utc);
        body.put("user_id", LoginUtils.getUser().getId());
        body.put("created_by_id", LoginUtils.getUser().getId());
        body.put("status", "active");
        body.put("title", notes_title);
        body.put("details", notes_content);
        body.put("is_notify",setReminder);
        RestClient.get().appApi().create_notes(body).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    CreateNoteMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    CreateNoteMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                CreateNoteMutableLiveData.setValue(null);
            }
        });
        return CreateNoteMutableLiveData;
    }
    @Override
    public LiveData<BaseModel<List<NotesData>>> get_notes(int note_id) {
        GetNoteMutableLiveData = new MutableLiveData<>();
        RestClient.get().appApi().get_notes(note_id).enqueue(new Callback<BaseModel<List<NotesData>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<NotesData>>> call, Response<BaseModel<List<NotesData>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    GetNoteMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    GetNoteMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<NotesData>>> call, Throwable t) {
                GetNoteMutableLiveData.setValue(null);
            }
        });
        return GetNoteMutableLiveData;
    }
    @Override
    public LiveData<BaseModel<List<Object>>> update_notes(String formattedEventDate, String time_utc, String notes_content, String notes_title, boolean setReminder, int notes_id) {
        CreateNoteMutableLiveData = new MutableLiveData<>();
        HashMap<String, Object> body = new HashMap<>();
        body.put("occurrence_date",formattedEventDate);
        body.put("occurrence_time",time_utc);
        body.put("modified_by_id", notes_id);
        body.put("id", LoginUtils.getUser().getId());
        body.put("created_by_id", LoginUtils.getUser().getId());
        body.put("status", "active");
        body.put("title", notes_title);
        body.put("details", notes_content);
        body.put("is_notify",setReminder);
        RestClient.get().appApi().update_notes(body).enqueue(new Callback<BaseModel<List<Object>>>() {
            @Override
            public void onResponse(Call<BaseModel<List<Object>>> call, Response<BaseModel<List<Object>>> response) {
                if (response.isSuccessful() && !response.body().isError())
                    CreateNoteMutableLiveData.setValue(response.body());
                if (response.code() == 500) {
                    CreateNoteMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<BaseModel<List<Object>>> call, Throwable t) {
                CreateNoteMutableLiveData.setValue(null);
            }
        });
        return CreateNoteMutableLiveData;
    }
}
