package com.hypernym.evaconnect.view.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.NotifyEvent;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.PrefUtils;
import com.hypernym.evaconnect.view.adapters.NotificationsAdapter;
import com.hypernym.evaconnect.view.dialogs.NavigationDialog;
import com.hypernym.evaconnect.view.dialogs.SearchDialog;
import com.hypernym.evaconnect.view.ui.fragments.ActivityFragment;
import com.hypernym.evaconnect.view.ui.fragments.BaseFragment;
import com.hypernym.evaconnect.view.ui.fragments.ChatFragment;
import com.hypernym.evaconnect.view.ui.fragments.ConnectionsFragment;
import com.hypernym.evaconnect.view.ui.fragments.ConnectionsTabFragment;
import com.hypernym.evaconnect.view.ui.fragments.EditProfileFragment;
import com.hypernym.evaconnect.view.ui.fragments.EventDetailFragment;
import com.hypernym.evaconnect.view.ui.fragments.HomeFragment;
import com.hypernym.evaconnect.view.ui.fragments.MainViewPagerFragment;
import com.hypernym.evaconnect.view.ui.fragments.MessageFragment;
import com.hypernym.evaconnect.view.ui.fragments.MyLikesFragment;
import com.hypernym.evaconnect.view.ui.fragments.NewPostFragment;
import com.hypernym.evaconnect.view.ui.fragments.NotificationsFragment;
import com.hypernym.evaconnect.view.ui.fragments.PersonProfileFragment;
import com.hypernym.evaconnect.view.ui.fragments.PostDetailsFragment;
import com.hypernym.evaconnect.view.ui.fragments.SearchResultFragment;
import com.hypernym.evaconnect.view.ui.fragments.SpecficJobFragment;
import com.hypernym.evaconnect.viewmodel.HomeViewModel;
import com.hypernym.evaconnect.viewmodel.UserViewModel;
import com.onesignal.OneSignal;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tv_pagetitle;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.img_menu)
    ImageView img_menu;

    @BindView(R.id.toolbarlayout)
    LinearLayout toolbarlayout;

    @BindView(R.id.toolbarSearch)
    LinearLayout toolbarSearch;


    @BindView(R.id.img_home)
    ImageView img_home;

    @BindView(R.id.home_click)
    LinearLayout home_click;


    @BindView(R.id.img_connections)
    ImageView img_connections;

    @BindView(R.id.connections_click)
    LinearLayout connections_click;

    @BindView(R.id.img_messages)
    ImageView img_messages;
    @BindView(R.id.img_post)
    ImageView img_post;



    @BindView(R.id.tv_post)
    TextView tv_post;

    @BindView(R.id.message_click)
    LinearLayout message_click;

    @BindView(R.id.img_logout)
    ImageView img_logout;

    @BindView(R.id.tv_home)
    TextView tv_home;

    @BindView(R.id.tv_connections)
    TextView tv_connections;

    @BindView(R.id.tv_message)
    TextView tv_message;

    @BindView(R.id.tv_profile)
    TextView tv_profile;

//    @BindView(R.id.tv_back)
//    ConstraintLayout tv_back;

    @BindView(R.id.badge_notification)
    TextView badge_notification;

    @BindView(R.id.home_selector)
    ImageView home_selector;

    @BindView(R.id.post_selector)
    ImageView post_selector;

    @BindView(R.id.messages_selector)
    ImageView messages_selector;

    @BindView(R.id.connections_selector)
    ImageView connections_selector;

    @BindView(R.id.profile_selector)
    ImageView profile_selector;

    NavigationDialog navigationDialog;
    SearchDialog searchDialog;
    private boolean notificationflag = false;
    private HomeViewModel homeViewModel;
    private NotificationsAdapter notificationsAdapter;
    private List<Post> notifications = new ArrayList<>();
    UserViewModel userViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        home_selector.setImageResource(R.drawable.bottomline);
        createUserOnFirebase();


        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getApplication(), this)).get(UserViewModel.class);
        if(getIntent().getExtras()!=null && getIntent().getExtras().getString("chat_room_id")!=null)
        {
            img_home.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
            img_connections.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
            img_messages.setColorFilter(ContextCompat.getColor(this, R.color.skyblue));
            img_logout.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));

            tv_home.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
            tv_connections.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
            tv_message.setTextColor(ContextCompat.getColor(this, R.color.skyblue));
            tv_profile.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
            ChatFragment chatFragment = new ChatFragment();
            Bundle bundle = new Bundle();
            bundle.putString("chat_room_id", getIntent().getExtras().getString("chat_room_id"));
            chatFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, chatFragment, this, false);
        }
        else
        {
            // img_home.setImageDrawable(getDrawable(R.drawable.home_selected));
            img_home.setColorFilter(ContextCompat.getColor(this, R.color.skyblue));
            tv_home.setTextColor(ContextCompat.getColor(this, R.color.skyblue));
            img_connections.setImageDrawable(getDrawable(R.drawable.ic_connection_1));
            img_messages.setImageDrawable(getDrawable(R.drawable.ic_message_1));
            img_logout.setImageDrawable(getDrawable(R.drawable.ic_profile_1));
        }

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
        if (getIntent().getExtras() != null && (getIntent().getExtras().getString("chat_room_id")!=null ||
                getIntent().getBundleExtra(Constants.DATA)!=null)) {
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

            loadFragment(R.id.framelayout, new MainViewPagerFragment(), this, false);
            //  getAllNotifications();
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);





        tv_pagetitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notifications.size() > 0) {
                    //  showNotificationPanel();
                }
            }
        });


    }

    private void setUserOnline() {
        Log.d("APPLICATION","APPLICATION ONLINE");
        userViewModel.userOnline(true).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {

            }
        });
    }



    private void setMessageNotificationCount() {
        int count = PrefUtils.getMessageCount(getApplicationContext());
        badge_notification.setText(String.valueOf(count));
        badge_notification.setVisibility(View.GONE);
    }
