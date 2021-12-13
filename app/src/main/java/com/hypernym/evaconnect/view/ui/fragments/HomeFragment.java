package com.hypernym.evaconnect.view.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.HomePostsAdapter;
import com.hypernym.evaconnect.view.dialogs.ShareDialog;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.EventViewModel;
import com.hypernym.evaconnect.viewmodel.HomeViewModel;
import com.hypernym.evaconnect.viewmodel.JobListViewModel;
import com.hypernym.evaconnect.viewmodel.NewsViewModel;
import com.hypernym.evaconnect.viewmodel.PostViewModel;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements HomePostsAdapter.ItemClickListener,
        SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.rc_home)
    RecyclerView rc_home;

    @BindView(R.id.newpost)
    TextView newpost;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private List<Post> posts = new ArrayList<>();
    private HomePostsAdapter homePostsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private HomeViewModel homeViewModel;
    private PostViewModel postViewModel;
    private NewsViewModel newsViewModel;
    private ConnectionViewModel connectionViewModel;
    private EventViewModel eventViewModel;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    int itemCount = 0;
    private List<Post> notifications = new ArrayList<>();
    private JobListViewModel jobListViewModel;
    private UserViewModel userViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    private void init() {
        homeViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(HomeViewModel.class);
        postViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(PostViewModel.class);
        connectionViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(ConnectionViewModel.class);
        jobListViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(JobListViewModel.class);
        eventViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(EventViewModel.class);
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(UserViewModel.class);
        newsViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(NewsViewModel.class);
        //   currentPage = PAGE_START;
        homePostsAdapter = new HomePostsAdapter(getContext(), posts, this);
        linearLayoutManager = new LinearLayoutManager(getContext());

        rc_home.setLayoutManager(linearLayoutManager);
        rc_home.setAdapter(homePostsAdapter);

        RecyclerView.ItemAnimator animator = rc_home.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        swipeRefresh.setOnRefreshListener(this);
        hideBackButton();

        /**
         * add scroll listener while user reach in bottom load more will call
         */
        rc_home.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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

    }

    @Override
    public void onResume() {
        super.onResume();
        init();
        onRefresh();

        if (NetworkUtils.isNetworkConnected(getContext())) {
            GetUserDetails();
        } else {
            networkErrorDialog();
        }

        newpost.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                loadFragment(R.id.framelayout, new NewPostFragment(), getContext(), true);
            }
        });
    }


    private void callPostsApi() {
        User user = LoginUtils.getLoggedinUser();

        homeViewModel.getDashboard(user, AppConstants.TOTAL_PAGES, currentPage).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> dashboardBaseModel) {

                //   homePostsAdapter.clear();
                if (dashboardBaseModel != null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().size() > 0 && dashboardBaseModel.getData().get(0) != null) {
                    for (Post post : dashboardBaseModel.getData()) {
                        if (post.getContent() == null) {
                            post.setContent("");
                        }
                        if (post.getType().equalsIgnoreCase("post") && post.getPost_image().size() > 0) {
                            post.setPost_type(AppConstants.IMAGE_TYPE);
                        } else if (post.getType().equalsIgnoreCase("post") && post.getPost_video() != null) {
                            post.setPost_type(AppConstants.VIDEO_TYPE);
                        } else if (post.getType().equalsIgnoreCase("post") && post.getPost_image().size() == 0 && !post.isIs_url()) {
                            post.setPost_type(AppConstants.TEXT_TYPE);
                        } else if (post.getType().equalsIgnoreCase("event")) {
                            post.setPost_type(AppConstants.EVENT_TYPE);
                        } else if (post.getType().equalsIgnoreCase("job")) {
                            post.setPost_type(AppConstants.JOB_TYPE);
                        }
                     else if (post.getType().equalsIgnoreCase("news")) {
                        post.setPost_type(AppConstants.NEWS_TYPE);
                    }
                        else if (post.getType().equalsIgnoreCase("post") && post.isIs_url()) {
                            post.setPost_type(AppConstants.LINK_POST);
                        }
                    }
                    posts.addAll(dashboardBaseModel.getData());
                    homePostsAdapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                    homePostsAdapter.removeLoading();
                    isLoading = false;
                } else if (dashboardBaseModel != null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().size() == 0) {
                    isLastPage = true;
                    homePostsAdapter.removeLoading();
                    isLoading = false;
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onItemClick(View view, int position) {
        if(!posts.get(position).getType().equalsIgnoreCase("news"))
        {
            PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("post", posts.get(position).getId());
            Log.d("TAAAGNOTIFY", "" + posts.get(position).getId());
            postDetailsFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, postDetailsFragment, getContext(), true);
        }
        else
        {
            NewsDetailsFragment postDetailsFragment = new NewsDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("post", posts.get(position).getId());
            Log.d("TAAAGNOTIFY", "" + posts.get(position).getId());
            postDetailsFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, postDetailsFragment, getContext(), true);
        }

    }

    @Override
    public void onLikeClick(View view, int position, TextView likeCount) {
        //showDialog();
        Post post = posts.get(position);
        User user = LoginUtils.getLoggedinUser();
        post.setPost_id(post.getId());
        post.setCreated_by_id(user.getId());
        if (post.getIs_post_like() == null || post.getIs_post_like() < 1) {
            post.setAction(AppConstants.LIKE);
            if (post.getIs_post_like() == null) {
                post.setIs_post_like(1);
                if (post.getLike_count() == null)
                    post.setLike_count(0);
                else
                    post.setLike_count(post.getLike_count() + 1);
            } else {
                post.setIs_post_like(post.getIs_post_like() + 1);
                if (post.getLike_count() == null)
                    post.setLike_count(0);
                else
                    post.setLike_count(post.getLike_count() + 1);
            }
        } else {
            post.setAction(AppConstants.UNLIKE);
            if (post.getIs_post_like() > 0) {
                post.setIs_post_like(post.getIs_post_like() - 1);
                post.setLike_count(post.getLike_count() - 1);
            } else {
                post.setIs_post_like(0);
                post.setLike_count(0);
            }

        }
        Log.d("Listing status", post.getAction() + " count" + post.getIs_post_like());
        if (NetworkUtils.isNetworkConnected(getContext())) {

            likePost(post, position);

        } else {
            networkErrorDialog();
        }

    }

    @Override
    public void onNewsLikeClick(View view, int position, TextView likeCount) {
        //showDialog();
        Post post = posts.get(position);
        User user = LoginUtils.getLoggedinUser();
        post.setRss_news_id(post.getId());
        post.setCreated_by_id(user.getId());
        if (post.getIs_news_like() == null || post.getIs_news_like() < 1) {
            post.setAction(AppConstants.LIKE);
            if (post.getIs_news_like() == null) {
                post.setIs_news_like(1);
                if (post.getLike_count() == null)
                    post.setLike_count(0);
                else
                    post.setLike_count(post.getLike_count() + 1);
            } else {
                post.setIs_news_like(post.getIs_news_like() + 1);
                if (post.getLike_count() == null)
                    post.setLike_count(0);
                else
                    post.setLike_count(post.getLike_count() + 1);
            }
        } else {
            post.setAction(AppConstants.UNLIKE);
            if (post.getIs_news_like() > 0) {
                post.setIs_news_like(post.getIs_news_like() - 1);
                post.setLike_count(post.getLike_count() - 1);
            } else {
                post.setIs_news_like(0);
                post.setLike_count(0);
            }

        }
        Log.d("Listing status", post.getAction() + " count" + post.getIs_post_like());
        if (NetworkUtils.isNetworkConnected(getContext())) {

            likeNews(post, position);

        } else {
            networkErrorDialog();
        }
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
                homePostsAdapter.notifyItemChanged(position);
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

                homePostsAdapter.notifyItemChanged(position);
                hideDialog();
            }
        });
    }

    private void likePost(Post post, int position) {
        postViewModel.likePost(post).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    homePostsAdapter.notifyItemChanged(position);
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void likeNews(Post post, int position) {
        newsViewModel.likePost(post).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    homePostsAdapter.notifyItemChanged(position);
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    @Override
    public void onShareClick(View view, int position) {
//       Intent intent=new Intent(getContext(), SharePostActivity.class);
//       intent.putExtra("post",posts.get(position));
//       startActivity(intent);
        ShareDialog shareDialog;
        Bundle bundle = new Bundle();
        bundle.putSerializable("PostData", posts.get(position));
        shareDialog = new ShareDialog(getContext(), bundle);
        shareDialog.show();
    }

    @Override
    public void onConnectClick(View view, int position) {

        TextView text = (TextView) view;
        if (NetworkUtils.isNetworkConnected(getContext())) {
            if(text.getText().toString().equalsIgnoreCase(AppConstants.REQUEST_ACCEPT)){
                Connection connection = new Connection();
                User user = LoginUtils.getLoggedinUser();
                connection.setStatus(AppConstants.ACTIVE);
                connection.setId(posts.get(position).getConnection_id());
                connection.setModified_by_id(user.getId());
                connection.setModified_datetime(DateUtils.GetCurrentdatetime());
                callDeclineConnectApi(connection);
            }
            else {
                callConnectApi(text, position);
            }

        } else {
            networkErrorDialog();
        }

    }

    @Override
    public void onVideoClick(View view, int position) {
        AppUtils.playVideo(getContext(), posts.get(position).getPost_video());
    }

    @Override
    public void onURLClick(View view, int position,String type) {
        if(type.equalsIgnoreCase("news"))
        {
            LoadUrlFragment loadUrlFragment = new LoadUrlFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", posts.get(position).getLink());
            loadUrlFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, loadUrlFragment, getContext(), true);
        }
        else
        {
            LoadUrlFragment loadUrlFragment = new LoadUrlFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", posts.get(position).getContent());
            loadUrlFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, loadUrlFragment, getContext(), true);
        }

    }

    @Override
    public void onProfileClick(View view, int position) {
        if(!posts.get(position).getType().equalsIgnoreCase("news"))
        {
            PersonProfileFragment personDetailFragment = new PersonProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("PostData", posts.get(position));
            personDetailFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, personDetailFragment, getContext(), true);
        }

    }

    @Override
    public void onApplyClick(View view, int position) {
        SpecficJobFragment specficJobFragment = new SpecficJobFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("job_id", posts.get(position).getId());
        specficJobFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, specficJobFragment, getContext(), true);
    }

    @Override
    public void onEventItemClick(View view, int position) {
        EventDetailFragment eventDetailFragment = new EventDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("id", posts.get(position).getId());
        eventDetailFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, eventDetailFragment, getContext(), true);
    }

    @Override
    public void onEventLikeClick(View view, int position, TextView likeCount) {
        //showDialog();
        Post post = posts.get(position);
        User user = LoginUtils.getLoggedinUser();
        post.setEvent_id(post.getId());
        post.setCreated_by_id(user.getId());
        if (post.getIs_event_like() == null || post.getIs_event_like() < 1) {
            post.setAction(AppConstants.LIKE);
            if (post.getIs_event_like() == null) {
                post.setIs_event_like(1);
                if (post.getLike_count() == null)
                    post.setLike_count(0);
                else
                    post.setLike_count(post.getLike_count() + 1);
            } else {
                post.setIs_event_like(post.getIs_event_like() + 1);
                if (post.getLike_count() == null)
                    post.setLike_count(0);
                else
                    post.setLike_count(post.getLike_count() + 1);
            }
        } else {
            post.setAction(AppConstants.UNLIKE);
            if (post.getIs_event_like() > 0) {
                post.setIs_event_like(post.getIs_event_like() - 1);
                post.setLike_count(post.getLike_count() - 1);
            } else {
                post.setIs_event_like(0);
                post.setLike_count(0);
            }

        }
        // Log.d("Listing status",post.getAction()+" count"+post.getIs_post_like());
        if (NetworkUtils.isNetworkConnected(getContext())) {
            likeEvent(post, position);
        } else {
            networkErrorDialog();
        }
    }

    private void likeEvent(Post post, int position) {
        Event event = new Event();
        event.setEvent_id(post.getEvent_id());
        event.setCreated_by_id(LoginUtils.getLoggedinUser().getId());
        event.setAction(post.getAction());
        event.setStatus(AppConstants.ACTIVE);
        eventViewModel.likeEvent(event).observe(this, new Observer<BaseModel<List<Event>>>() {
            @Override
            public void onChanged(BaseModel<List<Event>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    homePostsAdapter.notifyItemChanged(position);
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void callConnectApi(TextView text, int position) {
        if (text.getText().toString().equalsIgnoreCase(getString(R.string.connect))) {
            showDialog();
            User user = LoginUtils.getLoggedinUser();
            Connection connection = new Connection();
            connection.setReceiver_id(posts.get(position).getUser().getId());
            connection.setSender_id(user.getId());
            connection.setStatus(AppConstants.STATUS_PENDING);
            connectionViewModel.connect(connection).observe(this, new Observer<BaseModel<List<Connection>>>() {
                @Override
                public void onChanged(BaseModel<List<Connection>> listBaseModel) {
                    if (listBaseModel != null && !listBaseModel.isError()) {
                        text.setText("Pending");
                        onRefresh();

                    } else {
                        networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                    }
                    hideDialog();
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        itemCount = 0;
        currentPage = PAGE_START;
        isLastPage = false;
        homePostsAdapter.clear();
        if (NetworkUtils.isNetworkConnected(getContext())) {
            callPostsApi();
        } else {
            networkErrorDialog();
        }
    }

    private void GetUserDetails() {
        User user = new User();
        user = LoginUtils.getUser();
        userViewModel.getuser_details(user.getId()
        ).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel!=null && listBaseModel.getData() != null && !listBaseModel.isError()) {
                    swipeRefresh.setRefreshing(false);
                    LoginUtils.saveUser(listBaseModel.getData().get(0));
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }



    private void callDeclineConnectApi(Connection connection) {

        connectionViewModel.connect(connection).observe(this, new Observer<BaseModel<List<Connection>>>() {
            @Override
            public void onChanged(BaseModel<List<Connection>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {

                    onRefresh();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }


}
