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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePostsAdapter extends RecyclerView.Adapter {

    private List<Post> posts = new ArrayList<>();
    private Context mContext;
    int total_types;
    private boolean fabStateVolume = false;
    private HomePostsAdapter.ItemClickListener mClickListener;
    private SliderImageAdapter sliderImageAdapter;
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

        @BindView(R.id.tv_designation)
        TextView tv_designation;

        @BindView(R.id.tv_company)
        TextView tv_company;

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
                    mClickListener.onConnectClick(v,getAdapterPosition());
                }
            });

        }
    }

    public class EventTypeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_viewcomments)
        TextView tv_viewcomments;

        @BindView(R.id.tv_likecount)
        TextView tv_likecount;

        @BindView(R.id.tv_comcount)
        TextView tv_comcount;

        @BindView(R.id.img_like)
        ImageView img_like;

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

        @BindView(R.id.post_image)
        ImageView post_image;

        @BindView(R.id.post_detail)
        TextView post_detail;



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

        }


    }

    public class JobTypeViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_viewcomments)
        TextView tv_viewcomments;

        @BindView(R.id.tv_likecount)
        TextView tv_likecount;

        @BindView(R.id.tv_comcount)
        TextView tv_comcount;

        @BindView(R.id.tv_location)
        TextView tv_location;

        @BindView(R.id.img_like)
        ImageView img_like;

        @BindView(R.id.img_comment)
        ImageView img_comment;

        @BindView(R.id.comment_click)
        LinearLayout comment_click;

        @BindView(R.id.share_click)
        LinearLayout share_click;

        @BindView(R.id.like_click)
        LinearLayout like_click;


        @BindView(R.id.img_share)
        ImageView img_share;



        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.profile_image)
        ImageView profile_image;

        @BindView(R.id.tv_salary)
        TextView tv_salary;

        @BindView(R.id.tv_apply)
        TextView tv_apply;

        @BindView(R.id.view6)
        View top_image;

        public JobTypeViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tv_viewcomments.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    //  mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            like_click.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onJobLikeClick(v, getAdapterPosition(), tv_likecount);
                }
            });
            comment_click.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    // mClickListener.onItemClick(v, getAdapterPosition());
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
                    //mClickListener.onItemClick(v, getAdapterPosition());
                }
            });
            tv_apply.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onApplyClick(v, getAdapterPosition());
                }
            });
            profile_image.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onApplyClick(v, getAdapterPosition());
                }
            });

            tv_name.setOnClickListener(new OnOneOffClickListener() {
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

        @BindView(R.id.tv_designation)
        TextView tv_designation;

        @BindView(R.id.tv_company)
        TextView tv_company;

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
                    mClickListener.onConnectClick(v,getAdapterPosition());
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

        @BindView(R.id.like_click)
        LinearLayout like_click;

        @BindView(R.id.comment_click)
        LinearLayout comment_click;

        @BindView(R.id.share_click)
        LinearLayout share_click;

        @BindView(R.id.tv_designation)
        TextView tv_designation;

        @BindView(R.id.tv_company)
        TextView tv_company;

        @BindView(R.id.tv_connect)
        TextView tv_connect;



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
                    mClickListener.onConnectClick(v,getAdapterPosition());
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

        @BindView(R.id.comment_click)
        LinearLayout comment_click;

        @BindView(R.id.share_click)
        LinearLayout share_click;

        @BindView(R.id.like_click)
        LinearLayout like_click;

        @BindView(R.id.tv_designation)
        TextView tv_designation;

        @BindView(R.id.tv_company)
        TextView tv_company;

        @BindView(R.id.tv_connect)
        TextView tv_connect;


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
                    mClickListener.onURLClick(v, getAdapterPosition(),"link");
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
                    mClickListener.onConnectClick(v,getAdapterPosition());
                }
            });

        }

    }


    public class NewsTypeViewHolder extends RecyclerView.ViewHolder {
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


        @BindView(R.id.tv_connections)
        TextView tv_connections;

        @BindView(R.id.view6)
        View top_image;

        @BindView(R.id.img_video)
        ImageView img_link;

        @BindView(R.id.channelname)
        TextView channelname;

        @BindView(R.id.tv_url)
        TextView tv_url;

        @BindView(R.id.tv_newstitle)
        TextView tv_newstitle;

        @BindView(R.id.tv_visit)
        TextView tv_visit;

        @BindView(R.id.comment_click)
        LinearLayout comment_click;

        @BindView(R.id.share_click)
        LinearLayout share_click;

        @BindView(R.id.like_click)
        LinearLayout like_click;


        public NewsTypeViewHolder(View itemView) {
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
                    mClickListener.onNewsLikeClick(v, getAdapterPosition(), tv_likecount);
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

            tv_visit.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onURLClick(v, getAdapterPosition(),"news");
                }
            });

        }

    }

    public HomePostsAdapter(Context context, List<Post> data, HomePostsAdapter.ItemClickListener mClickListener) {
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
            case AppConstants.LOADING_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_loading, parent, false);
                return new LoadingTypeViewHolder(view);
            case AppConstants.VIDEO_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video, parent, false);
                return new VideoTypeViewHolder(view);
            case AppConstants.LINK_POST:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_link, parent, false);
                return new LinkTypeViewHolder(view);
            case AppConstants.NEWS_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
                return new NewsTypeViewHolder(view);
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
                case AppConstants.NEWS_TYPE:
                    return (position == posts.size() - 1 && isLoadingAdded) ? AppConstants.LOADING_TYPE : AppConstants.NEWS_TYPE;
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
                        ((TextTypeViewHolder) holder).tv_viewcomments.setVisibility(View.GONE);
                    }
                    ((TextTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));
                    ((TextTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));
                    ((TextTypeViewHolder) holder).tv_createdDateTime.setText(DateUtils.getFormattedDateTime(posts.get(position).getCreated_datetime()));

                    // ((TextTypeViewHolder) holder).tv_content.setText(posts.get(position).getContent());
                    AppUtils.makeTextViewResizable(((TextTypeViewHolder) holder).tv_content, 3, posts.get(position).getContent());
                    ((TextTypeViewHolder) holder).tv_minago.setText(DateUtils.getTimeAgo(posts.get(position).getCreated_datetime()));
                    if (posts.get(position).getIs_post_like() != null && posts.get(position).getIs_post_like() > 0) {
                        ((TextTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.like_selected));
                    } else {
                        ((TextTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.ic_like));
                    }
                    if (posts.get(position).getUser().getIs_linkedin() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getLinkedin_image_url())) {
                        AppUtils.setGlideImage(mContext, ((TextTypeViewHolder) holder).profile_image, posts.get(position).getUser().getLinkedin_image_url());
                    }
                    else if (posts.get(position).getUser().getIs_facebook() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getFacebook_image_url())){
                        AppUtils.setGlideImage(mContext, ((TextTypeViewHolder) holder).profile_image, posts.get(position).getUser().getFacebook_image_url());
                    }
                    else {
                        AppUtils.setGlideImage(mContext, ((TextTypeViewHolder) holder).profile_image, posts.get(position).getUser().getUser_image());
                    }

                    ((TextTypeViewHolder) holder).tv_name.setText(posts.get(position).getUser().getFirst_name());

                    ((TextTypeViewHolder) holder).tv_connections.setText(AppUtils.getConnectionsCount(posts.get(position).getUser().getTotal_connection()));
                    if (position == 0) {
                        ((TextTypeViewHolder) holder).top_image.setVisibility(View.GONE);
                    } else {
                        ((TextTypeViewHolder) holder).top_image.setVisibility(View.VISIBLE);
                    }
                    ((TextTypeViewHolder) holder).tv_company.setText(posts.get(position).getUser().getCompany_name() );
                    ((TextTypeViewHolder) holder).tv_designation.setText(posts.get(position).getUser().getDesignation() +" at");
                    if (posts.get(position).getUser().getId().equals(LoginUtils.getLoggedinUser().getId())) {
                        ((TextTypeViewHolder) holder).tv_connect.setVisibility(View.GONE);
                    } else {
                        ((TextTypeViewHolder) holder).tv_connect.setVisibility(View.VISIBLE);
                        String connectionstatus = AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver());
                        ((TextTypeViewHolder) holder).tv_connect.setText(AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver()));
                        if (connectionstatus.equals(AppConstants.REQUEST_ACCEPT)) {
                            ((TextTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_green);
                            ((TextTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.light_green));
                            //  holder.tv_decline.setVisibility(View.VISIBLE);
                        } else {
                            //  holder.tv_decline.setVisibility(View.GONE);
                            ((TextTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_blue);
                            ((TextTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.skyblue));
                        }
                        if (connectionstatus.equals(AppConstants.CONNECTED)) {
                            // holder.tv_decline.setVisibility(View.GONE);
                        }

                    }
                    break;
                case AppConstants.IMAGE_TYPE:
                    if (String.valueOf(posts.get(position).getComment_count()).equals("0")) {
                        ((ImageTypeViewHolder) holder).tv_viewcomments.setVisibility(View.GONE);
                    }
                    if (posts.get(position).getPost_image().size() > 1) {
                        ((ImageTypeViewHolder) holder).imageSlider.setVisibility(View.VISIBLE);
                        ((ImageTypeViewHolder) holder).post_image.setVisibility(View.GONE);
                        initializeSlider(((ImageTypeViewHolder) holder).imageSlider, position);
                    } else {
                        ((ImageTypeViewHolder) holder).imageSlider.setVisibility(View.GONE);
                        ((ImageTypeViewHolder) holder).post_image.setVisibility(View.VISIBLE);
                        AppUtils.setGlideImageUrl(mContext, ((ImageTypeViewHolder) holder).post_image, posts.get(position).getPost_image().get(0));
                    }

                    ((ImageTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));
                    ((ImageTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));
                    ((ImageTypeViewHolder) holder).tv_createdDateTime.setText(DateUtils.getFormattedDateTime(posts.get(position).getCreated_datetime()));
                    AppUtils.makeTextViewResizable(((ImageTypeViewHolder) holder).tv_content, 3, posts.get(position).getContent());
                    ((ImageTypeViewHolder) holder).tv_minago.setText(DateUtils.getTimeAgo(posts.get(position).getCreated_datetime()));
                    if (posts.get(position).getIs_post_like() != null && posts.get(position).getIs_post_like() > 0) {
                        ((ImageTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.like_selected));
                    } else {
                        ((ImageTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.ic_like));
                    }

                    if (posts.get(position).getUser().getIs_linkedin() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getLinkedin_image_url())) {
                        AppUtils.setGlideImage(mContext, ((ImageTypeViewHolder) holder).profile_image, posts.get(position).getUser().getLinkedin_image_url());
                    }
                    else if (posts.get(position).getUser().getIs_facebook() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getFacebook_image_url())){
                        AppUtils.setGlideImage(mContext, ((ImageTypeViewHolder) holder).profile_image, posts.get(position).getUser().getFacebook_image_url());
                    }
                    else {
                        AppUtils.setGlideImage(mContext, ((ImageTypeViewHolder) holder).profile_image, posts.get(position).getUser().getUser_image());
                    }

                    ((ImageTypeViewHolder) holder).tv_name.setText(posts.get(position).getUser().getFirst_name());
                    ((ImageTypeViewHolder) holder).tv_connections.setText(AppUtils.getConnectionsCount(posts.get(position).getUser().getTotal_connection()));

                    if (position == 0) {
                        ((ImageTypeViewHolder) holder).top_image.setVisibility(View.GONE);
                    } else {
                        ((ImageTypeViewHolder) holder).top_image.setVisibility(View.VISIBLE);
                    }
                    ((ImageTypeViewHolder) holder).tv_company.setText(posts.get(position).getUser().getCompany_name() );
                    ((ImageTypeViewHolder) holder).tv_designation.setText(posts.get(position).getUser().getDesignation() +" at ");

                    if (posts.get(position).getUser().getId().equals(LoginUtils.getLoggedinUser().getId())) {
                        ((ImageTypeViewHolder) holder).tv_connect.setVisibility(View.GONE);
                    } else {
                        ((ImageTypeViewHolder) holder).tv_connect.setVisibility(View.VISIBLE);
                        String connectionstatus = AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver());
                        ((ImageTypeViewHolder) holder).tv_connect.setText(AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver()));
                        if (connectionstatus.equals(AppConstants.REQUEST_ACCEPT)) {
                            ((ImageTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_green);
                            ((ImageTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.light_green));
                            //  holder.tv_decline.setVisibility(View.VISIBLE);
                        } else {
                            //  holder.tv_decline.setVisibility(View.GONE);
                            ((ImageTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_blue);
                            ((ImageTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.skyblue));
                        }


                    }
                    break;
                case AppConstants.EVENT_TYPE:
                    if (String.valueOf(posts.get(position).getComment_count()).equals("0")) {
                        ((EventTypeViewHolder) holder).tv_viewcomments.setVisibility(View.GONE);
                    }
                    ((EventTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));
                    ((EventTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));

                    if (posts.get(position).getIs_event_like() != null && posts.get(position).getIs_event_like() > 0) {
                        ((EventTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.like_selected));
                    } else {
                        ((EventTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.ic_like));
                    }
                    if (posts.get(position).getEvent_image().size() > 0) {
                        AppUtils.setGlideImageUrl(mContext, ((EventTypeViewHolder) holder).profile_image, posts.get(position).getEvent_image().get(0));
                        AppUtils.setGlideVideoThumbnail(mContext, ((EventTypeViewHolder) holder).post_image, posts.get(position).getEvent_image().get(0));
                    } else {
//                        ((EventTypeViewHolder) holder).profile_image.setBackground(mContext.getDrawable(R.drawable.no_thumbnail));
                        ((EventTypeViewHolder) holder).post_image.setBackground(mContext.getDrawable(R.drawable.no_thumbnail));
                    }

                    ((EventTypeViewHolder) holder).tv_name.setText(posts.get(position).getName());
                    ((EventTypeViewHolder) holder).tv_location.setText(posts.get(position).getAddress());
                    ((EventTypeViewHolder) holder).tv_eventdate.setText(DateUtils.getFormattedDateDMY(posts.get(position).getStart_date()));
                    ((EventTypeViewHolder) holder).post_detail.setText(posts.get(position).getContent());
                    if (position == 0) {
                        ((EventTypeViewHolder) holder).top_image.setVisibility(View.GONE);
                    } else {
                        ((EventTypeViewHolder) holder).top_image.setVisibility(View.VISIBLE);
                    }

                    break;
                case AppConstants.JOB_TYPE:
                    if (String.valueOf(posts.get(position).getComment_count()).equals("0")) {
                        ((JobTypeViewHolder) holder).tv_viewcomments.setVisibility(View.GONE);
                    }
                    ((JobTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));
                    ((JobTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));

                    if (posts.get(position).getIs_job_like() != null && posts.get(position).getIs_job_like() > 0) {
                        ((JobTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.like_selected));
                    } else {
                        ((JobTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.ic_like));
                    }

                    AppUtils.setGlideImage(mContext, ((JobTypeViewHolder) holder).profile_image, posts.get(position).getJob_image());
                    ((JobTypeViewHolder) holder).tv_name.setText(posts.get(position).getJob_title());
                    DecimalFormat myFormatter = new DecimalFormat("############");

                    ((JobTypeViewHolder) holder).tv_location.setText(posts.get(position).getJob_sector()
                            +" | "+ posts.get(position).getLocation()+" | "+"£ "+myFormatter.format(posts.get((position)).getSalary())+" PA");
                  //  ((JobTypeViewHolder) holder).tv_salary.setText("£ " + myFormatter.format(posts.get(position).getSalary()) + " pa");
                    if (position == 0) {
                        ((JobTypeViewHolder) holder).top_image.setVisibility(View.GONE);
                    } else {
                        ((JobTypeViewHolder) holder).top_image.setVisibility(View.VISIBLE);
                    }
                    if (LoginUtils.getUser() != null && LoginUtils.getUser().getType().equals("company")) {
                        ((JobTypeViewHolder) holder).tv_apply.setVisibility(View.GONE);
                    } else {
                        ((JobTypeViewHolder) holder).tv_apply.setVisibility(View.VISIBLE);
                        if(posts.get(position).getIs_applied()==1)
                        {
                            ((JobTypeViewHolder) holder).tv_apply.setText("Applied");
                        }
                        else
                        {
                            ((JobTypeViewHolder) holder).tv_apply.setText("Apply");
                            ((JobTypeViewHolder) holder).tv_apply.setOnClickListener(new OnOneOffClickListener() {
                                @Override
                                public void onSingleClick(View v) {

                                    mClickListener.onApplyClick(v, position);
                                }
                            });
                        }
                       // ((JobTypeViewHolder) holder).tv_apply.setText(posts.get(position).);
                    }


                    break;
                case AppConstants.VIDEO_TYPE:
                    if (String.valueOf(posts.get(position).getComment_count()).equals("0")) {
                        ((VideoTypeViewHolder) holder).tv_viewcomments.setVisibility(View.GONE);
                    }
                    ((VideoTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));
                    ((VideoTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));
                    ((VideoTypeViewHolder) holder).tv_createdDateTime.setText(DateUtils.getFormattedDateTime(posts.get(position).getCreated_datetime()));
                    AppUtils.makeTextViewResizable(((VideoTypeViewHolder) holder).tv_content, 3, posts.get(position).getContent());
                    ((VideoTypeViewHolder) holder).tv_minago.setText(DateUtils.getTimeAgo(posts.get(position).getCreated_datetime()));
                    if (posts.get(position).getIs_post_like() != null && posts.get(position).getIs_post_like() > 0) {
                        ((VideoTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.like_selected));
                    } else {
                        ((VideoTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.ic_like));
                    }

                    if (posts.get(position).getUser().getIs_linkedin() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getLinkedin_image_url())) {
                        AppUtils.setGlideImage(mContext, ((VideoTypeViewHolder) holder).profile_image, posts.get(position).getUser().getLinkedin_image_url());
                    }
                    else if (posts.get(position).getUser().getIs_facebook() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getFacebook_image_url())){
                        AppUtils.setGlideImage(mContext, ((VideoTypeViewHolder) holder).profile_image, posts.get(position).getUser().getFacebook_image_url());
                    }
                    else {
                        AppUtils.setGlideImage(mContext, ((VideoTypeViewHolder) holder).profile_image, posts.get(position).getUser().getUser_image());
                    }

                    ((VideoTypeViewHolder) holder).tv_name.setText(posts.get(position).getUser().getFirst_name());
                    ((VideoTypeViewHolder) holder).tv_connections.setText(AppUtils.getConnectionsCount(posts.get(position).getUser().getTotal_connection()));

                    if (position == 0) {
                        ((VideoTypeViewHolder) holder).top_image.setVisibility(View.GONE);
                    } else {
                        ((VideoTypeViewHolder) holder).top_image.setVisibility(View.VISIBLE);
                    }
                    AppUtils.setGlideVideoThumbnail(mContext, ((VideoTypeViewHolder) holder).img_video, posts.get(position).getPost_video());
                    ((VideoTypeViewHolder) holder).tv_company.setText(posts.get(position).getUser().getCompany_name());
                    ((VideoTypeViewHolder) holder).tv_designation.setText(posts.get(position).getUser().getDesignation()+" at ");

                    if (posts.get(position).getUser().getId().equals(LoginUtils.getLoggedinUser().getId())) {
                        ((VideoTypeViewHolder) holder).tv_connect.setVisibility(View.GONE);
                    } else {
                        ((VideoTypeViewHolder) holder).tv_connect.setVisibility(View.VISIBLE);
                        String connectionstatus = AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver());
                        ((VideoTypeViewHolder) holder).tv_connect.setText(AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver()));
                        if (connectionstatus.equals(AppConstants.REQUEST_ACCEPT)) {
                            ((VideoTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_green);
                            ((VideoTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.light_green));
                            //  holder.tv_decline.setVisibility(View.VISIBLE);
                        } else {
                            //  holder.tv_decline.setVisibility(View.GONE);
                            ((VideoTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_blue);
                            ((VideoTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.skyblue));
                        }
                    }
                    break;
                case AppConstants.LINK_POST:
                    if (String.valueOf(posts.get(position).getComment_count()).equals("0")) {
                        ((LinkTypeViewHolder) holder).tv_viewcomments.setVisibility(View.GONE);
                    }
                    //  ((LinkTypeViewHolder) holder).img_link.setBackground(null);
                    ((LinkTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));
                    ((LinkTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));
                    ((LinkTypeViewHolder) holder).tv_createdDateTime.setText(DateUtils.getFormattedDateTime(posts.get(position).getCreated_datetime()));

                    ArrayList<String> urls = AppUtils.containsURL(posts.get(position).getContent());
                    if (urls.size() > 0) {
                        // AppUtils.showUrlEmbeddedView(urls.get(0),((LinkTypeViewHolder) holder).img_link);
                        AppUtils.customUrlEmbeddedView(((LinkTypeViewHolder) holder).img_link.getContext(), urls.get(0), ((LinkTypeViewHolder) holder).img_link);

                        AppUtils.makeTextViewResizable(((LinkTypeViewHolder) holder).link, 1, urls.get(0));
                        AppUtils.setGlideVideoThumbnail(((LinkTypeViewHolder) holder).img_link.getContext(), ((LinkTypeViewHolder) holder).img_link, posts.get(position).getLink_thumbnail());

                    } else {
                        // ((LinkTypeViewHolder) holder).img_link.setBackground(null);
                        // ((LinkTypeViewHolder) holder).img_link.setVisibility(View.GONE);
                        // Glide.with(((LinkTypeViewHolder) holder).img_link.getContext()).clear(((LinkTypeViewHolder) holder).img_link);
                        // ((LinkTypeViewHolder) holder).tv_content.setText(posts.get(position).getContent());
                    }
                    AppUtils.makeTextViewResizable(((LinkTypeViewHolder) holder).tv_content, 3, posts.get(position).getContent());
                    // ((LinkTypeViewHolder) holder).tv_content.setText(posts.get(position).getContent());
                    ((LinkTypeViewHolder) holder).tv_minago.setText(DateUtils.getTimeAgo(posts.get(position).getCreated_datetime()));
                    if (posts.get(position).getIs_post_like() != null && posts.get(position).getIs_post_like() > 0) {
                        ((LinkTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.like_selected));
                    } else {
                        ((LinkTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.ic_like));
                    }
                    if (posts.get(position).getUser().getIs_linkedin() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getLinkedin_image_url())) {
                        AppUtils.setGlideImage(mContext, ((LinkTypeViewHolder) holder).profile_image, posts.get(position).getUser().getLinkedin_image_url());
                    }
                    else if (posts.get(position).getUser().getIs_facebook() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getFacebook_image_url())) {
                        AppUtils.setGlideImage(mContext, ((LinkTypeViewHolder) holder).profile_image, posts.get(position).getUser().getFacebook_image_url());
                    }
                    else {
                        AppUtils.setGlideImage(mContext, ((LinkTypeViewHolder) holder).profile_image, posts.get(position).getUser().getUser_image());
                    }

                    ((LinkTypeViewHolder) holder).tv_name.setText(posts.get(position).getUser().getFirst_name());
                    ((LinkTypeViewHolder) holder).tv_connections.setText(AppUtils.getConnectionsCount(posts.get(position).getUser().getTotal_connection()));

                    if (position == 0) {
                        ((LinkTypeViewHolder) holder).top_image.setVisibility(View.GONE);
                    } else {
                        ((LinkTypeViewHolder) holder).top_image.setVisibility(View.VISIBLE);
                    }
                    ((LinkTypeViewHolder) holder).tv_company.setText(posts.get(position).getUser().getCompany_name() );
                    ((LinkTypeViewHolder) holder).tv_designation.setText(posts.get(position).getUser().getDesignation()+" at ");

                    if (posts.get(position).getUser().getId().equals(LoginUtils.getLoggedinUser().getId())) {
                        ((LinkTypeViewHolder) holder).tv_connect.setVisibility(View.GONE);
                    } else {
                        ((LinkTypeViewHolder) holder).tv_connect.setVisibility(View.VISIBLE);
                        String connectionstatus = AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver());
                        ((LinkTypeViewHolder) holder).tv_connect.setText(AppUtils.getConnectionStatus(mContext, posts.get(position).getIs_connected(), posts.get(position).isIs_receiver()));
                        if (connectionstatus.equals(AppConstants.REQUEST_ACCEPT)) {
                            ((LinkTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_green);
                            ((LinkTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.light_green));
                            //  holder.tv_decline.setVisibility(View.VISIBLE);
                        } else {
                            //  holder.tv_decline.setVisibility(View.GONE);
                            ((LinkTypeViewHolder) holder).tv_connect.setBackgroundResource(R.drawable.rounded_button_border_blue);
                            ((LinkTypeViewHolder) holder).tv_connect.setTextColor(mContext.getResources().getColor(R.color.skyblue));
                        }
                    }
                    break;

                case AppConstants.NEWS_TYPE:
                    if (String.valueOf(posts.get(position).getComment_count()).equals("0")) {
                        ((NewsTypeViewHolder) holder).tv_viewcomments.setVisibility(View.GONE);
                    }
                    //  ((LinkTypeViewHolder) holder).img_link.setBackground(null);
                    ((NewsTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));
                    ((NewsTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));
                    ((NewsTypeViewHolder) holder).tv_createdDateTime.setText(DateUtils.getFormattedDateTime(posts.get(position).getCreated_datetime()));


                   // AppUtils.makeTextViewResizable(((NewsTypeViewHolder) holder).tv_content, 3, posts.get(position).getContent());
                    // ((LinkTypeViewHolder) holder).tv_content.setText(posts.get(position).getContent());
                    ((NewsTypeViewHolder) holder).tv_minago.setText(DateUtils.getTimeAgo(posts.get(position).getCreated_datetime()));
                    if (posts.get(position).getIs_news_like() != null && posts.get(position).getIs_news_like() > 0) {
                        ((NewsTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.like_selected));
                    } else {
                        ((NewsTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.ic_like));
                    }
