package com.hypernym.evaconnect.view.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.EventAttendees;
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

    @BindView(R.id.btn_viewEvent)
    TextView modify_event;

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

    private Event event;

    public MeetingDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_meeting_detail, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }
    private void init() {
        showBackButton();
        setPageTitle("Meeting");
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
    private void getMeetingDetails(int meeting_id) {
        meetingViewModel.getMeetingDetails(meeting_id).observe(this, new Observer<BaseModel<List<Event>>>() {
            @Override
            public void onChanged(BaseModel<List<Event>> listBaseModel) {

                if(listBaseModel!=null && !listBaseModel.isError() && listBaseModel.getData().size()>0)
                {
                    event=listBaseModel.getData().get(0);
                    setEventData(listBaseModel.getData().get(0));
                    for(EventAttendees user:event.getAttendees())
                    {
                        invitedConnections.add(user.getUser());
                    }
                    usersAdapter.notifyDataSetChanged();
                }
                else
                {
                    networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
                }
                if(event.getCreated_by_id()== LoginUtils.getLoggedinUser().getId())
                {
                    modify_event.setVisibility(View.VISIBLE);
                    attending_layout.setVisibility(View.GONE);

                }
                else
                {
                    modify_event.setVisibility(View.GONE);
                    attending_layout.setVisibility(View.VISIBLE);
                  //  accept_invite.setVisibility(View.VISIBLE);
                }
            }

            private void setEventData(Event event) {
                tv_name.setText(event.getName());

                tv_description.setText(event.getContent());

                tv_createdDate.setText(DateUtils.getFormattedDateDMY(event.getStart_date()));

                tv_createdLocation.setText(event.getAddress());

            }
        });
    }
    private void setAttendeesAdapter() {
        usersAdapter = new InvitedUsersAdapter(getContext(), invitedConnections);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        invite_people.setLayoutManager(linearLayoutManager);
        invite_people.setAdapter(usersAdapter);
    }

}
