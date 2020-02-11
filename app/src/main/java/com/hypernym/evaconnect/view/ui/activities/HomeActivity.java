package com.hypernym.evaconnect.view.ui.activities;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.NotifyEvent;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.NotificationsAdapter;
import com.hypernym.evaconnect.view.dialogs.NavigationDialog;
import com.hypernym.evaconnect.view.ui.fragments.ConnectionsFragment;
import com.hypernym.evaconnect.view.ui.fragments.HomeFragment;
import com.hypernym.evaconnect.view.ui.fragments.MessageFragment;
import com.hypernym.evaconnect.viewmodel.HomeViewModel;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.sql.SQLException;
import java.util.ArrayList;
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

    @BindView(R.id.tv_back)
    TextView tv_back;

    @BindView(R.id.tv_pagetitle)
    ConstraintLayout titleLayout;

    NavigationDialog navigationDialog;
    private boolean notificationflag=false;
    private HomeViewModel homeViewModel;
    private NotificationsAdapter notificationsAdapter;
    private List<Post> notifications=new ArrayList<>();
    String pageTitle="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        homeViewModel = ViewModelProviders.of(this,new CustomViewModelFactory(getApplication(),this)).get(HomeViewModel.class);
        loadFragment(R.id.framelayout,new HomeFragment(),this,false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        setRecyclerView();
        getAllNotifications();
        img_home.setImageDrawable(getDrawable(R.drawable.home_selected));
        img_connections.setImageDrawable(getDrawable(R.drawable.connections));
        img_messages.setImageDrawable(getDrawable(R.drawable.messages));
        img_logout.setImageDrawable(getDrawable(R.drawable.logout));
        pageTitle=tv_pagetitle.getText().toString();
        img_uparrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideNotificationPanel();
            }
        });

        tv_pagetitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notifications.size()>0)
                {
                    showNotificationPanel();
                }
            }
        });
    }

    private void setRecyclerView() {
        notificationsAdapter=new NotificationsAdapter(this,notifications,this);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        rc_notifications.setLayoutManager(linearLayoutManager);
        rc_notifications.setAdapter(notificationsAdapter);
    }
    private void getAllNotifications() {
        if(NetworkUtils.isNetworkConnected(this))
        {
            notifications.clear();
            homeViewModel.getAllNotifications().observe(this, new Observer<BaseModel<List<Post>>>() {
                @Override
                public void onChanged(BaseModel<List<Post>> listBaseModel) {
                    if(listBaseModel !=null && !listBaseModel.isError() && listBaseModel.getData().size() >0)
                    {
                        notifications.addAll(listBaseModel.getData());
                        notificationsAdapter.notifyDataSetChanged();
                        tv_pagetitle.setText(notifications.size()+" New Notifications");
                    }
                    setNotificationCount(notifications.size());
                }
            });
        }
        else
        {
            networkErrorDialog();
        }

    }
    public void showNotificationPanel()
    {
        rc_notifications.setVisibility(View.VISIBLE);
        titleLayout.setVisibility(View.GONE);
        img_uparrow.setVisibility(View.VISIBLE);
    }
    public void hideNotificationPanel()
    {
        notifications.clear();
        tv_pagetitle.setText(pageTitle);

        if(NetworkUtils.isNetworkConnected(this))
        {

            homeViewModel.notificationMarkAsRead( LoginUtils.getLoggedinUser().getId()).observe(this, new Observer<BaseModel<List<Post>>>() {
                @Override
                public void onChanged(BaseModel<List<Post>> listBaseModel) {
                    if(listBaseModel !=null && !listBaseModel.isError() && listBaseModel.getData().size() >0) {

                    }
                }
            });
        }
        else
        {
            networkErrorDialog();
        }
        rc_notifications.setVisibility(View.GONE);
        titleLayout.setVisibility(View.VISIBLE);
        img_uparrow.setVisibility(View.GONE);
    }


    @OnClick(R.id.img_home)
    public void home()
    {
        img_home.setImageDrawable(getDrawable(R.drawable.home_selected));
        img_connections.setImageDrawable(getDrawable(R.drawable.connections));
        img_messages.setImageDrawable(getDrawable(R.drawable.messages));
        img_logout.setImageDrawable(getDrawable(R.drawable.logout));

        Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if(f instanceof HomeFragment){}
        else
        {
            loadFragment(R.id.framelayout,new HomeFragment(),this,false);
        }
        tv_back.setVisibility(View.GONE);
    }

    @OnClick(R.id.img_connections)
    public void connections()
    {
        img_home.setImageDrawable(getDrawable(R.drawable.home));
        img_connections.setImageDrawable(getDrawable(R.drawable.connection_selected));
        img_messages.setImageDrawable(getDrawable(R.drawable.messages));
        img_logout.setImageDrawable(getDrawable(R.drawable.logout));
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if(f instanceof ConnectionsFragment){}
        else
        {
           ConnectionsFragment fragment = new ConnectionsFragment();
            loadFragment(R.id.framelayout,fragment,this,false);
        }
        tv_back.setVisibility(View.GONE);
    }

    @OnClick(R.id.img_messages)
    public void messages()
    {
        img_home.setImageDrawable(getDrawable(R.drawable.home));
        img_connections.setImageDrawable(getDrawable(R.drawable.connections));
        img_messages.setImageDrawable(getDrawable(R.drawable.message_selected));
        img_logout.setImageDrawable(getDrawable(R.drawable.logout));
        MessageFragment fragment = new MessageFragment();
        loadFragment(R.id.framelayout,fragment,this,false);
    }

    @OnClick(R.id.img_logout)
    public void signout()
    {
        AppUtils.logout(this);
    }

    @OnClick(R.id.img_menu)
    public void openDrawer(View view)
    {
        navigationDialog=new NavigationDialog(this);
        navigationDialog.show();
    }

    @OnClick(R.id.tv_back)
    public void back()
    {
        super.onBackPressed();
    }
    @Override
    public void onItemClick(View view, int position) {

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
            Log.e("TAAAF", "notify");
           // Toast.makeText(this, "Hey, my message", Toast.LENGTH_SHORT).show();
            getAllNotifications();


        } catch(Exception e){

        }

    }


}
