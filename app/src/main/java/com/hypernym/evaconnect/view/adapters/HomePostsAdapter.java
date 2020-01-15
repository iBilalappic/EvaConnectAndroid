package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.FitCenter;
import com.bumptech.glide.request.RequestOptions;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class HomePostsAdapter extends RecyclerView.Adapter {

    private List<Post> posts=new ArrayList<>();
    private  Context mContext;
    int total_types;
    private boolean fabStateVolume = false;
    private  HomePostsAdapter.ItemClickListener mClickListener;
    private  SliderImageAdapter sliderImageAdapter;

    public class TextTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_viewcomments)
        TextView tv_viewcomments;

        @BindView(R.id.tv_likecount)
        TextView tv_likecount;

        @BindView(R.id.tv_comcount)
        TextView tv_comcount;

        @BindView(R.id.img_like)
        ImageView img_like;

        @BindView(R.id.img_comment)
        ImageView img_comment;

        @BindView(R.id.img_share)
        ImageView img_share;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.profile_image)
        ImageView profile_image;

        @BindView(R.id.tv_createddateTime)
        TextView tv_createdDateTime;

        @BindView(R.id.tv_minago)
        TextView tv_minago;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.tv_connections)
        TextView tv_connections;

        public TextTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            tv_viewcomments.setOnClickListener(this);
            img_like.setOnClickListener(this);
            img_comment.setOnClickListener(this);
            img_share.setOnClickListener(this);
            tv_comcount.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                switch (v.getId())
                {
                    case R.id.img_like:
                        mClickListener.onLikeClick(v,getAdapterPosition(),tv_likecount);
                        break;
                    case R.id.tv_comcount:
                        mClickListener.onItemClick(v,getAdapterPosition());
                        break;
                    case R.id.img_comment:
                        mClickListener.onItemClick(v,getAdapterPosition());
                        break;
                    case R.id.tv_viewcomments:
                        mClickListener.onItemClick(v,getAdapterPosition());
                        break;
                    case R.id.img_share:
                        mClickListener.onShareClick(v,getAdapterPosition());
                        break;
                }
        }
    }

    public class EventTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_viewcomments)
        TextView tv_viewcomments;

        @BindView(R.id.tv_likecount)
        TextView tv_likecount;

        @BindView(R.id.tv_comcount)
        TextView tv_comcount;

        @BindView(R.id.img_like)
        ImageView img_like;

        @BindView(R.id.img_comment)
        ImageView img_comment;

        @BindView(R.id.img_share)
        ImageView img_share;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.profile_image)
        ImageView profile_image;

        @BindView(R.id.tv_connections)
        TextView tv_connections;


        public EventTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            tv_viewcomments.setOnClickListener(this);
            img_like.setOnClickListener(this);
            img_comment.setOnClickListener(this);
            img_share.setOnClickListener(this);
            tv_comcount.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                switch (v.getId())
                {
                    case R.id.img_like:
                        mClickListener.onLikeClick(v,getAdapterPosition(),tv_likecount);
                        break;
                    case R.id.img_comment:
                        mClickListener.onItemClick(v,getAdapterPosition());
                        break;
                    case R.id.tv_viewcomments:
                        mClickListener.onItemClick(v,getAdapterPosition());
                        break;
                    case R.id.img_share:
                        mClickListener.onShareClick(v,getAdapterPosition());
                        break;
                    case R.id.tv_comcount:
                        mClickListener.onItemClick(v,getAdapterPosition());
                        break;
                }
        }
    }
    public class JobTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_viewcomments)
        TextView tv_viewcomments;

        @BindView(R.id.tv_likecount)
        TextView tv_likecount;

        @BindView(R.id.tv_comcount)
        TextView tv_comcount;

        @BindView(R.id.img_like)
        ImageView img_like;

        @BindView(R.id.img_comment)
        ImageView img_comment;

        @BindView(R.id.img_share)
        ImageView img_share;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.profile_image)
        ImageView profile_image;

        @BindView(R.id.tv_connections)
        TextView tv_connections;

        public JobTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            tv_viewcomments.setOnClickListener(this);
            img_like.setOnClickListener(this);
            img_comment.setOnClickListener(this);
            img_share.setOnClickListener(this);
            tv_comcount.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                switch (v.getId())
                {
                    case R.id.img_like:
                        mClickListener.onLikeClick(v,getAdapterPosition(),tv_likecount);
                        break;
                    case R.id.img_comment:
                        mClickListener.onItemClick(v,getAdapterPosition());
                        break;
                    case R.id.tv_viewcomments:
                        mClickListener.onItemClick(v,getAdapterPosition());
                        break;
                    case R.id.img_share:
                        mClickListener.onShareClick(v,getAdapterPosition());
                        break;
                    case R.id.tv_comcount:
                        mClickListener.onItemClick(v,getAdapterPosition());
                        break;
                }
        }
    }

    public class ImageTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        @BindView(R.id.tv_viewcomments)
        TextView tv_viewcomments;

        @BindView(R.id.tv_likecount)
        TextView tv_likecount;

        @BindView(R.id.tv_comcount)
        TextView tv_comcount;

        @BindView(R.id.tv_createddateTime)
        TextView tv_createdDateTime;

        @BindView(R.id.tv_minago)
        TextView tv_minago;

        @BindView(R.id.slider_images)
        SliderView imageSlider;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.profile_image)
        ImageView profile_image;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_connections)
        TextView tv_connections;

        @BindView(R.id.img_like)
        ImageView img_like;

        @BindView(R.id.img_comment)
        ImageView img_comment;

        @BindView(R.id.img_share)
        ImageView img_share;


        public ImageTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            tv_viewcomments.setOnClickListener(this);
            img_like.setOnClickListener(this);
            img_comment.setOnClickListener(this);
            img_share.setOnClickListener(this);
            tv_comcount.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mClickListener != null)
                switch (v.getId())
                {
                    case R.id.img_like:
                        mClickListener.onLikeClick(v,getAdapterPosition(),tv_likecount);
                        break;
                    case R.id.img_comment:
                        mClickListener.onItemClick(v,getAdapterPosition());
                        break;
                    case R.id.tv_viewcomments:
                        mClickListener.onItemClick(v,getAdapterPosition());
                        break;
                    case R.id.img_share:
                        mClickListener.onShareClick(v,getAdapterPosition());
                        break;
                    case R.id.tv_comcount:
                        mClickListener.onItemClick(v,getAdapterPosition());
                        break;
                }
        }

    }


    public HomePostsAdapter(Context context,List<Post> data,HomePostsAdapter.ItemClickListener mClickListener) {
        this.posts = data;
        this.mContext = context;
        total_types = posts.size();
        this.mClickListener=mClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case AppConstants.TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_type, parent, false);
                return new TextTypeViewHolder(view);
            case AppConstants.IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                return new ImageTypeViewHolder(view);
            case AppConstants.EVENT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_type, parent, false);
                return new EventTypeViewHolder(view);
            case AppConstants.JOB_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_type, parent, false);
                return new JobTypeViewHolder(view);
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {

        switch (posts.get(position).getPost_type()) {
            case AppConstants.TEXT_TYPE:
                return AppConstants.TEXT_TYPE;
            case AppConstants.IMAGE_TYPE:
                return AppConstants.IMAGE_TYPE;
            case AppConstants.EVENT_TYPE:
                return AppConstants.EVENT_TYPE;
            case AppConstants.JOB_TYPE:
                return AppConstants.JOB_TYPE;
            default:
                return -1;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        Post object = posts.get(position);
        if (object != null) {
            switch (object.getPost_type()) {
                case AppConstants.TEXT_TYPE:
                    ((TextTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));
                    ((TextTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));
                    ((TextTypeViewHolder) holder).tv_createdDateTime.setText(DateUtils.getFormattedDateTime(posts.get(position).getCreated_datetime()));
                    ((TextTypeViewHolder) holder).tv_content.setText(posts.get(position).getContent());
                    ((TextTypeViewHolder) holder).tv_minago.setText(DateUtils.getTimeAgo(posts.get(position).getCreated_datetime()));
                    if(posts.get(position).getIs_post_like()!=null && posts.get(position).getIs_post_like()>0)
                    {
                        ((TextTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.mipmap.ic_like_selected));
                    }
                    else
                    {
                        ((TextTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.mipmap.ic_like));
                    }

                    AppUtils.setGlideImage(mContext,((TextTypeViewHolder) holder).profile_image,posts.get(position).getUser().getUser_image());
                    ((TextTypeViewHolder) holder).tv_name.setText(posts.get(position).getUser().getFirst_name());
                    ((TextTypeViewHolder) holder).tv_connections.setText(AppUtils.getConnectionsCount(posts.get(position).getTotal_connection()));


                    break;
                case AppConstants.IMAGE_TYPE:
                    initializeSlider(((ImageTypeViewHolder) holder).imageSlider,position);
                    ((ImageTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));
                    ((ImageTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));
                    ((ImageTypeViewHolder) holder).tv_createdDateTime.setText(DateUtils.getFormattedDateTime(posts.get(position).getCreated_datetime()));
                    ((ImageTypeViewHolder) holder).tv_content.setText(posts.get(position).getContent());
                    ((ImageTypeViewHolder) holder).tv_minago.setText(DateUtils.getTimeAgo(posts.get(position).getCreated_datetime()));
                    if(posts.get(position).getIs_post_like()!=null && posts.get(position).getIs_post_like()>0)
                    {
                        ((ImageTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.mipmap.ic_like_selected));
                    }
                    else
                    {
                        ((ImageTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.mipmap.ic_like));
                    }

                    AppUtils.setGlideImage(mContext,((ImageTypeViewHolder) holder).profile_image,posts.get(position).getUser().getUser_image());
                    ((ImageTypeViewHolder) holder).tv_name.setText(posts.get(position).getUser().getFirst_name());
                    ((ImageTypeViewHolder) holder).tv_connections.setText(AppUtils.getConnectionsCount(posts.get(position).getTotal_connection()));
//                    ((ImageTypeViewHolder) holder).txtType.setText(object.text);
//                    ((ImageTypeViewHolder) holder).image.setImageResource(object.data);
                    break;
                case AppConstants.EVENT_TYPE:
//                    if(posts.get(position).getIs_post_like()==1)
//                    {
//                        ((EventTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.mipmap.ic_like_selected));
//                    }
//                    else
//                    {
//                        ((EventTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.mipmap.ic_like));
//                    }
//
//                    Glide.with(mContext)
//                            .load(posts.get(position).getUser().getUser_image())
//                            .into(((EventTypeViewHolder) holder).profile_image);
//                    ((EventTypeViewHolder) holder).tv_name.setText(posts.get(position).getUser().getFirst_name());

                    break;
                case AppConstants.JOB_TYPE:
//                    if(posts.get(position).getIs_post_like()==1)
//                    {
//                        ((JobTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.mipmap.ic_like_selected));
//                    }
//                    else
//                    {
//                        ((JobTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.mipmap.ic_like));
//                    }
//
//                    Glide.with(mContext)
//                            .load(posts.get(position).getUser().getUser_image())
//                            .into(((JobTypeViewHolder) holder).profile_image);
//                    ((JobTypeViewHolder) holder).tv_name.setText(posts.get(position).getUser().getFirst_name());

                    break;

            }
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
        void onLikeClick(View view,int position,TextView likeCount);
        void onShareClick(View view,int position);
    }
    private void initializeSlider(SliderView imageSlider,int position) {
        sliderImageAdapter = new SliderImageAdapter(mContext,posts.get(position).getPost_image());
        imageSlider.setSliderAdapter(sliderImageAdapter);
        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
        imageSlider.startAutoCycle();
    }
}