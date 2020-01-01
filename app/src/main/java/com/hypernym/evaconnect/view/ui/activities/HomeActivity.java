package com.hypernym.evaconnect.view.ui.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.view.ui.fragments.HomeFragment;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends BaseActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    @BindView(R.id.fab_newpost)
    ExtendedFloatingActionButton fab_newpost;

    @BindView(R.id.tv_pagetitle)
    TextView tv_pagetitle;

    @BindView(R.id.drawer_layout)
    DrawerLayout drawer_layout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.img_menu)
    ImageView img_menu;

    @BindView(R.id.nav_view)
    NavigationView nav_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        init();
    }

    private void init()
    {
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        AppUtils.loadFragment(R.id.framelayout,new HomeFragment(),this);
        settingNavDrawer();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        settingNavDrawerItems();
    }

    private void settingNavDrawerItems() {
        // get menu from navigationView
//        Menu menu = nav_view.getMenu();
//
//        // find MenuItem you want to change
//        MenuItem nav_like = menu.findItem(R.id.nav_like).setActionView(R.layout.nav_menu_item);
//
//        TextView tracks = (TextView) nav_like.getActionView();
//        // set new title to the MenuItem
//        nav_like.setTitle("NewTitleForCamera");
//
//        // do the same for other MenuItems
//        MenuItem nav_gallery = menu.findItem(R.id.nav_like);
//        nav_gallery.setTitle("NewTitleForGallery");
    }

    private void settingNavDrawer() {
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer_layout.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment;
        switch (item.getItemId())
        {
            case R.id.nav_home:
                tv_pagetitle.setText(getString(R.string.home));
                fragment = new HomeFragment();
                AppUtils.loadFragment(R.id.framelayout,fragment,this);
                return true;

        }
        return false;
    }
    @OnClick(R.id.img_menu)
    public void openDrawer()
    {
        if(!drawer_layout.isDrawerOpen(Gravity.RIGHT)) drawer_layout.openDrawer(Gravity.RIGHT);
        else drawer_layout.closeDrawer(Gravity.LEFT);
    }


}
