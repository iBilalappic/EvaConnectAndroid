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
import androidx.lifecycle.Observer;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MultipartBody;


public class EditProfileBioFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;


    @BindView(R.id.tv_main_title)
    TextView tv_main_title;

    @BindView(R.id.edit_about_yourself)
    EditText edit_about_yourself;

    @BindView(R.id.btn_next)
    TextView btn_next;

    private UserViewModel userViewModel;
    User user = new User();
    User userData = new User();

    public EditProfileBioFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        ButterKnife.bind(this, view);
        img_backarrow.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        user = LoginUtils.getUser();

        if (user.getType().equalsIgnoreCase("company")) {
            tv_main_title.setText("Tell us a bit about the company");

        } else {
            tv_main_title.setText("Tell us a bit about yourself");

        }
        init();
        super.onViewCreated(view, savedInstanceState);
    }

    private void init() {

        if ((getArguments() != null)) {
            userData = getArguments().getParcelable(Constants.USER);
            edit_about_yourself.setText(userData.getBio_data());
        }
    }

    private void editBio() {
        showDialog();
        userData.setBio_data(edit_about_yourself.getText().toString());

        HashMap<String, Object> body = new HashMap<String, Object>();
        body.put("bio_data", userData.getBio_data());
        body.put("modified_by_id", userData.getId());
        body.put("modified_datetime", DateUtils.GetCurrentdatetime());
        userViewModel.editProfile(body, userData.getId()).observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> listBaseModel) {
                if (!listBaseModel.isError()) {
                    getActivity().onBackPressed();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
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
        }else if (v.getId()==R.id.btn_next){

            if (!edit_about_yourself.getText().toString().isEmpty()) {
                editBio();
            }
        }
    }
}