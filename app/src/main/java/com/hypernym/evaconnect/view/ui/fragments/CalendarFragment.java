package com.hypernym.evaconnect.view.ui.fragments;


import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.decorators.EventDecorator;

import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.CalendarMarks;
import com.hypernym.evaconnect.models.CalendarModel;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.EventAdapter;
import com.hypernym.evaconnect.view.adapters.MonthAdapter;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.view.ui.activities.BaseActivity;
import com.hypernym.evaconnect.viewmodel.CalendarViewModel;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends BaseFragment implements MonthAdapter.ItemClickListener, OnDateSelectedListener, OnMenuItemClickListener<PowerMenuItem> {

    @BindView(R.id.calendarView)
    MaterialCalendarView calendarView;

    @BindView(R.id.newevent)
    TextView newevent;

    @BindView(R.id.rc_month)
    RecyclerView rc_month;

    @BindView(R.id.rc_year)
    RecyclerView rc_year;

    @BindView(R.id.selectmonth)
    TextView selectmonth;

    @BindView(R.id.selectyear)
    TextView selectyear;

    @BindView(R.id.eventDetailsRecyclerview)
    RecyclerView eventDetailsRecyclerview;

    @BindView(R.id.dayOfMonth)
    TextView dayOfMonth;

    @BindView(R.id.close_selection)
    ImageView close_selection;

   /* @BindView(R.id.rc_events)
    RecyclerView rc_events;*/

    @BindView(R.id.edt_note)
    EditText edt_note;

    @BindView(R.id.btn_save)
    Button btn_save;

    @BindView(R.id.tv_nothing_happened)
    TextView tv_nothing_happened;

    @BindView(R.id.addEvent)
    FloatingActionButton addEvent;

    private CalendarViewModel calendarViewModel;
    int[] threeColors;
    List<Integer> colors=new ArrayList<>();

    private List<CalendarDay> events = new ArrayList<>();
    private List<String> months=new ArrayList<>();
    private List<String> years=new ArrayList<>();
    private List<PowerMenuItem> calendarAction = new ArrayList<>();
    private MonthAdapter monthAdapter,yearAdapter;
    private SimpleDialog simpleDialog;
    private List<CalendarModel> eventList=new ArrayList<>();
    private EventAdapter eventAdapter;
    List<CalendarMarks> calendarMarks=new ArrayList<>();

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

        initCalendarActions();

        return view;
    }

    private void initCalendarActions() {
        calendarAction.clear();
        calendarAction.add(new PowerMenuItem("Create a Meeting Schedule"));
        calendarAction.add(new PowerMenuItem("Create an Event"));

        PowerMenu powerMenu = new PowerMenu.Builder(getContext())
                .addItemList(calendarAction)
                .setAutoDismiss(true)
                .setTextSize(14)
                .setWidth(600)
                .setDivider(new ColorDrawable(ContextCompat.getColor(getContext(), R.color.gray)))
                .setDividerHeight(1)
                .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                .setMenuRadius(4f)
                .setMenuShadow(10f)
                .setTextColor(ContextCompat.getColor(getContext(), R.color.dark_gray_2))
                .setOnMenuItemClickListener(this)
                .build();

        addEvent.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                powerMenu.showAsAnchorCenter(addEvent);
            }
        });
    }

    private void init() {

        calendarViewModel= ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication(),getActivity())).get(CalendarViewModel.class);
        calendarView.setTopbarVisible(true);
        calendarView.setDynamicHeightEnabled(true);

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        /*CalendarDay day = CalendarDay.from(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,1);
        CalendarDay lastday = CalendarDay.from(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendarView.state().edit().setMinimumDate(day).setMaximumDate(lastday).commit();*/
        calendarView.setDateSelected(CalendarDay.today(),true);

        newevent.setVisibility(View.VISIBLE);
        dayOfMonth.setText(String.valueOf(1));
        String currentdate= String.valueOf(CalendarDay.today().getDay());
       // dayOfMonth.setText(currentdate);
        Log.d("TAAAF",currentdate);
        selectmonth.setText(DateUtils.getMonthForInt(cal.get(Calendar.MONTH)));
        selectyear.setText(String.valueOf(cal.get(Calendar.YEAR)));
        initMonths();
        initYears();
        calendarView.setOnDateChangedListener(this);

        calendarView.setAllowClickDaysOutsideCurrentMonth(false);

        edt_note.addTextChangedListener(new TextWatcher());

        btn_save.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if(NetworkUtils.isNetworkConnected(getContext()))
                {
                   saveNote();
                }
                else
                {
                    networkErrorDialog();
                }
            }
        });

        eventAdapter=new EventAdapter(getContext(),eventList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        eventDetailsRecyclerview.setLayoutManager(linearLayoutManager);
        eventDetailsRecyclerview.setAdapter(eventAdapter);
        showDialog();

        if(NetworkUtils.isNetworkConnected(getContext()))
        {
            getAllCalendarMarks(calendarView.getCurrentDate().getMonth(),calendarView.getCurrentDate().getYear());
        }
        else
        {
            networkErrorDialog();
        }

        new Handler().postDelayed(() -> {
            getCalendarMarksByDate(String.valueOf(calendarView.getCurrentDate().getYear()), String.valueOf(calendarView.getCurrentDate().getMonth()), currentdate);
            hideDialog();
        }, 3000);


        // fetch events/meeting of every month which the user has redirected to (either using arrows or swipe gesture)
        calendarView.setOnMonthChangedListener((widget, date) ->
        {
            CalendarModel calendarModel = new CalendarModel();
            calendarModel.setUser_id(LoginUtils.getLoggedinUser().getId());
            calendarModel.setMonth(String.valueOf(calendarView.getCurrentDate().getMonth()));
            calendarModel.setYear(String.valueOf(calendarView.getCurrentDate().getYear()));

            // API call, no need to call observer as we have already initialized observer once.
            calendarViewModel.getAllCalendarMarks(calendarModel);
        });


    }

    // making and saving notes have been removed from the new design
    private void saveNote() {
        CalendarModel calendarModel=new CalendarModel();
        calendarModel.setNotes(edt_note.getText().toString());
        calendarModel.setUser_id(LoginUtils.getLoggedinUser().getId());
        calendarModel.setObject_type("note");
        calendarModel.setStatus(AppConstants.ACTIVE);
        calendarModel.setOccurrence_date(selectyear.getText().toString()+"-"+(getMonthNum(selectmonth.getText().toString())+1)+"-"+dayOfMonth.getText().toString());
        calendarViewModel.createNote(calendarModel).observe(this, listBaseModel -> {
            if(listBaseModel!=null && !listBaseModel.isError()&& listBaseModel.getData()!=null)
            {
                simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), getString(R.string.msg_note_created), null, getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getActivity().onBackPressed();
                        simpleDialog.dismiss();
                        edt_note.setText("");
                        getAllCalendarMarks(calendarView.getCurrentDate().getMonth(),calendarView.getCurrentDate().getYear());
                    }
                });
                simpleDialog.show();
            }
            else
            {
                networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
            }
        });
    }

    // years filter dropdown have been removed and changed to arrow navigations
    private void initYears() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
      for(int i=1902;i<=cal.get(Calendar.YEAR);i++)
      {
          years.add(String.valueOf(i));
      }
        yearAdapter=new MonthAdapter(getContext(),years,this,false);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        rc_year.setLayoutManager(linearLayoutManager);
        rc_year.setAdapter(yearAdapter);
    }

    // month filter dropdown have been removed and changed to arrow navigations
    private void initMonths() {
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");
        monthAdapter=new MonthAdapter(getContext(),months,this,true);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        rc_month.setLayoutManager(linearLayoutManager);
        rc_month.setAdapter(monthAdapter);
    }

    // month filter dropdown have been removed and changed to arrow navigations
    @OnClick(R.id.selectmonth)
    public void selectMonth()
    {
        rc_month.setVisibility(View.VISIBLE);
        rc_year.setVisibility(View.GONE);
        close_selection.setVisibility(View.VISIBLE);
        selectmonth.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);

    }

    // years filter dropdown have been removed and changed to arrow navigations
    @OnClick(R.id.selectyear)
    public void selectYear()
    {
        rc_month.setVisibility(View.GONE);
        rc_year.setVisibility(View.VISIBLE);
        close_selection.setVisibility(View.VISIBLE);
        selectyear.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
    }

    private void getAllCalendarMarks(int month,int year) {

        CalendarModel calendarModel =new CalendarModel();
        calendarModel.setUser_id(LoginUtils.getLoggedinUser().getId());
        calendarModel.setMonth(String.valueOf(month));
        calendarModel.setYear(String.valueOf(year));
        calendarViewModel.getAllCalendarMarks(calendarModel).observe(this, listBaseModel -> {
            if(!listBaseModel.isError() && listBaseModel.getData()!=null)
            {
                setEvents(listBaseModel.getData());
               // makeJsonObjectRequest(listBaseModel.getData());
            }
            else
            {
                networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
            }

        });
    }

    private void setEvents(List<CalendarModel> marks) {

        try {

            for(CalendarModel calendarModel :marks)
            {

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date d = sdf.parse(calendarModel.getOccurrence_date());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    CalendarDay day = CalendarDay.from(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH));
                    CalendarMarks calendarMark=isRecordExist(calendarMarks,day);

                    if(calendarMark.getDay()==null)
                    {
                        events=new ArrayList<>();
                        calendarMark.setDay(day);
                        colors=new ArrayList<>();
                        if(calendarModel.getObject_type().equalsIgnoreCase("event") && !colors.contains(getContext().getResources().getColor(R.color.calendar_selection_colour)))
                        {
                            colors.add(getContext().getResources().getColor(R.color.calendar_selection_colour));
                        }
                        else if(calendarModel.getObject_type().equalsIgnoreCase("interview") &&  !colors.contains(getContext().getResources().getColor(R.color.colorAccent)))
                        {
                            colors.add(getContext().getResources().getColor(R.color.colorAccent));
                        }
                        /*else if(calendarModel.getObject_type().equalsIgnoreCase("note")  && !colors.contains(getContext().getResources().getColor(R.color.blue)))
                        {
                            colors.add(getContext().getResources().getColor(R.color.blue));
                        }*/
                        calendarMark.setColors(colors);
                        calendarMarks.add(calendarMark);
                        events.add(day);
                    }
                    else
                    {
                        if(calendarModel.getObject_type().equalsIgnoreCase("event") && !calendarMark.getColors().contains(getContext().getResources().getColor(R.color.calendar_selection_colour)))
                        {
                            calendarMark.getColors().add(getContext().getResources().getColor(R.color.calendar_selection_colour));
                        }
                        else if(calendarModel.getObject_type().equalsIgnoreCase("interview") && !calendarMark.getColors().contains(getContext().getResources().getColor(R.color.colorAccent)))
                        {
                            calendarMark.getColors().add(getContext().getResources().getColor(R.color.colorAccent));
                        }
                        /*else if(calendarModel.getObject_type().equalsIgnoreCase("note") && !calendarMark.getColors().contains(getContext().getResources().getColor(R.color.blue)))
                        {
                            calendarMark.getColors().add(getContext().getResources().getColor(R.color.blue));
                        }*/
                        calendarMark.setColors(calendarMark.getColors());

                    }


                } catch (ParseException ex) {
                    Log.v("Exception", ex.getLocalizedMessage());
                }

            }
            for(CalendarMarks colorMarks:calendarMarks)
            {
                events=new ArrayList<>();
                int size = colorMarks.getColors().size();
                int[] result = new int[size];
                Integer[] temp = colorMarks.getColors().toArray(new Integer[size]);
                for (int n = 0; n < size; ++n) {
                    result[n] = temp[n];
                }
                events.add(colorMarks.getDay());
                EventDecorator eventDecorator = new EventDecorator(events, result,calendarMarks);
                calendarView.addDecorator(eventDecorator);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @OnClick(R.id.newevent)
    public void newEvent()
    {
        loadFragment(R.id.framelayout,new CreateEventFragment(),getContext(),true);
    }

    @OnClick(R.id.close_selection)
    public void closeSelection()
    {
        rc_month.setVisibility(View.GONE);
        rc_year.setVisibility(View.GONE);
        close_selection.setVisibility(View.GONE);
        selectyear.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_arrow, 0);
        selectmonth.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down_arrow, 0);
    }



    private void makeJsonObjectRequest(List<CalendarModel> marks) {

        try {
            for(CalendarModel calendarModel :marks) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Date d = sdf.parse(calendarModel.getOccurrence_date());
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(d);
                    CalendarDay day = CalendarDay.from(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.get(Calendar.DAY_OF_MONTH));
                    if(calendarModel.getObject_type().equalsIgnoreCase("event"))
                    {
//                        threeColors[0]= Color.rgb(0, 0, 0);
//                        threeColors[1]= Color.rgb(0, 0, 255);
                    }
                    events.add(day);
                } catch (ParseException ex) {
                    Log.v("Exception", ex.getLocalizedMessage());
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
        EventDecorator eventDecorator = new EventDecorator(events, threeColors,calendarMarks);
        calendarView.addDecorator(eventDecorator);
    }

    @Override
    public void onItemClick(View view, int position, boolean isMonth) {

        if(isMonth)
        {
            selectmonth.setText(months.get(position));
        }
        else
        {
            selectyear.setText(years.get(position));
        }
        closeSelection();
        Calendar cal = Calendar.getInstance();
        cal.set(Integer.parseInt(selectyear.getText().toString()),getMonthNum(selectmonth.getText().toString()),Integer.parseInt(dayOfMonth.getText().toString()));
        CalendarDay day = CalendarDay.from(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,1);
        CalendarDay lastday = CalendarDay.from(cal.get(Calendar.YEAR),cal.get(Calendar.MONTH)+1,cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendarView.state().edit().setMinimumDate(day).setMaximumDate(lastday).commit();

        if(NetworkUtils.isNetworkConnected(getContext()))
        {
            getAllCalendarMarks(calendarView.getCurrentDate().getMonth(),calendarView.getCurrentDate().getYear());
        }
        else
        {
            networkErrorDialog();
        }
        edt_note.setVisibility(View.GONE);
        tv_nothing_happened.setVisibility(View.GONE);
        eventDetailsRecyclerview.setVisibility(View.GONE);


    }

    public int getMonthNum(String month)
    {
    for(int i=0;i<months.size();i++)
    {
        if(month.equalsIgnoreCase(months.get(i)))
        {
        return i;
        }
    }
    return  0;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        if(selected)
        {
            dayOfMonth.setText(String.valueOf(date.getDate().getDayOfMonth()));
            Log.d("TAAAF",String.valueOf(date.getDate().getDayOfMonth()));
         //   edt_note.setVisibility(View.VISIBLE);
            if(NetworkUtils.isNetworkConnected(getContext()))
            {
                getCalendarMarksByDate(String.valueOf(calendarView.getCurrentDate().getYear()), String.valueOf(calendarView.getCurrentDate().getMonth()), dayOfMonth.getText().toString());
            }
            else
            {
                networkErrorDialog();
            }
        }
    }

    private void getCalendarMarksByDate(String year, String month, String day) {
        CalendarModel calendarModel =new CalendarModel();
        calendarModel.setUser_id(LoginUtils.getLoggedinUser().getId());
/*        calendarModel.setDate(selectyear.getText().toString()+"-"+(getMonthNum(selectmonth.getText().toString())+1)+"-"+dayOfMonth.getText().toString());*/
        calendarModel.setDate(year + "-" + month + "-" + day);
        calendarViewModel.getCalendarMarksByDate(calendarModel).observe(this, new Observer<BaseModel<List<CalendarModel>>>() {
            @Override
            public void onChanged(BaseModel<List<CalendarModel>> listBaseModel) {
                if(!listBaseModel.isError() && listBaseModel.getData()!=null)
                {
                    eventList.clear();
                    eventList.addAll(listBaseModel.getData());
                    eventAdapter.notifyDataSetChanged();
                    eventDetailsRecyclerview.setVisibility(View.VISIBLE);
                    if(listBaseModel.getData().size()>0)
                    {
                        edt_note.setVisibility(View.GONE);
                        tv_nothing_happened.setVisibility(View.GONE);
                    }
                    else
                    {
                        edt_note.setVisibility(View.VISIBLE);
                        tv_nothing_happened.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
                }

            }
        });
    }
    private CalendarMarks isRecordExist(List<CalendarMarks> calendarMarks,CalendarDay day)
    {
        if(calendarMarks.size()>0)
        {
            for (CalendarMarks mark:calendarMarks)
            {
                if(mark.getDay().equals(day))
                {
                    return mark;
                }
            }
        }
        return new CalendarMarks();
    }

    @Override
    public void onItemClick(int position, PowerMenuItem item) {
        if (item.getTitle().equals("Create an Event"))
        {
            loadFragment(R.id.framelayout,new CreateEventFragment(),getContext(),true);
        }
        else if (item.getTitle().equals("Create a Meeting Schedule"))
        {
            loadFragment(R.id.framelayout,new CreateMeetingFragment(), getContext(),true);
        }
    }


    public class TextWatcher implements android.text.TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.length()>0)
            {
                btn_save.setVisibility(View.VISIBLE);
            }
            else {
                btn_save.setVisibility(View.GONE);
            }
        }

    }
}
