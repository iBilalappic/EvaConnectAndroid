package com.hypernym.evaconnect.view.ui.fragments;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.decorators.EventDecorator;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends BaseFragment {

    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;

    @BindView(R.id.newevent)
    TextView newevent;

    private List<CalendarDay> events = new ArrayList<>();

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

        makeJsonObjectRequest();
    }
    private void makeJsonObjectRequest() {

        try {
            CalendarDay day = CalendarDay.from(2020,03,05);
            CalendarDay day1 = CalendarDay.from(2020,03,05);
            events.add(day);
            events.add(day1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int[] threeColors = {
                Color.rgb(0, 0, 255),
                Color.rgb(0, 255, 0),
                Color.rgb(255, 0, 0)};
        EventDecorator eventDecorator = new EventDecorator(events, threeColors);
        calendarView.addDecorator(eventDecorator);
    }

    @OnClick(R.id.newevent)
    public void newEvent()
    {
        loadFragment(R.id.framelayout,new CreateEventFragment(),getContext(),true);
    }

}
