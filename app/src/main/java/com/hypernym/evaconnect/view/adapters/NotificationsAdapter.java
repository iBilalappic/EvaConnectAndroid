package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    private Context context;
    private List<Post> notificationsList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private boolean isLoaderVisible = false;

    public NotificationsAdapter(Context context, List<Post> notifications, OnItemClickListener itemClickListener) {
        this.context = context;
        this.notificationsList = notifications;
        this.onItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationsAdapter.ViewHolder holder, int position) {
        AppUtils.setGlideImage(context, holder.profile_image, notificationsList.get(position).getUser().getUser_image());

        if(notificationsList.get(position).getObject_type().equalsIgnoreCase("meeting") ||
                notificationsList.get(position).getObject_type().equalsIgnoreCase("event"))
        {
            holder.tv_status.setText(notificationsList.get(position).getContent());
        }
        else if (notificationsList.get(position).getIs_like()!=0) {
            holder.tv_status.setText(notificationsList.get(position).getName()+" "+"liked your post ");

        } else {
            holder.tv_status.setText(notificationsList.get(position).getName()+" "+"commented your post");

        }
      holder.tv_date.setText(DateUtils.formatToYesterdayOrToday(notificationsList.get(position).getCreated_datetime()));
//        holder.linearLayout10.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                onItemClickListener.onItemClick(v, position);
//            }
//        });
        //holder.tv_content.setText(notificationsList.get(position).getDetails());
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.profile_image)
        ImageView profile_image;

        @BindView(R.id.tv_status)
        TextView tv_status;

        @BindView(R.id.tv_date)
        TextView tv_date;

         @BindView(R.id.linearLayout10)
        ConstraintLayout linearLayout10;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    public void removeLoading() {
        isLoaderVisible = false;
        int position = notificationsList.size() - 1;
        Post item = getItem(position);
        if (item != null) {
           // notificationsList.remove(position);
           // notifyItemRemoved(position);
        }
    }
    Post getItem(int position) {
        if(notificationsList.size()>0)
            return notificationsList.get(position);
        else
            return null;
    }
    public void clear() {
        notificationsList.clear();
        notifyDataSetChanged();
    }


}
