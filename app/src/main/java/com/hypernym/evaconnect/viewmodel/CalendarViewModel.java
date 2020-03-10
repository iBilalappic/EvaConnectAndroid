package com.hypernym.evaconnect.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CalendarModel;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.repositories.ICalendarRepository;
import com.hypernym.evaconnect.repositories.impl.CalendarRepository;

import java.util.List;

public class CalendarViewModel extends AndroidViewModel {
    private ICalendarRepository iCalendarRepository =new CalendarRepository();

    public CalendarViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<BaseModel<List<CalendarModel>>> getAllCalendarMarks(CalendarModel calendarModel)
    {
        return iCalendarRepository.getAllCalendarMarks(calendarModel);
    }

    public LiveData<BaseModel<List<CalendarModel>>> createNote(CalendarModel calendarModel)
    {
        return iCalendarRepository.createNote(calendarModel);
    }

    public LiveData<BaseModel<List<CalendarModel>>> getCalendarMarksByDate(CalendarModel calendarModel)
    {
        return iCalendarRepository.getCalendarMarksByDate(calendarModel);
    }


}