//
//    NewPostFragment newPostFragment=new NewPostFragment();
//    Bundle bundle=new Bundle();
//                    bundle.putBoolean("isVideo",true);
//                    newPostFragment.setArguments(bundle);
//    loadFragment(R.id.framelayout, newPostFragment, getContext(), true);

    @OnClick(R.id.post_click)
    public void post() {

        post_selector.setImageResource(R.drawable.bottomline);
        home_selector.setImageResource(0);
        connections_selector.setImageResource(0);
        messages_selector.setImageResource(0);
        profile_selector.setImageResource(0);

        img_post.setColorFilter(ContextCompat.getColor(this, R.color.skyblue));
        tv_post.setTextColor(ContextCompat.getColor(this, R.color.skyblue));
        img_home.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        tv_home.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        tv_connections.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        tv_message.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        tv_profile.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        img_connections.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_messages.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_logout.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));

        NewPostFragment newPostFragment=new NewPostFragment();
        Bundle bundle=new Bundle();
        bundle.putBoolean("isVideo",true);
        newPostFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, newPostFragment, this, true);

    }

    @OnClick(R.id.home_click)
    public void home() {

        home_selector.setImageResource(R.drawable.bottomline);
        connections_selector.setImageResource(0);
        messages_selector.setImageResource(0);
        profile_selector.setImageResource(0);

        img_home.setColorFilter(ContextCompat.getColor(this, R.color.skyblue));
        tv_home.setTextColor(ContextCompat.getColor(this, R.color.skyblue));
        tv_connections.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        tv_message.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        tv_profile.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        img_connections.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_messages.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_logout.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));

