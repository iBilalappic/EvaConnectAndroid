package com.hypernym.evaconnect.view.ui.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.decorators.EventDecorator;
import com.hypernym.evaconnect.models.CalendarMarks;
import com.hypernym.evaconnect.models.CalendarModel;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.EventAdapter;
import com.hypernym.evaconnect.view.adapters.MonthAdapter;
import com.hypernym.evaconnect.view.bottomsheets.BottomsheetEventSelection;
import com.hypernym.evaconnect.view.dialogs.EventDialog;
import com.hypernym.evaconnect.view.dialogs.MeetingDialog;
import com.hypernym.evaconnect.view.dialogs.NotesDialog;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.CalendarViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends BaseFragment implements MonthAdapter.ItemClickListener, OnDateSelectedListener, OnMonthChangedListener, EventAdapter.OnItemClickListener {

    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;

    @BindView(R.id.rc_events)
    RecyclerView rc_events;


    @BindView(R.id.tv_nothing_happened)
    TextView tv_nothing_happened;

    @BindView(R.id.fab)
    TextView fab;


    private CalendarViewModel calendarViewModel;
    int[] threeColors;
    List<Integer> colors = new ArrayList<>();

    private List<CalendarDay> events = new ArrayList<>();
    private List<String> months = new ArrayList<>();
    private List<String> years = new ArrayList<>();
    private MonthAdapter monthAdapter, yearAdapter;
    private SimpleDialog simpleDialog;
    private List<CalendarModel> eventList = new ArrayList<>();
    private EventAdapter eventAdapter;
    List<CalendarMarks> calendarMarks = new ArrayList<>();
    boolean isFABOpen = false;

    public CalendarFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        ButterKnife.bind(this, view);
        getActivity().findViewById(R.id.seprator_line).setVisibility(View.VISIBLE);
        init();
        return view;
    }

    private void init() {

        calendarViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(CalendarViewModel.class);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        CalendarDay day = CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1);
        CalendarDay lastday = CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.getActualMaximum(Calendar.DAY_OF_MONTH));

        calendarView.setDateSelected(CalendarDay.today(), true);


        String currentdate = String.valueOf(CalendarDay.today().getDay());
        // dayOfMonth.setText(currentdate);

        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);

        fab.setOnClickListener(v -> {
            BottomsheetEventSelection bottomsheetEventSelection = new BottomsheetEventSelection(new YourDialogFragmentDismissHandler());
            bottomsheetEventSelection.show(getActivity().getSupportFragmentManager(), bottomsheetEventSelection.getTag());

        });

        eventAdapter = new EventAdapter(getContext(), eventList, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rc_events.setLayoutManager(linearLayoutManager);
        rc_events.setAdapter(eventAdapter);
        showDialog();
        hideBackButton();
        new Handler().postDelayed(() -> {
            hideDialog();
            getCalendarMarksByDate(calendarView.getCurrentDate().getYear(), calendarView.getCurrentDate().getMonth(), calendarView.getCurrentDate().getDay());
            if (NetworkUtils.isNetworkConnected(AppUtils.getApplicationContext())) {
                showDialog();
                getAllCalendarMarks(calendarView.getCurrentDate().getMonth(), calendarView.getCurrentDate().getYear());
            } else {
                networkErrorDialog();
            }

        }, 3000);




    }


    private void getAllCalendarMarks(int month, int year) {

        CalendarModel calendarModel = new CalendarModel();
        calendarModel.setUser_id(LoginUtils.getLoggedinUser().getId());
        calendarModel.setMonth(String.valueOf(month));
        calendarModel.setYear(String.valueOf(year));
        calendarViewModel.getAllCalendarMarks(calendarModel).observe(requireActivity(), listBaseModel -> {
            hideDialog();
            if (!listBaseModel.isError() && listBaseModel.getData() != null) {
                calendarMarks = new ArrayList<>();
                eventList.clear();
/*
                eventList.addAll(listBaseModel.getData());
*/
                eventAdapter.notifyDataSetChanged();
                setEvents(listBaseModel.getData());
                // makeJsonObjectRequest(listBaseModel.getData());
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }

        });
    }

    private void setEvents(List<CalendarModel> marks) {

        try {

            for (CalendarModel calendarModel : marks) {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date d = sdf.parse(calendarModel.getOccurrence_date());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    CalendarDay day = CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));

                    CalendarMarks calendarMark = isRecordExist(calendarMarks, day);

                    if (calendarMark.getDay() == null) {
                        events = new ArrayList<>();
                        calendarMark.setDay(day);
                        colors = new ArrayList<>();
                        if (calendarModel.getObject_type().equalsIgnoreCase("event") && !colors.contains(getContext().getResources().getColor(R.color.red))) {
                            colors.add(getContext().getResources().getColor(R.color.red));
                        } else if (calendarModel.getObject_type().equalsIgnoreCase("meeting") && !colors.contains(getContext().getResources().getColor(R.color.light_green))) {
                            colors.add(getContext().getResources().getColor(R.color.light_green));
                        }
                        else if (calendarModel.getObject_type().equalsIgnoreCase("note") && !colors.contains(getContext().getResources().getColor(R.color.light_blue))) {
                            colors.add(getContext().getResources().getColor(R.color.light_blue));
                        }

                        calendarMark.setColors(colors);
                        calendarMarks.add(calendarMark);
                        events.add(day);
                    } else {
                        if (calendarModel.getObject_type().equalsIgnoreCase("event") && !calendarMark.getColors().contains(getContext().getResources().getColor(R.color.red))) {
                            calendarMark.getColors().add(getContext().getResources().getColor(R.color.red));
                        } else if (calendarModel.getObject_type().equalsIgnoreCase("meeting") && !calendarMark.getColors().contains(getContext().getResources().getColor(R.color.light_green))) {
                            calendarMark.getColors().add(getContext().getResources().getColor(R.color.light_green));
                        }
                        else if (calendarModel.getObject_type().equalsIgnoreCase("note") && !calendarMark.getColors().contains(getContext().getResources().getColor(R.color.light_blue))) {
                            calendarMark.getColors().add(getContext().getResources().getColor(R.color.light_blue));
                        }

                        calendarMark.setColors(calendarMark.getColors());
                    }


                } catch (ParseException ex) {
                    Log.v("Exception", ex.getLocalizedMessage());
                }

            }
            for (CalendarMarks colorMarks : calendarMarks) {
                events = new ArrayList<>();
                int size = colorMarks.getColors().size();
                int[] result = new int[size];
                Integer[] temp = colorMarks.getColors().toArray(new Integer[size]);
                for (int n = 0; n < size; ++n) {
                    result[n] = temp[n];
                }
                events.add(colorMarks.getDay());
                EventDecorator eventDecorator = new EventDecorator(events, result, calendarMarks);
                calendarView.addDecorator(eventDecorator);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void makeJsonObjectRequest(List<CalendarModel> marks) {

        try {
            for (CalendarModel calendarModel : marks) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date d = sdf.parse(calendarModel.getOccurrence_date());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    CalendarDay day = CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH));
                    if (calendarModel.getObject_type().equalsIgnoreCase("event")) {
//                        threeColors[0]= Color.rgb(0, 0, 0);
//                        threeColors[1]= Color.rgb(0, 0, 255);
                    }
                    events.add(day);
                } catch (ParseException ex) {
                    Timber.tag("Exception").v(ex.getLocalizedMessage());
                }
