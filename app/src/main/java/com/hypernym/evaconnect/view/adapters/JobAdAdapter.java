package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobAdAdapter extends RecyclerView.Adapter<JobAdAdapter.ViewHolder> {
    private Context context;
    private List<JobAd> jobAdList = new ArrayList<>();
    private JobAdAdapter.OnItemClickListener onItemClickListener;
    private SimpleDialog simpleDialog;

    public JobAdAdapter(Context context, List<JobAd> jobAdList, JobAdAdapter.OnItemClickListener itemClickListener) {
        this.context = context;
        this.jobAdList = jobAdList;
        this.onItemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public JobAdAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_joblisting, parent, false);
        return new JobAdAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull JobAdAdapter.ViewHolder holder, int position) {

        holder.tv_name.setText(jobAdList.get(position).getJobTitle());
        holder.tv_postion.setText(jobAdList.get(position).getPosition());
        holder.tv_content.setText(jobAdList.get(position).getContent());
        DecimalFormat myFormatter = new DecimalFormat("############");
        holder.tv_salary.setText("£ " + myFormatter.format(jobAdList.get(position).getSalary()) + " pa");
        AppUtils.setGlideImage(context, (holder).profile_image, jobAdList.get(position).getJobImage());

        if (jobAdList.get(position).getIs_job_like() != null && jobAdList.get(position).getIs_job_like() > 0) {
            holder.img_like.setBackground(context.getDrawable(R.drawable.like_selected));
        } else {
            holder.img_like.setBackground(context.getDrawable(R.drawable.ic_like));
        }

        if(jobAdList.get(position).getUserId()== LoginUtils.getLoggedinUser().getId())
        {
            holder.img_more.setVisibility(View.VISIBLE);
            holder.tv_gotoadd.setVisibility(View.GONE);
        }
        else
        {
            holder.img_more.setVisibility(View.GONE);
            holder.tv_gotoadd.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return jobAdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name, tv_postion, tv_salary, tv_gotoadd,tv_content;
        CircleImageView profile_image;
        ImageView img_like, img_comment, img_share,img_more;
        LinearLayout comment_click;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_postion = itemView.findViewById(R.id.tv_connections);
            comment_click=itemView.findViewById(R.id.comment_click);

            tv_salary = itemView.findViewById(R.id.tv_salary);
            tv_gotoadd = itemView.findViewById(R.id.tv_gotoadd);
            profile_image = itemView.findViewById(R.id.profile_image);
            img_like = itemView.findViewById(R.id.img_like);
            img_comment = itemView.findViewById(R.id.img_comment);
            img_share = itemView.findViewById(R.id.img_share);
            tv_content = itemView.findViewById(R.id.tv_content);
            img_more=itemView.findViewById(R.id.img_more);
            itemView.setOnClickListener(this);
            img_like.setOnClickListener(this);
            img_share.setOnClickListener(this);
            tv_gotoadd.setOnClickListener(this);
//            img_comment.setOnClickListener(this);
            comment_click.setOnClickListener(this);
            img_more.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.img_like:
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                    break;
                case R.id.tv_gotoadd:
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                    break;
                case R.id.img_share:
                    onItemClickListener.onItemClick(v, getAdapterPosition());
                    break;
                case R.id.like_click:
                    onItemClickListener.onItemClick(v,getAdapterPosition());
                    break;
                case R.id.comment_click:
                    onItemClickListener.onItemClick(v,getAdapterPosition());
                    break;
                case R.id.img_more:
                    /** Instantiating PopupMenu class */
                    PopupMenu popup = new PopupMenu(context, v);

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
                            if (item.getTitle().toString().equalsIgnoreCase(context.getString(R.string.edit_comment))) {
                                onItemClickListener.onEditClick(v, getAdapterPosition());
//                            editCommentDialog=new EditCommentDialog(context,holder.tv_content.getText().toString(),context.getString(R.string.cancel),context.getString(R.string.button_save_changes), new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    switch (v.getId()) {
//                                        case R.id.button_positive:
//                                            EditText comment=(EditText)editCommentDialog.findViewById(R.id.edt_comment);
//                                            onItemClickListener.onEditComment(v, position,comment.getText().toString());
//                                            break;
//                                        case R.id.button_negative:
//                                            break;
//                                    }
//                                    editCommentDialog.dismiss();
//                                }
//                            });
//                            editCommentDialog.show();

                            }  else if (item.getTitle().toString().equalsIgnoreCase(context.getString(R.string.delete_comment))) {
                                simpleDialog = new SimpleDialog(context, context.getString(R.string.confirmation), context.getString(R.string.msg_remove_comment), context.getString(R.string.button_no), context.getString(R.string.button_yes), new OnOneOffClickListener() {
                                    @Override
                                    public void onSingleClick(View v) {
                                        switch (v.getId()) {
                                            case R.id.button_positive:
                                                onItemClickListener.onDeleteClick(v,getAdapterPosition());
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
                    popup.setForceShowIcon(true);

                    /** Showing the popup menu */
                    popup.show();
                    break;
            }

        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onEditClick(View view, int position);
        void onDeleteClick(View view, int position);
    }
    // parent activity will implement this method to respond to click events

}
