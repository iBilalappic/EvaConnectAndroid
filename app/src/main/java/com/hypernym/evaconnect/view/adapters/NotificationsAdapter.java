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
import com.hypernym.evaconnect.viewmodel.ConnectionViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.ViewHolder> {
    private Context context;
    private List<Post> notificationsList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private boolean isLoaderVisible = false;
    private ConnectionViewModel connectionViewModel;

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
        holder.tv_status.setText(notificationsList.get(position).getContent());
        holder.tv_date.setText(DateUtils.formatToYesterdayOrToday(notificationsList.get(position).getCreated_datetime()));
        if ( notificationsList.get(position).getContent().contains("commented")) {
            holder.tv_visit.setVisibility(View.VISIBLE);
        } else {
            holder.tv_visit.setVisibility(View.GONE);
        }
        if(notificationsList.get(position).getObject_type().equalsIgnoreCase("connection"))
        {
            holder.tv_connect.setVisibility(View.VISIBLE);
            holder.tv_visit.setVisibility(View.GONE);

        }
        else
        {
            holder.tv_connect.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!notificationsList.get(position).getObject_type().equalsIgnoreCase("connection"))
                {
                    onItemClickListener.onItemClick(v, position);
                }
            }
        });

        holder.tv_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onAcceptClick(v, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.profile_image)
        ImageView profile_image;

        @BindView(R.id.tv_status)
        TextView tv_status;

        @BindView(R.id.tv_date)
        TextView tv_date;

        @BindView(R.id.linearLayout10)
        ConstraintLayout linearLayout10;

        @BindView(R.id.tv_connect)
        TextView tv_connect;


        @BindView(R.id.tv_visit)
        TextView tv_visit;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }
//
//        @Override
//        public void onClick(View v) {
//            if(v.getId()==R.id.tv_connect)
//            onItemClickListener.onItemClick(v, getAdapterPosition());
//        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
        void onAcceptClick(View view,int position);
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
