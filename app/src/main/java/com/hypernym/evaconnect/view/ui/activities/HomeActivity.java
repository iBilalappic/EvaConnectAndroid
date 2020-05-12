package com.hypernym.evaconnect.view.ui.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.NotifyEvent;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.utils.PrefUtils;
import com.hypernym.evaconnect.view.adapters.NotificationsAdapter;
import com.hypernym.evaconnect.view.dialogs.NavigationDialog;
import com.hypernym.evaconnect.view.ui.fragments.BaseFragment;
import com.hypernym.evaconnect.view.ui.fragments.ConnectionsFragment;
import com.hypernym.evaconnect.view.ui.fragments.EditProfileFragment;
import com.hypernym.evaconnect.view.ui.fragments.EventDetailFragment;
import com.hypernym.evaconnect.view.ui.fragments.HomeFragment;
import com.hypernym.evaconnect.view.ui.fragments.MessageFragment;
import com.hypernym.evaconnect.view.ui.fragments.MyLikesFragment;
import com.hypernym.evaconnect.view.ui.fragments.NotificationsFragment;
import com.hypernym.evaconnect.view.ui.fragments.PersonDetailFragment;
import com.hypernym.evaconnect.view.ui.fragments.PostDetailsFragment;
import com.hypernym.evaconnect.view.ui.fragments.SpecficJobFragment;
import com.hypernym.evaconnect.viewmodel.HomeViewModel;
import com.onesignal.OneSignal;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements NotificationsAdapter.OnItemClickListener {

    @BindView(R.id.tv_title)
    TextView tv_pagetitle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.img_menu)
    ImageView img_menu;

    @BindView(R.id.toolbarlayout)
    LinearLayout toolbarlayout;

    @BindView(R.id.img_home)
    ImageView img_home;

    @BindView(R.id.img_connections)
    ImageView img_connections;

    @BindView(R.id.img_messages)
    ImageView img_messages;

    @BindView(R.id.img_logout)
    ImageView img_logout;

    @BindView(R.id.rc_notifications)
    RecyclerView rc_notifications;

    @BindView(R.id.img_uparrow)
    ImageView img_uparrow;

    @BindView(R.id.img_reddot)
    ImageView img_reddot;

    @BindView(R.id.tv_back)
    TextView tv_back;

    @BindView(R.id.tv_pagetitle)
    ConstraintLayout titleLayout;

    @BindView(R.id.badge_notification)
    TextView badge_notification;

    NavigationDialog navigationDialog;
    private boolean notificationflag = false;
    private HomeViewModel homeViewModel;
    private NotificationsAdapter notificationsAdapter;
    private List<Post> notifications = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        if (LoginUtils.isUserLogin()) {
            Intent intent = getIntent();
            String action = intent.getAction();
            Uri data = intent.getData();

            String url = null;
            if (data != null) {
                url = data.toString();
                String[] separated = url.split("/");
                String type = separated[3];
                int id = Integer.parseInt(separated[4]);
                Log.d("TAAG", "" + id);
                if (type != null && type.equals("event")) {

                    EventDetailFragment eventDetailFragment = new EventDetailFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", id);
                    eventDetailFragment.setArguments(bundle);
                    loadFragment(R.id.framelayout, eventDetailFragment, this, true);

                } else if (type != null && type.equals("post")) {
                    PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("post", id);
                    postDetailsFragment.setArguments(bundle);
                    loadFragment(R.id.framelayout, postDetailsFragment, this, true);

                } else if (type != null && type.equals("job")) {
                    SpecficJobFragment specficJobFragment = new SpecficJobFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("job_id", id);
                    specficJobFragment.setArguments(bundle);
                    loadFragment(R.id.framelayout, specficJobFragment, this, true);

                }
            }
            init();
        } else {
            Toast.makeText(this, "Please login first from Aviation Connect", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    private void init() {
        homeViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getApplication(), this)).get(HomeViewModel.class);
        if (getIntent().getExtras() != null) {
            String fragment_name = getIntent().getStringExtra(Constants.FRAGMENT_NAME);
            String fragmentName = getIntent().getStringExtra(Constants.FRAGMENT_NAME);
            Bundle bundle = getIntent().getBundleExtra(Constants.DATA);
            if (!TextUtils.isEmpty(fragmentName)) {
                Fragment fragment = Fragment.instantiate(this, fragmentName);
                if (bundle != null)
                    fragment.setArguments(bundle);
                loadFragment(R.id.framelayout, fragment, this, false);
            }
        } else {
            loadFragment(R.id.framelayout, new HomeFragment(), this, false);
            //  getAllNotifications();
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        setRecyclerView();


        // img_home.setImageDrawable(getDrawable(R.drawable.home_selected));
        img_home.setColorFilter(ContextCompat.getColor(this, R.color.skyblue));
        img_connections.setImageDrawable(getDrawable(R.drawable.ic_connection_1));
        img_messages.setImageDrawable(getDrawable(R.drawable.ic_message_1));
        img_logout.setImageDrawable(getDrawable(R.drawable.ic_profile_1));


        img_uparrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideNotificationPanel();
                img_reddot.setVisibility(View.GONE);
            }
        });

        tv_pagetitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notifications.size() > 0) {
                    showNotificationPanel();
                }
            }
        });
    }

    private void setMessageNotificationCount() {
        int count = PrefUtils.getMessageCount(getApplicationContext());
        badge_notification.setText(String.valueOf(count));
        badge_notification.setVisibility(View.VISIBLE);
    }

    private void setRecyclerView() {
        notificationsAdapter = new NotificationsAdapter(this, notifications, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rc_notifications.setLayoutManager(linearLayoutManager);
        rc_notifications.setAdapter(notificationsAdapter);
    }

    private void getAllNotifications() {
        if (NetworkUtils.isNetworkConnected(this)) {

            homeViewModel.getAllUnReadNotifications().observe(this, new Observer<BaseModel<List<Post>>>() {
                @Override
                public void onChanged(BaseModel<List<Post>> listBaseModel) {
                    if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {
                        notifications.clear();
                        notificationsAdapter.notifyDataSetChanged();
                        notifications.addAll(listBaseModel.getData());
                        //   Collections.reverse(notifications);
                        notificationsAdapter.notifyDataSetChanged();
                        tv_pagetitle.setText(notifications.size() + " New Notifications");
                        img_reddot.setVisibility(View.VISIBLE);
                    }
                    setNotificationCount(notifications.size());
                }
            });
        } else {
            networkErrorDialog();
        }

    }

    public void showNotificationPanel() {
        rc_notifications.setVisibility(View.VISIBLE);
        titleLayout.setVisibility(View.GONE);
        img_uparrow.setVisibility(View.VISIBLE);
    }

    public void hideNotificationPanel() {
        List<Post> postNotifications = new ArrayList();
        if (NetworkUtils.isNetworkConnected(this)) {

            homeViewModel.notificationMarkAsRead(LoginUtils.getLoggedinUser().getId()).observe(this, new Observer<BaseModel<List<Post>>>() {
                @Override
                public void onChanged(BaseModel<List<Post>> listBaseModel) {
                    if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {
                        postNotifications.addAll(listBaseModel.getData());
                        notifications = postNotifications;
                        notificationsAdapter.notifyDataSetChanged();
                    }
                }
            });
        } else {
            networkErrorDialog();
        }
        rc_notifications.setVisibility(View.GONE);
        titleLayout.setVisibility(View.VISIBLE);
        img_uparrow.setVisibility(View.GONE);
        img_reddot.setVisibility(View.GONE);
        tv_pagetitle.setText(BaseFragment.pageTitle);
    }


    @OnClick(R.id.img_home)
    public void home() {
        img_home.setColorFilter(ContextCompat.getColor(this, R.color.skyblue));
        img_connections.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_messages.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_logout.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));

