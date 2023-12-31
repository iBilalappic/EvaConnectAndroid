package com.hypernym.evaconnect.view.ui.fragments;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
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
import com.hypernym.evaconnect.models.Event;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.HomePostsAdapter;
import com.hypernym.evaconnect.view.bottomsheets.BottomsheetShareSelection;
import com.hypernym.evaconnect.viewmodel.NewsViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsFragment extends BaseFragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, HomePostsAdapter.ItemClickListener {

    @BindView(R.id.rc_event)
    RecyclerView rc_event;

    @BindView(R.id.newpost)
    TextView newpost;


    @BindView(R.id.tv_create_event)
    TextView tv_create_event;


//    @BindView(R.id.fab)
//    FloatingActionButton fab;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private NewsViewModel newsViewModel;

    private HomePostsAdapter postAdapter;
    private List<Post> posts = new ArrayList<>();
    int itemCount = 0;
    int item_position;
    private LinearLayoutManager linearLayoutManager;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;

    public NewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);

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
        newsViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(requireActivity().getApplication(), getActivity())).get(NewsViewModel.class);


        //   currentPage = PAGE_START;
        postAdapter = new HomePostsAdapter(getContext(), posts, this);
        linearLayoutManager = new LinearLayoutManager(getContext());
        tv_create_event.setVisibility(View.GONE);
        rc_event.setLayoutManager(linearLayoutManager);
        rc_event.setAdapter(postAdapter);
        RecyclerView.ItemAnimator animator = rc_event.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        swipeRefresh.setOnRefreshListener(this);
        /**
         * add scroll listener while user reach in bottom load more will call
         */
        rc_event.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final OvershootInterpolator interpolator = new OvershootInterpolator();
