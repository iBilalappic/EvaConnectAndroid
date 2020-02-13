package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.view.adapters.NotificationsAdapter;
import com.hypernym.evaconnect.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JobListingFragment extends BaseFragment implements View.OnClickListener
{

    @BindView(R.id.newjobAd)
    TextView newjobAd;

    public JobListingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_joblisting, container, false);
        setPageTitle(getString(R.string.joblist));
        ButterKnife.bind(this, view);
        newjobAd.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.newjobAd:
                loadFragment(R.id.framelayout,new JobCreateFragment(),getContext(),true);
                break;

        }
    }
}
