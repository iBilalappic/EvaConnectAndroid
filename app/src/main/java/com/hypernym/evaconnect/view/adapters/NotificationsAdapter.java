package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.Notification;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    private Context context;
    private List<Post> notificationsList=new ArrayList<>();
    private NotificationsAdapter.OnItemClickListener itemClickListener;

    public NotificationsAdapter(Context context,List<Post> notifications,NotificationsAdapter.OnItemClickListener itemClickListener)
    {
        this.context=context;
        this.notificationsList=notifications;
        this.itemClickListener=itemClickListener;
    }

    @NonNull
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.item_notification,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder holder, int position) {
        AppUtils.setGlideImage(context,holder.profile_image,notificationsList.get(position).getUser().getUser_image());
        holder.tv_name.setText(notificationsList.get(position).getUser().getFirst_name());
        if(notificationsList.get(position).getObject_type().equalsIgnoreCase("like"))
        {
            holder.tv_status.setText("Liked your post "+ DateUtils.getTimeAgo(notificationsList.get(position).getCreated_datetime()));
            holder.img_type.setImageDrawable(context.getDrawable(R.drawable.like_selected));
        }
        else if(notificationsList.get(position).getObject_type().equalsIgnoreCase("comment"))
        {
            holder.tv_status.setText("Commented your post "+ DateUtils.getTimeAgo(notificationsList.get(position).getCreated_datetime()));
            holder.img_type.setImageDrawable(context.getDrawable(R.drawable.comment));
        }
        AppUtils.makeTextViewResizable( holder.tv_content,3,notificationsList.get(position).getDetails());
        //holder.tv_content.setText(notificationsList.get(position).getDetails());
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.profile_image)
        ImageView profile_image;

        @BindView(R.id.tv_status)
        TextView tv_status;

        @BindView(R.id.tv_content)
        TextView tv_content;

        @BindView(R.id.img_type)
        ImageView img_type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener!=null)
                itemClickListener.onItemClick(v,getAdapterPosition());
        }
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
