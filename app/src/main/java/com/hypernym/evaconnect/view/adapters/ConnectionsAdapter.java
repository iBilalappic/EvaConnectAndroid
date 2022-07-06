package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.ConnectionModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectionsAdapter extends RecyclerView.Adapter<ConnectionsAdapter.ViewHolder> {
    private Context context;
    private List<ConnectionModel> connections, originalConnections;
    private ConnectionsAdapter.OnItemClickListener onItemClickListener;
    int count = 0;
    private boolean isLoaderVisible = false;

    public ConnectionsAdapter(Context context, List<ConnectionModel> connectionList, ConnectionsAdapter.OnItemClickListener onItemClickListener) {
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
        User user = LoginUtils.getLoggedinUser();


        Log.d("ahsan", "onBindViewHolder: " + originalConnections.get(position).firstName);
        Log.d("ahsan", "onBindViewHolder: " + user.first_name );

        if (connections.get(position).sender != null && connections.get(position).receiver != null) {
            if (user.getId().equals(connections.get(position).senderId)) {
                if (!TextUtils.isEmpty(connections.get(position).receiver.getUserImage())) {
                    AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).receiver.getUserImage());
                }
                if (connections.get(position).receiver.getFirstName() != null) {
                    holder.tv_name.setText(connections.get(position).receiver.getFirstName());
                }
                if (connections.get(position).receiver.getDesignation() != null && !connections.get(position).receiver.getDesignation().isEmpty()) {
                    holder.tv_designation.setText(connections.get(position).receiver.getDesignation());
                } else {
                    holder.tv_designation.setVisibility(View.GONE);
                }
                if (connections.get(position).receiver.getIs_online()) {
                    holder.tv_connection_status.setText("Online");
                    holder.tv_connection_status.setTextColor(context.getResources().getColor(R.color.skyblue));
                } else {
                    if (connections.get(position).receiver.getIs_online() != null) {
                        holder.tv_connection_status.setText("Last Online " + DateUtils.formatToYesterdayOrToday(connections.get(position).receiver.getLast_online_datetime()));
                        holder.tv_connection_status.setTextColor(context.getResources().getColor(R.color.gray));
                    } else {
                        holder.tv_connection_status.setText("-");
                    }
                }



            } else if (user.getId().equals(connections.get(position).receiverId)) {
                if (!TextUtils.isEmpty(connections.get(position).sender.getUserImage())) {
                    AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).sender.getUserImage());
                }
                if (connections.get(position).sender.getFirstName() != null) {
                    holder.tv_name.setText(connections.get(position).sender.getFirstName() + " " + connections.get(position).sender.getLastName());
                }
                if (connections.get(position).sender.getDesignation() != null && !connections.get(position).sender.getDesignation().isEmpty()) {
                    holder.tv_designation.setText(connections.get(position).sender.getDesignation());
                } else {
                    holder.tv_designation.setVisibility(View.GONE);
                }
                if (connections.get(position).sender.getIs_online()) {
                    holder.tv_connection_status.setText("Online");
                    holder.tv_connection_status.setTextColor(context.getResources().getColor(R.color.skyblue));
                } else {
                    if (connections.get(position).sender.getIs_online() != null) {
                        holder.tv_connection_status.setText("Last Online " + DateUtils.formatToYesterdayOrToday(connections.get(position).sender.getLast_online_datetime()));
                        holder.tv_connection_status.setTextColor(context.getResources().getColor(R.color.gray));
                    } else {
                        holder.tv_connection_status.setText("-");
                    }

                }

            }
        } else {
            if (!TextUtils.isEmpty(connections.get(position).userImage)) {
                AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).userImage);
            }
            if (connections.get(position).firstName != null) {
                holder.tv_name.setText(connections.get(position).firstName + " ");

                if (connections.get(position).lastName != null) {
                    holder.tv_name.setText(connections.get(position).firstName + " " + connections.get(position).lastName);

                }
            }
            if (connections.get(position).designation != null && !connections.get(position).designation.equals("")) {
                holder.tv_designation.setText(connections.get(position).designation.toString());
            } else {
                holder.tv_designation.setVisibility(View.GONE);
            }

            if (connections.get(position).isOnline) {
                holder.tv_connection_status.setText("Online");
                holder.tv_connection_status.setTextColor(context.getResources().getColor(R.color.skyblue));
            } else {
                if (connections.get(position).lastOnlineDatetime != null) {
                    holder.tv_connection_status.setText("Last Online " + DateUtils.formatToYesterdayOrToday(connections.get(position).lastOnlineDatetime));
                    holder.tv_connection_status.setTextColor(context.getResources().getColor(R.color.gray));
                } else {
                    holder.tv_connection_status.setText("-");
                }

            }
        }

//        else if (connections.get(position).getIs_facebook() == 1 && !TextUtils.isEmpty(connections.get(position).getFacebook_image_url())) {
//            AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getFacebook_image_url());
//        } else {
//            AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getUser_image());
//        }
//        if(connections.get(position).sender.getFirstName()!=null){
//            holder.tv_name.setText(connections.get(position).sender.getFirstName());
//        }
//        if (connections.get(position).sender.getBioData() != null && !connections.get(position).sender.getBioData().isEmpty()) {
//            holder.tv_designation.setText(connections.get(position).sender.getBioData());
//        } else {
//            holder.tv_designation.setText("--");
//        }

        /*
        if(connections.get(position).getDesignation()!=null)
        {
            holder.location.setText(connections.get(position).getDesignation()+" at ");
        }*/


//        if (connections.get(position).sender.getCompanyName()!=null && !connections.get(position).sender.getCompanyName().isEmpty()) {
//            holder.tv_designation.setVisibility(View.VISIBLE);
//            holder.tv_designation.setText(connections.get(position).sender.getCompanyName());
//        } else {
//            holder.tv_designation.setVisibility(View.GONE);
//        }
        //Hide connect option if post is from logged in user

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
//        if(connections.get(position).isOnline)
//        {
//            holder.tv_connection_status.setText("Online");
//            holder.tv_connection_status.setTextColor(context.getResources().getColor(R.color.skyblue));
//        }
//        else
//        {
//            if(connections.get(position).lastOnlineDatetime!=null)
//            {
//                holder.tv_connection_status.setText("Last Online "+ DateUtils.formatToYesterdayOrToday(connections.get(position).lastOnlineDatetime));
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

    public void hSetList(List<ConnectionModel> hSearchList) {
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

        @BindView(R.id.ly_main)
        LinearLayout ly_main;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            tv_connect.setOnClickListener(this);
            tv_decline.setOnClickListener(this);
            ly_main.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_connect:
                    if (onItemClickListener != null)
                        onItemClickListener.onItemClick(v, originalConnections.indexOf(connections.get(getAdapterPosition())));
                    break;
                case R.id.tv_decline:

                case R.id.ly_main:
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, originalConnections.indexOf(connections.get(getAdapterPosition())));
                    }
                    break;


            }

//            if (onItemClickListener != null)
//                onItemClickListener.onItemClick(v, originalConnections.indexOf(connections.get(getAdapterPosition())));
        }
    }

    //This method will filter the list
    //here we are passing the filtered data
    //and assigning it to the list with notifydatasetchanged method
    public void filterList(List<ConnectionModel> filterdNames) {
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
        ConnectionModel item = getItem(position);
        if (item != null) {
            connections.remove(position);
            notifyItemRemoved(position);
        }
    }

    ConnectionModel getItem(int position) {
        if (connections.size() > 0)
            return connections.get(position);
        else
            return null;
    }

}
