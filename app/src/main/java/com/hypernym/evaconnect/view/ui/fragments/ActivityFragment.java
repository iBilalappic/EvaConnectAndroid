package com.hypernym.evaconnect.view.ui.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.view.adapters.ActivityAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivityFragment extends BaseFragment {

    @BindView(R.id.tabLayout)
    TabLayout tabLayout;

    @BindView(R.id.viewPager)
    ViewPager viewPager;


    public ActivityFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_activity, container, false);
        ButterKnife.bind(this,view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        tabLayout.addTab(tabLayout.newTab().setText("Messages"));
        tabLayout.addTab(tabLayout.newTab().setText("Notifications"));
        final ActivityAdapter adapter = new ActivityAdapter(getContext(),getFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setCurrentItem(0);
       if(getArguments() !=null && getArguments().getBoolean("isActivity"))
       {
           viewPager.setCurrentItem(1);
       }


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}
