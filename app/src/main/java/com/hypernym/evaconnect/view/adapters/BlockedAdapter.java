package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.GetBlockedData;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BlockedAdapter extends RecyclerView.Adapter<BlockedAdapter.ViewHolder>{

    private Context context;
    private List<GetBlockedData> connections, originalConnections;
    private BlockedAdapter.OnItemClickListener onItemClickListener;
    int count = 0;
    private boolean isLoaderVisible = false;

    public BlockedAdapter(Context context, List<GetBlockedData> connectionList, BlockedAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.connections = connectionList;
        this.onItemClickListener = onItemClickListener;
        this.originalConnections = connectionList;
    }

    @NonNull
    @Override
    public BlockedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_block, parent, false);
        return new BlockedAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BlockedAdapter.ViewHolder holder, int position) {
        User user = LoginUtils.getLoggedinUser();

        if (connections.get(position).getSender() != null && connections.get(position).getReceiver() != null) {
            if (user.getId().equals(connections.get(position).getSenderId())) {
                if (!TextUtils.isEmpty(connections.get(position).getReceiver().getUserImage())) {
                    AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getReceiver().getUserImage());
                }
                if (connections.get(position).getReceiver().getFirstName() != null) {
                    holder.tv_name.setText(connections.get(position).getReceiver().getFirstName());
                    if (connections.get(position).getReceiver().getLastName() != null) {
                        holder.tv_name.setText(connections.get(position).getReceiver().getFirstName() + connections.get(position).getReceiver().getLastName());

                    }
                }
                if (connections.get(position).getReceiver().getBioData() != null && !connections.get(position).getReceiver().getBioData().isEmpty()) {
                    holder.tv_designation.setText(connections.get(position).getReceiver().getBioData());
                } else {
                    holder.tv_designation.setVisibility(View.GONE);
                }
                if (connections.get(position).getReceiver().getIs_online()) {
                    holder.tv_connection_status.setText("Online");
                    holder.tv_connection_status.setTextColor(context.getResources().getColor(R.color.skyblue));
                } else {
                    if (connections.get(position).getReceiver().getIs_online() != null) {
                        holder.tv_connection_status.setText("Last Online " + DateUtils.formatToYesterdayOrToday(connections.get(position).getReceiver().getLast_online_datetime()));
                        holder.tv_connection_status.setTextColor(context.getResources().getColor(R.color.gray));
                    } else {
                        holder.tv_connection_status.setText("-");
                    }

                }


            } else if (user.getId().equals(connections.get(position).getReceiverId())) {
                if (!TextUtils.isEmpty(connections.get(position).getSender().getUserImage())) {
                    AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getSender().getUserImage());
                }
                if (connections.get(position).getSender().getFirstName() != null) {
                    holder.tv_name.setText(connections.get(position).getSender().getFirstName());
                }
                if (connections.get(position).getSender().getBioData() != null && !connections.get(position).getSender().getBioData().isEmpty()) {
                    holder.tv_designation.setText(connections.get(position).getSender().getBioData());
                } else {
                    holder.tv_designation.setVisibility(View.GONE);
                }
                if (connections.get(position).getSender().getIs_online()) {
                    holder.tv_connection_status.setText("Online");
                    holder.tv_connection_status.setTextColor(context.getResources().getColor(R.color.skyblue));
                } else {
                    if (connections.get(position).getSender().getIs_online() != null) {
                        holder.tv_connection_status.setText("Last Online " + DateUtils.formatToYesterdayOrToday(connections.get(position).getSender().getLast_online_datetime()));
                        holder.tv_connection_status.setTextColor(context.getResources().getColor(R.color.gray));
                    } else {
                        holder.tv_connection_status.setText("-");
                    }

                }

            }
        }
      //  Receiver receiver = connections.get(0).getData().get(position).getSender();
//        if (!TextUtils.isEmpty(connections.get(position).getSender().getUserImage())) {
//            AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getSender().getUserImage());
//        }
//        else if (connections.get(position).getIs_facebook() == 1 && !TextUtils.isEmpty(connections.get(position).getFacebook_image_url())) {
//            AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getFacebook_image_url());
//        } else {
//            AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getUser_image());
//        }
//        holder.tv_name.setText(connections.get(position).getSender().getFirstName());
//        if (connections.get(position).getSender().getBioData() != null && !connections.get(position).getSender().getBioData().isEmpty()) {
//            holder.tv_designation.setText(connections.get(position).getSender().getBioData());
//        } else {
//            holder.tv_designation.setText("--");
//        }

        /*
        if(connections.get(position).getDesignation()!=null)
        {
            holder.location.setText(connections.get(position).getDesignation()+" at ");
        }*/


