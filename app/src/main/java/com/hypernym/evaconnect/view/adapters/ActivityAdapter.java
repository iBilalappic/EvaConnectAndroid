package com.hypernym.evaconnect.view.adapters;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.hypernym.evaconnect.view.ui.fragments.MessageFragment;
import com.hypernym.evaconnect.view.ui.fragments.NotificationsFragment;

public class ActivityAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;

    public ActivityAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm);
        myContext = context;
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                MessageFragment messageFragment = new MessageFragment();
                return messageFragment;
            case 1:
                NotificationsFragment notificationsFragment = new NotificationsFragment();
                return notificationsFragment;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}