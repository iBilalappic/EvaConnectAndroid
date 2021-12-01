package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class JobHomeAdapter extends RecyclerView.Adapter {

    private List<Post> posts = new ArrayList<>();
    private Context mContext;
    int total_types;
    private boolean fabStateVolume = false;
    private JobHomeAdapter.ItemClickListener mClickListener;
    private SliderImageAdapter sliderImageAdapter;
    // flag for footer ProgressBar (i.e. last item of list)
    private boolean isLoadingAdded = false;
    private boolean isLoaderVisible = false;
    GestureDetector gestureDetector;
    SimpleDialog simpleDialog;


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

        @BindView(R.id.comment_click)
        LinearLayout comment_click;

        @BindView(R.id.like_click)
        LinearLayout like_click;

        @BindView(R.id.share_click)
        LinearLayout share_click;

        @BindView(R.id.img_more)
        ImageView img_more;

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
                     mClickListener.onApplyCommentClick(v, getAdapterPosition());
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

            profile_image.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onApplyClick(v, getAdapterPosition());
                }
            });

            tv_name.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    mClickListener.onApplyClick(v, getAdapterPosition());
                }
            });
            img_more.setOnClickListener(new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    /** Instantiating PopupMenu class */
                    PopupMenu popup = new PopupMenu(mContext, v);

                    try {
                        Field[] fields = popup.getClass().getDeclaredFields();
                        for (Field field : fields) {
                            if ("mPopup".equals(field.getName())) {
                                field.setAccessible(true);
                                Object menuPopupHelper = field.get(popup);
                                Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                                Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                                setForceIcons.invoke(menuPopupHelper, true);
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    /** Adding menu items to the popumenu */
                    popup.getMenuInflater().inflate(R.menu.job_menu, popup.getMenu());

//                if(comments.get(position).isPostMine())
//                {
//                    holder.more.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    holder.more.setVisibility(View.GONE);
//                }
                    /** Defining menu item click listener for the popup menu */
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            //    Toast.makeText(getContext(), item.getGroupId()+"You selected the action : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                            if (item.getTitle().toString().equalsIgnoreCase(mContext.getString(R.string.edit_job))) {
                                mClickListener.onEditClick(v, getAdapterPosition());

                            }  else if (item.getTitle().toString().equalsIgnoreCase(mContext.getString(R.string.delete_job))) {
                                simpleDialog = new SimpleDialog(mContext, mContext.getString(R.string.confirmation), mContext.getString(R.string.msg_remove_job), mContext.getString(R.string.button_no), mContext.getString(R.string.button_yes), new OnOneOffClickListener() {
                                    @Override
                                    public void onSingleClick(View v) {
                                        switch (v.getId()) {
                                            case R.id.button_positive:
                                                mClickListener.onDeleteClick(v,getAdapterPosition());
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


    public JobHomeAdapter(Context context, List<Post> data, JobHomeAdapter.ItemClickListener mClickListener) {
        this.posts = data;
        this.mContext = context;
        total_types = posts.size();
        this.mClickListener = mClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {

            case AppConstants.JOB_TYPE:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.job_type, parent, false);
                return new JobHomeAdapter.JobTypeViewHolder(view);
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

                case AppConstants.JOB_TYPE:
                    if (String.valueOf(posts.get(position).getComment_count()).equals("0")) {
                        ((JobHomeAdapter.JobTypeViewHolder) holder).tv_viewcomments.setVisibility(View.GONE);
                    }
                    ((JobHomeAdapter.JobTypeViewHolder) holder).tv_likecount.setText(String.valueOf(posts.get(position).getLike_count()));
                    ((JobHomeAdapter.JobTypeViewHolder) holder).tv_comcount.setText(String.valueOf(posts.get(position).getComment_count()));

                    if (posts.get(position).getIs_job_like() != null && posts.get(position).getIs_job_like() > 0) {
                        ((JobHomeAdapter.JobTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.like_selected));
                    } else {
                        ((JobHomeAdapter.JobTypeViewHolder) holder).img_like.setBackground(mContext.getDrawable(R.drawable.ic_like));
                    }

                    AppUtils.setGlideImage(mContext, ((JobHomeAdapter.JobTypeViewHolder) holder).profile_image, posts.get(position).getJob_image());
                    ((JobHomeAdapter.JobTypeViewHolder) holder).tv_name.setText(posts.get(position).getJob_title());
                    DecimalFormat myFormatter = new DecimalFormat("############");

                    ((JobHomeAdapter.JobTypeViewHolder) holder).tv_location.setText(posts.get(position).getJob_sector()
                            +" | "+ posts.get(position).getLocation()+" | "+"£ "+myFormatter.format(posts.get((position)).getSalary())+" PA");
                   // ((JobHomeAdapter.JobTypeViewHolder) holder).tv_salary.setText("£ " + myFormatter.format(posts.get(position).getSalary()) + " pa");
                    if (position == 0) {
                        ((JobHomeAdapter.JobTypeViewHolder) holder).top_image.setVisibility(View.GONE);
                    } else {
                        ((JobHomeAdapter.JobTypeViewHolder) holder).top_image.setVisibility(View.VISIBLE);
                    }
                    if(posts.get(position).getUser().getId().equals(LoginUtils.getLoggedinUser().getId()))
                    {
                        ((JobHomeAdapter.JobTypeViewHolder) holder).img_more.setVisibility(View.VISIBLE);
                        ((JobTypeViewHolder) holder).tv_apply.setVisibility(View.GONE);
                    }
                    else
                    {
                        ((JobHomeAdapter.JobTypeViewHolder) holder).img_more.setVisibility(View.GONE);
                        ((JobTypeViewHolder) holder).tv_apply.setVisibility(View.VISIBLE);
                    }
                    if (LoginUtils.getUser() != null && LoginUtils.getUser().getType().equals("company")) {
                        ((JobHomeAdapter.JobTypeViewHolder) holder).tv_apply.setVisibility(View.GONE);
                    } else {
                        ((JobHomeAdapter.JobTypeViewHolder) holder).tv_apply.setVisibility(View.VISIBLE);
                        if(posts.get(position).getIs_applied()==1)
                        {
                            ((JobHomeAdapter.JobTypeViewHolder) holder).tv_apply.setText("Applied");
                        }
                        else
                        {
                            ((JobHomeAdapter.JobTypeViewHolder) holder).tv_apply.setText("Apply");
                            ((JobHomeAdapter.JobTypeViewHolder) holder).tv_apply.setOnClickListener(new OnOneOffClickListener() {
                                @Override
                                public void onSingleClick(View v) {

                                    mClickListener.onApplyClick(v, position);
                                }
                            });
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


        void onJobLikeClick(View view, int position, TextView likeCount);

        void onShareClick(View view, int position);

        void onApplyClick(View view, int position);

        void onApplyCommentClick(View view, int position);


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

}