//        holder.tv_designation.setText(connections.get(position).getSender().getCompanyName());
//        //Hide connect option if post is from logged in user
//        User user = LoginUtils.getLoggedinUser();
/*        if (connections.get(position).getId().equals(user.getId())) {
            holder.tv_connect.setVisibility(View.GONE);
        } else {
            holder.tv_connect.setVisibility(View.VISIBLE);
            String connectionstatus = AppUtils.getConnectionStatus(context, connections.get(position).getIs_connected(), connections.get(position).isIs_receiver());
            holder.tv_connect.setText(AppUtils.getConnectionStatus(context, connections.get(position).getIs_connected(), connections.get(position).isIs_receiver()));
            if (connectionstatus.equals(AppConstants.REQUEST_ACCEPT)) {
                holder.tv_connect.setBackgroundResource(R.drawable.rounded_button_border_green);
                holder.tv_connect.setTextColor(context.getResources().getColor(R.color.light_green));
                holder.tv_decline.setVisibility(View.VISIBLE);
            } else {
                holder.tv_decline.setVisibility(View.GONE);
                holder.tv_connect.setBackgroundResource(R.drawable.rounded_button_border_blue);
                holder.tv_connect.setTextColor(context.getResources().getColor(R.color.skyblue));
            }
            if (connectionstatus.equals(AppConstants.CONNECTED)) {
                holder.tv_decline.setVisibility(View.GONE);
            }

        }*/
//        if(connections.get(position).getSender().getIs_online())
//        {
//            holder.tv_connection_status.setText("Online");
//            holder.tv_connection_status.setTextColor(context.getResources().getColor(R.color.skyblue));
//        }
//        else
//        {
//            if(connections.get(position).getSender().getLast_online_datetime()!=null)
//            {
//                holder.tv_connection_status.setText("Last Online "+ DateUtils.formatToYesterdayOrToday(connections.get(position).getSender().getLast_online_datetime()));
//                holder.tv_connection_status.setTextColor(context.getResources().getColor(R.color.gray));
//            }
//            else
//            {
//                holder.tv_connection_status.setText("-");
//            }
//
//        }

    }

    @Override
    public int getItemCount() {
        return connections.size();
    }

    public void hSetList(List<GetBlockedData> hSearchList) {
        connections = hSearchList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.profile_image)
        ImageView profile_image;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_field)
        TextView tv_designation;

        @BindView(R.id.tv_connect)
        TextView tv_connect;

        @BindView(R.id.tv_decline)
        TextView tv_decline;

        @BindView(R.id.tv_connection_status)
        TextView tv_connection_status;

        @BindView(R.id.location)
        TextView location;

        @BindView(R.id.tv_unblock)
        TextView tv_unblock;

        @BindView(R.id.ly_main)
        LinearLayout ly_main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tv_connect.setOnClickListener(this);
            tv_decline.setOnClickListener(this);
            ly_main.setOnClickListener(this);
            tv_unblock.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_connect:
                case R.id.tv_decline:

                case R.id.ly_main:

                case R.id.tv_unblock:
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, originalConnections.indexOf(connections.get(getAdapterPosition())));
                    break;

            }

//            if (onItemClickListener != null)
//                onItemClickListener.onItemClick(v, originalConnections.indexOf(connections.get(getAdapterPosition())));
        }
    }

    public void filterList(List<GetBlockedData> filterdNames) {
        connections.clear();
        if (filterdNames.size() > 0) {

            connections.addAll(filterdNames);
        } else {
            connections.addAll(originalConnections);
        }

        //this.filteredConnections=connections;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void removeLoading() {
        isLoaderVisible = false;
        int position = connections.size() - 1;
        GetBlockedData item = getItem(position);
        if (item != null) {
            connections.remove(position);
            notifyItemRemoved(position);
        }
    }

    GetBlockedData getItem(int position) {
        if (connections.size() > 0)
            return connections.get(position);
        else
            return null;
    }

    public void removeAt(int position) {
        connections.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, connections.size());
    }

}
