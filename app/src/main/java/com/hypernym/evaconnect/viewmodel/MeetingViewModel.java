package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.repositories.IMeetingRepository;
import com.hypernym.evaconnect.repositories.impl.MeetingRepository;

import java.util.List;

public class MeetingViewModel extends AndroidViewModel {
    private IMeetingRepository iMeetingRepository =new MeetingRepository();

    public MeetingViewModel(@NonNull Application application) {
        super(application);
    }


    public LiveData<BaseModel<List<Event>>> getMeetingDetails(int event_id)
    {
        return iMeetingRepository.getMeetingDetails(event_id);
    }

}
