package com.hypernym.evaconnect.view.ui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditProfileFragment extends BaseFragment {

    @BindView(R.id.tv_location)
    TextView tv_location;

    @BindView(R.id.tv_designation)
    TextView tv_designation;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_connect)
    TextView tv_connect;


    @BindView(R.id.tv_biodata)
    TextView tv_biodata;

    @BindView(R.id.tv_field)
    TextView tv_field;


    @BindView(R.id.tv_company)
    TextView tv_company;


    @BindView(R.id.profile_image)
    CircleImageView cv_profile_image;

    User user = new User();


    public EditProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        ButterKnife.bind(this, view);
        user = LoginUtils.getUser();
        SettingUserProfile(user);

        return view;
    }

    private void SettingUserProfile(User user) {
        if (user != null) {
            Log.d("USERDATA",""+ GsonUtils.toJson(user));
            AppUtils.setGlideImage(getContext(), cv_profile_image, user.getUser_image());
            tv_name.setText(user.getFirst_name());
            tv_biodata.setText(user.getBio_data());
          //  tv_location.setText(user.getBio_data());
           // tv_designation.setText();
        }
    }

}
