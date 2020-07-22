package com.hypernym.evaconnect.view.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.PopupMenu;

import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.hypernym.evaconnect.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainViewPagerFragment extends BaseFragment implements FloatingActionButton.OnClickListener {
    private ViewPager mViewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    Context mContext;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_viewpager, container, false);
        ButterKnife.bind(this,view);

        hideBackButton();
        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(sectionsPagerAdapter);
        mViewPager.setCurrentItem(2);
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(mViewPager);
        fab.setOnClickListener(this);
        return view;
    }



    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                     return new PostFragment();
                case 1:
                     return new EventFragment();
                case 2:
                    return new HomeFragment();
                case 3:
                    return new JobFragment();
                case 4:
                    return new NewsFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Posts";
                case 1:
                    return "Events";
                case 2:
                    return "Everything";
                case 3:
                    return "Jobs";
                case 4:
                    return "News";
            }
            return null;
        }

    }
    @Override
    public void onClick(View v) {
        final OvershootInterpolator interpolator = new OvershootInterpolator();
        ViewCompat.animate(fab).
                rotation(135f).
                withLayer().
                setDuration(300).
                setInterpolator(interpolator).
                start();
        /** Instantiating PopupMenu class */
        PopupMenu popup = new PopupMenu(getContext(), v);

        /** Adding menu items to the popumenu */
        popup.getMenuInflater().inflate(R.menu.dashboard_menu, popup.getMenu());

        popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
                ViewCompat.animate(fab).
                        rotation(0f).
                        withLayer().
                        setDuration(300).
                        setInterpolator(interpolator).
                        start();
            }
        });
        /** Defining menu item click listener for the popup menu */
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //    Toast.makeText(getContext(), item.getGroupId()+"You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.menu1))) {
                    loadFragment(R.id.framelayout, new NewPostFragment(), getContext(), true);
                }  else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.menu2))) {
                    ShareVideoFragment shareVideoFragment=new ShareVideoFragment();
                    Bundle bundle=new Bundle();
                    bundle.putBoolean("isVideo",true);
                    shareVideoFragment.setArguments(bundle);
                    loadFragment(R.id.framelayout, shareVideoFragment, getContext(), true);
                }
                else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.menu3))) {
                    loadFragment(R.id.framelayout, new CreateEventFragment(), getContext(), true);
                } else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.menu4))) {
                    ShareVideoFragment shareVideoFragment=new ShareVideoFragment();
                    Bundle bundle=new Bundle();
                    bundle.putBoolean("isVideo",false);
                    shareVideoFragment.setArguments(bundle);
                    loadFragment(R.id.framelayout, shareVideoFragment, getContext(), true);
                }
                return true;
            }
        });

        /** Showing the popup menu */
        popup.show();

    }
}
