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

public class HorizontalMessageAdapter extends RecyclerView.Adapter<HorizontalMessageAdapter.ViewHolder> {
    private Context context;
    private List<NetworkConnection> networkConnectionList = new ArrayList<>();
    private List<NetworkConnection> originalNetworkConnectionList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private boolean issearchApplied=false;
    private int row_index = 0;

    public HorizontalMessageAdapter(Context context, List<NetworkConnection> networkConnections, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.networkConnectionList = networkConnections;
        this.onItemClickListener = onItemClickListener;
        this.originalNetworkConnectionList=networkConnectionList;
    }

    @NonNull
    @Override
    public HorizontalMessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_horizontal_friendlist, parent, false);
        return new HorizontalMessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalMessageAdapter.ViewHolder holder, int position) {
        if (networkConnectionList.get(position).getSenderId().equals(LoginUtils.getUser().getId())) {
            holder.mtextview20.setText(networkConnectionList.get(position).getReceiver().getFirstName());
            AppUtils.setGlideImage(context, (holder).mImageview6, networkConnectionList.get(position).getReceiver().getUserImage());
        }
        if (networkConnectionList.get(position).getReceiverId().equals(LoginUtils.getUser().getId())) {
            holder.mtextview20.setText(networkConnectionList.get(position).getSender().getFirstName());
            AppUtils.setGlideImage(context, (holder).mImageview6, networkConnectionList.get(position).getSender().getUserImage());
        }
        if(issearchApplied && networkConnectionList.size()>0)
        {
            onItemClickListener.onItemClick(holder.constraintLayout, networkConnectionList.get(0), originalNetworkConnectionList.indexOf(networkConnectionList.get(0)), "horizontalAdapter");
        }
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                row_index = position;
                notifyDataSetChanged();
                issearchApplied=false;
                onItemClickListener.onItemClick(view, networkConnectionList.get(position), originalNetworkConnectionList.indexOf(networkConnectionList.get(position)), "horizontalAdapter");

            }
        });
        if (row_index == position) {
            holder.view5.setBackgroundResource(R.color.colorAccent);

            /*holder.selected_view.setVisibility(View.GONE);*/
            /*holder.selected_view2.setVisibility(View.VISIBLE);*/
        } else {
            holder.view5.setBackgroundResource(R.color.gray);
            /*            holder.selected_view.setVisibility(View.GONE);*/
            /*holder.selected_view2.setVisibility(View.GONE);*/
        }

    }

    public void filterList(List<NetworkConnection> filteredList) {
        networkConnectionList = filteredList;
        issearchApplied=true;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return networkConnectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mtextview20;
        CircleImageView mImageview6;
        ConstraintLayout constraintLayout;
        View view5;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtextview20 = (TextView) itemView.findViewById(R.id.textView20);

            mImageview6 = (CircleImageView) itemView.findViewById(R.id.imageView6);
            constraintLayout = (ConstraintLayout) itemView.findViewById(R.id.linearLayout6);
            view5 = (View) itemView.findViewById(R.id.view5);
        }
    }
}
