package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Event;

import java.util.List;

public interface IMeetingRepository {
    LiveData<BaseModel<List<Event>>> getMeetingDetails(int event_id);
}
