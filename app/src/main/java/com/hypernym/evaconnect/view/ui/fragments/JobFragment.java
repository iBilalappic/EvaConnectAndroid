package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.EventHomeAdapter;
import com.hypernym.evaconnect.view.adapters.JobHomeAdapter;
import com.hypernym.evaconnect.view.dialogs.ShareDialog;
import com.hypernym.evaconnect.viewmodel.EventViewModel;
import com.hypernym.evaconnect.viewmodel.JobListViewModel;
import com.hypernym.evaconnect.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

public class JobFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, JobHomeAdapter.ItemClickListener {

    @BindView(R.id.rc_job)
    RecyclerView rc_job;

    @BindView(R.id.newpost)
    TextView newpost;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private JobListViewModel jobListViewModel;
    private PostViewModel postViewModel;
    private JobHomeAdapter postAdapter;
    private List<Post> posts = new ArrayList<>();
    int itemCount = 0;
    private LinearLayoutManager linearLayoutManager;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;

    public JobFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_job, container, false);

        ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onClick(View v) {

    }


    @Override
    public void onResume() {
        super.onResume();
        init();
        onRefresh();

        newpost.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                loadFragment(R.id.framelayout, new NewPostFragment(), getContext(), true);
            }
        });
    }

    private void init() {
        jobListViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(JobListViewModel.class);
        postViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(PostViewModel.class);
        //   currentPage = PAGE_START;
        postAdapter = new JobHomeAdapter(getContext(), posts, this);
        linearLayoutManager = new LinearLayoutManager(getContext());

        rc_job.setLayoutManager(linearLayoutManager);
        rc_job.setAdapter(postAdapter);
        RecyclerView.ItemAnimator animator = rc_job.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        swipeRefresh.setOnRefreshListener(this);
        /**
         * add scroll listener while user reach in bottom load more will call
         */
        rc_job.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage = AppConstants.TOTAL_PAGES + currentPage;
                if (NetworkUtils.isNetworkConnected(getContext())) {
                    callPostsApi();
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final OvershootInterpolator interpolator = new OvershootInterpolator();
                ViewCompat.animate(fab).
                        rotation(135f).
                        withLayer().
                        setDuration(300).
                        setInterpolator(interpolator).
                        start();
                /** Instantiating PopupMenu class */
                PopupMenu popup = new PopupMenu(getContext(), v);

                /** Adding menu items to the popumenu */
                popup.getMenuInflater().inflate(R.menu.dashboard_menu, popup.getMenu());

                popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu menu) {
                        ViewCompat.animate(fab).
                                rotation(0f).
                                withLayer().
                                setDuration(300).
                                setInterpolator(interpolator).
                                start();
                    }
                });
                /** Defining menu item click listener for the popup menu */
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        //    Toast.makeText(getContext(), item.getGroupId()+"You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
//                        if(item.getTitle().toString().equalsIgnoreCase(getString(R.string.action1)))
//                        {
//                            loadFragment(R.id.framelayout,new CreateEventFragment(),getContext(),true);
//                        }
//                        else
//                        {
//                            loadFragment(R.id.framelayout,new CreateMeetingFragment(), getContext(),true);
//                        }

                        return true;
                    }
                });

                /** Showing the popup menu */
                popup.show();
            }
        });

    }


    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        postAdapter.clear();
        if (NetworkUtils.isNetworkConnected(getContext())) {
            callPostsApi();
        } else {
            networkErrorDialog();
        }
    }




    private void callPostsApi() {
        User user = LoginUtils.getLoggedinUser();

        jobListViewModel.getJob(user, AppConstants.TOTAL_PAGES, currentPage).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> dashboardBaseModel) {

                //   homePostsAdapter.clear();
                if (dashboardBaseModel != null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().size() > 0 && dashboardBaseModel.getData().get(0) != null) {
                    for (Post post : dashboardBaseModel.getData()) {
                        if (post.getContent() == null) {
                            post.setContent("");
                        }
                        else if (post.getType().equalsIgnoreCase("job")) {
                            post.setPost_type(AppConstants.JOB_TYPE);
                        }
                    }
                    posts.addAll(dashboardBaseModel.getData());
                    postAdapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                    postAdapter.removeLoading();
                    isLoading = false;
                } else if (dashboardBaseModel != null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().size() == 0) {
                    isLastPage = true;
                    postAdapter.removeLoading();
                    isLoading = false;
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

    }


    @Override
    public void onJobLikeClick(View view, int position, TextView likeCount) {
        if (posts.get(position).getIs_job_like() != null && posts.get(position).getIs_job_like() > 0) {
            SetJobUnLike(posts.get(position).getId(), position);
        } else {
            SetJobLike(posts.get(position).getId(), position);
        }
    }
    private void SetJobLike(Integer id, int position) {

        jobListViewModel.setJobLike(LoginUtils.getUser(), id, "like").observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> setlike) {
                Post post = posts.get(position);
                post.setAction(AppConstants.LIKE);
                if (post.getIs_job_like() == null) {
                    post.setIs_job_like(1);
                    if (post.getLike_count() == null)
                        post.setLike_count(0);
                    else
                        post.setLike_count(post.getLike_count() + 1);
                } else {
                    post.setIs_job_like(post.getIs_job_like() + 1);
                    if (post.getLike_count() == null)
                        post.setLike_count(0);
                    else
                        post.setLike_count(post.getLike_count() + 1);
                }
                postAdapter.notifyItemChanged(position);
//                if (setlike != null && !setlike.isError()) {
                //     onRefresh();
//                } else {
//                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
//                }

                hideDialog();

            }
        });
    }

    private void SetJobUnLike(Integer id, int position) {
        jobListViewModel.setJobLike(LoginUtils.getUser(), id, "unlike").observe(this, new Observer<BaseModel<List<Object>>>() {
            @Override
            public void onChanged(BaseModel<List<Object>> setlike) {
                // onRefresh();
                Post post = posts.get(position);
                post.setAction(AppConstants.UNLIKE);
                if (post.getIs_job_like() > 0) {
                    post.setIs_job_like(post.getIs_job_like() - 1);
                    post.setLike_count(post.getLike_count() - 1);
                } else {
                    post.setIs_job_like(0);
                    post.setLike_count(0);
                }

                postAdapter.notifyItemChanged(position);
                hideDialog();
            }
        });
    }

    @Override
    public void onShareClick(View view, int position) {
        ShareDialog shareDialog;
        Bundle bundle = new Bundle();
        bundle.putSerializable("PostData",posts.get(position));
        shareDialog = new ShareDialog(getContext(),bundle);
        shareDialog.show();
    }

    @Override
    public void onApplyClick(View view, int position) {
        SpecficJobFragment specficJobFragment = new SpecficJobFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("job_id", posts.get(position).getId());
        specficJobFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, specficJobFragment, getContext(), true);
    }

}