//        img_connections.setImageDrawable(getDrawable(R.drawable.connections));
//        img_messages.setImageDrawable(getDrawable(R.drawable.messages));
//        img_logout.setImageDrawable(getDrawable(R.drawable.logout));

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if (f instanceof MainViewPagerFragment) {
        } else {
            loadFragment(R.id.framelayout, new MainViewPagerFragment(), this, false);
        }
        //   tv_back.setVisibility(View.GONE);

    }

    @OnClick(R.id.connections_click)
    public void connections() {

        home_selector.setImageResource(0);
        connections_selector.setImageResource(R.drawable.bottomline);
        messages_selector.setImageResource(0);
        profile_selector.setImageResource(0);


        img_home.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_connections.setColorFilter(ContextCompat.getColor(this, R.color.skyblue));
        img_messages.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_logout.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_post.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));

        tv_home.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        tv_connections.setTextColor(ContextCompat.getColor(this, R.color.skyblue));
        tv_message.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        tv_profile.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        tv_post.setTextColor(ContextCompat.getColor(this, R.color.gray_1));

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if (f instanceof ConnectionsTabFragment) {
        } else {
            ConnectionsTabFragment fragment = new ConnectionsTabFragment();
            loadFragment(R.id.framelayout, fragment, this, true);
        }
        //   tv_back.setVisibility(View.GONE);
        BaseFragment.pageTitle = getString(R.string.connections);
    }

    @OnClick(R.id.message_click)
    public void messages() {

        home_selector.setImageResource(0);
        connections_selector.setImageResource(0);
        messages_selector.setImageResource(R.drawable.bottomline);
        profile_selector.setImageResource(0);


        img_home.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_connections.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_messages.setColorFilter(ContextCompat.getColor(this, R.color.skyblue));
        img_logout.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_post.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));

        tv_home.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        tv_connections.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        tv_message.setTextColor(ContextCompat.getColor(this, R.color.skyblue));
        tv_profile.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        tv_post.setTextColor(ContextCompat.getColor(this, R.color.gray_1));


        Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if (f instanceof ActivityFragment) {
        } else {
            PrefUtils.saveMessageCount(getApplicationContext(), 0);
            badge_notification.setVisibility(View.GONE);
            ActivityFragment fragment = new ActivityFragment();
            loadFragment(R.id.framelayout, fragment, this, true);
            BaseFragment.pageTitle = getString(R.string.messages);
        }



    }

    @OnClick(R.id.profile_click)
    public void signout() {
        // AppUtils.logout(this);


        home_selector.setImageResource(0);
        connections_selector.setImageResource(0);
        messages_selector.setImageResource(0);
        profile_selector.setImageResource(R.drawable.bottomline);

        img_home.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_connections.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_messages.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
        img_logout.setColorFilter(ContextCompat.getColor(this, R.color.skyblue));
        img_post.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));

        tv_home.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        tv_connections.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        tv_message.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        tv_post.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
        tv_profile.setTextColor(ContextCompat.getColor(this, R.color.skyblue));


        Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if (f instanceof PersonProfileFragment) {
        } else {
            PersonProfileFragment fragment = new PersonProfileFragment();
            loadFragment(R.id.framelayout, fragment, this, true);
        }


        //  BaseFragment.pageTitle = "Profile";

    }

    @OnClick(R.id.toolbarlayout)
    public void openDrawer(View view) {
        navigationDialog = new NavigationDialog(this);
        navigationDialog.show();
    }
    public void createUserOnFirebase()
    {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            //To do//
                            return;
                        }
                        // Get the Instance ID token//
                        String token = task.getResult().getToken();

                        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference userRef = databaseReference.child(AppConstants.FIREASE_USER_ENDPOINT);
                        DatabaseReference userRefByID=userRef.child(LoginUtils.getLoggedinUser().getId().toString());
                        userRefByID.child("imageName").setValue(LoginUtils.getLoggedinUser().getUser_image());
                        userRefByID.child("name").setValue(LoginUtils.getLoggedinUser().getEmail());
                        userRefByID.child("fcm-token").setValue(token);
                    }
                });

    }

    @OnClick(R.id.toolbarSearch)
    public void openDialog(View view) {
       /* searchDialog = new SearchDialog(this);
        searchDialog.show();*/
        SearchResultFragment searchResultFragment = new SearchResultFragment();
        FragmentTransaction transaction = ( this).getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.framelayout, searchResultFragment);
        transaction.addToBackStack(null);
/*        Bundle bundle = new Bundle();
        bundle.putString(Constants.SEARCH,edt_keyword.getText().toString());
        searchResultFragment.setArguments(bundle);*/
        transaction.commit();

    }
    //
    @OnClick(R.id.tv_back)
    public void back() {
        super.onBackPressed();
//        findViewById(R.id.seprator_line).setVisibility(View.VISIBLE);
//        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
//            Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameLayout);
//            if (f instanceof MainViewPagerFragment || f instanceof ActivityFragment) {
//                // Do something
//                findViewById(R.id.seprator_line).setVisibility(View.GONE);
//            }
//        }
    }


    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        setUserOnline();
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



            HashMap<String,Object> body=new HashMap<>();

            body.put("modified_by_id",LoginUtils.getLoggedinUser().getId());
            body.put("last_online_datetime", DateUtils.GetCurrentdatetime());
            body.put("is_online",false);
            RestClient.get().appApi().userOnline(LoginUtils.getLoggedinUser().getId(),body).enqueue(new Callback<BaseModel<List<User>>>() {
                @Override
                public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {
                    finish();
                }

                @Override
                public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {
                    finish();
                }
            });




        } else if (fragment instanceof MyLikesFragment || fragment instanceof ConnectionsTabFragment || fragment instanceof EditProfileFragment || fragment instanceof NotificationsFragment || fragment instanceof MessageFragment
        || fragment instanceof NewPostFragment) {
//            img_home.setImageDrawable(getDrawable(R.drawable.home_selected));
//            img_connections.setImageDrawable(getDrawable(R.drawable.connections));
//            img_messages.setImageDrawable(getDrawable(R.drawable.messages));
//            img_logout.setImageDrawable(getDrawable(R.drawable.logout));

            img_home.setColorFilter(ContextCompat.getColor(this, R.color.skyblue));
            img_connections.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
            img_messages.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
            img_logout.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));
            img_post.setColorFilter(ContextCompat.getColor(this, R.color.gray_1));

            tv_home.setTextColor(ContextCompat.getColor(this, R.color.skyblue));
            tv_connections.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
            tv_message.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
            tv_profile.setTextColor(ContextCompat.getColor(this, R.color.gray_1));
            tv_post.setTextColor(ContextCompat.getColor(this, R.color.gray_1));


            super.onBackPressed();
        } else {
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            if (findViewById(R.id.tv_back) != null)
                findViewById(R.id.tv_back).setVisibility(View.GONE);
            super.onBackPressed();
        }
    }
}
