package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.dateTimePicker.DateTime;
import com.hypernym.evaconnect.dateTimePicker.DateTimePicker;
import com.hypernym.evaconnect.dateTimePicker.SimpleDateTimePicker;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.utils.PrefUtils;
import com.hypernym.evaconnect.view.adapters.InvitedUsersAdapter;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.EventViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTouch;

public class CreateMeetingFragment extends BaseFragment implements Validator.ValidationListener, DateTimePicker.OnDateTimeSetListener {

    @BindView(R.id.tv_startdate)
    EditText tv_startdate;

    @BindView(R.id.tv_enddate)
    EditText tv_enddate;

    @BindView(R.id.tv_startTime)
    EditText tv_startTime;

    @BindView(R.id.tv_endTime)
    EditText tv_endTime;

    @NotEmpty
    @BindView(R.id.edt_eventname)
    EditText edt_eventname;

    @NotEmpty
    @BindView(R.id.edt_eventlocation)
    EditText edt_eventCity;

    @BindView(R.id.invite_people_meeting)
    RecyclerView invite_people;

    @NotEmpty
    @BindView(R.id.edt_content)
    EditText edt_description;

    @BindView(R.id.post)
    TextView post;

    private static final String TAG = CreateMeetingFragment.class.getSimpleName();

    private boolean isStartDate = false;
    private Date startDate = new Date();
    private Date selectedDate = new Date();
    Date endselectDate;
    DateFormat time = new SimpleDateFormat("hh:mm a");
    DateFormat dateformat = new SimpleDateFormat("E, dd MMM yyyy");
    SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";

    private List<User> invitedConnections = new ArrayList<>();
    private InvitedUsersAdapter usersAdapter;

    private Validator validator;
    private SimpleDialog simpleDialog;


    @Override
    public void onResume() {
        if (PrefUtils.getConnectionsMeeting(getContext()) != null)
        {
            invitedConnections.clear();
            invitedConnections.addAll(PrefUtils.getConnectionsMeeting(getContext()));

            Log.e(TAG, "onResume: " + GsonUtils.toJson(invitedConnections));

            usersAdapter.notifyDataSetChanged();
        }

        super.onResume();
    }

    public CreateMeetingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_meeting, container, false);

        ButterKnife.bind(this, view);
        init();
        initRecyclerview();

        return view;
    }

    @Override
    public void onValidationSucceeded() {
        if (NetworkUtils.isNetworkConnected(getContext())){
            // API CALL

            // when meeting is successfully created remove the saved connections
            PrefUtils.remove(getContext(), Constants.PERSIST_CONNECTIONS_MEETING);
        }
        else{
            networkErrorDialog();
        }
    }

    @OnClick(R.id.post)
    public void post() {
        validator.validate();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void initRecyclerview() {
        usersAdapter = new InvitedUsersAdapter(getContext(), invitedConnections);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        invite_people.setLayoutManager(linearLayoutManager);
        invite_people.setAdapter(usersAdapter);
    }

    private void init() {
        setPageTitle("Create a Meeting Schedule");
        tv_startdate.setInputType(InputType.TYPE_NULL);
        tv_startdate.setText(dateformat.format(new Date()));

        tv_startTime.setText(time.format(new Date()));
        tv_startTime.setInputType(InputType.TYPE_NULL);

        tv_enddate.setText(dateformat.format(new Date()));
        tv_enddate.setInputType(InputType.TYPE_NULL);

        tv_endTime.setText(time.format(new Date()));
        tv_endTime.setInputType(InputType.TYPE_NULL);

        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    @OnTouch({R.id.tv_startdate, R.id.tv_startTime})
    public void setStartDate() {
        isStartDate = true;
        try {
            selectedDate = dateformat.parse(tv_startdate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        startDate = new Date();
        showDateTimePicker("Set Start Date & Time");
    }

    @OnTouch({R.id.tv_enddate, R.id.tv_endTime})
    public void setEndDate() {
        isStartDate = false;
        try {
            selectedDate = dateformat.parse(tv_enddate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        showDateTimePicker("Set End Date & Time");
    }

    @OnClick(R.id.add1)
    public void addConnections(){
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FRAGMENT_TYPE, Constants.FRAGMENT_NAME_1);

        loadFragment_bundle(R.id.framelayout, new InviteConnections(), getContext(), true, bundle);
    }

    private void showDateTimePicker(String title) {
        // Create a SimpleDateTimePicker and Show it

        SimpleDateTimePicker simpleDateTimePicker = SimpleDateTimePicker.make(
                title,
                selectedDate, startDate,
                this,
                getFragmentManager()
        );

        simpleDateTimePicker.show();
    }

    @Override
    public void DateTimeSet(Date date) {
        Date selectedDate = null;
        Date endDate = null;
        DateTime mDateTime = new DateTime(date);
        if (isStartDate) {
            tv_startdate.setText(dateformat.format(mDateTime.getDate()));
            tv_startTime.setText(time.format(mDateTime.getDate()));
            startDate = mDateTime.getDate();
            tv_enddate.setText(dateformat.format(mDateTime.getDate()));
            tv_endTime.setText(time.format(mDateTime.getDate()));
        }

        String str2 = mDateTime.getDate().toString();
        try {
            endDate = formatter.parse(str2);
            Log.d("TAAAG", "enddate:" + endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (mDateTime.getDate().getMonth() == startDate.getMonth() && mDateTime.getDate().getDate() == startDate.getDate()) {

            if (endDate.compareTo(startDate) > 0) {
                tv_enddate.setText(dateformat.format(mDateTime.getDate()));
                tv_endTime.setText(time.format(mDateTime.getDate()));

            } else {
                Toast.makeText(getContext(), "End time must be greater than start time", Toast.LENGTH_SHORT).show();
            }
        } else if (endDate.compareTo(startDate) > 0) {
            tv_enddate.setText(dateformat.format(mDateTime.getDate()));
            tv_endTime.setText(time.format(mDateTime.getDate()));
        } else {
            tv_enddate.setText(dateformat.format(mDateTime.getDate()));
            tv_endTime.setText(time.format(mDateTime.getDate()));
        }

        Log.v("TEST_TAG", "Date and Time selected: " + mDateTime.getDateString());
    }
}
