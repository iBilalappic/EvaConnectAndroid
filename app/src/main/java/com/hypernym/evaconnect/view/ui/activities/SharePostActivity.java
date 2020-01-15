package com.hypernym.evaconnect.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.smarteist.autoimageslider.SliderView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SharePostActivity extends AppCompatActivity {

    @BindView(R.id.user_image)
    ImageView user_image;

    @BindView(R.id.tv_username)
    TextView tv_username;

    @BindView(R.id.tv_minago)
    TextView tv_minago;

    @BindView(R.id.tv_connections)
    TextView tv_connections;

    @BindView(R.id.tv_userconnections)
    TextView tv_userconnections;

    @BindView(R.id.tv_content)
    TextView tv_content;

    @BindView(R.id.slider_images)
    SliderView slider_images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        User user=LoginUtils.getLoggedinUser();
        AppUtils.setGlideImage(this,user_image,user.getUser_image());
        tv_username.setText(user.getFirst_name());
        tv_userconnections.setText(AppUtils.getConnectionsCount(user.getTotal_connection()));
    }
}
