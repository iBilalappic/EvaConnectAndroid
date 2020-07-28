package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnDoubleTapClickListner;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PostAdapter  extends RecyclerView.Adapter {

    private List<Post> posts = new ArrayList<>();
    private Context mContext;
    int total_types;
    private boolean fabStateVolume = false;
    private PostAdapter.ItemClickListener mClickListener;

    // flag for footer ProgressBar (i.e. last item of list)
    private boolean isLoadingAdded = false;
    private boolean isLoaderVisible = false;
    GestureDetector gestureDetector;


    public class TextTypeViewHolder extends RecyclerView.ViewHolder {
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

        @BindView(R.id.view6)
        View top_image;

        @BindView(R.id.tv_company)
        TextView tv_company;

        @BindView(R.id.tv_designation)
        TextView tv_designation;

        @BindView(R.id.tv_connect)
        TextView tv_connect;

        @BindView(R.id.comment_click)
        LinearLayout comment_click;

        @BindView(R.id.share_click)
        LinearLayout share_click;

        @BindView(R.id.like_click)
        LinearLayout like_click;

        public TextTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tv_viewcomments.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            like_click.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onLikeClick(v, getAdapterPosition(), tv_likecount);
                }
            });
            comment_click.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
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
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            tv_content.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            profile_image.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onProfileClick(v, getAdapterPosition());
                }
            });

            tv_name.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onProfileClick(v, getAdapterPosition());
                }
            });

            tv_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onConnectClick(v, getAdapterPosition());
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

    public class ImageTypeViewHolder extends RecyclerView.ViewHolder {
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

        @BindView(R.id.view6)
        View top_image;

        @BindView(R.id.post_image)
        ImageView post_image;

        @BindView(R.id.tv_company)
        TextView tv_company;

        @BindView(R.id.tv_designation)
        TextView tv_designation;

        @BindView(R.id.tv_connect)
        TextView tv_connect;

        @BindView(R.id.comment_click)
        LinearLayout comment_click;

        @BindView(R.id.share_click)
        LinearLayout share_click;

        @BindView(R.id.like_click)
        LinearLayout like_click;


        public ImageTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tv_viewcomments.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            like_click.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onLikeClick(v, getAdapterPosition(), tv_likecount);
                }
            });
            comment_click.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
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
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            tv_content.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            profile_image.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onProfileClick(v, getAdapterPosition());
                }
            });

            tv_name.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onProfileClick(v, getAdapterPosition());
                }
            });

            post_image.setOnClickListener(new OnDoubleTapClickListner() {
                @Override
                public void onSingleClick(View v) {
                }

                @Override
                public void onDoubleClick(View v) {
                    mClickListener.onLikeClick(v, getAdapterPosition(), tv_likecount);
                }
            });

            tv_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onConnectClick(v, getAdapterPosition());
                }
            });

        }


    }

    public class VideoTypeViewHolder extends RecyclerView.ViewHolder {
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

        @BindView(R.id.img_video)
        ImageView img_video;

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

        @BindView(R.id.view6)
        View top_image;

        @BindView(R.id.tv_company)
        TextView tv_company;

        @BindView(R.id.tv_designation)
        TextView tv_designation;

        @BindView(R.id.tv_connect)
        TextView tv_connect;

        @BindView(R.id.comment_click)
        LinearLayout comment_click;

        @BindView(R.id.share_click)
        LinearLayout share_click;

        @BindView(R.id.like_click)
        LinearLayout like_click;




        public VideoTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tv_viewcomments.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            like_click.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onLikeClick(v, getAdapterPosition(), tv_likecount);
                }
            });
            comment_click.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
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
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            img_video.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onVideoClick(v, getAdapterPosition());
                }
            });
            tv_content.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            profile_image.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onProfileClick(v, getAdapterPosition());
                }
            });

            tv_name.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onProfileClick(v, getAdapterPosition());
                }
            });

            tv_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onConnectClick(v, getAdapterPosition());
                }
            });

        }
    }

    public class LinkTypeViewHolder extends RecyclerView.ViewHolder {
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

        @BindView(R.id.view6)
        View top_image;

        @BindView(R.id.img_video)
        ImageView img_link;

        @BindView(R.id.link)
        TextView link;

        @BindView(R.id.tv_company)
        TextView tv_company;

        @BindView(R.id.tv_designation)
        TextView tv_designation;

        @BindView(R.id.tv_connect)
        TextView tv_connect;

        @BindView(R.id.comment_click)
        LinearLayout comment_click;

        @BindView(R.id.share_click)
        LinearLayout share_click;

        @BindView(R.id.like_click)
        LinearLayout like_click;

        public LinkTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tv_viewcomments.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            like_click.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onLikeClick(v, getAdapterPosition(), tv_likecount);
                }
            });
            comment_click.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
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
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            tv_content.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            link.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onURLClick(v, getAdapterPosition());
                }
            });
            profile_image.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onProfileClick(v, getAdapterPosition());
                }
            });

            tv_name.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onProfileClick(v, getAdapterPosition());
                }
            });

            tv_connect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mClickListener.onConnectClick(v, getAdapterPosition());
                }
            });

        }

    }

    public PostAdapter(Context context, List<Post> data, PostAdapter.ItemClickListener mClickListener) {
        this.posts = data;
        this.mContext = context;
        total_types = posts.size();
        this.mClickListener = mClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case AppConstants.TEXT_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_type, parent, false);
                return new PostAdapter.TextTypeViewHolder(view);
            case AppConstants.IMAGE_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
                return new PostAdapter.ImageTypeViewHolder(view);
            case AppConstants.LOADING_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
                return new PostAdapter.LoadingTypeViewHolder(view);
            case AppConstants.VIDEO_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
                return new PostAdapter.VideoTypeViewHolder(view);
            case AppConstants.LINK_POST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_link, parent, false);
                return new PostAdapter.LinkTypeViewHolder(view);
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
                case AppConstants.TEXT_TYPE:
                    if (String.valueOf(posts.get(position).getComment_count()).equals("0")) {
                        ((PostAdapter.TextTypeViewHolder) holder).tv_viewcomments.setVisibility(View.GONE);
                    }
                    ((PostAdapter.TextTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));
                    ((PostAdapter.TextTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));
                    ((PostAdapter.TextTypeViewHolder) holder).tv_createdDateTime.setText(DateUtils.getFormattedDateTime(posts.get(position).getCreated_datetime()));

                    // ((TextTypeViewHolder) holder).tv_content.setText(posts.get(position).getContent());
                    AppUtils.makeTextViewResizable(((PostAdapter.TextTypeViewHolder) holder).tv_content, 3, posts.get(position).getContent());
                    ((PostAdapter.TextTypeViewHolder) holder).tv_minago.setText(DateUtils.getTimeAgo(posts.get(position).getCreated_datetime()));
                    if (posts.get(position).getIs_post_like() != null && posts.get(position).getIs_post_like() > 0) {
                        ((PostAdapter.TextTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.like_selected));
                    } else {
                        ((PostAdapter.TextTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.ic_like));
                    }
                    if (posts.get(position).getUser().getIs_linkedin() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getLinkedin_image_url())) {
                        AppUtils.setGlideImage(mContext, ((PostAdapter.TextTypeViewHolder) holder).profile_image, posts.get(position).getUser().getLinkedin_image_url());
                    }
                    else if (posts.get(position).getUser().getIs_facebook() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getFacebook_image_url())){
                        AppUtils.setGlideImage(mContext, ((PostAdapter.TextTypeViewHolder) holder).profile_image, posts.get(position).getUser().getFacebook_image_url());
                    }
                    else {
                        AppUtils.setGlideImage(mContext, ((PostAdapter.TextTypeViewHolder) holder).profile_image, posts.get(position).getUser().getUser_image());
                    }

                    ((PostAdapter.TextTypeViewHolder) holder).tv_name.setText(posts.get(position).getUser().getFirst_name());

                    ((PostAdapter.TextTypeViewHolder) holder).tv_connections.setText(AppUtils.getConnectionsCount(posts.get(position).getUser().getTotal_connection()));
                    if (position == 0) {
                        ((PostAdapter.TextTypeViewHolder) holder).top_image.setVisibility(View.GONE);
                    } else {
                        ((PostAdapter.TextTypeViewHolder) holder).top_image.setVisibility(View.VISIBLE);
                    }
                    ((PostAdapter.TextTypeViewHolder) holder).tv_company.setText(posts.get(position).getUser().getCompany_name());
                    ((PostAdapter.TextTypeViewHolder) holder).tv_designation.setText(posts.get(position).getUser().getDesignation() +" at ");
                    if (posts.get(position).getUser().getId().equals(LoginUtils.getLoggedinUser().getId())) {
                        ((TextTypeViewHolder) holder).tv_connect.setVisibility(View.GONE);
                    } else {
                        ((PostAdapter.TextTypeViewHolder) holder).tv_connect.setVisibility(View.VISIBLE);
                        String connectionstatus = AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver());
                        ((PostAdapter.TextTypeViewHolder) holder).tv_connect.setText(AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver()));
                        if (connectionstatus.equals(AppConstants.REQUEST_ACCEPT)) {
                            ((PostAdapter.TextTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_green);
                            ((PostAdapter.TextTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.light_green));
                          //  holder.tv_decline.setVisibility(View.VISIBLE);
                        } else {
                          //  holder.tv_decline.setVisibility(View.GONE);
                            ((PostAdapter.TextTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_blue);
                            ((PostAdapter.TextTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.skyblue));
                        }
                        if (connectionstatus.equals(AppConstants.CONNECTED)) {
                           // holder.tv_decline.setVisibility(View.GONE);
                        }

                    }

                    break;
                case AppConstants.IMAGE_TYPE:
                    if (String.valueOf(posts.get(position).getComment_count()).equals("0")) {
                        ((PostAdapter.ImageTypeViewHolder) holder).tv_viewcomments.setVisibility(View.GONE);
                    }
                    if (posts.get(position).getPost_image().size() > 1) {
                        ((PostAdapter.ImageTypeViewHolder) holder).imageSlider.setVisibility(View.VISIBLE);
                        ((PostAdapter.ImageTypeViewHolder) holder).post_image.setVisibility(View.GONE);
                        initializeSlider(((PostAdapter.ImageTypeViewHolder) holder).imageSlider, position);
                    } else {
                        ((PostAdapter.ImageTypeViewHolder) holder).imageSlider.setVisibility(View.GONE);
                        ((PostAdapter.ImageTypeViewHolder) holder).post_image.setVisibility(View.VISIBLE);
                        AppUtils.setGlideImageUrl(mContext, ((PostAdapter.ImageTypeViewHolder) holder).post_image, posts.get(position).getPost_image().get(0));
                    }

                    ((PostAdapter.ImageTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));
                    ((PostAdapter.ImageTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));
                    ((PostAdapter.ImageTypeViewHolder) holder).tv_createdDateTime.setText(DateUtils.getFormattedDateTime(posts.get(position).getCreated_datetime()));
                    AppUtils.makeTextViewResizable(((PostAdapter.ImageTypeViewHolder) holder).tv_content, 3, posts.get(position).getContent());
                    ((PostAdapter.ImageTypeViewHolder) holder).tv_minago.setText(DateUtils.getTimeAgo(posts.get(position).getCreated_datetime()));
                    if (posts.get(position).getIs_post_like() != null && posts.get(position).getIs_post_like() > 0) {
                        ((PostAdapter.ImageTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.like_selected));
                    } else {
                        ((PostAdapter.ImageTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.ic_like));
                    }

                    if (posts.get(position).getUser().getIs_linkedin() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getLinkedin_image_url())) {
                        AppUtils.setGlideImage(mContext, ((PostAdapter.ImageTypeViewHolder) holder).profile_image, posts.get(position).getUser().getLinkedin_image_url());
                    }
                    else if (posts.get(position).getUser().getIs_facebook() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getFacebook_image_url())){
                        AppUtils.setGlideImage(mContext, ((PostAdapter.ImageTypeViewHolder) holder).profile_image, posts.get(position).getUser().getFacebook_image_url());
                    }
                    else {
                        AppUtils.setGlideImage(mContext, ((PostAdapter.ImageTypeViewHolder) holder).profile_image, posts.get(position).getUser().getUser_image());
                    }

                    ((PostAdapter.ImageTypeViewHolder) holder).tv_name.setText(posts.get(position).getUser().getFirst_name());
                    ((PostAdapter.ImageTypeViewHolder) holder).tv_connections.setText(AppUtils.getConnectionsCount(posts.get(position).getUser().getTotal_connection()));

                    if (position == 0) {
                        ((PostAdapter.ImageTypeViewHolder) holder).top_image.setVisibility(View.GONE);
                    } else {
                        ((PostAdapter.ImageTypeViewHolder) holder).top_image.setVisibility(View.VISIBLE);
                    }
                    ((PostAdapter.ImageTypeViewHolder) holder).tv_company.setText(posts.get(position).getUser().getCompany_name());
                    ((PostAdapter.ImageTypeViewHolder) holder).tv_designation.setText(posts.get(position).getUser().getDesignation() +" at ");
                    if (posts.get(position).getUser().getId().equals(LoginUtils.getLoggedinUser().getId())) {
                        ((ImageTypeViewHolder) holder).tv_connect.setVisibility(View.GONE);
                    } else {
                        ((PostAdapter.ImageTypeViewHolder) holder).tv_connect.setVisibility(View.VISIBLE);
                        String connectionstatus = AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver());
                        ((PostAdapter.ImageTypeViewHolder) holder).tv_connect.setText(AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver()));
                        if (connectionstatus.equals(AppConstants.REQUEST_ACCEPT)) {
                            ((PostAdapter.ImageTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_green);
                            ((PostAdapter.ImageTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.light_green));
                            //  holder.tv_decline.setVisibility(View.VISIBLE);
                        } else {
                            //  holder.tv_decline.setVisibility(View.GONE);
                            ((PostAdapter.ImageTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_blue);
                            ((PostAdapter.ImageTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.skyblue));
                        }
                        if (connectionstatus.equals(AppConstants.CONNECTED)) {
                            // holder.tv_decline.setVisibility(View.GONE);
                        }

                    }

                    break;
                case AppConstants.VIDEO_TYPE:
                    if (String.valueOf(posts.get(position).getComment_count()).equals("0")) {
                        ((PostAdapter.VideoTypeViewHolder) holder).tv_viewcomments.setVisibility(View.GONE);
                    }
                    ((PostAdapter.VideoTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));
                    ((PostAdapter.VideoTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));
                    ((PostAdapter.VideoTypeViewHolder) holder).tv_createdDateTime.setText(DateUtils.getFormattedDateTime(posts.get(position).getCreated_datetime()));
                    AppUtils.makeTextViewResizable(((PostAdapter.VideoTypeViewHolder) holder).tv_content, 3, posts.get(position).getContent());
                    ((PostAdapter.VideoTypeViewHolder) holder).tv_minago.setText(DateUtils.getTimeAgo(posts.get(position).getCreated_datetime()));
                    if (posts.get(position).getIs_post_like() != null && posts.get(position).getIs_post_like() > 0) {
                        ((PostAdapter.VideoTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.like_selected));
                    } else {
                        ((PostAdapter.VideoTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.ic_like));
                    }

                    if (posts.get(position).getUser().getIs_linkedin() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getLinkedin_image_url())) {
                        AppUtils.setGlideImage(mContext, ((PostAdapter.VideoTypeViewHolder) holder).profile_image, posts.get(position).getUser().getLinkedin_image_url());
                    }
                    else if (posts.get(position).getUser().getIs_facebook() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getFacebook_image_url())){
                        AppUtils.setGlideImage(mContext, ((PostAdapter.VideoTypeViewHolder) holder).profile_image, posts.get(position).getUser().getFacebook_image_url());
                    }
                    else {
                        AppUtils.setGlideImage(mContext, ((PostAdapter.VideoTypeViewHolder) holder).profile_image, posts.get(position).getUser().getUser_image());
                    }

                    ((PostAdapter.VideoTypeViewHolder) holder).tv_name.setText(posts.get(position).getUser().getFirst_name());
                    ((PostAdapter.VideoTypeViewHolder) holder).tv_connections.setText(AppUtils.getConnectionsCount(posts.get(position).getUser().getTotal_connection()));

                    if (position == 0) {
                        ((PostAdapter.VideoTypeViewHolder) holder).top_image.setVisibility(View.GONE);
                    } else {
                        ((PostAdapter.VideoTypeViewHolder) holder).top_image.setVisibility(View.VISIBLE);
                    }
                    AppUtils.setGlideVideoThumbnail(mContext, ((PostAdapter.VideoTypeViewHolder) holder).img_video, posts.get(position).getPost_video());
                    ((PostAdapter.VideoTypeViewHolder) holder).tv_company.setText(posts.get(position).getUser().getCompany_name());
                    ((PostAdapter.VideoTypeViewHolder) holder).tv_designation.setText(posts.get(position).getUser().getDesignation()+" at ");
                    if (posts.get(position).getUser().getId().equals(LoginUtils.getLoggedinUser().getId())) {
                        ((VideoTypeViewHolder) holder).tv_connect.setVisibility(View.GONE);
                    } else {
                        ((PostAdapter.VideoTypeViewHolder) holder).tv_connect.setVisibility(View.VISIBLE);
                        String connectionstatus = AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver());
                        ((PostAdapter.VideoTypeViewHolder) holder).tv_connect.setText(AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver()));
                        if (connectionstatus.equals(AppConstants.REQUEST_ACCEPT)) {
                            ((PostAdapter.VideoTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_green);
                            ((PostAdapter.VideoTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.light_green));
                            //  holder.tv_decline.setVisibility(View.VISIBLE);
                        } else {
                            //  holder.tv_decline.setVisibility(View.GONE);
                            ((PostAdapter.VideoTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_blue);
                            ((PostAdapter.VideoTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.skyblue));
                        }
                        if (connectionstatus.equals(AppConstants.CONNECTED)) {
                            // holder.tv_decline.setVisibility(View.GONE);
                        }

                    }

                    break;
                case AppConstants.LINK_POST:
                    if (String.valueOf(posts.get(position).getComment_count()).equals("0")) {
                        ((PostAdapter.LinkTypeViewHolder) holder).tv_viewcomments.setVisibility(View.GONE);
                    }
                    //  ((LinkTypeViewHolder) holder).img_link.setBackground(null);
                    ((PostAdapter.LinkTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));
                    ((PostAdapter.LinkTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));
                    ((PostAdapter.LinkTypeViewHolder) holder).tv_createdDateTime.setText(DateUtils.getFormattedDateTime(posts.get(position).getCreated_datetime()));

                    ArrayList<String> urls = AppUtils.containsURL(posts.get(position).getContent());
                    if (urls.size() > 0) {
                        // AppUtils.showUrlEmbeddedView(urls.get(0),((LinkTypeViewHolder) holder).img_link);
                        AppUtils.customUrlEmbeddedView(((PostAdapter.LinkTypeViewHolder) holder).img_link.getContext(), urls.get(0), ((PostAdapter.LinkTypeViewHolder) holder).img_link);

                        AppUtils.makeTextViewResizable(((PostAdapter.LinkTypeViewHolder) holder).link, 1, urls.get(0));
                        AppUtils.setGlideVideoThumbnail(((PostAdapter.LinkTypeViewHolder) holder).img_link.getContext(), ((PostAdapter.LinkTypeViewHolder) holder).img_link, posts.get(position).getLink_thumbnail());

                    } else {
                        // ((LinkTypeViewHolder) holder).img_link.setBackground(null);
                        // ((LinkTypeViewHolder) holder).img_link.setVisibility(View.GONE);
                        // Glide.with(((LinkTypeViewHolder) holder).img_link.getContext()).clear(((LinkTypeViewHolder) holder).img_link);
                        // ((LinkTypeViewHolder) holder).tv_content.setText(posts.get(position).getContent());
                    }
                    AppUtils.makeTextViewResizable(((PostAdapter.LinkTypeViewHolder) holder).tv_content, 3, posts.get(position).getContent());
                    // ((LinkTypeViewHolder) holder).tv_content.setText(posts.get(position).getContent());
                    ((PostAdapter.LinkTypeViewHolder) holder).tv_minago.setText(DateUtils.getTimeAgo(posts.get(position).getCreated_datetime()));
                    if (posts.get(position).getIs_post_like() != null && posts.get(position).getIs_post_like() > 0) {
                        ((PostAdapter.LinkTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.like_selected));
                    } else {
                        ((PostAdapter.LinkTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.ic_like));
                    }
                    if (posts.get(position).getUser().getIs_linkedin() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getLinkedin_image_url())) {
                        AppUtils.setGlideImage(mContext, ((PostAdapter.LinkTypeViewHolder) holder).profile_image, posts.get(position).getUser().getLinkedin_image_url());
                    }
                    else if (posts.get(position).getUser().getIs_facebook() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getFacebook_image_url())) {
                        AppUtils.setGlideImage(mContext, ((PostAdapter.LinkTypeViewHolder) holder).profile_image, posts.get(position).getUser().getFacebook_image_url());
                    }
                    else {
                        AppUtils.setGlideImage(mContext, ((PostAdapter.LinkTypeViewHolder) holder).profile_image, posts.get(position).getUser().getUser_image());
                    }

                    ((PostAdapter.LinkTypeViewHolder) holder).tv_name.setText(posts.get(position).getUser().getFirst_name());
                    ((PostAdapter.LinkTypeViewHolder) holder).tv_connections.setText(AppUtils.getConnectionsCount(posts.get(position).getUser().getTotal_connection()));

                    if (position == 0) {
                        ((PostAdapter.LinkTypeViewHolder) holder).top_image.setVisibility(View.GONE);
                    } else {
                        ((PostAdapter.LinkTypeViewHolder) holder).top_image.setVisibility(View.VISIBLE);
                    }
                    ((PostAdapter.LinkTypeViewHolder) holder).tv_company.setText(posts.get(position).getUser().getCompany_name());
                    ((PostAdapter.LinkTypeViewHolder) holder).tv_designation.setText(posts.get(position).getUser().getDesignation()+" at");
                    if (posts.get(position).getUser().getId().equals(LoginUtils.getLoggedinUser().getId())) {
                        ((LinkTypeViewHolder) holder).tv_connect.setVisibility(View.GONE);
                    } else {
                        ((PostAdapter.LinkTypeViewHolder) holder).tv_connect.setVisibility(View.VISIBLE);
                        String connectionstatus = AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver());
                        ((PostAdapter.LinkTypeViewHolder) holder).tv_connect.setText(AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver()));
                        if (connectionstatus.equals(AppConstants.REQUEST_ACCEPT)) {
                            ((PostAdapter.LinkTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_green);
                            ((PostAdapter.LinkTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.light_green));
                            //  holder.tv_decline.setVisibility(View.VISIBLE);
                        } else {
                            //  holder.tv_decline.setVisibility(View.GONE);
                            ((PostAdapter.LinkTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_blue);
                            ((PostAdapter.LinkTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.skyblue));
                        }
                        if (connectionstatus.equals(AppConstants.CONNECTED)) {
                            // holder.tv_decline.setVisibility(View.GONE);
                        }

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

        void onShareClick(View view, int position);

        void onVideoClick(View view, int position);

        void onURLClick(View view, int position);

        void onProfileClick(View view, int position);

        void onConnectClick(View view, int position);



    }

    private void initializeSlider(SliderView imageSlider, int position) {

        SliderImageAdapter sliderImageAdapter = new SliderImageAdapter(mContext, posts.get(position).getPost_image(), imageSlider);
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
