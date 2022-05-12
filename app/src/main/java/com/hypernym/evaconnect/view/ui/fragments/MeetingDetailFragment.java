package com.hypernym.evaconnect.view.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.EventAttendees;
import com.hypernym.evaconnect.models.Meeting;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.InvitedUsersAdapter;
import com.hypernym.evaconnect.viewmodel.MeetingViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeetingDetailFragment extends BaseFragment {
    int meeting_id;
    MeetingViewModel meetingViewModel;
    InvitedUsersAdapter usersAdapter;
    private List<User> invitedConnections = new ArrayList<>();

    @BindView(R.id.invite_people)
    RecyclerView invite_people;

    @BindView(R.id.btn_modifymeeting)
    TextView btn_modifymeeting;

    @BindView(R.id.tv_title)
    TextView tv_name;

    @BindView(R.id.tv_description)
    TextView tv_description;

    @BindView(R.id.tv_createdDate)
    TextView tv_createdDate;

    @BindView(R.id.tv_createdLocation)
    TextView tv_createdLocation;

    @BindView(R.id.attending_layout)
    ConstraintLayout attending_layout;

    @BindView(R.id.tv_updated)
    TextView tv_updated;

    @BindView(R.id.btn_attending)
    TextView btn_attending;

    @BindView(R.id.btn_notattending)
    TextView btn_notattending;

    @BindView(R.id.title)
    TextView title;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    private Event event;

    public MeetingDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_meeting_detail, container, false);
        ButterKnife.bind(this, view);
        getActivity().findViewById(R.id.seprator_line).setVisibility(View.VISIBLE);
        init();

        hClickListener();

        return view;
    }

    private void hClickListener() {
        img_backarrow.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {

                (requireActivity()).onBackPressed();

            }
        });


    }

    @Override
    public void onResume() {
        invitedConnections.clear();
        usersAdapter.notifyDataSetChanged();
        super.onResume();
    }

    private void init() {
        //  showBackButton();
        meeting_id=getArguments().getInt("id");
        meetingViewModel = ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication(),getActivity())).get(MeetingViewModel.class);
        setAttendeesAdapter();
        if(NetworkUtils.isNetworkConnected(getContext()))
        {
            getMeetingDetails(meeting_id);

        }
        else
        {
            networkErrorDialog();
        }
    }
    @OnClick(R.id.btn_modifymeeting)
    public void modify_meeting()
    {
        CreateMeetingFragment createMeetingFragment=new CreateMeetingFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("meeting",event);
        createMeetingFragment.setArguments(bundle);
        loadFragment(R.id.framelayout,createMeetingFragment,getContext(),true);
    }

    private void getMeetingDetails(int meeting_id) {
        meetingViewModel.getMeetingDetails(meeting_id).observe(getViewLifecycleOwner(), new Observer<BaseModel<List<Event>>>() {
            @Override
            public void onChanged(BaseModel<List<Event>> listBaseModel) {

                if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {
                    event = listBaseModel.getData().get(0);
                    setEventData(listBaseModel.getData().get(0));
                    for (EventAttendees user : event.getAttendees()) {
                        invitedConnections.add(user.getUser());
                    }
                    usersAdapter.notifyDataSetChanged();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                if (event.getCreated_by_id() == LoginUtils.getLoggedinUser().getId()) {
                    btn_modifymeeting.setVisibility(View.VISIBLE);
                    attending_layout.setVisibility(View.GONE);

                } else {
                    btn_modifymeeting.setVisibility(View.GONE);
                    attending_layout.setVisibility(View.VISIBLE);
                    //  accept_invite.setVisibility(View.VISIBLE);
                }
            }

            private void setEventData(Event event) {
                tv_name.setText(event.getName());

                tv_description.setText(event.getContent());

                tv_createdDate.setText(DateUtils.getFormattedDateDMY(event.getStart_date()) + " - " + DateUtils.getFormattedDateDMY(event.getEnd_date()) + " | " + DateUtils.getTimeUTC((event.getStart_time())) + " - " + DateUtils.getTimeUTC((event.getEnd_time())));

                tv_createdLocation.setText(event.getAddress());
                if (event.getIs_attending() != null && event.getIs_attending().equalsIgnoreCase("Going")) {
                    btn_notattending.setBackground(getResources().getDrawable(R.drawable.notattending_disable));
                    btn_attending.setBackground(getResources().getDrawable(R.drawable.attending_enable));
                } else {
                    btn_notattending.setBackground(getResources().getDrawable(R.drawable.notattending_enable));
                    btn_attending.setBackground(getResources().getDrawable(R.drawable.attending_disable));
                }

            }


        });
    }




    @OnClick(R.id.btn_notattending)
    public void notAttending() {
        btn_notattending.setBackground(getResources().getDrawable(R.drawable.notattending_enable));
        btn_attending.setBackground(getResources().getDrawable(R.drawable.attending_disable));
        Meeting meeting = new Meeting();
        meeting.setUser_id(LoginUtils.getLoggedinUser().getId());
        meeting.setMeeting_id(event.getId());
        meeting.setAttendance_status("Not Going");
        meeting.setModified_by_id(LoginUtils.getLoggedinUser().getId());
        meeting.setModified_datetime(DateUtils.GetCurrentdatetime());
        updateMeetingAttendence(meeting);

    }

    private void updateMeetingAttendence(Meeting meeting) {
        meetingViewModel.updateMeetingAttendance(meeting).observe(this, new Observer<BaseModel<List<Meeting>>>() {
            @Override
            public void onChanged(BaseModel<List<Meeting>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError())
                {
                    tv_updated.setVisibility(View.VISIBLE);
                }
                else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
            }
        });
    }

    @OnClick(R.id.btn_attending)
    public void attending()
    {
        btn_notattending.setBackground(getResources().getDrawable(R.drawable.notattending_disable));
        btn_attending.setBackground(getResources().getDrawable(R.drawable.attending_enable));
        Meeting meeting=new Meeting();
        meeting.setUser_id(LoginUtils.getLoggedinUser().getId());
        meeting.setMeeting_id(event.getId());
        meeting.setAttendance_status("Going");
        meeting.setModified_by_id(LoginUtils.getLoggedinUser().getId());
        meeting.setModified_datetime(DateUtils.GetCurrentdatetime());
        updateMeetingAttendence(meeting);
    }
    private void setAttendeesAdapter() {
        usersAdapter = new InvitedUsersAdapter(getContext(), invitedConnections,false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        invite_people.setLayoutManager(linearLayoutManager);
        invite_people.setAdapter(usersAdapter);
    }

}
