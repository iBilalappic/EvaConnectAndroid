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
import com.hypernym.evaconnect.listeners.OnDoubleTapClickListner;
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
import com.hypernym.evaconnect.view.dialogs.ShareDialog;
import com.hypernym.evaconnect.view.dialogs.VideoViewDialog;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.PostViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.apache.commons.lang3.StringEscapeUtils;
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
public class PostDetailsFragment extends BaseFragment implements Validator.ValidationListener {

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

    @BindView(R.id.slider_images_detail)
    SliderView slider_images_detail;

    @BindView(R.id.img_user)
    ImageView img_user;

    @BindView(R.id.img_like)
    ImageView img_like;

    @BindView(R.id.img_share)
    ImageView img_share;



    @BindView(R.id.profile_image)
    ImageView profile_image;

    @BindView(R.id.tv_connect)
    TextView tv_connect;

    @BindView(R.id.img_video)
    ImageView img_video;

    @BindView(R.id.img_play)
    ImageView img_play;

    @BindView(R.id.link)
    TextView link;

    @BindView(R.id.tv_goback)
    TextView tv_goback;

    private CommentsAdapter commentsAdapter;
    private SliderImageAdapter sliderImageAdapter;
    private List<Comment> comments = new ArrayList<>();
    private PostViewModel postViewModel;
    Post post = new Post();
    int post_id;
    private Validator validator;
    private ConnectionViewModel connectionViewModel;

    public PostDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_details, container, false);
        ButterKnife.bind(this, view);
        init();

        postViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(PostViewModel.class);
        if (getArguments() != null) {
            post_id = getArguments().getInt("post");
            getPostDetails(post_id);
            // getPostDetails(getArguments().getInt("id"));
        }
        //  post=(Post)getArguments().getSerializable("post");
        //  setPostData(post);
