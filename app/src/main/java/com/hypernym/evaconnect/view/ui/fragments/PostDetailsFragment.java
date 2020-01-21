package com.hypernym.evaconnect.view.ui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.CommentsAdapter;
import com.hypernym.evaconnect.view.adapters.HomePostsAdapter;
import com.hypernym.evaconnect.view.adapters.SliderImageAdapter;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.PostViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostDetailsFragment extends BaseFragment implements Validator.ValidationListener  {

    @BindView(R.id.rc_comments)
    RecyclerView rc_comments;

    @NotEmpty
    @BindView(R.id.edt_comment)
    EditText edt_comment;

    @BindView(R.id.btn_addcomment)
    TextView btn_addcomment;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_connections)
    TextView tv_connections;

    @BindView(R.id.tv_minago)
    TextView tv_minago;

    @BindView(R.id.tv_createddateTime)
    TextView tv_createddateTime;

    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.tv_likecount)
    TextView tv_likecount;

    @BindView(R.id.tv_comcount)
    TextView tv_comcount;

    @BindView(R.id.slider_images)
    SliderView imageSlider;

    @BindView(R.id.img_user)
    ImageView img_user;

    @BindView(R.id.img_like)
    ImageView img_like;

    @BindView(R.id.profile_image)
    ImageView profile_image;

    @BindView(R.id.tv_connect)
    TextView tv_connect;

    private CommentsAdapter commentsAdapter;
    private SliderImageAdapter sliderImageAdapter;
    private List<Comment> comments=new ArrayList<>();
    private PostViewModel postViewModel;
    Post post=new Post();
    private Validator validator;
    private ConnectionViewModel connectionViewModel;

    public PostDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_post_details, container, false);
        ButterKnife.bind(this,view);
        init();
        post=(Post) getArguments().getSerializable("post");
        postViewModel= ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication())).get(PostViewModel.class);
        getComments();
        initRecyclerView();
        setPostData();
        btn_addcomment.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                validator.validate();
            }
        });
        return view;
    }

    private void init() {
        validator = new Validator(this);
        validator.setValidationListener(this);
        showBackButton();
    }



    private void setPostData() {
        initializeSlider();
        connectionViewModel=ViewModelProviders.of(this,new CustomViewModelFactory(getActivity().getApplication(),getActivity())).get(ConnectionViewModel.class);
        tv_comcount.setText(String.valueOf(post.getComment_count()));
        tv_likecount.setText(String.valueOf(post.getLike_count()));
        tv_connections.setText(AppUtils.getConnectionsCount(post.getUser().getTotal_connection()));
        tv_createddateTime.setText(DateUtils.getFormattedDateTime(post.getCreated_datetime()));
        tv_minago.setText(DateUtils.getTimeAgo(post.getCreated_datetime()));
        tv_name.setText(post.getUser().getFirst_name());
        tv_content.setText(post.getContent());
        //Hide connect option if post is from logged in user
        User user=LoginUtils.getLoggedinUser();
        if(post.getUser().getId()==user.getId())
        {
            tv_connect.setVisibility(View.GONE);
        }
        else
        {
            tv_connect.setVisibility(View.VISIBLE);
            tv_connect.setText(AppUtils.getConnectionStatus(getContext(),post.getIs_connected()));
        }

        AppUtils.setGlideImage(getContext(),profile_image,post.getUser().getUser_image());
        AppUtils.setGlideImage(getContext(),img_user,post.getUser().getUser_image());
        if(post.getIs_post_like()!=null && post.getIs_post_like()>0)
        {
            img_like.setBackground(getContext().getDrawable(R.mipmap.ic_like_selected));
        }
        else
        {
            img_like.setBackground(getContext().getDrawable(R.mipmap.ic_like));
        }
        if(post.getPost_type()==AppConstants.IMAGE_TYPE)
        {
            imageSlider.setVisibility(View.VISIBLE);
        }
        else
        {
            imageSlider.setVisibility(View.GONE);
        }
    }

    private void addComment(int postID) {
        showDialog();
        User user= LoginUtils.getUser();
        Comment comment=new Comment();
        comment.setContent(edt_comment.getText().toString());
        comment.setCreated_by_id(user.getUser_id());
        comment.setStatus(AppConstants.STATUS_ACTIVE);
        comment.setPost_id(postID);
        postViewModel.addComment(comment).observe(this, new Observer<BaseModel<List<Comment>>>() {
            @Override
            public void onChanged(BaseModel<List<Comment>> listBaseModel) {
                if(!listBaseModel.isError())
                {

                    Toast.makeText(getContext(),getString(R.string.msg_comment_created),Toast.LENGTH_LONG).show();
                    edt_comment.setText("");
                    //networkResponseDialog(getString(R.string.success),getString(R.string.msg_comment_created));
                    getComments();
                }
                else
                {
                    networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void getComments() {
        showDialog();
        postViewModel.getComments(post).observe(this, new Observer<BaseModel<List<Comment>>>() {
            @Override
            public void onChanged(BaseModel<List<Comment>> listBaseModel) {
                comments.clear();
                if(listBaseModel!=null && !listBaseModel.isError())
                {
                    comments.addAll(listBaseModel.getData());
                    Collections.reverse(comments);
                    commentsAdapter.notifyDataSetChanged();
                }
                else
                {
                    networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void initializeSlider() {
        sliderImageAdapter = new SliderImageAdapter(getContext(),post.getPost_image());
        imageSlider.setSliderAdapter(sliderImageAdapter);
        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
        imageSlider.startAutoCycle();
    }

    private void initRecyclerView() {
        commentsAdapter=new CommentsAdapter(getContext(),comments);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        rc_comments.setLayoutManager(linearLayoutManager);
        rc_comments.setAdapter(commentsAdapter);
    }

    @Override
    public void onValidationSucceeded() {
        addComment(post.getId());
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(getContext());
            // Display error messages
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
            }
        }
    }

    @OnClick(R.id.img_like)
    public void likePost(View view)
    {
     //   showDialog();
        post.setPost_id(post.getId());
        User user=LoginUtils.getLoggedinUser();
        post.setCreated_by_id(user.getId());
        if(post.getIs_post_like()==null || post.getIs_post_like()<1)
        {
            post.setAction(AppConstants.LIKE);
            if(post.getIs_post_like()==null)
            {
                post.setIs_post_like(1);

            }
            else
            {
                post.setIs_post_like(post.getIs_post_like()+1);

            }
        }
        else
        {
            post.setAction(AppConstants.UNLIKE);
            post.setIs_post_like(post.getIs_post_like()-1);
          //  post.setLike_count(post.getLike_count()-1);
        }
        Log.d("Detail status",post.getAction()+" count"+post.getIs_post_like());
        postViewModel.likePost(post).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> listBaseModel) {
                if(listBaseModel!=null && !listBaseModel.isError())
                {

                 AppUtils.setLikeCount(getContext(),tv_likecount,post.getAction(),img_like);

                }
                else
                {
                    networkResponseDialog(getString(R.string.error),getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    @OnClick(R.id.tv_connect)
    public void onConnectClick() {
        if(NetworkUtils.isNetworkConnected(getContext())) {
            callConnectApi();
        }
        else
        {
            networkErrorDialog();
        }

    }

    private void callConnectApi() {
        if(tv_connect.getText().toString().equalsIgnoreCase(getString(R.string.connect)))
        {
            showDialog();
            User user=LoginUtils.getLoggedinUser();
            Connection connection=new Connection();
            connection.setReceiver_id(post.getUser().getId());
            connection.setSender_id(user.getId());
            connection.setStatus(AppConstants.STATUS_PENDING);
            connectionViewModel.connect(connection).observe(this, new Observer<BaseModel<List<Connection>>>() {
                @Override
                public void onChanged(BaseModel<List<Connection>> listBaseModel) {
                    if(listBaseModel!=null && !listBaseModel.isError())
                    {

                        tv_connect.setText(AppConstants.REQUEST_SENT);
                        post.setIs_connected(AppConstants.ACTIVE);
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
}
