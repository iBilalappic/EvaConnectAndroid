package com.hypernym.evaconnect.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
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
    SimpleDialog simpleDialog;



    public class EventTypeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_viewcomments)
        TextView tv_viewcomments;

        @BindView(R.id.tv_likecount)
        TextView tv_likecount;

        @BindView(R.id.tv_comcount)
        TextView tv_comcount;

        @BindView(R.id.tv_detail)
        TextView tv_detail;
/*
        @BindView(R.id.img_like)
        ImageView img_like;*/

        @BindView(R.id.like_click)
        LinearLayout like_click;

        @BindView(R.id.img_comment)
        ImageView img_comment;

        @BindView(R.id.comment_click)
        LinearLayout comment_click;

        @BindView(R.id.img_share)
        ImageView img_share;

        @BindView(R.id.share_click)
        LinearLayout share_click;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_address)
        TextView tv_address;

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.profile_image)
        ImageView profile_image;

        @BindView(R.id.post_image)
        ImageView post_image;

        @BindView(R.id.view6)
        View top_image;

        @BindView(R.id.tv_attending)
        TextView tv_attending;

        @BindView(R.id.tv_location)
        TextView tv_location;

        @BindView(R.id.tv_eventdate)
        TextView tv_eventdate;

        @BindView(R.id.post_detail)
        TextView post_detail;

        @BindView(R.id.img_more)
        ImageView img_more;

        @BindView(R.id.linearLayout3)
        ConstraintLayout linearLayout3;

        @BindView(R.id.linearLayout4)
        ConstraintLayout linearLayout4;

        @BindView(R.id.tv_interested)
        TextView tv_interested;

        @BindView(R.id.tv_created_time)
        TextView tv_created_time;

        @BindView(R.id.like_pb)
        ProgressBar like_pb;



        public EventTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tv_interested.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {

                    mClickListener.onEventItemClick(v, getAdapterPosition());
                }
            });
            tv_viewcomments.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onEventItemClick(v, getAdapterPosition());
                }
            });
            post_image.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onEventItemClick(v, getAdapterPosition());
                }
            });

            tv_detail.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onEventItemClick(v, getAdapterPosition());
                }
            });
            tv_attending.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    posts.get(getAdapterPosition()).setLikeLoading(true);
                   notifyItemChanged(getAdapterPosition());

                    mClickListener.onEventItemClick(v, getAdapterPosition());
                }
            });
            like_click.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onEventLikeClick(v, getAdapterPosition(), tv_likecount);
                }
            });
            comment_click.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onEventItemClick(v, getAdapterPosition());
                }
            });
            share_click.setOnClickListener(new OnOneOffClickListener() {
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
            img_more.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    moreOptions(v,getAdapterPosition());
                }
            });

            // Added by AliRaza
            profile_image.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                      mClickListener.onProfileClick(v, getAdapterPosition());
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

    @SuppressLint({"SetTextI18n", "ResourceType"})
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        Log.d("api", "adapter is called: ");
        Post object = posts.get(position);
        if (object != null && object.getType() != null) {
            switch (object.getPost_type()) {

                case AppConstants.EVENT_TYPE:
                    Log.d("api", "adapter is inside eventType: ");
                    if (String.valueOf(posts.get(position).getComment_count()).equals("0")) {
                        ((EventHomeAdapter.EventTypeViewHolder) holder).tv_viewcomments.setVisibility(View.GONE);
                    }
                    if(posts.get(position).isLikeLoading()){
                        Log.d("api", "value: "+posts.get(position).isLikeLoading());
                        ((EventTypeViewHolder) holder).like_pb.setVisibility(View.VISIBLE);
                        ((EventTypeViewHolder) holder).tv_attending.setVisibility(View.GONE);

                    }
                    else{
                        ((EventTypeViewHolder) holder).like_pb.setVisibility(View.GONE);
                        ((EventTypeViewHolder) holder).tv_attending.setVisibility(View.VISIBLE);
                    }
                    ((EventHomeAdapter.EventTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));
                    ((EventHomeAdapter.EventTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));
                    if (posts.get(position).getAttendees() != null) {
                        ((EventHomeAdapter.EventTypeViewHolder) holder).tv_interested.setText(String.valueOf(posts.get(position).getAttendees()) + " interested");
                    } else {
                        ((EventHomeAdapter.EventTypeViewHolder) holder).tv_interested.setText(String.valueOf(0) + " interested");

                    }
                    ((EventHomeAdapter.EventTypeViewHolder) holder).tv_created_time.setText(DateUtils.getFormattedDateDMY(object.getStart_date()) + " - " + DateUtils.getFormattedDateDMY(object.getEnd_date()) + " | " + DateUtils.getTimeUTC(object.getStart_time()) + " - " + DateUtils.getTimeUTC(object.getEnd_time()));

                    if (posts.get(position).getIs_attending() != null) {
                        if (posts.get(position).getIs_attending().equalsIgnoreCase("Going")) {
                            Drawable[] compoundDrawables = ((EventTypeViewHolder) holder).tv_attending.getCompoundDrawables();
                            Drawable leftCompoundDrawable = compoundDrawables[0];
                            if (leftCompoundDrawable == null) {


                                ((EventTypeViewHolder) holder).tv_attending.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check, 0, 0, 0);
                            }
                        } else {
                            ((EventTypeViewHolder) holder).tv_attending.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        }
                    }


                    if (posts.get(position).getEvent_image().size() > 0) {
                        AppUtils.setGlideUrlThumbnail(mContext, ((EventHomeAdapter.EventTypeViewHolder) holder).profile_image, posts.get(position).getUser().getUser_image());
                        AppUtils.setGlideImageUrl(mContext, ((EventTypeViewHolder) holder).post_image, posts.get(position).getEvent_image().get(0));
                    } else {
//                        ((EventHomeAdapter.EventTypeViewHolder) holder).profile_image.setBackground(mContext.getDrawable(R.drawable.no_thumbnail));
                        ((EventTypeViewHolder) holder).post_image.setBackground(mContext.getDrawable(R.drawable.no_thumbnail));
                    }


                    ((EventTypeViewHolder) holder).tv_address.setText(posts.get(position).getAddress());
                    ((EventTypeViewHolder) holder).tv_date.setText(DateUtils.eventDate(posts.get(position).getStart_date(), posts.get(position).getEnd_date()));
                    //
                    ((EventHomeAdapter.EventTypeViewHolder) holder).tv_name.setText(posts.get(position).getName());
                    ((EventHomeAdapter.EventTypeViewHolder) holder).tv_location.setText(posts.get(position).getAddress());
                    ((EventHomeAdapter.EventTypeViewHolder) holder).tv_eventdate.setText(DateUtils.eventDate(posts.get(position).getStart_date(),posts.get(position).getEnd_date()));
                    ((EventTypeViewHolder) holder).post_detail.setText(posts.get(position).getContent());

                    if (posts.get(position).getUser().getId().equals(LoginUtils.getLoggedinUser().getId())) {
                        ((EventTypeViewHolder) holder).tv_location.setVisibility(View.VISIBLE);

                        ((EventTypeViewHolder) holder).tv_eventdate.setVisibility(View.VISIBLE);
                        ((EventTypeViewHolder) holder).tv_attending.setVisibility(View.GONE);
                        ((EventTypeViewHolder) holder).img_more.setVisibility(View.VISIBLE);
                        //
                        AppUtils.makeTextViewResizable(((EventTypeViewHolder) holder).tv_detail, 3, posts.get(position).getContent());
                        ((EventTypeViewHolder) holder).linearLayout3.setVisibility(View.GONE);
                        ((EventTypeViewHolder) holder).linearLayout4.setVisibility(View.VISIBLE);


                    } else {
                        ((EventTypeViewHolder) holder).tv_location.setVisibility(View.GONE);
                        ((EventTypeViewHolder) holder).tv_eventdate.setVisibility(View.GONE);
                        ((EventTypeViewHolder) holder).tv_attending.setVisibility(View.VISIBLE);
                        ((EventTypeViewHolder) holder).img_more.setVisibility(View.GONE);
                        //
                        ((EventTypeViewHolder) holder).linearLayout3.setVisibility(View.VISIBLE);
                        ((EventTypeViewHolder) holder).linearLayout4.setVisibility(View.GONE);
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

        void onEditClick(View view, int position);

        void onDeleteClick(View view, int position);


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
    public void moreOptions(View v,int position)
    {
        final OvershootInterpolator interpolator = new OvershootInterpolator();
        ViewCompat.animate(v).
                rotation(135f).
                withLayer().rotation(0).
                setInterpolator(interpolator).
                start();
        /** Instantiating PopupMenu class */
        PopupMenu popup = new PopupMenu(mContext, v);

        /** Adding menu items to the popumenu */
        popup.getMenuInflater().inflate(R.menu.event_menu, popup.getMenu());

        /** Defining menu item click listener for the popup menu */
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getTitle().toString().equalsIgnoreCase(mContext.getString(R.string.edit_event))) {
                    mClickListener.onEditClick(v,position);
                }
                else
                {
                    simpleDialog = new SimpleDialog(mContext, mContext.getString(R.string.confirmation), mContext.getString(R.string.msg_remove_event), mContext.getString(R.string.button_no), mContext.getString(R.string.button_yes), new OnOneOffClickListener() {
                        @Override
                        public void onSingleClick(View v) {
                            switch (v.getId()) {
                                case R.id.button_positive:
                                    mClickListener.onDeleteClick(v,position);
                                    break;
                                case R.id.button_negative:
                                    break;
                            }
                            simpleDialog.dismiss();
                        }
                    });
                    simpleDialog.show();

                }
                return true;
            }
        });
      //  popup.setForceShowIcon(true);

        /** Showing the popup menu */
        popup.show();
    }
}