//
        btn_addcomment.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                validator.validate();
            }
        });
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareDialog shareDialog;
                Bundle bundle = new Bundle();
                bundle.putSerializable("PostData",post);
                shareDialog = new ShareDialog(getContext(),bundle);
                shareDialog.show();
            }
        });
        return view;
    }

    private void getPostDetails(int id) {
        postViewModel.getPostByID(id).observe(this, new Observer<BaseModel<List<Post>>>() {
            @Override
            public void onChanged(BaseModel<List<Post>> listBaseModel) {
                if (listBaseModel != null && !listBaseModel.isError()) {
                    post = listBaseModel.getData().get(0);
                    settingpostType();
                    setPostData(listBaseModel.getData().get(0));
                    initRecyclerView();
                    setclickEvents();
                    getComments();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
            }
        });
    }

    private void settingpostType() {
        if (post.getContent() == null) {
            post.setContent("");
        }
        if (post.getType().equalsIgnoreCase("post") && post.getPost_image().size() > 0) {
            post.setPost_type(AppConstants.IMAGE_TYPE);
        } else if (post.getType().equalsIgnoreCase("post") && post.getPost_video() != null) {
            post.setPost_type(AppConstants.VIDEO_TYPE);
        } else if (post.getType().equalsIgnoreCase("post") && post.getPost_image().size() == 0 && AppUtils.containsURL(post.getContent()).size() == 0) {
            post.setPost_type(AppConstants.TEXT_TYPE);
        } else if (post.getType().equalsIgnoreCase("event")) {
            post.setPost_type(AppConstants.EVENT_TYPE);
        } else if (post.getType().equalsIgnoreCase("post") && AppUtils.containsURL(post.getContent()).size() > 0) {
            post.setPost_type(AppConstants.LINK_POST);
        }
    }

    private void init() {
        validator = new Validator(this);
        validator.setValidationListener(this);
        showBackButton();
    }

    private void setPostData(Post post) {
        initializeSlider(post);
        tv_comcount.setText(String.valueOf(post.getComment_count()));
        tv_likecount.setText(String.valueOf(post.getLike_count()));
        tv_connections.setText(AppUtils.getConnectionsCount(post.getUser().getTotal_connection()));
        tv_createddateTime.setText(DateUtils.getFormattedDateTime(post.getCreated_datetime()));
        tv_minago.setText(DateUtils.getTimeAgo(post.getCreated_datetime()));
        tv_name.setText(post.getUser().getFirst_name());
        tv_content.setText(post.getContent());
        //Hide connect option if post is from logged in user
        User user = LoginUtils.getLoggedinUser();
        if (post.getUser().getId() == user.getId()) {
            tv_connect.setVisibility(View.GONE);
        } else {
            tv_connect.setVisibility(View.VISIBLE);
            tv_connect.setText(AppUtils.getConnectionStatus(getContext(), post.getIs_connected(), post.isIs_receiver()));
        }
        if (post.getUser().getIs_linkedin() == 1) {
            AppUtils.setGlideImage(getContext(), profile_image, post.getUser().getLinkedin_image_url());

        } else {
            AppUtils.setGlideImage(getContext(), profile_image, post.getUser().getUser_image());
        }
        if (LoginUtils.getUser().getIs_linkedin() == 1) {
            AppUtils.setGlideImage(getContext(), img_user, user.getLinkedin_image_url());
        } else {
            AppUtils.setGlideImage(getContext(), img_user, user.getUser_image());
        }


        if (post.getIs_post_like() != null && post.getIs_post_like() > 0) {
            img_like.setBackground(getContext().getDrawable(R.mipmap.ic_like_selected));
        } else {
            img_like.setBackground(getContext().getDrawable(R.mipmap.ic_like));
        }
        slider_images_detail.setVisibility(View.GONE);
        img_video.setVisibility(View.GONE);
        img_play.setVisibility(View.GONE);
        link.setVisibility(View.GONE);
        if (post.getPost_type() == AppConstants.IMAGE_TYPE) {
            slider_images_detail.setVisibility(View.VISIBLE);
        } else if (post.getPost_type() == AppConstants.VIDEO_TYPE) {
            img_video.setVisibility(View.VISIBLE);
            img_play.setVisibility(View.VISIBLE);
            AppUtils.setGlideVideoThumbnail(getContext(), img_video, post.getPost_video());
        } else if (post.getPost_type() == AppConstants.LINK_POST) {
            img_video.setVisibility(View.VISIBLE);
            ArrayList<String> URLs = AppUtils.containsURL(post.getContent().toString());
            if (URLs.size() > 0) {
                AppUtils.customUrlEmbeddedView(getContext(), URLs.get(0), img_video);
                link.setText(URLs.get(0));
                link.setVisibility(View.VISIBLE);
            }

        }
    }

    private void addComment(int postID) {
        showDialog();
        User user = LoginUtils.getUser();
        Comment comment = new Comment();
        String toServerUnicodeEncoded = StringEscapeUtils.escapeJava(edt_comment.getText().toString());

        comment.setContent(toServerUnicodeEncoded);
        comment.setCreated_by_id(user.getUser_id());
        comment.setStatus(AppConstants.STATUS_ACTIVE);
        comment.setPost_id(postID);
        postViewModel.addComment(comment).observe(this, new Observer<BaseModel<List<Comment>>>() {
            @Override
            public void onChanged(BaseModel<List<Comment>> listBaseModel) {
                if (!listBaseModel.isError()) {

                    // Toast.makeText(getContext(), getString(R.string.msg_comment_created), Toast.LENGTH_LONG).show();
                    edt_comment.setText("");
                    //networkResponseDialog(getString(R.string.success),getString(R.string.msg_comment_created));
                    getComments();
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
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
                if (listBaseModel != null && !listBaseModel.isError()) {
                    comments.addAll(listBaseModel.getData());
                    Collections.reverse(comments);
                    commentsAdapter.notifyDataSetChanged();
                    post.setComment_count(comments.size());
                    tv_comcount.setText(String.valueOf(comments.size()));
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }

    private void initializeSlider(Post post) {
        sliderImageAdapter = new SliderImageAdapter(getContext(), post.getPost_image(), slider_images_detail);
        slider_images_detail.setSliderAdapter(sliderImageAdapter);
        slider_images_detail.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        slider_images_detail.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        slider_images_detail.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        slider_images_detail.setScrollTimeInSec(4); //set scroll delay in seconds :
        slider_images_detail.startAutoCycle();
    }

    private void initRecyclerView() {
        commentsAdapter = new CommentsAdapter(getContext(), comments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rc_comments.setLayoutManager(linearLayoutManager);
        rc_comments.setAdapter(commentsAdapter);
    }

    private void setclickEvents() {
        img_like.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                //   showDialog();
                post.setPost_id(post.getId());
                User user = LoginUtils.getLoggedinUser();
                post.setCreated_by_id(user.getId());
                if (post.getIs_post_like() == null || post.getIs_post_like() < 1) {
                    post.setAction(AppConstants.LIKE);
                    if (post.getIs_post_like() == null) {
                        post.setIs_post_like(1);

                    } else {
                        post.setIs_post_like(post.getIs_post_like() + 1);

                    }
                } else {
                    post.setAction(AppConstants.UNLIKE);
                    post.setIs_post_like(post.getIs_post_like() - 1);
                    //  post.setLike_count(post.getLike_count()-1);
                }
                Log.d("Detail status", post.getAction() + " count" + post.getIs_post_like());
                postViewModel.likePost(post).observe(getActivity(), new Observer<BaseModel<List<Post>>>() {
                    @Override
                    public void onChanged(BaseModel<List<Post>> listBaseModel) {
                        if (listBaseModel != null && !listBaseModel.isError()) {

                            AppUtils.setLikeCount(getContext(), tv_likecount, post.getAction(), img_like);
                            post.setLike_count(Integer.parseInt(tv_likecount.getText().toString()));

                        } else {
                            networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                        }
                        hideDialog();
                    }
                });
            }
        });

        tv_connect.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (NetworkUtils.isNetworkConnected(getContext())) {
                    post.getUser().setConnection_id(post.getConnection_id());
                    callConnectApi(tv_connect, post.getUser());
                } else {
                    networkErrorDialog();
                }
            }
        });

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

    @OnClick(R.id.img_video)
    public void playVideo() {

        AppUtils.playVideo(getContext(), post.getPost_video());
    }

    @OnClick(R.id.tv_goback)
    public void goBack() {
        getActivity().onBackPressed();
    }

    @OnClick(R.id.link)
    public void openUrl() {
        LoadUrlFragment loadUrlFragment = new LoadUrlFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", post.getContent());
        loadUrlFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, loadUrlFragment, getContext(), true);
    }

}
