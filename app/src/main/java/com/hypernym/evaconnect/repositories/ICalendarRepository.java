package com.hypernym.evaconnect.repositories;

import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CalendarModel;
import com.hypernym.evaconnect.models.Event;

import java.util.List;

public interface ICalendarRepository {
    LiveData<BaseModel<List<CalendarModel>>> getAllCalendarMarks(CalendarModel calendarModel);

    LiveData<BaseModel<List<CalendarModel>>> createNote(CalendarModel calendarModel);

    LiveData<BaseModel<List<CalendarModel>>> getCalendarMarksByDate(CalendarModel calendarModel);
}
