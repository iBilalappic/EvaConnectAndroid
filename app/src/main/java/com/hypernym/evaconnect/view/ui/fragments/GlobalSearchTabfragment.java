package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.ArrayList;
import java.util.List;

public class GlobalSearchTabfragment extends BaseFragment implements View.OnClickListener {

    EditText edt_search;

    User user = new User();
    TabLayout tabs;

    public GlobalSearchTabfragment() {
        // Required empty public constructor
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        user = LoginUtils.getUser();
        // Setting ViewPager for each Tabs
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        edt_search = (EditText) view.findViewById(R.id.edt_search);
        tabs = view.findViewById(R.id.tabLayout2);
        setupViewPager(viewPager);
        // Set Tabs inside Toolbar


        tabs.setupWithViewPager(viewPager);
        edt_search.addTextChangedListener(new GlobalSearchTabfragment.TextWatcher());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_global_search_tab, container, false);
    }

    @Override
    public void onClick(View v) {

    }

    // Add Fragments to Tabs
    private void setupViewPager(ViewPager viewPager) {


        GlobalSearchTabfragment.Adapter adapter = new GlobalSearchTabfragment.Adapter(getChildFragmentManager());


        if (user != null && user.getType().equals("company")) {
            adapter.addFragment(new GlobalPostFragment(), "Posts");
            adapter.addFragment(new GlobalConnectionsFragment(), "Followers");
            adapter.addFragment(new GlobalEventFragment(), "Events");
            tabs.setTabMode(TabLayout.MODE_FIXED);

        } else {
            adapter.addFragment(new GlobalPostFragment(), "Posts");
            adapter.addFragment(new GlobalConnectionsFragment(), "Connections");
            adapter.addFragment(new GlobalConnectionsFragment(), "Companies");
            adapter.addFragment(new GlobalNewsFragment(), "News");
            adapter.addFragment(new GlobalEventFragment(), "Events");

            tabs.setTabMode(TabLayout.MODE_SCROLLABLE);


        }

        viewPager.setAdapter(adapter);


    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public Adapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


    public class TextWatcher implements android.text.TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {
            //  currentPage = PAGE_START;
                mEventBus.post(s.toString());


        }

    }


}