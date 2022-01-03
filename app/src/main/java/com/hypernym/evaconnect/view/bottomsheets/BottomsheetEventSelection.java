package com.hypernym.evaconnect.view.bottomsheets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.ui.fragments.ActivityFragment;
import com.hypernym.evaconnect.view.ui.fragments.CreateEventFragment;
import com.hypernym.evaconnect.view.ui.fragments.CreateMeetingFragment;
import com.hypernym.evaconnect.view.ui.fragments.CreateNoteFragment;
import com.hypernym.evaconnect.view.ui.fragments.MainViewPagerFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomsheetEventSelection extends BottomSheetDialogFragment implements View.OnClickListener {

    @BindView(R.id.create_event)
    LinearLayout create_event;

    BottomsheetEventSelection.ViewHolder mHolder;
    Handler handleDismissal = new Handler();
User user = new User();




    public BottomsheetEventSelection() {
        // Required empty public constructor
    }


    @SuppressLint("ValidFragment")
    public BottomsheetEventSelection(Handler handlerEvents) {
        this.handleDismissal = handlerEvents;
    }

//    @Override
//    public void onDismiss(DialogInterface dialog) {
//        super.onDismiss(dialog);
//        handleDismissal.sendEmptyMessage(0);
//    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottomsheet_event_selection, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(view);
        mHolder = new BottomsheetEventSelection.ViewHolder(view);
        mHolder.linearLayoutEvent.setOnClickListener(this);
        mHolder.linearLayoutMeeting.setOnClickListener(this);
        mHolder.linearLayoutNote.setOnClickListener(this);
        user = LoginUtils.getUser();
       if(user.getType().equalsIgnoreCase("user")) {
           mHolder.linearLayoutEvent.setVisibility(View.GONE);
       }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.create_event:
                loadFragment_bundle(R.id.framelayout, new CreateEventFragment(), getContext(), true,null);

                dismiss();
                break;
            case R.id.create_meeting:
                loadFragment_bundle(R.id.framelayout, new CreateMeetingFragment(), getContext(), true,null);

                dismiss();
                break;
            case R.id.create_note:
                loadFragment_bundle(R.id.framelayout, new CreateNoteFragment(), getContext(), true,null);
                dismiss();
                break;
        }
            }


    public static class ViewHolder {
        LinearLayout linearLayoutEvent,linearLayoutMeeting,linearLayoutNote;

        public ViewHolder(View view) {
            linearLayoutEvent = view.findViewById(R.id.create_event);
            linearLayoutMeeting = view.findViewById(R.id.create_meeting);
            linearLayoutNote = view.findViewById(R.id.create_note);

        }


    }

    public void loadFragment_bundle(int id, Fragment fragment, Context context, boolean isBack, Bundle bundle)
    {

        FragmentTransaction transaction =((AppCompatActivity)context).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);

        fragment.setArguments(bundle);
        getActivity().findViewById(R.id.seprator_line).setVisibility(View.VISIBLE);
        if (fragment instanceof MainViewPagerFragment || fragment instanceof ActivityFragment) {
            getActivity().findViewById(R.id.seprator_line).setVisibility(View.GONE);
        }
        transaction.replace(id, fragment);

        if(isBack)
        {
            transaction.addToBackStack(null);
        }
        transaction.commit();

    }

}
