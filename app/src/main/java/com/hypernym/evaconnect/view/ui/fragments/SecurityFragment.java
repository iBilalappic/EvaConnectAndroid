package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.hypernym.evaconnect.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SecurityFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.ly_change_password)
    LinearLayout ly_change_password;

    public SecurityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        img_backarrow.setOnClickListener(this);
        ly_change_password.setOnClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_security, container, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_backarrow:
                getActivity().onBackPressed();
                break;
            case R.id.ly_change_password:
                EditProfileChangePasswordFragment editProfileChangePasswordFragment = new EditProfileChangePasswordFragment();
                loadFragment(R.id.framelayout, editProfileChangePasswordFragment, getContext(), true);
                break;
        }
    }
}