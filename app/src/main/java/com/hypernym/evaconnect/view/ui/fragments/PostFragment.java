package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.PostAdapter;
import com.hypernym.evaconnect.view.dialogs.ShareDialog;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

public class PostFragment extends BaseFragment implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener, PostAdapter.ItemClickListener {

    @BindView(R.id.rc_post)
    RecyclerView rc_post;

    @BindView(R.id.newpost)
    TextView newpost;

//    @BindView(R.id.fab)
//    FloatingActionButton fab;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    private PostViewModel postViewModel;
    private PostAdapter postAdapter;
    private List<Post> posts = new ArrayList<>();
    int itemCount = 0;
    private LinearLayoutManager linearLayoutManager;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private ConnectionViewModel connectionViewModel;

    public PostFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post, container, false);

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
        postViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(PostViewModel.class);
        //   currentPage = PAGE_START;
       connectionViewModel=ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication(),getActivity())).get(ConnectionViewModel.class);
        postAdapter = new PostAdapter(getContext(), posts, this);
        linearLayoutManager = new LinearLayoutManager(getContext());

        rc_post.setLayoutManager(linearLayoutManager);
        rc_post.setAdapter(postAdapter);
        RecyclerView.ItemAnimator animator = rc_post.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        swipeRefresh.setOnRefreshListener(this);
        /**
         * add scroll listener while user reach in bottom load more will call
         */
        rc_post.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
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
//
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
        PostDetailsFragment postDetailsFragment = new PostDetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("post", posts.get(position).getId());
        Log.d("TAAAGNOTIFY", "" + posts.get(position).getId());
        postDetailsFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, postDetailsFragment, getContext(), true);
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
    private void likePost(Post post, int position) {
        postViewModel.likePost(post).observe(this, new Observer<BaseModel<List<Post>>>() {
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

    public void onShareClick(View view, int position) {
        ShareDialog shareDialog;
        Bundle bundle = new Bundle();
        bundle.putSerializable("PostData",posts.get(position));
        shareDialog = new ShareDialog(getContext(),bundle);
        shareDialog.show();
    }

    @Override
    public void onVideoClick(View view, int position) {
        AppUtils.playVideo(getContext(), posts.get(position).getPost_video());
    }

    @Override
    public void onURLClick(View view, int position) {
        LoadUrlFragment loadUrlFragment = new LoadUrlFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", posts.get(position).getContent());
        loadUrlFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, loadUrlFragment, getContext(), true);
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
            else
            {
                callConnectApi(text, position);
            }



        } else {
            networkErrorDialog();
        }
    }

    @Override
    public void onMoreClick(View view, int position) {

    }

    @Override
    public void onEditClick(View view, int position) {
//            postViewModel.editPost(posts.get(position)).observe(this, new Observer<BaseModel<List<Post>>>() {
//                @Override
//                public void onChanged(BaseModel<List<Post>> listBaseModel) {
//                    if (NetworkUtils.isNetworkConnected(getContext())) {
//                        posts.clear();
//                        callPostsApi();
//                    } else {
//                        networkErrorDialog();
//                    }
//                }
//            });
        if (posts.get(position).getType().equalsIgnoreCase("post") && posts.get(position).getPost_image().size() > 0) {
            NewPostFragment newPostFragment = new NewPostFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("post", posts.get(position).getId());
            bundle.putBoolean("isEdit",true);
            Log.d("TAAAGNOTIFY", "" + posts.get(position).getId());
            newPostFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, newPostFragment, getContext(), true);
        } else if (posts.get(position).getType().equalsIgnoreCase("post") && posts.get(position).getPost_video() != null) {
            ShareVideoFragment shareVideoFragment = new ShareVideoFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("post", posts.get(position).getId());
            bundle.putBoolean("isEdit",true);
            Log.d("TAAAGNOTIFY", "" + posts.get(position).getId());
            shareVideoFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, shareVideoFragment, getContext(), true);
        } else if (posts.get(position).getType().equalsIgnoreCase("post") && posts.get(position).getPost_image().size() == 0 && !posts.get(position).isIs_url() && posts.get(position).getPost_document()==null) {
            NewPostFragment newPostFragment = new NewPostFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("post", posts.get(position).getId());
            bundle.putBoolean("isEdit",true);
            Log.d("TAAAGNOTIFY", "" + posts.get(position).getId());
            newPostFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, newPostFragment, getContext(), true);
        } else if (posts.get(position).getType().equalsIgnoreCase("post") && posts.get(position).isIs_url()) {
            NewPostFragment newPostFragment = new NewPostFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("post", posts.get(position).getId());
            bundle.putBoolean("isEdit",true);
            Log.d("TAAAGNOTIFY", "" + posts.get(position).getId());
            newPostFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, newPostFragment, getContext(), true);
        }
        else if (posts.get(position).getType().equalsIgnoreCase("post") && posts.get(position).getPost_document()!=null) {
            ShareArticleFragment newPostFragment = new ShareArticleFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("post", posts.get(position).getId());
            bundle.putBoolean("isEdit",true);
            Log.d("TAAAGNOTIFY", "" + posts.get(position).getId());
            newPostFragment.setArguments(bundle);
            loadFragment(R.id.framelayout, newPostFragment, getContext(), true);
        }

    }

    @Override
    public void onDeleteClick(View view, int position) {

        postViewModel.deletePost(posts.get(position)).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> listBaseModel) {
                if (NetworkUtils.isNetworkConnected(getContext())) {
                    posts.clear();
                    callPostsApi();
                } else {
                    networkErrorDialog();
                }
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
    private void callPostsApi() {
        User user = LoginUtils.getLoggedinUser();

        postViewModel.getPost(user, AppConstants.TOTAL_PAGES, currentPage).observe(this, new Observer<BaseModel<List<Post>>>() {
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
                        } else if (post.getType().equalsIgnoreCase("post") && post.getPost_image().size() == 0 && !post.isIs_url()  && post.getPost_document() == null) {
                            post.setPost_type(AppConstants.TEXT_TYPE);
                        } else if (post.getType().equalsIgnoreCase("post") && post.isIs_url() && post.getPost_document() == null) {
                            post.setPost_type(AppConstants.LINK_POST);
                        }
                        else if(post.getType().equalsIgnoreCase("post") && post.getPost_document() != null)
                        {
                            post.setPost_type(AppConstants.DOCUMENT_TYPE);
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

}
