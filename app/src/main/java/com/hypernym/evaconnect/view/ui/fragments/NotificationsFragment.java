package com.hypernym.evaconnect.view.ui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Notification;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.view.adapters.NotificationsAdapter;
import com.hypernym.evaconnect.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends BaseFragment implements NotificationsAdapter.OnItemClickListener {

    @BindView(R.id.rc_notifications)
    RecyclerView rc_notifications;

    NotificationsAdapter notificationsAdapter;
    private List<Post> notifications = new ArrayList<>();
    private HomeViewModel homeViewModel;
    private List<Post> posts=new ArrayList<>();
    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this, view);
        setPageTitle(getString(R.string.home));
        homeViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(HomeViewModel.class);
        initRecyclerView();
        getAllNotifications();
        return view;
    }

    private void getAllNotifications() {
        homeViewModel.getAllNotifications().observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    notifications.addAll(listBaseModel.getData());
                    notificationsAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void initRecyclerView() {
        notificationsAdapter = new NotificationsAdapter(getContext(), notifications, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rc_notifications.setLayoutManager(linearLayoutManager);
        rc_notifications.setAdapter(notificationsAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        PostDetailsFragment postDetailsFragment=new PostDetailsFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("post",notifications.get(position).getObject_id());
        Log.d("TAAAGNOTIFY",""+GsonUtils.toJson(notifications.get(position)));
        postDetailsFragment.setArguments(bundle);
        loadFragment(R.id.framelayout,postDetailsFragment,getContext(),true);
    }
}
