package com.hypernym.evaconnect.view.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Dashboard;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.HomePostsAdapter;
import com.hypernym.evaconnect.view.ui.activities.SharePostActivity;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.HomeViewModel;
import com.hypernym.evaconnect.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements HomePostsAdapter.ItemClickListener,SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.rc_home)
    RecyclerView rc_home;

    @BindView(R.id.newpost)
    TextView newpost;

    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private List<Post> posts=new ArrayList<>();
    private HomePostsAdapter homePostsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private HomeViewModel homeViewModel;
    private PostViewModel postViewModel;
    private ConnectionViewModel connectionViewModel;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    int itemCount = 0;
    private List<Post> notifications=new ArrayList<>();
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
        return view;
    }

    private void init() {
        setPageTitle(getString(R.string.home));
        homeViewModel = ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication(),getActivity())).get(HomeViewModel.class);
        postViewModel=ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication(),getActivity())).get(PostViewModel.class);
        connectionViewModel=ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication(),getActivity())).get(ConnectionViewModel.class);
        currentPage = PAGE_START;
        homePostsAdapter=new HomePostsAdapter(getContext(),posts,this);
        linearLayoutManager=new LinearLayoutManager(getContext());
        rc_home.setLayoutManager(linearLayoutManager);
        rc_home.setAdapter(homePostsAdapter);
        if(posts.size()==0)
        {
            callPostsApi();
        }

        swipeRefresh.setOnRefreshListener(this);
        /**
         * add scroll listener while user reach in bottom load more will call
         */
        rc_home.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage=AppConstants.TOTAL_PAGES+currentPage;
                if(NetworkUtils.isNetworkConnected(getContext())) {
                    callPostsApi();
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
    public void onResume() {
        super.onResume();
        init();

        newpost.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                loadFragment(R.id.framelayout,new NewPostFragment(),getContext(),true);
            }
        });
    }



    private void callPostsApi() {
        User user=LoginUtils.getLoggedinUser();

        homeViewModel.getDashboard(user,AppConstants.TOTAL_PAGES,currentPage).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> dashboardBaseModel) {
             //   homePostsAdapter.clear();
                if(dashboardBaseModel !=null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().size()>0 && dashboardBaseModel.getData().get(0)!=null)
                {
                    for(Post post:dashboardBaseModel.getData())
                    {
                        if(post.getContent()==null)
                        {
                            post.setContent("");
                        }
                        if(post.getType().equalsIgnoreCase("post") && post.getPost_image().size()>0)
                        {
                            post.setPost_type(AppConstants.IMAGE_TYPE);
                        }
                        else if(post.getType().equalsIgnoreCase("post") && post.getPost_video()!=null)
                        {
                            post.setPost_type(AppConstants.VIDEO_TYPE);
                        }
                        else if(post.getType().equalsIgnoreCase("post") && post.getPost_image().size()==0  && AppUtils.containsURL(post.getContent()).size()==0)
                        {
                            post.setPost_type(AppConstants.TEXT_TYPE);
                        }
                        else if(post.getType().equalsIgnoreCase("event"))
                        {
                            post.setPost_type(AppConstants.EVENT_TYPE);
                        }
                        else if(post.getType().equalsIgnoreCase("post") && AppUtils.containsURL(post.getContent()).size()>0)
                        {
                            post.setPost_type(AppConstants.LINK_POST);
                        }
                    }
                    posts.addAll(dashboardBaseModel.getData());
                    homePostsAdapter.notifyDataSetChanged();
                    swipeRefresh.setRefreshing(false);
                    homePostsAdapter.removeLoading();
                    isLoading = false;
                }
                else if(dashboardBaseModel !=null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().size()==0)
                {
                    isLastPage = true;
                    homePostsAdapter.removeLoading();
                    isLoading = false;
                }
                else
                {
                    networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
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
        PostDetailsFragment postDetailsFragment=new PostDetailsFragment();
        Bundle bundle=new Bundle();
        bundle.putInt("post",posts.get(position).getId());
        Log.d("TAAAGNOTIFY",""+posts.get(position).getId());
        postDetailsFragment.setArguments(bundle);
        loadFragment(R.id.framelayout,postDetailsFragment,getContext(),true);
    }

    @Override
    public void onLikeClick(View view, int position,TextView likeCount) {
        //showDialog();
        Post post=posts.get(position);
        User user=LoginUtils.getLoggedinUser();
        post.setPost_id(post.getId());
        post.setCreated_by_id(user.getId());
        if(post.getIs_post_like()==null ||post.getIs_post_like()<1)
        {
            post.setAction(AppConstants.LIKE);
            if(post.getIs_post_like()==null)
            {
                post.setIs_post_like(1);
                if(post.getLike_count()==null)
                    post.setLike_count(0);
                else
                    post.setLike_count(post.getLike_count()+1);
            }
            else
            {
                post.setIs_post_like(post.getIs_post_like()+1);
                if(post.getLike_count()==null)
                    post.setLike_count(0);
                else
                    post.setLike_count(post.getLike_count()+1);
            }
        }
        else
        {
            post.setAction(AppConstants.UNLIKE);
            if(post.getIs_post_like()>0)
            {
                post.setIs_post_like(post.getIs_post_like()-1);
                post.setLike_count(post.getLike_count()-1);
            }
            else
            {
                post.setIs_post_like(0);
                post.setLike_count(0);
            }

        }
        Log.d("Listing status",post.getAction()+" count"+post.getIs_post_like());
        if(NetworkUtils.isNetworkConnected(getContext())) {
            likePost(post,position);
        }
        else
        {
            networkErrorDialog();
        }

    }

    private void likePost(Post post,int position) {
        postViewModel.likePost(post).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> listBaseModel) {
                if(listBaseModel!=null && !listBaseModel.isError())
                {
                    homePostsAdapter.notifyItemChanged(position);
                }
                else
                {
                    networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    @Override
    public void onShareClick(View view, int position) {
       Intent intent=new Intent(getContext(), SharePostActivity.class);
       intent.putExtra("post",posts.get(position));
       startActivity(intent);
    }

    @Override
    public void onConnectClick(View view, int position) {

        TextView text=(TextView)view;
        if(NetworkUtils.isNetworkConnected(getContext())) {
            callConnectApi(text,position);
        }
        else
        {
            networkErrorDialog();
        }

    }

    @Override
    public void onVideoClick(View view, int position) {
        AppUtils.playVideo(getContext(),posts.get(position).getPost_video());
    }

    @Override
    public void onURLClick(View view, int position) {
        LoadUrlFragment loadUrlFragment =new LoadUrlFragment();
        Bundle bundle=new Bundle();
        bundle.putString("url",posts.get(position).getContent());
        loadUrlFragment.setArguments(bundle);
        loadFragment(R.id.framelayout,loadUrlFragment,getContext(),true);
    }

    @Override
    public void onProfileClick(View view, int position) {
        PersonDetailFragment personDetailFragment=new PersonDetailFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("PostData",posts.get(position));
        personDetailFragment.setArguments(bundle);
        loadFragment(R.id.framelayout,personDetailFragment,getContext(),true);
    }

    private void callConnectApi(TextView text,int position) {
        if(text.getText().toString().equalsIgnoreCase(getString(R.string.connect)))
        {
            showDialog();
            User user=LoginUtils.getLoggedinUser();
            Connection connection=new Connection();
            connection.setReceiver_id(posts.get(position).getUser().getId());
            connection.setSender_id(user.getId());
            connection.setStatus(AppConstants.STATUS_PENDING);
            connectionViewModel.connect(connection).observe(this, new Observer<BaseModel<List<Connection>>>() {
                @Override
                public void onChanged(BaseModel<List<Connection>> listBaseModel) {
                    if(listBaseModel!=null && !listBaseModel.isError())
                    {
                        text.setText("Request Sent");
                    }
                    else
                    {
                        networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
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
        if(NetworkUtils.isNetworkConnected(getContext())) {
            callPostsApi();
        }
        else
        {
            networkErrorDialog();
        }
    }
}