//                    if (posts.get(position).getUser().getIs_linkedin() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getLinkedin_image_url())) {
//                        AppUtils.setGlideImage(mContext, ((NewsTypeViewHolder) holder).profile_image, posts.get(position).getUser().getLinkedin_image_url());
//                    }
//                    else if (posts.get(position).getUser().getIs_facebook() == 1 && !TextUtils.isEmpty(posts.get(position).getUser().getFacebook_image_url())) {
//                        AppUtils.setGlideImage(mContext, ((NewsTypeViewHolder) holder).profile_image, posts.get(position).getUser().getFacebook_image_url());
//                    }
//                    else {
//                        AppUtils.setGlideImage(mContext, ((NewsTypeViewHolder) holder).profile_image, posts.get(position).getUser().getUser_image());
//                    }

                    ((NewsTypeViewHolder) holder).tv_name.setText(posts.get(position).getNews_source().getName());
                    ((NewsTypeViewHolder) holder).tv_url.setText(posts.get(position).getNews_source().getUrl());
                    ((NewsTypeViewHolder) holder).channelname.setText(posts.get(position).getNews_source().getName());
                    ((NewsTypeViewHolder) holder).tv_newstitle.setText(posts.get(position).getTitle());
                    AppUtils.setGlideImage(mContext, ((NewsTypeViewHolder) holder).profile_image, posts.get(position).getNews_source().getImage());
                    AppUtils.setGlideUrlThumbnail(mContext, ((NewsTypeViewHolder) holder).img_link, posts.get(position).getImage());
                    if (position == 0) {
                        ((NewsTypeViewHolder) holder).top_image.setVisibility(View.GONE);
                    } else {
                        ((NewsTypeViewHolder) holder).top_image.setVisibility(View.VISIBLE);
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

        void onNewsLikeClick(View view, int position, TextView likeCount);

        void onJobLikeClick(View view, int position, TextView likeCount);

        void onShareClick(View view, int position);

        void onConnectClick(View view, int position);

        void onVideoClick(View view, int position);

        void onURLClick(View view, int position,String type);

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
