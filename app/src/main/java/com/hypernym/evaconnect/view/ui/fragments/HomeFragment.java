package com.hypernym.evaconnect.view.ui.fragments;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.Posts;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.view.adapters.HomePostsAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements HomePostsAdapter.ItemClickListener {
    @BindView(R.id.rc_home)
    RecyclerView rc_home;

    private List<Posts> posts=new ArrayList<>();
    private HomePostsAdapter homePostsAdapter;
    private LinearLayoutManager linearLayoutManager;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
        init();
         return view;
    }

    private void init() {
        posts.add(new Posts());
        posts.add(new Posts());
        homePostsAdapter=new HomePostsAdapter(getContext(),posts,this);
        linearLayoutManager=new LinearLayoutManager(getContext());
        rc_home.setLayoutManager(linearLayoutManager);
        rc_home.setAdapter(homePostsAdapter);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onItemClick(View view, int position) {
        AppUtils.loadFragment(R.id.framelayout,new PostDetailsFragment(),getContext());
    }
}
