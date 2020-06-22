package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.dateTimePicker.DateTime;
import com.hypernym.evaconnect.dateTimePicker.DateTimePicker;
import com.hypernym.evaconnect.dateTimePicker.SimpleDateTimePicker;
import com.hypernym.evaconnect.listeners.InvitedConnectionListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Meeting;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.EventAttendees;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
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

public class CreateMeetingFragment extends BaseFragment implements Validator.ValidationListener, DateTimePicker.OnDateTimeSetListener, InvitedConnectionListener {

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

    private ArrayList<User> invitedConnections = new ArrayList<>();
    private List<Integer> event_attendees=new ArrayList<>();
    private InvitedUsersAdapter usersAdapter;

    private Validator validator;
    private SimpleDialog simpleDialog;
    private EventViewModel eventViewModel;
    private  Event event=new Event();


    @Override
    public void onResume() {

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
       // initRecyclerview();

        return view;
    }

    @Override
    public void onValidationSucceeded() {
        if (NetworkUtils.isNetworkConnected(getContext())){
            showDialog();

            event_attendees.clear();
            for(User inviteConnections:invitedConnections)
            {
                event_attendees.add(inviteConnections.getId());
            }
            Meeting meeting=new Meeting();
            meeting.setUser_id( LoginUtils.getLoggedinUser().getId());

            meeting.setModified_by_id( LoginUtils.getLoggedinUser().getId());
            meeting.setModified_datetime(DateUtils.GetCurrentdatetime());
            meeting.setCreated_by_id(LoginUtils.getLoggedinUser().getId());
            meeting.setName(edt_eventname.getText().toString());
            meeting.setContent(edt_description.getText().toString());
            meeting.setAddress(edt_eventCity.getText().toString());
            if(DateUtils.isValidDate(tv_startdate.getText().toString()))
            {
                meeting.setStart_date(tv_startdate.getText().toString());
            }
            else {
                meeting.setStart_date(DateUtils.getFormattedEventDate(tv_startdate.getText().toString()));
            }
            if(DateUtils.isValidDate(tv_enddate.getText().toString()))
            {
                meeting.setEnd_date(tv_enddate.getText().toString());
            }
            else {
                meeting.setEnd_date( DateUtils.getFormattedEventDate(tv_enddate.getText().toString()));
            }
            if(DateUtils.isValidTime(tv_startTime.getText().toString()))
            {
                meeting.setStart_time(tv_startTime.getText().toString());
            }
            else {
                meeting.setStart_time( DateUtils.getFormattedTime(tv_startTime.getText().toString()));
            }

            if(DateUtils.isValidTime(tv_startTime.getText().toString()))
            {
                meeting.setEnd_time(tv_endTime.getText().toString());
            }
            else {
                meeting.setEnd_time( DateUtils.getFormattedTime(tv_endTime.getText().toString()));
            }

             meeting.setStatus(AppConstants.USER_STATUS);
             meeting.setAttendees(event_attendees);

            if(getArguments()!=null)
            {
                 meeting.setId(event.getId());
                 eventViewModel.updateMeeting(meeting).observe(this, new Observer<BaseModel<List<Meeting>>>() {
                     @Override
                     public void onChanged(BaseModel<List<Meeting>> listBaseModel) {
                         if (listBaseModel != null && !listBaseModel.isError())
                         {
                             simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), getString(R.string.msg_meeting_updated), null, getString(R.string.ok), new View.OnClickListener() {
                                 @Override
                                 public void onClick(View v) {
                                     // when meeting is successfully created remove the saved connections
                                     PrefUtils.remove(getContext(), Constants.PERSIST_CONNECTIONS_MEETING);

                                     if (getFragmentManager().getBackStackEntryCount() != 0) {
                                         getFragmentManager().popBackStack();
                                     }
                                     simpleDialog.dismiss();
                                 }
                             });
                             simpleDialog.show();
                         }
                         else {
                             networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                         }
                         hideDialog();
                     }
                 });
            }
            else
            {
                eventViewModel.createMeeting(meeting).observe(this, listBaseModel -> {
                    if (listBaseModel != null && !listBaseModel.isError())
                    {
                        simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), getString(R.string.msg_meeting_created), null, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // when meeting is successfully created remove the saved connections
                                PrefUtils.remove(getContext(), Constants.PERSIST_CONNECTIONS_MEETING);

                                if (getFragmentManager().getBackStackEntryCount() != 0) {
                                    getFragmentManager().popBackStack();
                                }
                                simpleDialog.dismiss();
                            }
                        });
                        simpleDialog.show();
                    }
                    else {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                    }
                    hideDialog();
                });
            }


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

        tv_enddate.setInputType(InputType.TYPE_NULL);
        tv_endTime.setInputType(InputType.TYPE_NULL);

        validator = new Validator(this);
        validator.setValidationListener(this);
        eventViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(EventViewModel.class);
        initRecyclerview();
        if(getArguments()!=null)
        {
            setPageTitle("Update Meeting Schedule");
            event=(Event) getArguments().getSerializable("meeting");
            edt_eventname.setText(event.getName());
            edt_description.setText(event.getContent());
            edt_eventCity.setText(event.getAddress());
            try {
                tv_startdate.setText(dateformat.format(DateUtils.formattedDateWithoutTime(event.getStart_date())));
                tv_startTime.setText(time.format(DateUtils.formattedDateTime(event.getStart_time())));
                tv_enddate.setText(dateformat.format(DateUtils.formattedDateWithoutTime(event.getEnd_date())));
                tv_endTime.setText(time.format(DateUtils.formattedDateTime(event.getEnd_time())));
            }
            catch (Exception ex)
            {
                Log.d("exception",ex.getMessage());
            }


            for(EventAttendees eventAttendees:event.getAttendees())
            {
                invitedConnections.add(eventAttendees.getUser());
            }
            usersAdapter.notifyDataSetChanged();
            post.setText("Update Meeting");
        }


    }

    @OnClick({R.id.tv_startdate, R.id.tv_startTime})
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

    @OnClick({R.id.tv_enddate, R.id.tv_endTime})
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
        bundle.putParcelableArrayList("connections",invitedConnections);
        loadFragment_bundle(R.id.framelayout, new InviteConnections(this), getContext(), true, bundle);
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
            tv_enddate.setText("");
            tv_endTime.setText("");
            /*tv_enddate.setText(dateformat.format(mDateTime.getDate()));
            tv_endTime.setText(time.format(mDateTime.getDate()));*/
        }
        else{
            String str2 = mDateTime.getDate().toString();
            try {
                endDate = formatter.parse(str2);
                Log.d("TAAAG", "enddate:" + endDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (endDate != null && startDate!=null)
        {
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
        }

        Log.v("TEST_TAG", "Date and Time selected: " + mDateTime.getDateString());
    }

    @Override
    public void invitedConnections(List<User> connections) {
        invitedConnections.clear();
        invitedConnections.addAll(connections);
        Log.e(TAG, "onResume: " + GsonUtils.toJson(invitedConnections));
        usersAdapter.notifyDataSetChanged();
    }
}
