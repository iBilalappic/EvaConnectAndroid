package com.hypernym.evaconnect.view.ui.fragments;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.hypernym.evaconnect.listeners.PaginationScrollListener.PAGE_START;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.listeners.PaginationScrollListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.ConnectionModel;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.Constants;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.adapters.PostAdapter;
import com.hypernym.evaconnect.view.bottomsheets.BottomsheetShareSelection;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.PostViewModel;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonProfileFragment extends BaseFragment implements View.OnClickListener, PostAdapter.ItemClickListener {

    @BindView(R.id.profile_image)
    ImageView profile_image;

    @BindView(R.id.bell)
    ImageView bell;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_connect)
    TextView tv_connect;
    @BindView(R.id.tv_view_post)
    TextView tv_view_post;

    @BindView(R.id.view_view_post)
    View view_view_post;
    @BindView(R.id.layout_account_private)
    ConstraintLayout layout_account_private;

    @BindView(R.id.tv_profession)
    TextView tv_profession;

    @BindView(R.id.tv_company)
    TextView tv_company;

 /*   @BindView(R.id.tv_location)
    TextView tv_location;*/

    @BindView(R.id.tv_connections_count)
    TextView tv_connections_count;

    @BindView(R.id.layout_notification)
    LinearLayout layout_notification;

    @BindView(R.id.layout_EditDetail)
    LinearLayout layout_EditDetail;

    @BindView(R.id.layout_message)
    TextView layout_message;

    @BindView(R.id.layout_disconnect)
    LinearLayout layout_disconnect;

    @BindView(R.id.layout_settings)
    LinearLayout layout_settings;

    @BindView(R.id.layout_block)
    TextView layout_block;

    @BindView(R.id.view0)
    View view0;

    @BindView(R.id.view1)
    View view1;

    @BindView(R.id.view2)
    View view2;

    @BindView(R.id.view3)
    View view3;

    @BindView(R.id.view4)
    View view4;

    @BindView(R.id.img_backarrow)
    ImageView img_backarrow;

    @BindView(R.id.tv_bio)
    TextView tv_bio;
    @BindView(R.id.tv_connections)
    TextView tv_connections;


    @BindView(R.id.rc_post)
    RecyclerView rc_post;


    SimpleDialog simpleDialog;
    Post post = new Post();
    User user = new User();
    User targetUser = new User();
    ConnectionModel connected_user = new ConnectionModel();
    private ConnectionViewModel connectionViewModel;
    private UserViewModel userViewModel;
    String argumentReceived = "";

    //for view post
    private PostViewModel postViewModel;
    private PostAdapter postAdapter;
    private List<Post> posts = new ArrayList<>();
    int itemCount = 0;
    int item_position;
    private LinearLayoutManager linearLayoutManager;
    private int currentPage = PAGE_START;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int user_id=0;

    public PersonProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person_profile, container, false);
        ButterKnife.bind(this, view);
        layout_disconnect.setOnClickListener(this);
        layout_block.setOnClickListener(this);
        layout_EditDetail.setOnClickListener(this);
        layout_settings.setOnClickListener(this);
        img_backarrow.setOnClickListener(this);
        layout_notification.setOnClickListener(this);
        layout_message.setOnClickListener(this);
        init();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        posts.clear();
        // callPostsApi();
    }

    private void init() {
        connectionViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(ConnectionViewModel.class);
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(UserViewModel.class);

        hideChatPerson();
        setPageTitle("Profile");
        user = LoginUtils.getLoggedinUser();


        if (user.getType() != null && user.getType().equalsIgnoreCase("company")) {
            tv_connections.setText("Followers");
        } else {
            tv_connections.setText("Connections");
        }

        if ((getArguments() != null)) {
            //  showBackButton();

            targetUser = getArguments().getParcelable("user");
            connected_user = getArguments().getParcelable("connected_user");
            post = (Post) getArguments().getSerializable("PostData");
            user_id = getArguments().getInt("user_id");
            Log.d("profile", "Recived User Id : "+String.valueOf(user_id));


            if (post != null) {
                if (!TextUtils.isEmpty(post.getUser().getUser_image())) {
                    AppUtils.setGlideImage(getContext(), profile_image, post.getUser().getUser_image());
                }

//            else if (post.getUser().getIs_facebook() == 1 && !TextUtils.isEmpty(post.getUser().getFacebook_image_url())){
//                AppUtils.setGlideImage(getContext(), profile_image, post.getUser().getFacebook_image_url());
//            }
//            else {
//                AppUtils.setGlideImage(getContext(), profile_image, post.getUser().getUser_image());
//            }

                tv_name.setText(post.getUser().getFirst_name());

                if (post.getUser().getBio_data() != null && !post.getUser().getBio_data().isEmpty()) {
                    tv_bio.setText(post.getUser().getBio_data());
                } else {
                    tv_bio.setVisibility(View.GONE);
                }

                if (post.getUser().getDesignation() != null && !post.getUser().getDesignation().isEmpty()) {
                    tv_profession.setText(post.getUser().getDesignation());
                } else {
                    tv_profession.setText(post.getUser().getCompany_name());
                }

                Log.d("user", "init: "+GsonUtils.getGson().toJson(user));
//            tv_location.setText(post.getUser().getCountry() + "," + post.getUser().getCity());
            tv_company.setText(post.getUser().getCity() + " | " + post.getUser().getCountry());
                tv_connections_count.setText(String.valueOf(post.getUser().getTotal_connection()));



//            if(post.getUser().getIs_notifications()>0)
//            {
//                bell.setImageResource(R.drawable.ic_notification_bell);
//            }
//            else
//            {
//                bell.setImageResource(R.drawable.ic_bell);
//            }


                getUserDetails();

                if (post.getUser().getId().equals(user.getId())) {
                    tv_connect.setVisibility(View.GONE);

                    layout_account_private.setVisibility(View.GONE);

                    layout_message.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);

                    /* *//* layout_disconnect.setVisibility(View.GONE);*//*
                    view4.setVisibility(View.GONE);*/
                    layout_block.setVisibility(View.GONE);

                    layout_settings.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);

                    layout_EditDetail.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);

                    layout_notification.setVisibility(View.VISIBLE);
                    view0.setVisibility(View.VISIBLE);

                } else {
                    tv_connect.setVisibility(View.VISIBLE);
                    layout_message.setVisibility(View.VISIBLE);
                    view3.setVisibility(View.VISIBLE);

                   /* layout_disconnect.setVisibility(View.VISIBLE);
                    view4.setVisibility(View.VISIBLE);*/

                    layout_block.setVisibility(View.VISIBLE);

                    layout_settings.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);

                    layout_EditDetail.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);

                    layout_notification.setVisibility(View.GONE);

                    view0.setVisibility(View.GONE);


                    String status = AppUtils.getConnectionStatus(requireContext(), post.getIs_connected(), post.isIs_receiver());
                    if (status.equals(AppConstants.DELETED)) {
                        tv_connect.setVisibility(View.GONE);
                    } else {
                        tv_connect.setVisibility(View.VISIBLE);
                        layout_account_private.setVisibility(View.GONE);
                        tv_connect.setText(AppUtils.getConnectionStatusWithUserType(requireContext(), post.getIs_connected(), post.isIs_receiver(), user.getType()));
                        if (AppUtils.getConnectionStatus(requireContext(), post.getIs_connected(), post.isIs_receiver()).equalsIgnoreCase(AppConstants.CONNECTED)) {
                            /*  layout_disconnect.setVisibility(View.VISIBLE);*/
                            layout_block.setVisibility(View.VISIBLE);
                            layout_account_private.setVisibility(View.GONE);
                            rc_post.setVisibility(View.VISIBLE);
                            view_view_post.setVisibility(View.VISIBLE);
                            tv_view_post.setVisibility(View.VISIBLE);
                        } else {
                            /* layout_disconnect.setVisibility(View.GONE);*/
                            layout_account_private.setVisibility(View.VISIBLE);
                            layout_block.setVisibility(View.GONE);
                            rc_post.setVisibility(View.GONE);
                            view_view_post.setVisibility(View.GONE);
                            tv_view_post.setVisibility(View.GONE);
                        }
                    }
                }
                tv_connect.setOnClickListener(new OnOneOffClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        if (NetworkUtils.isNetworkConnected(getContext())) {
                            callConnectApi(tv_connect, post.getUser());
                        } else {
                            networkErrorDialog();
                        }
                    }
                });
            } else if (targetUser != null) {
                argumentReceived = "user";
                if (!TextUtils.isEmpty(targetUser.getUser_image())) {
                    AppUtils.setGlideImage(getContext(), profile_image, targetUser.getUser_image());

                }

                tv_name.setText(targetUser.getFirst_name());

                if (targetUser.getBio_data() != null && !targetUser.getBio_data().isEmpty()) {
                    tv_bio.setText(targetUser.getBio_data());
                } else {
                    tv_bio.setVisibility(View.GONE);
                }

                if (targetUser.getDesignation() != null && !targetUser.getDesignation().isEmpty()) {
                    tv_profession.setText(targetUser.getDesignation());
                } else {
                    tv_profession.setText(targetUser.getCompany_name());
                }

                tv_connections_count.setText(String.valueOf(targetUser.getTotal_connection()));

                getUserDetails();

                if (targetUser.getId().equals(user.getId())) {
                    tv_connect.setVisibility(View.GONE);

                    layout_account_private.setVisibility(View.GONE);

                    layout_message.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);

                  /*  layout_disconnect.setVisibility(View.GONE);
                    view4.setVisibility(View.GONE);*/
                    layout_block.setVisibility(View.GONE);

                    layout_settings.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);

                    layout_EditDetail.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);

                    layout_notification.setVisibility(View.VISIBLE);
                    view0.setVisibility(View.VISIBLE);

                } else {
                    tv_connect.setVisibility(View.VISIBLE);
                    layout_message.setVisibility(View.VISIBLE);
                    view3.setVisibility(View.VISIBLE);


                    layout_settings.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);

                    layout_EditDetail.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);

                    layout_notification.setVisibility(View.GONE);

                    view0.setVisibility(View.GONE);


                    String status = AppUtils.getConnectionStatus(getContext(), targetUser.getIs_connected(), targetUser.isIs_receiver());
                    if (status.equals(AppConstants.DELETED)) {
                        tv_connect.setVisibility(View.GONE);
                    } else {
                        tv_connect.setVisibility(View.VISIBLE);
                        layout_account_private.setVisibility(View.GONE);
                        tv_connect.setText(AppUtils.getConnectionStatusWithUserType(getContext(), targetUser.getIs_connected(), targetUser.isIs_receiver(), user.getType()));
                        if (AppUtils.getConnectionStatus(getContext(), targetUser.getIs_connected(), targetUser.isIs_receiver()).equalsIgnoreCase(AppConstants.CONNECTED)) {
                            /* layout_disconnect.setVisibility(View.VISIBLE);*/
                            layout_block.setVisibility(View.GONE);
                            layout_account_private.setVisibility(View.GONE);
                            rc_post.setVisibility(View.VISIBLE);
                            view_view_post.setVisibility(View.VISIBLE);
                            tv_view_post.setVisibility(View.VISIBLE);
                        } else {
                            /*  layout_disconnect.setVisibility(View.GONE);*/
                            layout_account_private.setVisibility(View.VISIBLE);
                            layout_block.setVisibility(View.VISIBLE);
                            rc_post.setVisibility(View.GONE);
                            view_view_post.setVisibility(View.GONE);
                            tv_view_post.setVisibility(View.GONE);
                        }
                    }
                }
                User finalTargetUser = targetUser;
                tv_connect.setOnClickListener(new OnOneOffClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        if (NetworkUtils.isNetworkConnected(getContext())) {
                            callConnectApi(tv_connect, finalTargetUser);
                        } else {
                            networkErrorDialog();
                        }
                    }
                });

            }
            else if(user_id!=0){
                getUserDetails();
            }
        } else {
            user = LoginUtils.getLoggedinUser();
            Log.d("TAAAG", GsonUtils.toJson(user));

            if (!TextUtils.isEmpty(user.getUser_image())) {
                AppUtils.setGlideImage(getContext(), profile_image, user.getUser_image());

            }
            tv_name.setText(user.getFirst_name());

            //  tv_location.setText(user.getCountry() + "," + user.getCity());

            if (user.getSector() != null) {

                if (user.getDesignation() != null) {

                    tv_company.setText(user.getSector() + " | " + user.getDesignation());
                }
            }
            tv_connections_count.setText(String.valueOf(user.getConnection_count()));


            if (user.getDesignation() != null && !user.getDesignation().isEmpty()) {
                tv_profession.setText(user.getDesignation());
            } else {
                tv_profession.setText(user.getCompany_name());
            }
            tv_connect.setVisibility(View.GONE);
            layout_message.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);


            layout_block.setVisibility(View.GONE);

            tv_connect.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    if (NetworkUtils.isNetworkConnected(getContext())) {
                        callConnectApi(tv_connect, user);
                    } else {
                        networkErrorDialog();
                    }
                }
            });
            tv_company.setText(LoginUtils.getUser().getCity() + " , " + LoginUtils.getUser().getCountry());
        }


        postViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(PostViewModel.class);
        currentPage = PAGE_START;
        postAdapter = new PostAdapter(getContext(), posts, this);
        linearLayoutManager = new LinearLayoutManager(getContext());

        callPostsApi();


        rc_post.setLayoutManager(linearLayoutManager);
        rc_post.setAdapter(postAdapter);
        RecyclerView.ItemAnimator animator = rc_post.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
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

        // Added by Ali Raza

        tv_bio.setText(LoginUtils.getLoggedinUser().getBio_data());
    }


    @SuppressLint("SetTextI18n")
    public void getUserDetails() {
        //  User user = new User();
        // user = LoginUtils.getUser();
        int id;


        if (argumentReceived.equalsIgnoreCase("user")) {
            id = targetUser.getId();
        } else if (user_id != 0) {
            id = user_id;
        } else {
            id = post.getUser().getId();
        }

        Log.d("profile", "getUserDetails: " + id);

//213
        userViewModel.getuser_details(id, true).observe(getViewLifecycleOwner(), listBaseModel -> {
            if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                if (!TextUtils.isEmpty(listBaseModel.getData().get(0).getUser_image())) {
                    AppUtils.setGlideImage(getContext(), profile_image, listBaseModel.getData().get(0).getUser_image());
                }
                if (listBaseModel.getData().get(0).getType() != null && listBaseModel.getData().get(0).getType().equalsIgnoreCase("company")) {
                    tv_company.setText(listBaseModel.getData().get(0).getWork_aviation());

                } else {
                    tv_company.setText(listBaseModel.getData().get(0).getCompany_name() + " | " + listBaseModel.getData().get(0).getDesignation());

                }
                // tv_location.setText(listBaseModel.getData().get(0).getCountry() + "," + listBaseModel.getData().get(0).getCity());

                tv_name.setText(listBaseModel.getData().get(0).getFirst_name());

                if (listBaseModel.getData().get(0).getBio_data() != null && !listBaseModel.getData().get(0).getBio_data().isEmpty()) {
                    tv_bio.setText(listBaseModel.getData().get(0).getBio_data());
                } else {
                    tv_bio.setVisibility(View.GONE);
                }

                if (listBaseModel.getData().get(0).getDesignation() != null && !listBaseModel.getData().get(0).getDesignation().isEmpty()) {
                    tv_profession.setText(listBaseModel.getData().get(0).getDesignation());
                } else {
                    tv_profession.setText(listBaseModel.getData().get(0).getCompany_name());
                }
                tv_connections_count.setText(String.valueOf(listBaseModel.getData().get(0).getConnection_count()));
                if (listBaseModel.getData().get(0).getId().equals(user.getId())) {
                    tv_connect.setVisibility(View.GONE);

                    layout_account_private.setVisibility(View.GONE);

                    layout_message.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);

                    /* *//* layout_disconnect.setVisibility(View.GONE);*//*
                view4.setVisibility(View.GONE);*/
                    layout_block.setVisibility(View.GONE);

                    layout_settings.setVisibility(View.VISIBLE);
                    view2.setVisibility(View.GONE);

                    layout_EditDetail.setVisibility(View.VISIBLE);
                    view1.setVisibility(View.VISIBLE);

                    layout_notification.setVisibility(View.VISIBLE);
                    view0.setVisibility(View.VISIBLE);

                } else {
                    tv_connect.setVisibility(View.VISIBLE);
                    layout_message.setVisibility(View.VISIBLE);
                    view3.setVisibility(View.VISIBLE);

               /* layout_disconnect.setVisibility(View.VISIBLE);
                view4.setVisibility(View.VISIBLE);*/

                    layout_block.setVisibility(View.VISIBLE);

                    layout_settings.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);

                    layout_EditDetail.setVisibility(View.GONE);
                    view1.setVisibility(View.GONE);

                    layout_notification.setVisibility(View.GONE);

                    view0.setVisibility(View.GONE);

                    if (connected_user != null) {
                        Log.d("isReceiver", "getUserDetails: "+connected_user.isReceiver);

                        String status = AppUtils.getConnectionStatus(getContext(), connected_user.isConnected, connected_user.isReceiver);
                        if (status.equals(AppConstants.DELETED)) {
                            tv_connect.setVisibility(View.GONE);
                        } else {
                            tv_connect.setVisibility(View.VISIBLE);
                            layout_account_private.setVisibility(View.GONE);
                            tv_connect.setText(AppUtils.getConnectionStatusWithUserType(getContext(), connected_user.isConnected, connected_user.isReceiver, connected_user.type));
                            if (AppUtils.getConnectionStatus(getContext(), connected_user.isConnected, connected_user.isReceiver).equalsIgnoreCase(AppConstants.CONNECTED)) {
                                /*  layout_disconnect.setVisibility(View.VISIBLE);*/
                                layout_block.setVisibility(View.VISIBLE);
                                layout_account_private.setVisibility(View.GONE);
                                view_view_post.setVisibility(View.VISIBLE);
                                tv_view_post.setVisibility(View.VISIBLE);
                            } else {
                                /* layout_disconnect.setVisibility(View.GONE);*/
                                layout_account_private.setVisibility(View.VISIBLE);
                                layout_block.setVisibility(View.GONE);
                                view_view_post.setVisibility(View.GONE);
                                tv_view_post.setVisibility(View.GONE);
                            }
                        }

                    }

                }


                targetUser = listBaseModel.getData().get(0);

            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
        });
    }

    private void callPostsApi() {
        User user = new User();
        if (post != null) {
            Log.d("profile", "callPostsApi: Post :" + post.getCreated_by_id());
            if (post.getCreated_by_id() != null) {
                user.setUser_id(post.getCreated_by_id());
            }
        } else if (targetUser != null) {
            Log.d("profile", "callPostsApi: Target : " + targetUser.getUser_id());

            if (targetUser.getId() != null) {
                user.setUser_id(targetUser.getId());
                Log.d("profile", "callPostsApi setUser_id: Target : " + targetUser.getUser_id());

            }

        } else if (connected_user != null) {
            Log.d("profile", "callPostsApi: Connection : " + connected_user.id);

            if (connected_user.id != null) {

                Log.d("profile", "callPostsApi setUser_id: Connection : " + connected_user.id);

                user.setUser_id(connected_user.id);


                Log.d("profile", "callPostsApi: " + GsonUtils.toJson(user));
            }
        }


        postViewModel.getPostFilter(user, AppConstants.TOTAL_PAGES, currentPage).observe(getViewLifecycleOwner(), dashboardBaseModel -> {

            Log.d("profile", "api call: " + GsonUtils.toJson(user));


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
                    } else if (post.getType().equalsIgnoreCase("post") && post.getPost_image().size() == 0 && !post.isIs_url() && post.getPost_document() == null) {
                        post.setPost_type(AppConstants.TEXT_TYPE);
                    } else if (post.getType().equalsIgnoreCase("post") && post.isIs_url() && post.getPost_document() == null) {
                        post.setPost_type(AppConstants.LINK_POST);
                    } else if (post.getType().equalsIgnoreCase("post") && post.getPost_document() != null) {
                        post.setPost_type(AppConstants.DOCUMENT_TYPE);
                    }
                }
                posts.addAll(dashboardBaseModel.getData());


                postAdapter.notifyDataSetChanged();


                //  swipeRefresh.setRefreshing(false);
//                postAdapter.removeLoading();
                isLoading = false;
            } else if (dashboardBaseModel != null && !dashboardBaseModel.isError() && dashboardBaseModel.getData().size() == 0) {
                isLastPage = true;
//                postAdapter.removeLoading();
                isLoading = false;
            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }

         /*   if (posts.size() == 0) {
                rc_post.setVisibility(View.GONE);
                tv_view_post.setVisibility(View.GONE);
                view_view_post.setVisibility(View.GONE);
            } else {
                rc_post.setVisibility(View.VISIBLE);
                tv_view_post.setVisibility(View.VISIBLE);
                view_view_post.setVisibility(View.VISIBLE);
            }*/
        });
    }

    public void callConnectApi(TextView tv_connect, User connectionItem) {
        Connection connection = new Connection();
        User user = LoginUtils.getLoggedinUser();
        connection.setReceiver_id(connectionItem.getId());
        connection.setSender_id(user.getId());
        if (!tv_connect.getText().toString().equalsIgnoreCase(AppConstants.CONNECTED) && post.getConnection_id() == null && !tv_connect.getText().toString().equalsIgnoreCase(AppConstants.REQUEST_SENT)) {
            connection.setStatus(AppConstants.STATUS_PENDING);
            showDialog();
            callApi(tv_connect, connection, connectionItem);
        } else if (!tv_connect.getText().toString().equalsIgnoreCase(AppConstants.CONNECTED) && post.getConnection_id() != null && !tv_connect.getText().toString().equalsIgnoreCase(AppConstants.REQUEST_SENT)) {
            connection.setStatus(AppConstants.ACTIVE);
            connection.setId(post.getConnection_id());
            connection.setModified_by_id(user.getId());
            connection.setModified_datetime(DateUtils.GetCurrentdatetime());
            callApi(tv_connect, connection, connectionItem);
        }
    }

    private void callApi(TextView tv_connect, Connection connection, User connectionItem) {
        connectionViewModel.connect(connection).observe(this, listBaseModel -> {
            if (listBaseModel != null && !listBaseModel.isError()) {
                if (tv_connect.getText().toString().equalsIgnoreCase(getString(R.string.connect)) ||
                        tv_connect.getText().toString().equalsIgnoreCase(getString(R.string.invite_to_follow))) {

                    tv_connect.setText(AppConstants.INVITED);
                } else {
                    if (user.getType() != null && user.getType().equalsIgnoreCase("user")) {
                        tv_connect.setText(AppConstants.CONNECTED);
                    } else {
                        tv_connect.setText(AppConstants.INVITED);
                    }
                }
                connectionItem.setIs_connected(AppConstants.ACTIVE);

            } else {
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_disconnect:
                RemoveUserApiCall();
                break;
            case R.id.layout_block:
                if (connected_user != null) {
                    BlockUserApiCall(connected_user.id);
                } else {
                    BlockUserApiCall(post.getUser().getId());
                }
                break;
            case R.id.layout_message:

                if (tv_connect.getText().toString().equals("Connected")) {
                    Log.d("message", "Connected");


                    ChatFragment chatFragment = new ChatFragment();
                    Bundle bundlemessage = new Bundle();
                    if (argumentReceived.equalsIgnoreCase("PostData")) {
                        bundlemessage.putSerializable("user", post.getUser());
                    } else {
                        bundlemessage.putSerializable("user", targetUser);
                    }
                    chatFragment.setArguments(bundlemessage);
                    loadFragment(R.id.framelayout, chatFragment, getContext(), true);
                } else {
                    Log.d("message", "no Connected");

                    Toast.makeText(requireContext(), "Only connected user can chat.", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.layout_EditDetail:
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                loadFragment(R.id.framelayout, editProfileFragment, getContext(), true);
                break;
            case R.id.img_backarrow:
                getActivity().onBackPressed();
                break;
            case R.id.layout_notification:

                bell.setImageResource(R.drawable.ic_bell);
                ActivityFragment activityFragment = new ActivityFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("isNotification", true);
                activityFragment.setArguments(bundle);
                loadFragment(R.id.framelayout, activityFragment, getContext(), true);
                break;
            case R.id.layout_settings:
                SettingsFragment settingsFragment = new SettingsFragment();
                loadFragment(R.id.framelayout, settingsFragment, getContext(), true);
                break;
        }
    }

    private void BlockUserApiCall( int receiver_id) {

       /* int id;
        User user;
        if (argumentReceived.equalsIgnoreCase("Postuser")) {
            id = post.getUser().getConnection_id();
            user = post.getUser();
        } else {
            id = targetUser.getConnection_id();
            user = targetUser;
        }*/

        Connection connection = new Connection();
        User user = LoginUtils.getLoggedinUser();
        connection.setSender_id(user.getId());
        connection.setReceiver_id(receiver_id);
        connection.setStatus(AppConstants.DELETED);

        connectionViewModel.block(connection).observe(this, listBaseModel -> {
            if (!listBaseModel.isError()) {
                simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), getString(R.string.msg_block_user), null, getString(R.string.ok), v -> {
                    simpleDialog.dismiss();

                    requireActivity().onBackPressed();
                });
            } else {

                simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), "Your connection is already block", null, getString(R.string.ok), v -> {
                    getActivity().onBackPressed();
                    simpleDialog.dismiss();
                });
            }
            hideDialog();
            simpleDialog.show();
            simpleDialog.setCancelable(false);
        });


    }

    private void RemoveUserApiCall() {
        if (post.getConnection_id() != null) {
            connectionViewModel.remove_user(post.getConnection_id()).observe(this, new Observer<BaseModel<List<Object>>>() {
                @Override
                public void onChanged(BaseModel<List<Object>> listBaseModel) {
                    if (!listBaseModel.isError()) {

                        simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), getString(R.string.msg_remove_user), null, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getActivity().onBackPressed();
                                simpleDialog.dismiss();
                            }
                        });
                    } else {

                        simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), "Your connection is already remove", null, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getActivity().onBackPressed();
                                simpleDialog.dismiss();
                            }
                        });
                    }
                    hideDialog();
                    simpleDialog.show();
                    simpleDialog.setCancelable(false);
                }
            });
        } else {
            Toast.makeText(getActivity(), "Your connection with user does not exist!", Toast.LENGTH_SHORT).show();
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
    public void onDocumentClick(View view, int position) {

    }

    @Override
    public void onLikeClick(View view, int position, TextView likeCount, ProgressBar like_pb) {
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
        postViewModel.likePost(post).observe(this, listBaseModel -> {
            if (listBaseModel != null && !listBaseModel.isError()) {
                post.setLikeLoading(false);
                postAdapter.notifyItemChanged(position);
            } else {
                post.setLikeLoading(false);
                networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
            }
            hideDialog();
        });
    }

    @Override
    public void onShareClick(View view, int position) {

        item_position = position;
        BottomsheetShareSelection bottomSheetPictureSelection = new BottomsheetShareSelection(new YourDialogFragmentDismissHandler());
//        bottomSheetPictureSelection.show(getActivity().getSupportFragmentManager(), bottomSheetPictureSelection.getTag());
        bottomSheetPictureSelection.show(requireActivity().getSupportFragmentManager(), bottomSheetPictureSelection.getTag());




    }

    @Override
    public void onVideoClick(View view, int position) {

    }

    @Override
    public void onURLClick(View view, int position) {

    }

    @Override
    public void onProfileClick(View view, int position) {

    }

    @Override
    public void onConnectClick(View view, int position) {

    }

    @Override
    public void onMoreClick(View view, int position) {

    }

    @Override
    public void onEditClick(View view, int position) {

    }

    @Override
    public void onDeleteClick(View view, int position) {

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
                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
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
            } else if (msg.what == 103) {
                ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(CLIPBOARD_SERVICE);
                ClipData clip;
                clip = ClipData.newPlainText("label", "https://www.evaintmedia.com/" + posts.get(item_position).getType() + "/" + posts.get(item_position).getId());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(requireContext(), "link copied", Toast.LENGTH_SHORT).show();
            } else if (msg.what == 101) {
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

