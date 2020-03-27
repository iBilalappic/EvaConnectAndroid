package com.hypernym.evaconnect.view.ui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.utils.NetworkUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonDetailFragment extends BaseFragment implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

    @BindView(R.id.profile_image)
    ImageView profile_image;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_connect)
    TextView tv_connect;

    @BindView(R.id.tv_biodata)
    TextView tv_biodata;

    @BindView(R.id.tv_openchat)
    TextView tv_openchat;

    @BindView(R.id.img_options)
    ImageView img_options;


    Post post = new Post();
    User user = new User();

    public PersonDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_person_detail, container, false);
        ButterKnife.bind(this, view);
        tv_openchat.setOnClickListener(this);
        img_options.setOnClickListener(this);
        init();
        return view;
    }

    private void init() {
        setPageTitle("Profile");
        user = LoginUtils.getLoggedinUser();
        if ((getArguments() != null)) {
            showBackButton();
            post = (Post) getArguments().getSerializable("PostData");
            AppUtils.setGlideImage(getContext(), profile_image, post.getUser().getUser_image());
            tv_name.setText(post.getUser().getFirst_name());

            if (post.getUser().getId() == user.getId()) {
                tv_connect.setVisibility(View.GONE);
            } else {
                tv_connect.setVisibility(View.VISIBLE);
                tv_connect.setText(AppUtils.getConnectionStatus(getContext(), post.getIs_connected(), post.isIs_receiver()));
            }

            tv_biodata.setText(post.getUser().getBio_data());
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
            AppUtils.setGlideImage(getContext(), profile_image, user.getUser_image());
            tv_name.setText(user.getFirst_name());
            if (post.getUser().getId() == user.getId()) {
                tv_connect.setVisibility(View.GONE);
            } else {
                tv_connect.setVisibility(View.VISIBLE);
                tv_connect.setText(AppUtils.getConnectionStatus(getContext(), post.getIs_connected(), post.isIs_receiver()));
            }
            tv_biodata.setText(user.getBio_data());
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_openchat:
                MessageFragment fragment = new MessageFragment();
                loadFragment(R.id.framelayout, fragment, getContext(), true);
                break;
            case R.id.img_options:
                PopupMenu popup = new PopupMenu(getContext(), v);
                popup.setOnMenuItemClickListener(this);
                popup.inflate(R.menu.menu_options);
                popup.show();
                break;
        }

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
       // Toast.makeText(getContext(), "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
        switch (item.getItemId()) {
            case R.id.item_blockuser:
                // do your code
                return true;
            case R.id.item_removeuser:
                // do your code
                return true;
            default:
                return false;
        }
    }
}
