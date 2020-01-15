package com.hypernym.evaconnect.view.ui.fragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Dashboard;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.adapters.HomePostsAdapter;
import com.hypernym.evaconnect.view.ui.activities.SharePostActivity;
import com.hypernym.evaconnect.viewmodel.HomeViewModel;
import com.hypernym.evaconnect.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements HomePostsAdapter.ItemClickListener {
    @BindView(R.id.rc_home)
    RecyclerView rc_home;

    @BindView(R.id.newpost)
    TextView newpost;

    private List<Post> posts=new ArrayList<>();
    private HomePostsAdapter homePostsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private HomeViewModel homeViewModel;
    private PostViewModel postViewModel;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this,view);
        init();
        newpost.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                loadFragment(R.id.framelayout,new NewPostFragment(),getContext(),true);
            }
        });
        return view;
    }

    private void init() {
        homeViewModel = ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication(),getActivity())).get(HomeViewModel.class);
        postViewModel=ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication(),getActivity())).get(PostViewModel.class);
        callPostsApi();
        homePostsAdapter=new HomePostsAdapter(getContext(),posts,this);
        linearLayoutManager=new LinearLayoutManager(getContext());
        rc_home.setLayoutManager(linearLayoutManager);
        rc_home.setAdapter(homePostsAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void callPostsApi() {
        showDialog();
        User user=LoginUtils.getLoggedinUser();
        homeViewModel.getDashboard(user).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> dashboardBaseModel) {
                if(dashboardBaseModel !=null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().get(0)!=null)
                {
                    posts.clear();
                    for(Post post:dashboardBaseModel.getData())
                    {
                        if(post.getPost_image().size()>0)
                        {
                            post.setPost_type(AppConstants.IMAGE_TYPE);
                        }
                        else
                        {
                            post.setPost_type(AppConstants.TEXT_TYPE);
                        }
                    }
                    posts.addAll(dashboardBaseModel.getData());
                   // Collections.reverse(posts);
                    homePostsAdapter.notifyDataSetChanged();
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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onItemClick(View view, int position) {
        PostDetailsFragment postDetailsFragment=new PostDetailsFragment();
        Bundle bundle=new Bundle();
        bundle.putSerializable("post",posts.get(position));
        postDetailsFragment.setArguments(bundle);
        loadFragment(R.id.framelayout,postDetailsFragment,getContext(),true);
    }

    @Override
    public void onLikeClick(View view, int position,TextView likeCount) {
        showDialog();
        Post post=posts.get(position);
        User user=LoginUtils.getLoggedinUser();
        post.setPost_id(post.getId());
        post.setCreated_by_id(user.getId());
        if(posts.get(position).getIs_post_like()!=null && posts.get(position).getIs_post_like()>0)
        {
            post.setAction(AppConstants.UNLIKE);
        }
        else {
            post.setAction(AppConstants.LIKE);
        }

        postViewModel.likePost(post).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> listBaseModel) {
                if(listBaseModel!=null && !listBaseModel.isError())
                {
                    int likecount=0;
                    if(posts.get(position).getIs_post_like()==null)
                    {
                        posts.get(position).setIs_post_like(0);
                    }
                    if(posts.get(position).getIs_post_like()!=null && posts.get(position).getIs_post_like()>0)
                    {
                        posts.get(position).setLike_count(posts.get(position).getLike_count()-1);
                        posts.get(position).setIs_post_like(posts.get(position).getIs_post_like()-1);
                       // view.setBackground(getContext().getDrawable(R.mipmap.ic_like));
                    }
                    else
                    {
                        posts.get(position).setLike_count(posts.get(position).getLike_count()+1);
                        posts.get(position).setIs_post_like(posts.get(position).getIs_post_like()+1);
                      //  view.setBackground(getContext().getDrawable(R.mipmap.ic_like_selected));
                    }
                   // likeCount.setText(String.valueOf(likecount));
                    homePostsAdapter.notifyDataSetChanged();
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
       startActivity(intent);
    }
}
