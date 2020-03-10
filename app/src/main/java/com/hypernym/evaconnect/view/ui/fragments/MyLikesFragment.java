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
import android.widget.Toast;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.MessageAdapter;
import com.hypernym.evaconnect.view.adapters.MyLikeAdapter;
import com.hypernym.evaconnect.view.adapters.NotificationsAdapter;
import com.hypernym.evaconnect.viewmodel.MessageViewModel;
import com.hypernym.evaconnect.viewmodel.MylikesViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyLikesFragment extends BaseFragment implements MyLikeAdapter.OnItemClickListener, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.rc_mylikes)
    RecyclerView rc_mylikes;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private MylikesViewModel mylikeViewModel;
    private MyLikeAdapter myLikeAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<MyLikesModel> myLikesModelList = new ArrayList<>();
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    int itemCount = 0;

    public MyLikesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_likes, container, false);
        ButterKnife.bind(this, view);

        init();

        return view;
    }

    private void init() {
        mylikeViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(MylikesViewModel.class);
        swipeRefresh.setOnRefreshListener(this);

        setupRecyclerview();
        setPageTitle("My Likes");
        GetMyLikes();


    }

    private void setupRecyclerview() {
//        networkConnectionList = removeDuplicates(networkConnectionList);
        myLikeAdapter = new MyLikeAdapter(getContext(), myLikesModelList, this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rc_mylikes.setLayoutManager(linearLayoutManager);
        rc_mylikes.setAdapter(myLikeAdapter);

        /**
         * add scroll listener while user reach in bottom load more will call
         */
        rc_mylikes.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage = AppConstants.TOTAL_PAGES + currentPage;
                if (NetworkUtils.isNetworkConnected(getContext())) {
                    GetMyLikes();
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

    private void GetMyLikes() {
        showDialog();
        User user = LoginUtils.getLoggedinUser();
        mylikeViewModel.SetLikes(user.getUser_id(), AppConstants.TOTAL_PAGES, currentPage).observe(this, new Observer<BaseModel<List<MyLikesModel>>>() {
            @Override
            public void onChanged(BaseModel<List<MyLikesModel>> getnetworkconnection) {
                if (getnetworkconnection != null && !getnetworkconnection.isError() && getnetworkconnection.getData().size() > 0 && getnetworkconnection.getData().get(0) != null) {
                    //  myLikesModelList.clear();
                    myLikesModelList.addAll(getnetworkconnection.getData());
                    myLikeAdapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                    myLikeAdapter.removeLoading();
                    isLoading = false;
                    //hideDialog();
                } else if (getnetworkconnection != null && !getnetworkconnection.isError() && getnetworkconnection.getData().size() == 0) {
                    isLastPage = true;
                    myLikeAdapter.removeLoading();
                    isLoading = false;
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
       if (myLikesModelList.get(position).getObjectType().equals("job")) {
            SpecficJobFragment specficJobFragment = new SpecficJobFragment();
            Bundle bundle = new Bundle();
            JobAd jobAd = new JobAd();
            jobAd.setId(myLikesModelList.get(position).getObjectId());
            bundle.putSerializable("JOB_AD", jobAd);
            specficJobFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, specficJobFragment, getContext(), true);
        } else {
            PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("post", myLikesModelList.get(position).getObjectId());
            postDetailsFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, postDetailsFragment, getContext(), true);
        }

    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        myLikeAdapter.clear();
        if (NetworkUtils.isNetworkConnected(getContext())) {
            GetMyLikes();
        } else {
            networkErrorDialog();
        }
    }
}
