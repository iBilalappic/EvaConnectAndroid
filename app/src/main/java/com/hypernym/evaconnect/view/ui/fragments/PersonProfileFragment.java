package com.hypernym.evaconnect.view.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.repositories.CustomViewModelFactory;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.GsonUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;
import com.hypernym.evaconnect.viewmodel.UserViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PersonProfileFragment extends BaseFragment implements View.OnClickListener {

    @BindView(R.id.profile_image)
    ImageView profile_image;

    @BindView(R.id.bell)
    ImageView bell;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_connect)
    TextView tv_connect;


    @BindView(R.id.tv_profession)
    TextView tv_profession;

    @BindView(R.id.tv_company)
    TextView tv_company;

    @BindView(R.id.tv_location)
    TextView tv_location;

    @BindView(R.id.tv_connections_count)
    TextView tv_connections_count;

    @BindView(R.id.layout_notification)
    LinearLayout layout_notification;

    @BindView(R.id.layout_EditDetail)
    LinearLayout layout_EditDetail;

    @BindView(R.id.layout_message)
    LinearLayout layout_message;

    @BindView(R.id.layout_disconnect)
    LinearLayout layout_disconnect;

    @BindView(R.id.layout_settings)
    LinearLayout layout_settings;

    @BindView(R.id.layout_block)
    LinearLayout layout_block;

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

    SimpleDialog simpleDialog;
    Post post = new Post();
    User user = new User();
    private ConnectionViewModel connectionViewModel;
    private UserViewModel userViewModel;

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
        layout_notification.setOnClickListener(this);
        layout_message.setOnClickListener(this);
        init();
        return view;
    }

    private void init() {
        connectionViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(ConnectionViewModel.class);
        userViewModel = ViewModelProviders.of(this, new CustomViewModelFactory(getActivity().getApplication(), getActivity())).get(UserViewModel.class);

        hideChatPerson();
        setPageTitle("Profile");
        user = LoginUtils.getLoggedinUser();
        if ((getArguments() != null)) {
          //  showBackButton();
            post = (Post) getArguments().getSerializable("PostData");
            Log.d("TAAAG", GsonUtils.toJson(post));




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

            if (post.getUser().getDesignation() != null && !post.getUser().getDesignation().isEmpty()) {
                tv_profession.setText(post.getUser().getDesignation());
            }
            else
            {
                tv_profession.setText(post.getUser().getCompany_name());
            }

//            tv_location.setText(post.getUser().getCountry() + "," + post.getUser().getCity());
//            tv_company.setText(post.getUser().getSector() + " | " + post.getUser().getCompany_name());
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
                layout_message.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);


                layout_disconnect.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
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

                layout_disconnect.setVisibility(View.VISIBLE);
                view4.setVisibility(View.VISIBLE);

                layout_block.setVisibility(View.VISIBLE);

                layout_settings.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);

                layout_EditDetail.setVisibility(View.GONE);
                view1.setVisibility(View.GONE);

                layout_notification.setVisibility(View.GONE);
                view0.setVisibility(View.GONE);


                String status = AppUtils.getConnectionStatus(getContext(), post.getIs_connected(), post.isIs_receiver());
                if (status.equals(AppConstants.DELETED)) {
                    tv_connect.setVisibility(View.GONE);
                } else {
                    tv_connect.setVisibility(View.VISIBLE);
                    tv_connect.setText(AppUtils.getConnectionStatus(getContext(), post.getIs_connected(), post.isIs_receiver()));
                    if(AppUtils.getConnectionStatus(getContext(), post.getIs_connected(), post.isIs_receiver()).equalsIgnoreCase(AppConstants.CONNECTED))
                    {
                        layout_disconnect.setVisibility(View.VISIBLE);
                        layout_block.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        layout_disconnect.setVisibility(View.GONE);
                        layout_block.setVisibility(View.VISIBLE);
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
        } else {
            user = LoginUtils.getLoggedinUser();
            Log.d("TAAAG", GsonUtils.toJson(user));

            if (!TextUtils.isEmpty(user.getUser_image())) {
                AppUtils.setGlideImage(getContext(), profile_image, user.getUser_image());

            }
//            else if (user.getIs_facebook() == 1 && !TextUtils.isEmpty(user.getFacebook_image_url())){
//                AppUtils.setGlideImage(getContext(), profile_image, user.getFacebook_image_url());
//            }
//            else {
//                AppUtils.setGlideImage(getContext(), profile_image, user.getUser_image());
//            }

            tv_name.setText(user.getFirst_name());

            tv_location.setText(user.getCountry() + "," + user.getCity());
            tv_company.setText(user.getSector() + " | " + user.getCompany_name());
            tv_connections_count.setText(String.valueOf(user.getConnection_count()));


//            if(user.getIs_notifications()>0)
//            {
//                bell.setImageResource(R.drawable.ic_notification_bell);
//            }
//            else
//            {
//                bell.setImageResource(R.drawable.ic_bell);
//            }
            if (user.getDesignation() != null && !user.getDesignation().isEmpty()) {
                tv_profession.setText(user.getDesignation());
            }
            else
            {
                tv_profession.setText(user.getCompany_name());
            }
            tv_connect.setVisibility(View.GONE);
            layout_message.setVisibility(View.GONE);
            view3.setVisibility(View.GONE);


            layout_disconnect.setVisibility(View.GONE);
            view4.setVisibility(View.GONE);
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
        }
    }
    public void getUserDetails()
    {
      //  User user = new User();
       // user = LoginUtils.getUser();
        userViewModel.getuser_details(post.getUser().getId()
        ).observe(this, new Observer<BaseModel<List<User>>>() {
            @Override
            public void onChanged(BaseModel<List<User>> listBaseModel) {
                if (listBaseModel.getData() != null && !listBaseModel.isError()) {
                    tv_location.setText(listBaseModel.getData().get(0).getCountry() + "," + listBaseModel.getData().get(0).getCity());
                    tv_company.setText(listBaseModel.getData().get(0).getSector() + " | " + listBaseModel.getData().get(0).getCompany_name());
                } else {
                    networkResponseDialog(getString(R.string.error), getString(R.string.err_unknown));
                }
                hideDialog();
            }
        });
    }
    public void callConnectApi(TextView tv_connect,User connectionItem) {
        Connection connection=new Connection();
        User user= LoginUtils.getLoggedinUser();
        connection.setReceiver_id(connectionItem.getId());
        connection.setSender_id(user.getId());

        if(!tv_connect.getText().toString().equalsIgnoreCase(AppConstants.CONNECTED) && post.getConnection_id()==null && !tv_connect.getText().toString().equalsIgnoreCase(AppConstants.REQUEST_SENT))
        {
            connection.setStatus(AppConstants.STATUS_PENDING);
            showDialog();
            callApi(tv_connect,connection,connectionItem);
        }
        else if (!tv_connect.getText().toString().equalsIgnoreCase(AppConstants.CONNECTED) && post.getConnection_id()!=null && !tv_connect.getText().toString().equalsIgnoreCase(AppConstants.REQUEST_SENT))
        {
            connection.setStatus(AppConstants.ACTIVE);
            connection.setId(post.getConnection_id());
            connection.setModified_by_id(user.getId());
            connection.setModified_datetime(DateUtils.GetCurrentdatetime());
            callApi(tv_connect,connection,connectionItem);
        }
    }

    private void callApi(TextView tv_connect,Connection connection,User connectionItem )
    {

        connectionViewModel.connect(connection).observe(this, new Observer<BaseModel<List<Connection>>>() {
            @Override
            public void onChanged(BaseModel<List<Connection>> listBaseModel) {
                if(listBaseModel!=null && !listBaseModel.isError())
                {
                    if(tv_connect.getText().toString().equalsIgnoreCase(getString(R.string.connect)))
                    {
                        tv_connect.setText(AppConstants.REQUEST_SENT);

                    }
                    else
                    {
                        tv_connect.setText(AppConstants.CONNECTED);
                    }
                    connectionItem.setIs_connected(AppConstants.ACTIVE);

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_disconnect:
                RemoveUserApiCall();
                break;
            case R.id.layout_block:
                BlockUserApiCall();
                break;
            case R.id.layout_message:
                ChatFragment chatFragment=new ChatFragment();
                Bundle bundlemessage=new Bundle();
                bundlemessage.putSerializable("user",post.getUser());
                chatFragment.setArguments(bundlemessage);
                loadFragment(R.id.framelayout, chatFragment, getContext(), true);
                break;
            case R.id.layout_EditDetail:
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                loadFragment(R.id.framelayout, editProfileFragment, getContext(), true);
                break;
            case R.id.layout_notification:

                bell.setImageResource(R.drawable.ic_bell);
                ActivityFragment activityFragment=new ActivityFragment();
                Bundle bundle=new Bundle();
                bundle.putBoolean("isNotification",true);
                activityFragment.setArguments(bundle);
                loadFragment(R.id.framelayout, activityFragment, getContext(), true);
                break;
            case R.id.layout_settings:
                SettingsFragment settingsFragment = new SettingsFragment();
                loadFragment(R.id.framelayout, settingsFragment, getContext(), true);
                break;
        }
    }

    private void BlockUserApiCall() {

            connectionViewModel.block_user(post.getConnection_id(), post.getUser()).observe(this, new Observer<BaseModel<List<Object>>>() {
                @Override
                public void onChanged(BaseModel<List<Object>> listBaseModel) {
                    if (!listBaseModel.isError()) {
                        simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), getString(R.string.msg_block_user), null, getString(R.string.ok), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                getActivity().onBackPressed();
                                simpleDialog.dismiss();
                            }
                        });
                    } else {

                        simpleDialog = new SimpleDialog(getActivity(), getString(R.string.success), "Your connection is already block", null, getString(R.string.ok), new View.OnClickListener() {
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


}
