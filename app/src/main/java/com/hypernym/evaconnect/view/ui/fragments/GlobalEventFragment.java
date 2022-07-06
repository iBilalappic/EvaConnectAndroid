package com.hypernym.evaconnect.view.ui.fragments;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.EventHomeAdapter;
import com.hypernym.evaconnect.view.bottomsheets.BottomsheetShareSelection;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.EventViewModel;
import com.hypernym.evaconnect.viewmodel.HomeViewModel;
import com.hypernym.evaconnect.viewmodel.JobListViewModel;
import com.hypernym.evaconnect.viewmodel.NewsViewModel;
import com.hypernym.evaconnect.viewmodel.PostViewModel;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GlobalEventFragment extends BaseFragment implements View.OnClickListener, EventHomeAdapter.ItemClickListener {
    @BindView(R.id.rc_home)
    RecyclerView rc_home;

    private String search_key;
    private List<Post> posts = new ArrayList<>();
    private EventHomeAdapter homePostsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private HomeViewModel homeViewModel;
    private PostViewModel postViewModel;
    private ConnectionViewModel connectionViewModel;
    private EventViewModel eventViewModel;
    private NewsViewModel newsViewModel;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private boolean isSearchFlag = false;
    int itemCount = 0;
    int item_position ;
    private List<Post> notifications = new ArrayList<>();
    private JobListViewModel jobListViewModel;
    private UserViewModel userViewModel;
    String filter = "event";
    String keyword;
    int totalsize=0;
    private int firstTime;


    public GlobalEventFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        ButterKnife.bind(this, view);
        getActivity().findViewById(R.id.seprator_line).setVisibility(View.VISIBLE);
        if (!mEventBus.getDefault().isRegistered(this)) mEventBus.getDefault().register(this);

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

        initRecycler();
        // showBackButton();
        // setPageTitle("Search Results");
        /**
         * add scroll listener while user reach in bottom load more will call
         */

        ManapulateBundle(getArguments());
    }

    private void initRecycler() {
        homePostsAdapter = new EventHomeAdapter(getContext(), posts, this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        rc_home.setLayoutManager(linearLayoutManager);
        rc_home.setAdapter(homePostsAdapter);

        rc_home.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage = AppConstants.TOTAL_PAGES + currentPage;
                if (posts.size() >= 9) {
                    if (NetworkUtils.isNetworkConnected(getContext())) {
                        callPostsApi();
                    } else {
                        networkErrorDialog();
                    }
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

    private void ManapulateBundle(Bundle arguments) {
        if (arguments != null) {
            keyword = getArguments().getString(Constants.SEARCH);
        }
    }

    private void callPostsApi() {

        showDialog();
        User user = LoginUtils.getLoggedinUser();

        if (search_key.length() > 0)
            user.setSearch_key(search_key);

        homeViewModel.getDashboardSearch(user, AppConstants.TOTAL_PAGES, currentPage, filter).observe(this, dashboardBaseModel -> {
            hideDialog();
            //   homePostsAdapter.clear();
            if (dashboardBaseModel != null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().size() > 0 && dashboardBaseModel.getData().get(0) != null) {

                if (currentPage == PAGE_START) {
                    posts.clear();
                    homePostsAdapter.notifyDataSetChanged();
                }
                if(firstTime<2){
                    rc_home.setVisibility(View.GONE);
                }
                else{
                    rc_home.setVisibility(View.VISIBLE);
                }
                totalsize = 0;

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
                    } else if (post.getType().equalsIgnoreCase("news")) {
                        post.setPost_type(AppConstants.NEWS_TYPE);
                    } else if (post.getType().equalsIgnoreCase("job")) {
                        post.setPost_type(AppConstants.JOB_TYPE);
                    } else if (post.getType().equalsIgnoreCase("post") && post.isIs_url()) {
                        post.setPost_type(AppConstants.LINK_POST);
                    }
                }
                posts.addAll(dashboardBaseModel.getData());
                homePostsAdapter.notifyDataSetChanged();
                //  homePostsAdapter.removeLoading();
                totalsize = totalsize + posts.size();
                isLoading = false;
            } else if (dashboardBaseModel != null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().size() == 0) {
                isLastPage = true;
                totalsize = 0;
                // homePostsAdapter.removeLoading();
                homePostsAdapter.notifyDataSetChanged();
                isLoading = false;
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }

        });
    }


    @Override
    public void onResume() {
        super.onResume();
        init();
        if (NetworkUtils.isNetworkConnected(getContext())) {
            GetUserDetails();
        } else {
            networkErrorDialog();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_backarrow:
                getActivity().onBackPressed();
                break;


        }
    }


    @Override
    public void onItemClick(View view, int position) {
        if (!posts.get(position).getType().equalsIgnoreCase("news")) {
            PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("post", posts.get(position).getId());
            Log.d("TAAAGNOTIFY", "" + posts.get(position).getId());
            postDetailsFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, postDetailsFragment, getContext(), true);
        } else {
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


    private void likeNews(Post post, int position) {
        newsViewModel.likePost(post).observe(this, listBaseModel -> {
            if (listBaseModel != null && !listBaseModel.isError()) {
                homePostsAdapter.notifyItemChanged(position);
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
        });
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

        jobListViewModel.setJobLike(LoginUtils.getUser(), id, "like").observe(this, setlike -> {
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

    @Override
    public void onShareClick(View view, int position) {
//        ShareDialog shareDialog;
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("PostData", posts.get(position));
//        shareDialog = new ShareDialog(getContext(), bundle);
//        shareDialog.show();
        item_position=position;
        BottomsheetShareSelection bottomSheetPictureSelection = new BottomsheetShareSelection(new GlobalEventFragment.YourDialogFragmentDismissHandler());
        bottomSheetPictureSelection.show(getActivity().getSupportFragmentManager(), bottomSheetPictureSelection.getTag());
    }

    @Override
    public void onConnectClick(View view, int position) {
        TextView text = (TextView) view;
        if (NetworkUtils.isNetworkConnected(getContext())) {
            callConnectApi(text, position);
        } else {
            networkErrorDialog();
        }

    }

    @Override
    public void onVideoClick(View view, int position) {
        AppUtils.playVideo(getContext(), posts.get(position).getPost_video());
    }

    @Override
    public void onURLClick(View view, int position) {

    }


    @Override
    public void onProfileClick(View view, int position) {
        PersonProfileFragment personDetailFragment = new PersonProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("PostData", posts.get(position));
        personDetailFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, personDetailFragment, getContext(), true);
    }

    @Override
    public void onApplyClick(View view, int position) {
        if (posts.get(position).getIs_applied() == 0) {
            SpecficJobFragment specficJobFragment = new SpecficJobFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("job_id", posts.get(position).getId());
            specficJobFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, specficJobFragment, getContext(), true);
        }

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

    @Override
    public void onEditClick(View view, int position) {

    }

    @Override
    public void onDeleteClick(View view, int position) {

    }

    private void likeEvent(Post post, int position) {
        Event event = new Event();
        event.setEvent_id(post.getEvent_id());
        event.setCreated_by_id(LoginUtils.getLoggedinUser().getId());
        event.setAction(post.getAction());
        event.setStatus(AppConstants.ACTIVE);
        eventViewModel.likeEvent(event).observe(this, listBaseModel -> {
            if (listBaseModel != null && !listBaseModel.isError()) {
                homePostsAdapter.notifyItemChanged(position);
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
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
            connectionViewModel.connect(connection).observe(this, listBaseModel -> {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    text.setText("Request Sent");
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            });
        }
    }

    private void GetUserDetails() {
        User user = new User();
        user = LoginUtils.getUser();
        userViewModel.getuser_details(user.getId(), false
        ).observe(this, listBaseModel -> {
            if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                LoginUtils.saveUser(listBaseModel.getData().get(0));
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
        });
    }

    public class TextWatcher implements android.text.TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


        }

        @Override
        public void afterTextChanged(Editable s) {
            currentPage = PAGE_START;
            if (s.length() > 0) {
                isSearchFlag = true;
                posts.clear();
                homePostsAdapter.notifyDataSetChanged();
                callPostsApi();
            } else {
                posts.clear();
                homePostsAdapter.notifyDataSetChanged();
                isSearchFlag = false;
            }


        }

    }


    protected class YourDialogFragmentDismissHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 102) {
                Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                whatsappIntent.setType("text/plain");
                whatsappIntent.setPackage("com.whatsapp");
                whatsappIntent.putExtra(Intent.EXTRA_TEXT, "https://www.evaintmedia.com/" + posts.get(item_position).getType() + "/" + posts.get(item_position).getId());
                try {
                    getContext().startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(requireContext(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            } else if (msg.what == 100) {
                ShareConnectionFragment shareConnectionFragment = new ShareConnectionFragment();
                FragmentTransaction transaction = ((AppCompatActivity) requireActivity()).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.framelayout, shareConnectionFragment);
                Bundle bundle = new Bundle();
                {
                    bundle.putInt(Constants.DATA, posts.get(item_position).getId());
                    bundle.putString(Constants.TYPE, posts.get(item_position).getType());
                }
                shareConnectionFragment.setArguments(bundle);
                if (true) {
                    transaction.addToBackStack(null);
                }
                transaction.commit();
            }else if(msg.what==103){
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip;
                clip = ClipData.newPlainText("label", "https://www.evaintmedia.com/" + posts.get(item_position).getType() + "/" + posts.get(item_position).getId());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(requireContext(), "link copied", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Subscribe
    public void onEvent(String mtitle) {
        firstTime++;
        Log.d("TAG", "onEvent: " + mtitle);

        if (mtitle.equals("")) {
            rc_home.setVisibility(View.GONE);
        } else {
            rc_home.setVisibility(View.VISIBLE);

            search_key = mtitle;
            callPostsApi();
        }

    }

}
