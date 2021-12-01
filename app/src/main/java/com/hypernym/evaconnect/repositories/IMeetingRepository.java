package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.Meeting;

import java.util.List;

public interface IMeetingRepository {
    LiveData<BaseModel<List<Event>>> getMeetingDetails(int event_id);

    LiveData<BaseModel<List<Meeting>>> updateMeetingAttendance(Meeting meeting);
}
