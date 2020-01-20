package com.hypernym.evaconnect.view.ui.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.adapters.HomePostsAdapter;
import com.hypernym.evaconnect.view.adapters.SliderImageAdapter;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @BindView(R.id.profile_image)
    ImageView profile_image;

    @BindView(R.id.tv_name)
    TextView tv_name;

    @BindView(R.id.tv_createddateTime)
    TextView tv_createddateTime;

    @BindView(R.id.tv_close)
    TextView tv_close;

    private Post post=new Post();
    private SliderImageAdapter sliderImageAdapter;

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
        post=(Post) getIntent().getSerializableExtra("post");
        setPostData();
    }

    private void setPostData() {
        AppUtils.setGlideImage(this,profile_image,post.getUser().getUser_image());
        tv_name.setText(post.getUser().getFirst_name());
        tv_connections.setText(AppUtils.getConnectionsCount(post.getUser().getTotal_connection()));
        tv_minago.setText(DateUtils.getTimeAgo(post.getCreated_datetime()));
        tv_createddateTime.setText(DateUtils.getFormattedDateTime(post.getCreated_datetime()));
        tv_content.setText(post.getContent());
        if(post.getPost_image().size()>0)
        {
            initializeSlider();
        }
        else
        {
            slider_images.setVisibility(View.GONE);
        }
    }
    private void initializeSlider() {
        sliderImageAdapter = new SliderImageAdapter(this, post.getPost_image());
        slider_images.setSliderAdapter(sliderImageAdapter);
        slider_images.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        slider_images.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        slider_images.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        slider_images.setScrollTimeInSec(4); //set scroll delay in seconds :
        slider_images.startAutoCycle();
    }

    @OnClick(R.id.tv_close)
    public void close()
    {
        finish();
    }

}
