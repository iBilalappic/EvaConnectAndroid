package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectionsAdapter extends RecyclerView.Adapter<ConnectionsAdapter.ViewHolder> {
    private Context context;
    private List<User> connections, originalConnections;
    private ConnectionsAdapter.OnItemClickListener onItemClickListener;
    int count = 0;
    private boolean isLoaderVisible = false;

    public ConnectionsAdapter(Context context, List<User> connectionList, ConnectionsAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.connections = connectionList;
        this.onItemClickListener = onItemClickListener;
        this.originalConnections = connectionList;
    }

    @NonNull
    @Override
    public ConnectionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_connection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionsAdapter.ViewHolder holder, int position) {
        if (!TextUtils.isEmpty(connections.get(position).getUser_image())) {
            AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getUser_image());
        }
//        else if (connections.get(position).getIs_facebook() == 1 && !TextUtils.isEmpty(connections.get(position).getFacebook_image_url())) {
//            AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getFacebook_image_url());
//        } else {
//            AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getUser_image());
//        }
        holder.tv_name.setText(connections.get(position).getFirst_name());
        if (connections.get(position).getBio_data() != null && !connections.get(position).getBio_data().isEmpty()) {
            holder.tv_designation.setText(connections.get(position).getBio_data());
        } else {
            holder.tv_designation.setText("--");
        }



holder.location.setText(connections.get(position).getDesignation()+" at ");
        holder.tv_designation.setText(connections.get(position).getCompany_name());
        //Hide connect option if post is from logged in user
        User user = LoginUtils.getLoggedinUser();
        if (connections.get(position).getId().equals(user.getId())) {
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

        }
        if(connections.get(position).isIs_online())
        {
            holder.tv_connection_status.setText("Online");
            holder.tv_connection_status.setTextColor(context.getResources().getColor(R.color.skyblue));
        }
        else
        {
            if(connections.get(position).getLast_online_datetime()!=null)
            {
                holder.tv_connection_status.setText("Last Online "+ DateUtils.formatToYesterdayOrToday(connections.get(position).getLast_online_datetime()));
                holder.tv_connection_status.setTextColor(context.getResources().getColor(R.color.gray));
            }
            else
            {
                holder.tv_connection_status.setText("-");
            }

        }

    }

    @Override
    public int getItemCount() {
        return connections.size();
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


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tv_connect.setOnClickListener(this);
            tv_decline.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_connect:
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, originalConnections.indexOf(connections.get(getAdapterPosition())));
                    break;
                case R.id.tv_decline:
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, originalConnections.indexOf(connections.get(getAdapterPosition())));
                    break;
            }

//            if (onItemClickListener != null)
//                onItemClickListener.onItemClick(v, originalConnections.indexOf(connections.get(getAdapterPosition())));
        }
    }

    //This method will filter the list
    //here we are passing the filtered data
    //and assigning it to the list with notifydatasetchanged method
    public void filterList(List<User> filterdNames) {
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
        User item = getItem(position);
        if (item != null) {
            connections.remove(position);
            notifyItemRemoved(position);
        }
    }

    User getItem(int position) {
        if (connections.size() > 0)
            return connections.get(position);
        else
            return null;
    }

}
