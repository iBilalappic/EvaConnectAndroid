package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.NotesData;

import java.util.List;

public interface INotesRepository {
    LiveData<BaseModel<List<Object>>> create_notes(String formattedEventDate, String time_utc, String notes_content, String notes_title, boolean setReminder);
    LiveData<BaseModel<List<Object>>> update_notes(String formattedEventDate, String time_utc, String notes_content, String notes_title, boolean setReminder, int notes_id);
    LiveData<BaseModel<List<NotesData>>> get_notes(int note_id);

}
