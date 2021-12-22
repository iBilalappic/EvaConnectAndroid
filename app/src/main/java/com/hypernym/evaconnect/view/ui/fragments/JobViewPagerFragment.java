package com.hypernym.evaconnect.view.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.hypernym.evaconnect.R;
import butterknife.ButterKnife;

public class JobViewPagerFragment extends BaseFragment {
    private ViewPager mViewPager;
    private JobViewPagerFragment.SectionsPagerAdapter sectionsPagerAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job_view_pager, container, false);
        ButterKnife.bind(this,view);
        requireActivity().findViewById(R.id.seprator_line).setVisibility(View.GONE);
        hideBackButton();
        sectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(sectionsPagerAdapter);
        mViewPager.setCurrentItem(0);
        TabLayout tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(mViewPager);



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
                    return new JobFragment();
                case 1:
                    return new JobIndustryFragment();
                case 2:
                    return new JobSavedFragment();
                case 3:
                    return new JobAppliedFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
                return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All";
                case 1:
                    return "Industry";
                case 2:
                    return "Saved";
                case 3:
                    return "Applied";
            }
            return null;
        }

    }
}
