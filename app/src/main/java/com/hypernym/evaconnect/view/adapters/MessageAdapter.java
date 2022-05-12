package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.toolbar.OnItemClickListener;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    private Context context;
    private List<NetworkConnection> networkConnectionList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public MessageAdapter(Context context, List<NetworkConnection> networkConnections, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.networkConnectionList = networkConnections;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend_list, parent, false);
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        holder.tv_name.setText(networkConnectionList.get(position).getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tv_lastmsg.setText(Html.fromHtml(networkConnectionList.get(position).getMessage(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tv_lastmsg.setText(Html.fromHtml(networkConnectionList.get(position).getMessage()));
        }
       AppUtils.setGlideImage(context, (holder).mImageview6, networkConnectionList.get(position).getUserImage());
        holder.tv_minago.setText(DateUtils.getDateTimeFromTimestamp(networkConnectionList.get(position).getCreatedDatetime()));

        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, networkConnectionList.get(position), position, "SimpleAdapter");
            }
        });
        if (networkConnectionList.get(position).getCreatedDatetime() != null) {
            holder.mtextview22.setText(DateUtils.getTimeAgo(networkConnectionList.get(position).getCreatedDatetime()));
        }
        if(networkConnectionList.get(position).isUnread() && networkConnectionList.get(position).getMessageCount()>0)
        {
            holder.notification.setVisibility(View.VISIBLE);
            holder.tv_count.setVisibility(View.VISIBLE);
            holder.tv_count.setText(String.valueOf(networkConnectionList.get(position).getMessageCount()));
            holder.tv_lastmsg.setTextColor(context.getResources().getColor(com.skydoves.powermenu.R.color.black));
            holder.tv_lastmsg.setTypeface(holder.tv_lastmsg.getTypeface(), Typeface.BOLD);
        }
        else
        {
            holder.notification.setVisibility(View.GONE);
            holder.tv_count.setVisibility(View.GONE);
            holder.tv_lastmsg.setTextColor(context.getResources().getColor(R.color.gray));
        }
       // holder.tv_name.setText(String.valueOf(networkConnectionList.get(position).getReceiverId()));
    }

    @Override
    public int getItemCount() {
        return networkConnectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_lastmsg, mtextview22,tv_minago,tv_count;
        CircleImageView mImageview6;
        ConstraintLayout constraintLayout;
        ImageView notification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_lastmsg = (TextView) itemView.findViewById(R.id.tv_lastmsg);
            mtextview22 = (TextView) itemView.findViewById(R.id.textView22);
            mImageview6 = (CircleImageView) itemView.findViewById(R.id.imageView6);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.ly_main);
            tv_minago=itemView.findViewById(R.id.tv_minago);
            tv_count=itemView.findViewById(R.id.tv_count);
            notification=itemView.findViewById(R.id.notification);
        }
    }
}