//                CalendarDay day = CalendarDay.from(2020, 3, 5);
//
//                events.add(day);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        int[] threeColors = {
                Color.rgb(0, 0, 255),
                Color.rgb(0, 255, 0),
                Color.rgb(255, 0, 0)};
        EventDecorator eventDecorator = new EventDecorator(events, threeColors, calendarMarks);
        calendarView.addDecorator(eventDecorator);
    }

    @Override
    public void onItemClick(View view, int position, boolean isMonth) {


        Calendar cal = Calendar.getInstance();

        CalendarDay day = CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1);
        CalendarDay lastday = CalendarDay.from(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendarView.state().edit().setMinimumDate(day).setMaximumDate(lastday).commit();

        if (NetworkUtils.isNetworkConnected(getContext())) {
            getAllCalendarMarks(calendarView.getCurrentDate().getMonth(), calendarView.getCurrentDate().getYear());
        } else {
            networkErrorDialog();
        }

        tv_nothing_happened.setVisibility(View.GONE);
        rc_events.setVisibility(View.GONE);


    }

    public int getMonthNum(String month) {
        for (int i = 0; i < months.size(); i++) {
            if (month.equalsIgnoreCase(months.get(i))) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        if (selected) {
            Timber.d(String.valueOf(date.getDate().getDayOfMonth()));
            //   edt_note.setVisibility(View.VISIBLE);
            if (NetworkUtils.isNetworkConnected(getContext())) {
                getCalendarMarksByDate(date.getDate().getYear(), date.getDate().getMonthValue(), date.getDate().getDayOfMonth());
            } else {
                networkErrorDialog();
            }
        }
    }

    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        if (NetworkUtils.isNetworkConnected(getContext())) {
            getAllCalendarMarks(date.getMonth(), date.getYear());
        } else {
            networkErrorDialog();
        }
    }

    private void getCalendarMarksByDate(int year, int month, int day) {
        CalendarModel calendarModel = new CalendarModel();
        calendarModel.setUser_id(LoginUtils.getLoggedinUser().getId());
        calendarModel.setDate(year + "-" + month + "-" + day);
        calendarViewModel.getCalendarMarksByDate(calendarModel).observe(requireActivity(), listBaseModel -> {
            if (!listBaseModel.isError() && listBaseModel.getData() != null) {
                eventList.clear();
                eventList.addAll(listBaseModel.getData());
                eventAdapter.notifyDataSetChanged();
                rc_events.setVisibility(View.VISIBLE);
                if (listBaseModel.getData().size() > 0) {

                    tv_nothing_happened.setVisibility(View.GONE);
                } else {

                    tv_nothing_happened.setVisibility(View.VISIBLE);
                }
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }

        });
    }

    private CalendarMarks isRecordExist(List<CalendarMarks> calendarMarks, CalendarDay day) {
        if (calendarMarks.size() > 0) {
            for (CalendarMarks mark : calendarMarks) {
                if (mark.getDay().equals(day)) {
                    return mark;
                }
            }
        }
        return new CalendarMarks();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (eventList.get(position).getObject_type().equalsIgnoreCase("meeting")) {
            new MeetingDialog(eventList.get(position), getContext()).show();
        }else if (eventList.get(position).getObject_type().equalsIgnoreCase("note")) {
            new NotesDialog(eventList.get(position), getContext()).show();} else {
            new EventDialog(eventList.get(position), getContext()).show();}
    }
}