//        img_connections.setImageDrawable(getDrawable(R.drawable.connections));
//        img_messages.setImageDrawable(getDrawable(R.drawable.messages));
//        img_logout.setImageDrawable(getDrawable(R.drawable.logout));

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if (f instanceof HomeFragment) {
        } else {
            loadFragment(R.id.framelayout, new HomeFragment(), this, false);
        }
        tv_back.setVisibility(View.GONE);
        BaseFragment.pageTitle = getString(R.string.home);

    }

    @OnClick(R.id.img_connections)
    public void connections() {

        img_home.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_connections.setColorFilter(ContextCompat.getColor(this, R.color.skyblue));
        img_messages.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_logout.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if (f instanceof ConnectionsFragment) {
        } else {
            ConnectionsFragment fragment = new ConnectionsFragment();
            loadFragment(R.id.framelayout, fragment, this, true);
        }
        tv_back.setVisibility(View.GONE);
        BaseFragment.pageTitle = getString(R.string.connections);
    }

    @OnClick(R.id.img_messages)
    public void messages() {

        img_home.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_connections.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_messages.setColorFilter(ContextCompat.getColor(this, R.color.skyblue));
        img_logout.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        PrefUtils.saveMessageCount(getApplicationContext(), 0);
        badge_notification.setVisibility(View.GONE);
        MessageFragment fragment = new MessageFragment();
        loadFragment(R.id.framelayout, fragment, this, true);
        BaseFragment.pageTitle = getString(R.string.messages);
    }

    @OnClick(R.id.img_logout)
    public void signout() {
       // AppUtils.logout(this);
        img_home.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_connections.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_messages.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_logout.setColorFilter(ContextCompat.getColor(this, R.color.skyblue));
        EditProfileFragment fragment = new EditProfileFragment();
        loadFragment(R.id.framelayout, fragment, this, true);
        BaseFragment.pageTitle = "Profile";

    }

    @OnClick(R.id.toolbarlayout)
    public void openDrawer(View view) {
        navigationDialog = new NavigationDialog(this);
        navigationDialog.show();
    }

    @OnClick(R.id.tv_back)
    public void back() {
        super.onBackPressed();
    }

    @Override
    public void onItemClick(View view, int position) {
        if (notifications.get(position).getObject_type().equals("job")) {
            SpecficJobFragment specficJobFragment = new SpecficJobFragment();
            Bundle bundle = new Bundle();
//            JobAd jobAd = new JobAd();
//            jobAd.setId(notifications.get(position).getObject_id());
            bundle.putInt("job_id", notifications.get(position).getObject_id());
            specficJobFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, specficJobFragment, this, true);
            hideNotificationPanel();
        } else if (notifications.get(position).getObject_type().equals("post")) {
            PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("post", notifications.get(position).getObject_id());
            postDetailsFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, postDetailsFragment, this, true);
            hideNotificationPanel();
        } else if (notifications.get(position).getObject_type().equals("event")) {
            EventDetailFragment eventDetailsFragment = new EventDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", notifications.get(position).getObject_id());
            eventDetailsFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, eventDetailsFragment, this, true);
            hideNotificationPanel();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
        getAllNotifications();

        //  hideNotificationPanel();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {

        super.onPause();
    }


    @Subscribe
    public void onEvent(NotifyEvent event) {

        try {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    // Stuff that updates the UI
                    if (event.getMessage().equalsIgnoreCase("notifcation")) {
                        Log.e("TAAAF", "notify");
                        // Toast.makeText(this, "Hey, my message", Toast.LENGTH_SHORT).show();
                        getAllNotifications();
                    } else {
                        Log.e("TAAAF", "message notification");
                        setMessageNotificationCount();
                    }
                }
            });


        } catch (Exception e) {
            Log.e("TAAAF", "exception" + e.getMessage());
        }

    }

    @Override
    public void onBackPressed() {

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if (fragment instanceof HomeFragment) {

            finish();

        } else if (fragment instanceof MyLikesFragment || fragment instanceof ConnectionsFragment || fragment instanceof EditProfileFragment || fragment instanceof NotificationsFragment || fragment instanceof MessageFragment) {
//            img_home.setImageDrawable(getDrawable(R.drawable.home_selected));
//            img_connections.setImageDrawable(getDrawable(R.drawable.connections));
//            img_messages.setImageDrawable(getDrawable(R.drawable.messages));
//            img_logout.setImageDrawable(getDrawable(R.drawable.logout));

            img_home.setColorFilter(ContextCompat.getColor(this, R.color.skyblue));
            img_connections.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
            img_messages.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
            img_logout.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));


            super.onBackPressed();
        } else {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            if (findViewById(R.id.tv_back) != null)
                findViewById(R.id.tv_back).setVisibility(View.GONE);
            super.onBackPressed();
        }
    }
}
