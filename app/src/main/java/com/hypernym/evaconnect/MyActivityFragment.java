package com.hypernym.evaconnect;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.MyActivitiesModel;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.MyActivitiesAdapter;
import com.hypernym.evaconnect.view.ui.activities.BaseActivity;
import com.hypernym.evaconnect.view.ui.fragments.BaseFragment;
import com.hypernym.evaconnect.view.ui.fragments.EventDetailFragment;
import com.hypernym.evaconnect.view.ui.fragments.MeetingDetailFragment;
import com.hypernym.evaconnect.view.ui.fragments.PostDetailsFragment;
import com.hypernym.evaconnect.view.ui.fragments.SpecficJobFragment;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.HomeViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyActivityFragment extends BaseFragment implements MyActivitiesAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rc_notifications)
    RecyclerView rc_notifications;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefreshLayout;

    MyActivitiesAdapter notificationsAdapter;
    private List<MyActivitiesModel> hActivitiesList = new ArrayList<>();
    private HomeViewModel homeViewModel;
    private ConnectionViewModel connectionViewModel;
    private List<Post> posts = new ArrayList<>();
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    int itemCount = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_activity, container, false);
        ButterKnife.bind(this, view);
        homeViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(requireActivity().getApplication(), getActivity())).get(HomeViewModel.class);
        connectionViewModel=ViewModelProviders.of(this,new CustomViewModelFactory(requireActivity().getApplication(),getActivity())).get(ConnectionViewModel.class);
        swipeRefreshLayout.setOnRefreshListener(this);
        initRecyclerView();
        showDialog();
        getAllNotifications();
        readAllNotifications();
        return view;
    }

    private void readAllNotifications() {
        BaseActivity.setNotificationCount(0);
        setPageTitle("Notifications");
        if (NetworkUtils.isNetworkConnected(getContext())) {
            homeViewModel.notificationMarkAsRead(LoginUtils.getLoggedinUser().getId()).observe(getViewLifecycleOwner(), listBaseModel -> {
                if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0) {


                }
            });
        } else {
            networkErrorDialog();
        }
    }


    private void initRecyclerView() {
        notificationsAdapter = new MyActivitiesAdapter(getContext(), hActivitiesList, this);
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
                currentPage = AppConstants.TOTAL_PAGES + currentPage;
                if (NetworkUtils.isNetworkConnected(getContext())) {
                    getAllNotifications();
                } else {
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
        if (hActivitiesList.get(position).getObjectType().equals("job")) {
            SpecficJobFragment specficJobFragment = new SpecficJobFragment();
            Bundle bundle = new Bundle();
//            JobAd jobAd = new JobAd();
//            jobAd.setId(hActivitiesList.get(position).getObject_id());
            bundle.putInt("job_id", hActivitiesList.get(position).getObjectId());
            specficJobFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, specficJobFragment, getContext(), true);
        } else if (hActivitiesList.get(position).getObjectType().equals("post")) {
            PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("post", hActivitiesList.get(position).getObjectId());
            Log.d("TAAAGNOTIFY", "" + GsonUtils.toJson(hActivitiesList.get(position)));
            postDetailsFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, postDetailsFragment, getContext(), true);
        } else if (hActivitiesList.get(position).getObjectType().equals("event")) {
            EventDetailFragment eventDetailsFragment = new EventDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", hActivitiesList.get(position).getObjectId());
            eventDetailsFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, eventDetailsFragment, getContext(), true);
        } else if (hActivitiesList.get(position).getObjectType().equals("meeting")) {
            MeetingDetailFragment meetingDetailFragment = new MeetingDetailFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("id", hActivitiesList.get(position).getObjectId());
            meetingDetailFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, meetingDetailFragment, getContext(), true);
        }

    }

    @Override
    public void onAcceptClick(View view, int position) {
        Connection connection = new Connection();
        User user = LoginUtils.getLoggedinUser();
        connection.setStatus(AppConstants.ACTIVE);
//        connection.setId(hActivitiesList.get(position).getConnection_id());
        connection.setModified_by_id(user.getId());
        connection.setModified_datetime(DateUtils.GetCurrentdatetime());
        connection.setSender_id(LoginUtils.getLoggedinUser().getId());
        connection.setId(hActivitiesList.get(position).getObjectId());
//        connection.setReceiver_id(hActivitiesList.get(position).getReceiver_id());
        callDeclineConnectApi(connection);
    }

    private void callDeclineConnectApi(Connection connection) {

        showDialog();
        connectionViewModel.connect(connection).observe(this, listBaseModel -> {

            hideDialog();
            if (listBaseModel != null && !listBaseModel.isError()) {

                hActivitiesList.clear();
                notificationsAdapter.notifyDataSetChanged();
                if (NetworkUtils.isNetworkConnected(getContext())) {
                    getAllNotifications();
                } else {
                    networkErrorDialog();
                }
                //getConnectionByFilter(type, PAGE_START, true);
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
        });
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        notificationsAdapter.clear();
        if (NetworkUtils.isNetworkConnected(getContext())) {
            getAllNotifications();
        } else {
            networkErrorDialog();
        }
    }


    private void getAllNotifications() {
        showDialog();

        homeViewModel.getAllMyActivity(AppConstants.TOTAL_PAGES, currentPage).observe(getViewLifecycleOwner(), listBaseModel -> {
            hideDialog();
            if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() > 0 && listBaseModel.getData().get(0) != null) {
                //  myLikesModelList.clear();


                hSortListByCreatedTime(listBaseModel.getData());


                //hideDialog();
            } else if (listBaseModel != null && !listBaseModel.isError() && listBaseModel.getData().size() == 0) {
                isLastPage = true;
                notificationsAdapter.removeLoading();
                isLoading = false;
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void hSortListByCreatedTime(List<MyActivitiesModel> data) {
        Collections.sort(data, MyActivitiesModel.DateCreaterComparator);
        hActivitiesList.addAll(data);
        notificationsAdapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
        notificationsAdapter.removeLoading();
        isLoading = false;
    }
}