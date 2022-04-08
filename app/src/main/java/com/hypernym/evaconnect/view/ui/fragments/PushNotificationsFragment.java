package com.hypernym.evaconnect.view.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.NotificationSettingsModel;
import com.hypernym.evaconnect.models.NotificationSettingsRootModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.mobsandgeeks.saripaar.Validator;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PushNotificationsFragment extends BaseFragment implements View.OnClickListener {


    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;


    @BindView(R.id.switch_push)
    Switch switch_push;


    @BindView(R.id.switch_push1)
    Switch switch_push1;


    @BindView(R.id.switch_push2)
    Switch switch_push2;


    @BindView(R.id.switch_push3)
    Switch switch_push3;


    @BindView(R.id.switch_push4)
    Switch switch_push4;


    @BindView(R.id.switch_push5)
    Switch switch_push5;


    @BindView(R.id.switch_push6)
    Switch switch_push6;


    @BindView(R.id.switch_push7)
    Switch switch_push7;


    @BindView(R.id.switch_push8)
    Switch switch_push8;


    @BindView(R.id.switch_push9)
    Switch btn_submit9;


    @BindView(R.id.switch_push10)
    Switch btn_submit10;


    @BindView(R.id.switch_push11)
    Switch btn_submit11;


    @BindView(R.id.switch_push12)
    Switch btn_submit12;


    @BindView(R.id.btn_submit)
    TextView btn_submit;

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
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(requireActivity().getApplication(), getActivity())).get(UserViewModel.class);

        hInitView();


        hInitClickListener();
        user = LoginUtils.getUser();
        return view;
    }

    private void hInitView() {
        hCallNotificationAPI();
    }

    private void hCallNotificationAPI() {

        userViewModel.hGetNotificationSettings(LoginUtils.getUser().getId()).observe(requireActivity(), new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> listBaseModel) {
                if (listBaseModel.getData() != null) {
                    Log.d("notification_check", "GET Request");

                }
            }
        });
    }

    private void hInitClickListener() {

        btn_submit.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                btn_submit.setOnClickListener(new OnOneOffClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        NotificationSettingsModel notificationSettingsModel = new NotificationSettingsModel();

                        hPopulateSettingModel(notificationSettingsModel);

                        NotificationSettingsRootModel notificationSettingsModelRoot = new NotificationSettingsRootModel(LoginUtils.getUser().getStatus(), LoginUtils.getLoggedinUser().getId(), DateUtils.GetCurrentdatetime(), LoginUtils.getLoggedinUser().getUser_id(), AppConstants.OS, notificationSettingsModel);

                        hPostSettingData(notificationSettingsModelRoot);

                    }
                });
            }
        });


    }

    private void hPostSettingData(NotificationSettingsRootModel notificationSettingsModel) {

        userViewModel.hPostUserSettingsData(notificationSettingsModel);

    }

    private void hPopulateSettingModel(NotificationSettingsModel notificationSettingsModel) {

        if (switch_push.isSelected()) {
            notificationSettingsModel.setNew_events(1);
        } else {
            notificationSettingsModel.setNew_events(0);
        }


        if (switch_push1.isSelected()) {
            notificationSettingsModel.setMessage(1);
        } else {
            notificationSettingsModel.setMessage(0);
        }


        if (switch_push2.isSelected()) {
            notificationSettingsModel.setNews_update(1);
        } else {
            notificationSettingsModel.setNews_update(0);
        }


        if (switch_push3.isSelected()) {
            notificationSettingsModel.setProfile_views(1);
        } else {
            notificationSettingsModel.setProfile_views(0);
        }


        if (switch_push4.isSelected()) {
            notificationSettingsModel.setNew_job_post(1);
        } else {
            notificationSettingsModel.setNew_job_post(0);
        }


        if (switch_push5.isSelected()) {
            notificationSettingsModel.setCalendar_view(1);
        } else {
            notificationSettingsModel.setCalendar_view(0);
        }


        if (switch_push6.isSelected()) {
            notificationSettingsModel.setPost_comments(1);
        } else {
            notificationSettingsModel.setPost_comments(0);
        }


        if (switch_push7.isSelected()) {
            notificationSettingsModel.setMeeting_reminders(1);
        } else {
            notificationSettingsModel.setMeeting_reminders(0);
        }

        if (switch_push8.isSelected()) {
            notificationSettingsModel.setSuggested_connections(1);
        } else {
            notificationSettingsModel.setSuggested_connections(0);
        }


        if (switch_push.isSelected()) {
            notificationSettingsModel.setPost_like(1);
        } else {
            notificationSettingsModel.setPost_like(0);
        }


        if (switch_push.isSelected()) {
            notificationSettingsModel.setPost_updates(1);
        } else {
            notificationSettingsModel.setPost_updates(0);
        }
        if (switch_push.isSelected()) {
            notificationSettingsModel.setConnection_requests(1);
        } else {
            notificationSettingsModel.setConnection_requests(0);
        }

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
        ).observe(this, listBaseModel -> {
            if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                hideDialog();
                Toast.makeText(getContext(), "" + listBaseModel.getMessage(), Toast.LENGTH_SHORT).show();
                GetUserDetails();
            } else {
                hideDialog();
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
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

