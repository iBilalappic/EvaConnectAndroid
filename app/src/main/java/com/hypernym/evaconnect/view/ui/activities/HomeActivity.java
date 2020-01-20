package com.hypernym.evaconnect.view.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.IconPowerMenuItem;
import com.hypernym.evaconnect.models.Notification;
import com.hypernym.evaconnect.view.adapters.IconPowerMenuAdapter;
import com.hypernym.evaconnect.view.ui.fragments.ConnectionsFragment;
import com.hypernym.evaconnect.view.ui.fragments.HomeFragment;
import com.skydoves.powermenu.CustomPowerMenu;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;


import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity {

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

private boolean notificationflag=false;

    CustomPowerMenu customPowerMenu;
    private List<Notification> notifications=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        init();

//        tv_pagetitle.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // showNotificationPanel();
//            }
//        });
        img_uparrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideNotificationPanel();
            }
        });

    }

    private void init()
    {
        loadFragment(R.id.framelayout,new HomeFragment(),this,false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        initPowerMenu();
        notifications.add(new Notification());
        notifications.add(new Notification());
        notifications.add(new Notification());
        if(notifications.size()>0)
        {
            tv_pagetitle.setText(notifications.size()+" New Notifications");
            getNotifications(rc_notifications,notifications);
        }

    }
    private void showNotificationPanel()
    {
        rc_notifications.setVisibility(View.VISIBLE);
        tv_pagetitle.setVisibility(View.GONE);
        img_uparrow.setVisibility(View.VISIBLE);
    }
    private void hideNotificationPanel()
    {
        rc_notifications.setVisibility(View.GONE);
        tv_pagetitle.setVisibility(View.VISIBLE);
        img_uparrow.setVisibility(View.GONE);
    }
    private void initPowerMenu() {
      customPowerMenu = new CustomPowerMenu.Builder<>(this, new IconPowerMenuAdapter()).setHeaderView(R.layout.nav_header_main)
                .addItem(new IconPowerMenuItem(ContextCompat.getDrawable(this, R.drawable.like), "WeChat"))
                .addItem(new IconPowerMenuItem(ContextCompat.getDrawable(this, R.drawable.like), "Facebook"))
                .addItem(new IconPowerMenuItem(ContextCompat.getDrawable(this, R.drawable.like), "Twitter"))
                .addItem(new IconPowerMenuItem(ContextCompat.getDrawable(this, R.drawable.like), "Line"))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .setAnimation(MenuAnimation.SHOWUP_TOP_RIGHT)
                .setWidth(400)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .build();
    }

    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            Toast.makeText(getBaseContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
            customPowerMenu.setSelectedPosition(position); // change selected item
            customPowerMenu.dismiss();
        }
    };

    @OnClick(R.id.img_home)
    public void home()
    {
        if(notifications.size()>0)
        {
            tv_pagetitle.setText(notifications.size()+" New Notifications");
            getNotifications(rc_notifications,notifications);
        }
        else
        {
            tv_pagetitle.setText(getString(R.string.home));
        }
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
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.framelayout);
        if(f instanceof ConnectionsFragment){}
        else
        {
            tv_pagetitle.setText(getString(R.string.connections));
            ConnectionsFragment fragment = new ConnectionsFragment();
            loadFragment(R.id.framelayout,fragment,this,false);
        }
        tv_back.setVisibility(View.GONE);
    }

    @OnClick(R.id.img_messages)
    public void messages()
    {

    }

    @OnClick(R.id.img_logout)
    public void signout()
    {
        logout();
    }

    @OnClick(R.id.img_menu)
    public void openDrawer(View view)
    {
       //customPowerMenu.showAsDropDown(view);
    }
    @OnClick(R.id.tv_back)
    public void back()
    {
        super.onBackPressed();

    }


}
