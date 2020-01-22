package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.Notification;
import com.hypernym.evaconnect.toolbar.OnItemClickListener;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

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
        if (networkConnectionList.get(position).getSenderId().equals(LoginUtils.getUser().getId())) {
            holder.mtextview20.setText(networkConnectionList.get(position).getReceiver().getFirstName());
            holder.mtextview21.setText(networkConnectionList.get(position).getReceiver().getBioData());
            AppUtils.setGlideImage(context, (holder).mImageview6, networkConnectionList.get(position).getReceiver().getUserImage());
        }
        if (networkConnectionList.get(position).getReceiverId().equals(LoginUtils.getUser().getId())) {
            holder.mtextview20.setText(networkConnectionList.get(position).getSender().getFirstName());
            holder.mtextview21.setText(networkConnectionList.get(position).getSender().getBioData());
            AppUtils.setGlideImage(context, (holder).mImageview6, networkConnectionList.get(position).getSender().getUserImage());
        }
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, networkConnectionList.get(position), position, "SimpleAdapter");
            }
        });
        if (networkConnectionList.get(position).getCreatedDatetime() != null) {
            holder.mtextview22.setText(DateUtils.getTimeAgo(networkConnectionList.get(position).getCreatedDatetime()));
        }
    }

    @Override
    public int getItemCount() {
        return networkConnectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mtextview20, mtextview21, mtextview22;
        CircleImageView mImageview6;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtextview20 = (TextView) itemView.findViewById(R.id.textView20);
            mtextview21 = (TextView) itemView.findViewById(R.id.textView21);
            mtextview22 = (TextView) itemView.findViewById(R.id.textView22);
            mImageview6 = (CircleImageView) itemView.findViewById(R.id.imageView6);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.linearLayout6);
        }
    }
}
