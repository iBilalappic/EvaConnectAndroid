package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventHomeAdapter extends RecyclerView.Adapter {

    private List<Post> posts = new ArrayList<>();
    private Context mContext;
    int total_types;
    private boolean fabStateVolume = false;
    private EventHomeAdapter.ItemClickListener mClickListener;
    private SliderImageAdapter sliderImageAdapter;
    // flag for footer ProgressBar (i.e. last item of list)
    private boolean isLoadingAdded = false;
    private boolean isLoaderVisible = false;
    GestureDetector gestureDetector;



    public class EventTypeViewHolder extends RecyclerView.ViewHolder {
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

        @BindView(R.id.view6)
        View top_image;

        @BindView(R.id.tv_attending)
        TextView tv_attending;

        @BindView(R.id.tv_location)
        TextView tv_location;

        @BindView(R.id.tv_eventdate)
        TextView tv_eventdate;

        public EventTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tv_viewcomments.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onEventItemClick(v, getAdapterPosition());
                }
            });

            tv_attending.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onEventItemClick(v, getAdapterPosition());
                }
            });
            img_like.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onEventLikeClick(v, getAdapterPosition(), tv_likecount);
                }
            });
            img_comment.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onEventItemClick(v, getAdapterPosition());
                }
            });
            img_share.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onShareClick(v, getAdapterPosition());
                }
            });
            tv_comcount.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    //  mClickListener.onItemClick(v, getAdapterPosition());
                }
            });

        }


    }



    public class LoadingTypeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public LoadingTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }





    public EventHomeAdapter(Context context, List<Post> data, EventHomeAdapter.ItemClickListener mClickListener) {
        this.posts = data;
        this.mContext = context;
        total_types = posts.size();
        this.mClickListener = mClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {

            case AppConstants.EVENT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_type, parent, false);
                return new EventHomeAdapter.EventTypeViewHolder(view);

        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        if (posts.get(position).getType() == null) {
            return AppConstants.LOADING_TYPE;
        } else {
            switch (posts.get(position).getPost_type()) {
                case AppConstants.TEXT_TYPE:
                    return (position == posts.size() - 1 && isLoadingAdded) ? AppConstants.LOADING_TYPE : AppConstants.TEXT_TYPE;
                case AppConstants.IMAGE_TYPE:
                    return (position == posts.size() - 1 && isLoadingAdded) ? AppConstants.LOADING_TYPE : AppConstants.IMAGE_TYPE;
                case AppConstants.EVENT_TYPE:
                    return (position == posts.size() - 1 && isLoadingAdded) ? AppConstants.LOADING_TYPE : AppConstants.EVENT_TYPE;
                case AppConstants.JOB_TYPE:
                    return (position == posts.size() - 1 && isLoadingAdded) ? AppConstants.LOADING_TYPE : AppConstants.JOB_TYPE;
                case AppConstants.VIDEO_TYPE:
                    return (position == posts.size() - 1 && isLoadingAdded) ? AppConstants.LOADING_TYPE : AppConstants.VIDEO_TYPE;
                case AppConstants.LINK_POST:
                    return (position == posts.size() - 1 && isLoadingAdded) ? AppConstants.LOADING_TYPE : AppConstants.LINK_POST;
                default:
                    return -1;
            }
        }
    }

    Post getItem(int position) {
        if (posts.size() > 0)
            return posts.get(position);
        else
            return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Post object = posts.get(position);
        if (object != null && object.getType() != null) {
            switch (object.getPost_type()) {

                case AppConstants.EVENT_TYPE:
                    if (String.valueOf(posts.get(position).getComment_count()).equals("0")) {
                        ((EventHomeAdapter.EventTypeViewHolder) holder).tv_viewcomments.setVisibility(View.GONE);
                    }
                    ((EventHomeAdapter.EventTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));
                    ((EventHomeAdapter.EventTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));

                    if (posts.get(position).getIs_event_like() != null && posts.get(position).getIs_event_like() > 0) {
                        ((EventHomeAdapter.EventTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.ic_like));
                    } else {
                        ((EventHomeAdapter.EventTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.ic_like));
                    }
                    if (posts.get(position).getEvent_image().size() > 0) {
                        AppUtils.setGlideVideoThumbnail(mContext, ((EventHomeAdapter.EventTypeViewHolder) holder).profile_image, posts.get(position).getEvent_image().get(0));
                    } else {
                        ((EventHomeAdapter.EventTypeViewHolder) holder).profile_image.setBackground(mContext.getDrawable(R.drawable.no_thumbnail));
                    }

                    ((EventHomeAdapter.EventTypeViewHolder) holder).tv_name.setText(posts.get(position).getName());
                    ((EventHomeAdapter.EventTypeViewHolder) holder).tv_location.setText(posts.get(position).getEvent_city());
                    ((EventHomeAdapter.EventTypeViewHolder) holder).tv_eventdate.setText(DateUtils.getFormattedDateDMY(posts.get(position).getStart_date()));

                    if (position == 0) {
                        ((EventHomeAdapter.EventTypeViewHolder) holder).top_image.setVisibility(View.GONE);
                    } else {
                        ((EventHomeAdapter.EventTypeViewHolder) holder).top_image.setVisibility(View.VISIBLE);
                    }

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

        void onLikeClick(View view, int position, TextView likeCount);

        void onJobLikeClick(View view, int position, TextView likeCount);

        void onShareClick(View view, int position);

        void onConnectClick(View view, int position);

        void onVideoClick(View view, int position);

        void onURLClick(View view, int position);

        void onProfileClick(View view, int position);

        void onApplyClick(View view, int position);

        void onEventItemClick(View view, int position);

        void onEventLikeClick(View view, int position, TextView likeCount);

    }

    private void initializeSlider(SliderView imageSlider, int position) {

        sliderImageAdapter = new SliderImageAdapter(mContext, posts.get(position).getPost_image(), imageSlider);
        imageSlider.setSliderAdapter(sliderImageAdapter);
        imageSlider.setIndicatorAnimation(IndicatorAnimations.WORM); //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
        imageSlider.startAutoCycle();

    }

    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    public void addLoading() {
        isLoaderVisible = true;
        posts.add(new Post());
        notifyItemInserted(posts.size() - 1);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = posts.size() - 1;
        Post item = getItem(position);
        if (item != null) {
            posts.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void addItems(List<Post> postItems) {
        posts.addAll(postItems);
        notifyDataSetChanged();
    }

}
