package com.hypernym.evaconnect.view.ui.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Comment;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.CommentsAdapter;
import com.hypernym.evaconnect.view.adapters.SliderImageAdapter;
import com.hypernym.evaconnect.view.dialogs.ShareDialog;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.NewsViewModel;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewsDetailsFragment extends BaseFragment implements Validator.ValidationListener,CommentsAdapter.OnItemClickListener {

    @BindView(R.id.rc_comments)
    RecyclerView rc_comments;

    @NotEmpty
    @BindView(R.id.edt_comment)
    EditText edt_comment;

    @BindView(R.id.btn_addcomment)
    ImageView btn_addcomment;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_minago)
    TextView tv_minago;

    @BindView(R.id.tv_createddateTime)
    TextView tv_createddateTime;

    @BindView(R.id.tv_likecount)
    TextView tv_likecount;

    @BindView(R.id.tv_comcount)
    TextView tv_comcount;

    @BindView(R.id.img_user)
    ImageView img_user;

    @BindView(R.id.img_like)
    ImageView img_like;

    @BindView(R.id.img_share)
    ImageView img_share;



    @BindView(R.id.profile_image)
    ImageView profile_image;


    @BindView(R.id.img_video)
    ImageView img_video;


    @BindView(R.id.tv_goback)
    TextView tv_goback;

    @BindView(R.id.channelname)
    TextView tv_channelName;

    @BindView(R.id.tv_newstitle)
    TextView tv_newstitle;

    @BindView(R.id.tv_url)
    TextView tv_url;

    @BindView(R.id.tv_visit)
    TextView tv_visit;

    @BindView(R.id.layout_editcomment)
    LinearLayout layout_editcomment;

    @BindView(R.id.button_cancel)
    Button button_cancel;

    @BindView(R.id.button_save)
    Button button_save;

    private CommentsAdapter commentsAdapter;
    private SliderImageAdapter sliderImageAdapter;
    private List<Comment> comments = new ArrayList<>();
    private NewsViewModel newsViewModel;

    Post post = new Post();
    int post_id,comment_id;
    private Validator validator;
    private ConnectionViewModel connectionViewModel;

    public NewsDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_news_details, container, false);
        ButterKnife.bind(this, view);
        init();

        newsViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication())).get(NewsViewModel.class);
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
        newsViewModel.getNewsByID(id).observe(this, new Observer<BaseModel<List<Post>>>() {
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

        post.setPost_type(AppConstants.NEWS_TYPE);
    }

    private void init() {
        validator = new Validator(this);
        validator.setValidationListener(this);
        showBackButton();
        setPageTitle("News Details");
    }

    private void setPostData(Post post) {

        tv_comcount.setText(String.valueOf(post.getComment_count()));
        tv_likecount.setText(String.valueOf(post.getLike_count()));
      //  tv_connections.setText(AppUtils.getConnectionsCount(post.getUser().getTotal_connection()));
        tv_createddateTime.setText(DateUtils.getFormattedDateTime(post.getCreated_datetime()));
        tv_minago.setText(DateUtils.getTimeAgo(post.getCreated_datetime()));
        tv_name.setText(post.getNews_source().getName());
        tv_channelName.setText(post.getNews_source().getName());
        tv_newstitle.setText(post.getTitle());
        tv_url.setText(post.getNews_source().getUrl());

        AppUtils.setGlideImage(getContext(), profile_image, post.getNews_source().getImage());

        AppUtils.setGlideUrlThumbnail(getContext(),img_video,post.getImage());

        if (post.getIs_post_like() != null && post.getIs_post_like() > 0) {
            img_like.setBackground(getContext().getDrawable(R.drawable.like_selected));
        } else {
            img_like.setBackground(getContext().getDrawable(R.drawable.ic_like));
        }



    }

    private void addComment(int postID) {
        showDialog();
        User user = LoginUtils.getUser();
        Comment comment = new Comment();
        String toServerUnicodeEncoded = StringEscapeUtils.escapeJava(edt_comment.getText().toString());

        comment.setContent(toServerUnicodeEncoded);
        comment.setCreated_by_id(user.getId());
        comment.setStatus(AppConstants.STATUS_ACTIVE);
        comment.setPost_id(postID);
        newsViewModel.addComment(comment).observe(this, new Observer<BaseModel<List<Comment>>>() {
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
        newsViewModel.getComments(post).observe(this, new Observer<BaseModel<List<Comment>>>() {
            @Override
            public void onChanged(BaseModel<List<Comment>> listBaseModel) {
                comments.clear();
                if (listBaseModel != null && !listBaseModel.isError()) {
                    comments.addAll(listBaseModel.getData());

                    Collections.reverse(comments);
                    commentsAdapter.notifyDataSetChanged();
                    post.setComment_count(comments.size());
                    tv_comcount.setText(String.valueOf(comments.size()));
                    layout_editcomment.setVisibility(View.GONE);
                    btn_addcomment.setVisibility(View.VISIBLE);
                    edt_comment.setText("");
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }


    private void initRecyclerView() {
        commentsAdapter = new CommentsAdapter(getContext(), comments,this);
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
                newsViewModel.likePost(post).observe(getActivity(), new Observer<BaseModel<List<Post>>>() {
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



    @OnClick(R.id.tv_visit)
    public void playVideo() {

        LoadUrlFragment loadUrlFragment = new LoadUrlFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", post.getLink());
        loadUrlFragment.setArguments(bundle);
        loadFragment(R.id.framelayout, loadUrlFragment, getContext(), true);
    }

    @OnClick(R.id.tv_goback)
    public void goBack() {
        getActivity().onBackPressed();
    }


    @Override
    public void onEditComment(View view, int position, String comment) {
        layout_editcomment.setVisibility(View.VISIBLE);
        btn_addcomment.setVisibility(View.GONE);
        edt_comment.setText(comment);
        comment_id=comments.get(position).getId();
    }

    @Override
    public void onDeleteComment(View view, int position) {
        newsViewModel.deleteComment(comments.get(position).getId()).observe(this, new Observer<BaseModel<List<Comment>>>() {
            @Override
            public void onChanged(BaseModel<List<Comment>> listBaseModel) {
                comments.remove(position);
                commentsAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.button_cancel)
    public void cancel()
    {
        layout_editcomment.setVisibility(View.GONE);
        btn_addcomment.setVisibility(View.VISIBLE);
        edt_comment.setText("");
    }

    @OnClick(R.id.button_save)
    public void saveComment()
    {
        Comment newcomment=new Comment();
        newcomment.setId(comment_id);
        newcomment.setContent(edt_comment.getText().toString());
        newcomment.setModified_by_id(LoginUtils.getLoggedinUser().getId());
        newcomment.setModified_datetime(DateUtils.GetCurrentdatetime());

        newsViewModel.editComment(newcomment).observe(this, new Observer<BaseModel<List<Comment>>>() {
            @Override
            public void onChanged(BaseModel<List<Comment>> listBaseModel) {
                if(NetworkUtils.isNetworkConnected(getContext()))
                {
                   // getEventDetails(event_id);
                    getComments();
                }
                else
                {
                    networkErrorDialog();
                }
            }
        });
    }

}
