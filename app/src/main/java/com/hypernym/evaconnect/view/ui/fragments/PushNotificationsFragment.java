package com.hypernym.evaconnect.view.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.Validator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class PushNotificationsFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;
   /* @BindView(R.id.tv_name)
    TextView tv_name;*/

  /*  @BindView(R.id.tv_location)
    TextView tv_location;

    @BindView(R.id.btn_save)
    TextView btn_save;*/

    @BindView(R.id.switch_push)
    Switch switch_push;

  /*  @BindView(R.id.profile_image)
    CircleImageView cv_profile_image;*/



    User user = new User();
    private Validator validator;
    private UserViewModel userViewModel;

    boolean passwordMatches;
    Integer notification_check = 0;

    Dialog mdialog;


    public PushNotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_push_notification, container, false);
        ButterKnife.bind(this, view);
        img_backarrow.setOnClickListener(this);
        switch_push.setOnClickListener(this);
        switch_push.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    notification_check = 1;
                } else {
                    notification_check = 0;
                }

            }
        });
/*        btn_save.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                UpdateSetting();
            }
        });*/


        user = LoginUtils.getUser();
        SettingUserProfile(user);
        return view;
    }

    private void SettingUserProfile(User user) {
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);
       // GetUserDetails();
    }

    private void GetUserDetails() {
        showDialog();
        userViewModel.getuser_details(user.getId()).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    if (!TextUtils.isEmpty(listBaseModel.getData().get(0).getUser_image())) {
                       // AppUtils.setGlideImage(getContext(), cv_profile_image, listBaseModel.getData().get(0).getUser_image());
                    }
//                    else if (listBaseModel.getData().get(0).getIs_facebook() == 1 && !TextUtils.isEmpty(listBaseModel.getData().get(0).getFacebook_image_url())) {
//                        AppUtils.setGlideImage(getContext(), cv_profile_image, listBaseModel.getData().get(0).getFacebook_image_url());
//                    } else {
//                        AppUtils.setGlideImage(getContext(), cv_profile_image, listBaseModel.getData().get(0).getUser_image());
//                    }
                  //  tv_location.setText(listBaseModel.getData().get(0).getCountry() + "," + listBaseModel.getData().get(0).getCity());
                  //  tv_name.setText(listBaseModel.getData().get(0).getFirst_name());
                    notification_check = listBaseModel.getData().get(0).getIs_notifications();
                    if (notification_check == 1) {
                        switch_push.setChecked(true);
                    } else {
                        switch_push.setChecked(false);
                    }

                    LoginUtils.saveUser(listBaseModel.getData().get(0));
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }


    private void UpdateSetting() {
        showDialog();
        userViewModel.setting_update(notification_check, user.getId()
        ).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    hideDialog();
                    Toast.makeText(getContext(), "" + listBaseModel.getMessage(), Toast.LENGTH_SHORT).show();
                    GetUserDetails();
                } else {
                    hideDialog();
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_backarrow:
                getActivity().onBackPressed();
                break;
        }
    }
}

