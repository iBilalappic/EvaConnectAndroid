package com.hypernym.evaconnect.view.ui.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
public class PersonDetailFragment extends BaseFragment {

    @BindView(R.id.profile_image)
    ImageView profile_image;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_connect)
    TextView tv_connect;

    @BindView(R.id.tv_biodata)
    TextView tv_biodata;
    Post post= new Post();
    User user=new User();
    public PersonDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_person_detail, container, false);
        ButterKnife.bind(this,view);
        init();
        return view;
    }

    private void init() {
        setPageTitle("Profile");
        if((getArguments() !=null))
        {
            showBackButton();
            post=(Post)getArguments().getSerializable("PostData");
            AppUtils.setGlideImage(getContext(),profile_image,post.getUser().getUser_image());
            tv_name.setText(post.getUser().getFirst_name());
            tv_connect.setText(AppUtils.getConnectionStatus(getContext(),post.getIs_connected(),post.isIs_receiver()));
            tv_biodata.setText(post.getUser().getBio_data());
        }
        else
        {
            user= LoginUtils.getLoggedinUser();
            AppUtils.setGlideImage(getContext(),profile_image,user.getUser_image());
            tv_name.setText(user.getFirst_name());
            tv_connect.setText(AppUtils.getConnectionStatus(getContext(),user.getIs_connected(),user.isIs_receiver()));
            tv_biodata.setText(user.getBio_data());
        }

        tv_connect.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if(NetworkUtils.isNetworkConnected(getContext())) {
                    callConnectApi(tv_connect,user);
                }
                else
                {
                    networkErrorDialog();
                }
            }
        });
    }
}
