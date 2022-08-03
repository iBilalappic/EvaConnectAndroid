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
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.NotificationsSettingsModelNew;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
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
    Switch switch_message;


    @BindView(R.id.switch_push1)
    Switch switch_post_comment;


    @BindView(R.id.switch_push2)
    Switch switch_post_like;


    @BindView(R.id.switch_push3)
    Switch switch_connection_request;


    @BindView(R.id.switch_push4)
    Switch switch_new_connections;


    @BindView(R.id.switch_push5)
    Switch switch_profile_view;


    @BindView(R.id.switch_push6)
    Switch switch_suggested_connections;


    @BindView(R.id.switch_push7)
    Switch switch_news_events;


    @BindView(R.id.switch_push8)
    Switch switch_job_post;


    @BindView(R.id.switch_push9)
    Switch btn_news_update;


    @BindView(R.id.switch_push10)
    Switch btn_company_post_update;


    @BindView(R.id.switch_push11)
    Switch btn_calender_reminder;


    @BindView(R.id.switch_push12)
    Switch btn_meeting_reminder;


    @BindView(R.id.btn_submit)
    TextView btn_submit;

  /*  @BindView(R.id.profile_image)
    CircleImageView cv_profile_image;*/


    User user = new User();
    private NotificationsSettingsModelNew notificationsSettingsModelNew;

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
//        switch_push.setOnClickListener(this);
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

        userViewModel.hGetNotificationSettings(LoginUtils.getUser().getId()).observe(requireActivity(), listBaseModel -> {
            if (listBaseModel.getData() != null) {
                Log.d("notification_check", "GET Request"+ GsonUtils.getGson().toJson(listBaseModel));

                notificationsSettingsModelNew = listBaseModel;

                hPrePopulateNotifications(listBaseModel);

            }
        });
    }

    private void hPrePopulateNotifications(NotificationsSettingsModelNew hNotificationSettingModel) {


        User user=LoginUtils.getLoggedinUser();
        if (user.getType().equalsIgnoreCase("company")) {
            switch_news_events.setVisibility(View.GONE);
            btn_news_update.setVisibility(View.GONE);
            switch_job_post.setVisibility(View.GONE);

        }else{
            switch_news_events.setVisibility(View.VISIBLE);
            btn_news_update.setVisibility(View.VISIBLE);
            switch_job_post.setVisibility(View.VISIBLE);
        }

//        notificationsSettingsModelNew = new NotificationsSettingsModelNew(hNotificationSettingModel);
        switch_message.setChecked(hNotificationSettingModel.getData().get(0).getNotifications().getMessage() != 0);
        switch_post_like.setChecked(hNotificationSettingModel.getData().get(0).getNotifications().getPostLikes() != 0);
        switch_post_comment.setChecked(hNotificationSettingModel.getData().get(0).getNotifications().getPostComments() != 0);
        switch_connection_request.setChecked(hNotificationSettingModel.getData().get(0).getNotifications().getConnectionRequests() != 0);
//        switch_new_connections.setChecked(hNotificationSettingModel.getData().get(0).getNotifications().get() != 0);
        switch_profile_view.setChecked(hNotificationSettingModel.getData().get(0).getNotifications().getProfileViews() != 0);
        switch_suggested_connections.setChecked(hNotificationSettingModel.getData().get(0).getNotifications().getSuggestedConnections() != 0);


        btn_company_post_update.setChecked(hNotificationSettingModel.getData().get(0).getNotifications().getPostComments() != 0);
        btn_calender_reminder.setChecked(hNotificationSettingModel.getData().get(0).getNotifications().getCalendarReminder() != 0);
        btn_meeting_reminder.setChecked(hNotificationSettingModel.getData().get(0).getNotifications().getMeetingReminders() != 0);



    }

    private void hInitClickListener() {

        btn_submit.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                btn_submit.setOnClickListener(new OnOneOffClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        NotificationsSettingsModelNew notificationSettingsModel = new NotificationsSettingsModelNew();

                        hPopulateSettingModel(notificationSettingsModel);


                        hPostSettingData(notificationSettingsModel);

                    }
                });
            }
        });


    }

    private void hPostSettingData(NotificationsSettingsModelNew notificationSettingsModel) {
        showDialog();

        userViewModel.hPostUserSettingsData(notificationSettingsModel).observe(getViewLifecycleOwner(), new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> listBaseModel) {

                if (listBaseModel != null && !listBaseModel.isError()) {

                    hideDialog();

                    onBackPressed();

                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));

                }
            }
        });

        hideDialog();


    }

    private void hPopulateSettingModel(NotificationsSettingsModelNew notificationSettingsModel) {


      /*  if (switch_push.isSelected()) {
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


        if (switch_suggection_connections.isSelected()) {
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
        }*/

    }


    private void GetUserDetails() {
        showDialog();
        userViewModel.getuser_details(user.getId(), false).observe(this, listBaseModel -> {
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
                    switch_message.setChecked(true);
                } else {
                    switch_message.setChecked(false);
                }

                LoginUtils.saveUser(listBaseModel.getData().get(0));
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
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

