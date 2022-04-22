package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectionsCompaniesAdapter extends RecyclerView.Adapter<ConnectionsCompaniesAdapter.ViewHolder> {
    private Context context;
    private List<ConnectionModel> connections, originalConnections;
    private ConnectionsCompaniesAdapter.OnItemClickListener onItemClickListener;
    int count = 0;
    private boolean isLoaderVisible = false;

    public ConnectionsCompaniesAdapter(Context context, List<ConnectionModel> connectionList, ConnectionsCompaniesAdapter.OnItemClickListener onItemClickListener) {
        this.context = context;
        this.connections = connectionList;
        this.onItemClickListener = onItemClickListener;
        this.originalConnections = connectionList;
    }

    @NonNull
    @Override
    public ConnectionsCompaniesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_connection, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionsCompaniesAdapter.ViewHolder holder, int position) {
        ConnectionModel user = new ConnectionModel();

        user = connections.get(position);


        if (user.userImage != null) {

            AppUtils.setGlideImage(context, holder.profile_image, user.userImage);
        }


        if (user.firstName != null && user.lastName != null) {
            holder.tv_name.setText(user.firstName + " " + user.lastName);
        }


        if (user.designation != null) {
            holder.tv_designation.setText(user.designation.toString());


        }


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
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(v, originalConnections.indexOf(connections.get(getAdapterPosition())));
                    }
                    break;

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
