package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.NotesData;
import com.hypernym.evaconnect.repositories.INotesRepository;
import com.hypernym.evaconnect.repositories.impl.NotesRepository;

import java.util.List;

public class NotesViewModel extends AndroidViewModel {

    private INotesRepository iNotesRepository = new NotesRepository();

    public NotesViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseModel<List<Object>>> create_notes(String formattedEventDate, String timeUtc, String notes_content, String notes_title, boolean setReminder) {
        return iNotesRepository.create_notes(formattedEventDate,timeUtc,notes_content,notes_title,setReminder);
    }
    public LiveData<BaseModel<List<NotesData>>> get_notes(int note_id) {
        return iNotesRepository.get_notes(note_id);
    }
    public LiveData<BaseModel<List<Object>>> update_notes(String formattedEventDate, String timeUtc, String notes_content, String notes_title, boolean setReminder, int notes_id) {
        return iNotesRepository.update_notes(formattedEventDate,timeUtc,notes_content,notes_title,setReminder,notes_id);
    }
}