//                ViewCompat.animate(fab).
//                        rotation(135f).
//                        withLayer().
//                        setDuration(300).
//                        setInterpolator(interpolator).
//                        start();
//                /** Instantiating PopupMenu class */
//                PopupMenu popup = new PopupMenu(getContext(), v);
//
//                /** Adding menu items to the popumenu */
//                popup.getMenuInflater().inflate(R.menu.dashboard_menu, popup.getMenu());
//
//                popup.setOnDismissListener(new PopupMenu.OnDismissListener() {
//                    @Override
//                    public void onDismiss(PopupMenu menu) {
//                        ViewCompat.animate(fab).
//                                rotation(0f).
//                                withLayer().
//                                setDuration(300).
//                                setInterpolator(interpolator).
//                                start();
//                    }
//                });
//                /** Defining menu item click listener for the popup menu */
//                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//
//                    @Override
//                    public boolean onMenuItemClick(MenuItem item) {
//                        if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.menu1))) {
//                            loadFragment(R.id.framelayout, new NewPostFragment(), getContext(), true);
//                        }  else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.menu2))) {
//                            loadFragment(R.id.framelayout, new ShareVideoFragment(), getContext(), true);
//                        }
//                        else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.menu3))) {
//                            loadFragment(R.id.framelayout, new CreateEventFragment(), getContext(), true);
//                        } else if (item.getTitle().toString().equalsIgnoreCase(getString(R.string.menu3))) {
//                            loadFragment(R.id.framelayout, new ShareVideoFragment(), getContext(), true);
//                        }
//
//
//                        return true;
//                    }
//                });
//
//                /** Showing the popup menu */
//                popup.show();
//            }
//        });

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

    @Override
    public void onItemClick(View view, int position) {
        NewsDetailsFragment postDetailsFragment = new NewsDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("post", posts.get(position).getId());
        Log.d("TAAAGNOTIFY", "" + posts.get(position).getId());
        postDetailsFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, postDetailsFragment, getContext(), true);
    }

    @Override
    public void onLikeClick(View view, int position, TextView likeCount, ProgressBar pv) {


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
    private void likeNews(Post post, int position) {
        newsViewModel.likePost(post).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    postAdapter.notifyItemChanged(position);
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }
    @Override
    public void onJobLikeClick(View view, int position, TextView likeCount) {

    }

    private void likePost(Post post, int position) {

        newsViewModel.likePost(post).observe(this, listBaseModel -> {
            if (listBaseModel != null && !listBaseModel.isError()) {
                postAdapter.notifyItemChanged(position);
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
        });
    }

    public void onShareClick(View view, int position) {
//        ShareDialog shareDialog;
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("PostData",posts.get(position));
//        shareDialog = new ShareDialog(getContext(),bundle);
//        shareDialog.show();
        item_position=position;
        BottomsheetShareSelection bottomSheetPictureSelection = new BottomsheetShareSelection(new YourDialogFragmentDismissHandler());
        bottomSheetPictureSelection.show(requireActivity().getSupportFragmentManager(), bottomSheetPictureSelection.getTag());

    }

    @Override
    public void onConnectClick(View view, int position) {

    }

    @Override
    public void onVideoClick(View view, int position) {

    }

    @Override
    public void onURLClick(View view, int position, String type) {
        LoadUrlFragment loadUrlFragment = new LoadUrlFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", posts.get(position).getLink());
        loadUrlFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, loadUrlFragment, getContext(), true);
    }


    @Override
    public void onProfileClick(View view, int position) {

    }

    @Override
    public void onApplyClick(View view, int position) {

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

    private void likeEvent(Post post, int position) {
        Event event = new Event();
        event.setEvent_id(post.getEvent_id());
        event.setCreated_by_id(LoginUtils.getLoggedinUser().getId());
        event.setAction(post.getAction());
        event.setStatus(AppConstants.ACTIVE);
//        eventViewModel.likeEvent(event).observe(this, new Observer<BaseModel<List<Event>>>() {
//            @Override
//            public void onChanged(BaseModel<List<Event>> listBaseModel) {
//                if (listBaseModel != null && !listBaseModel.isError()) {
//                    postAdapter.notifyItemChanged(position);
//                } else {
//                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
//                }
//                hideDialog();
//            }
//        });
    }

    private void callPostsApi() {
        User user = LoginUtils.getLoggedinUser();

        showDialog();
        newsViewModel.getNews(user, AppConstants.TOTAL_PAGES, currentPage).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> dashboardBaseModel) {

                //   homePostsAdapter.clear();
                if (dashboardBaseModel != null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().size() > 0 && dashboardBaseModel.getData().get(0) != null) {

                    hideDialog();
                    for (Post post : dashboardBaseModel.getData()) {

                        post.setPost_type(AppConstants.NEWS_TYPE);
                    }
                    posts.addAll(dashboardBaseModel.getData());
                    postAdapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
//                    postAdapter.removeLoading();
                    isLoading = false;
                } else if (dashboardBaseModel != null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().size() == 0) {
                    isLastPage = true;
//                    postAdapter.removeLoading();
                    swipeRefresh.setRefreshing(false);
                    isLoading = false;
                    hideDialog();
                } else {
                    hideDialog();
                    swipeRefresh.setRefreshing(false);
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }

            }
        });
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
                    requireContext().startActivity(whatsappIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(requireContext(), "Whatsapp have not been installed.", Toast.LENGTH_SHORT).show();
                }
            }
            else if(msg.what==100){
                ShareConnectionFragment shareConnectionFragment = new ShareConnectionFragment();
                FragmentTransaction transaction = ((AppCompatActivity) requireActivity()).getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
                transaction.replace(R.id.framelayout, shareConnectionFragment);
                Bundle bundle = new Bundle();
                {
                    bundle.putInt(Constants.DATA, posts.get(item_position).getId());
                    bundle.putString(Constants.TYPE,  posts.get(item_position).getType());
                }
                shareConnectionFragment.setArguments(bundle);
                if (true) {
                    transaction.addToBackStack(null);
                }
                transaction.commit();
            }else if(msg.what==103){
                ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip;
                clip = ClipData.newPlainText("label", "https://www.evaintmedia.com/" + posts.get(item_position).getType() + "/" + posts.get(item_position).getId());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(requireContext(), "link copied", Toast.LENGTH_SHORT).show();
            }  else if (msg.what == 101) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, posts.get(item_position).getContent());
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
            }
        }
    }
}
