package com.hypernym.evaconnect.view.ui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.Notification;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.NotificationsAdapter;
import com.hypernym.evaconnect.view.ui.activities.BaseActivity;
import com.hypernym.evaconnect.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsFragment extends BaseFragment implements NotificationsAdapter.OnItemClickListener,SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rc_notifications)
    RecyclerView rc_notifications;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    NotificationsAdapter notificationsAdapter;
    private List<Post> notifications = new ArrayList<>();
    private HomeViewModel homeViewModel;
    private List<Post> posts=new ArrayList<>();
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    int itemCount = 0;
    public NotificationsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        ButterKnife.bind(this, view);
        setPageTitle(getString(R.string.notifications));
        homeViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(HomeViewModel.class);
        swipeRefreshLayout.setOnRefreshListener(this);
        initRecyclerView();
        getAllNotifications();
        //  readAllNotifications();
        return view;
    }
    private void readAllNotifications() {
        BaseActivity.setNotificationCount(0);
        setPageTitle("Notifications");
        if(NetworkUtils.isNetworkConnected(getContext()))
        {
            homeViewModel.notificationMarkAsRead( LoginUtils.getLoggedinUser().getId()).observe(this, new Observer<BaseModel<List<Post>>>() {
                @Override
                public void onChanged(BaseModel<List<Post>> listBaseModel) {
                    if(listBaseModel !=null && !listBaseModel.isError() && listBaseModel.getData().size() >0) {

                    }
                }
            });
        }
        else
        {
            networkErrorDialog();
        }
    }

    private void getAllNotifications() {
       // notifications.clear();
        showDialog();
        homeViewModel.getAllNotifications(AppConstants.TOTAL_PAGES,currentPage).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0 && listBaseModel.getData().get(0) != null) {
                    //  myLikesModelList.clear();
                    notifications.addAll(listBaseModel.getData());
                    notificationsAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                    notificationsAdapter.removeLoading();
                    isLoading = false;
                    //hideDialog();
                } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {
                    isLastPage = true;
                    notificationsAdapter.removeLoading();
                    isLoading = false;
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void initRecyclerView() {
        notificationsAdapter = new NotificationsAdapter(getContext(), notifications, this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rc_notifications.setLayoutManager(linearLayoutManager);
        rc_notifications.setAdapter(notificationsAdapter);
        /**
         * add scroll listener while user reach in bottom load more will call
         */
        rc_notifications.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage= AppConstants.TOTAL_PAGES+currentPage;
                if(NetworkUtils.isNetworkConnected(getContext())) {
                    getAllNotifications();
                }
                else
                {
                    networkErrorDialog();
                }
            }

            @Override
            public boolean isLastPage() {
                return isLastPage;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        if (notifications.get(position).getObject_type().equals("job")) {
            SpecficJobFragment specficJobFragment = new SpecficJobFragment();
            Bundle bundle = new Bundle();
//            JobAd jobAd = new JobAd();
//            jobAd.setId(notifications.get(position).getObject_id());
            bundle.putInt("job_id", notifications.get(position).getObject_id());
            specficJobFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, specficJobFragment, getContext(), true);
        }else if (notifications.get(position).getObject_type().equals("post")) {
            PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("post", notifications.get(position).getObject_id());
            Log.d("TAAAGNOTIFY", "" + GsonUtils.toJson(notifications.get(position)));
            postDetailsFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, postDetailsFragment, getContext(), true);
        }
        else if (notifications.get(position).getObject_type().equals("post")) {
            EventDetailFragment eventDetailsFragment = new EventDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", notifications.get(position).getObject_id());
            eventDetailsFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, eventDetailsFragment, getContext(), true);
        }
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        notificationsAdapter.clear();
        if(NetworkUtils.isNetworkConnected(getContext())) {
            getAllNotifications();
        }
        else
        {
            networkErrorDialog();
        }
    }

}
