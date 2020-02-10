package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.toolbar.OnItemClickListener;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageDialogAdapter extends RecyclerView.Adapter<MessageDialogAdapter.ViewHolder> {
    private Context context;
    private List<NetworkConnection> networkConnectionList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public MessageDialogAdapter(Context context, List<NetworkConnection> networkConnections, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.networkConnectionList = networkConnections;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MessageDialogAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_friend_list, parent, false);
        return new MessageDialogAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageDialogAdapter.ViewHolder holder, int position) {
        if (networkConnectionList.get(position).getSenderId().equals(LoginUtils.getUser().getId())) {
            holder.tv_name.setText(networkConnectionList.get(position).getReceiver().getFirstName());
            holder.tv_lastmsg.setText(networkConnectionList.get(position).getMessage());
            AppUtils.setGlideImage(context, (holder).mImageview6, networkConnectionList.get(position).getReceiver().getUserImage());
            holder.tv_minago.setText(DateUtils.getTimeAgo(networkConnectionList.get(position).getCreatedDatetime()));
        }
        if (networkConnectionList.get(position).getReceiverId().equals(LoginUtils.getUser().getId())) {
            holder.tv_name.setText(networkConnectionList.get(position).getSender().getFirstName());
            holder.tv_lastmsg.setText(networkConnectionList.get(position).getMessage());
            AppUtils.setGlideImage(context, (holder).mImageview6, networkConnectionList.get(position).getSender().getUserImage());
            holder.tv_minago.setText(DateUtils.getTimeAgo(networkConnectionList.get(position).getCreatedDatetime()));
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
        // holder.tv_name.setText(String.valueOf(networkConnectionList.get(position).getReceiverId()));
    }

    @Override
    public int getItemCount() {
        return networkConnectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name, tv_lastmsg, mtextview22,tv_minago;
        CircleImageView mImageview6;
        ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_lastmsg = (TextView) itemView.findViewById(R.id.tv_lastmsg);
            mtextview22 = (TextView) itemView.findViewById(R.id.textView22);
            mImageview6 = (CircleImageView) itemView.findViewById(R.id.imageView6);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.linearLayout6);
            tv_minago=itemView.findViewById(R.id.tv_minago);
        }
    }
}
