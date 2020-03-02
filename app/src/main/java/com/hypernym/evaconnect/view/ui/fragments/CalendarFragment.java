package com.hypernym.evaconnect.view.ui.fragments;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.hypernym.evaconnect.R;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    @BindView(R.id.calendarView)
    CalendarView calendarView;


    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init() {
//        List<EventDay> events = new ArrayList<>();
//
//        Calendar calendar = Calendar.getInstance();
//        events.add(new EventDay(calendar, R.drawable.redcircle));
//        Calendar calendar1 = Calendar.getInstance();
//        events.add(new EventDay(calendar1, R.drawable.bluecircle));
//or
        // events.add(new EventDay(calendar, new Drawable()));
//or if you want to specify event label color
        //events.add(new EventDay(calendar, R.drawable.sample_icon, Color.parseColor("#228B22")));

        //CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);
        //calendarView.setEvents(events);
//
//        String[] arr = {"2020-03-10", "2020-03-11", "2020-03-15", "2020-03-16", "2020-03-25"};
//        for (int i = 0; i < 5; i++) {
//            int eventCount = 3;
//            customCalendar.addAnEvent(arr[i], eventCount, getEventDataList(eventCount));
//        }
        List<EventDay> events = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        events.add(new EventDay(calendar, R.drawable.redcircle));

        Calendar calendar1 = Calendar.getInstance();
        events.add(new EventDay(calendar1, R.drawable.bluecircle));

        calendarView.setEvents(events);
    }

//    private ArrayList<EventData> getEventDataList(int eventCount) {
//        EventData eventData=new EventData();
//        eventData.setData();
//        return  null;
//    }


}
