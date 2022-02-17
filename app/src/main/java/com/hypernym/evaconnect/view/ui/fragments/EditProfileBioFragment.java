package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;


public class EditProfileBioFragment extends Fragment implements View.OnClickListener {


    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;


    @BindView(R.id.tv_main_title)
    TextView tv_main_title;


    private UserViewModel userViewModel;
    User user = new User();


    public EditProfileBioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        img_backarrow.setOnClickListener(this);
        user = LoginUtils.getUser();

        if (user.getType().equalsIgnoreCase("company")) {
            tv_main_title.setText("Tell us a bit about the company");

        } else {
            tv_main_title.setText("Tell us a bit about yourself");

        }

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_profile_bio, container, false);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.img_backarrow) {
            getActivity().onBackPressed();
        }
    }
}