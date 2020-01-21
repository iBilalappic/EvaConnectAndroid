package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.Connection;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectionsAdapter extends RecyclerView.Adapter<ConnectionsAdapter.ViewHolder> {
    private Context context;
    private List<User> connections;

    public ConnectionsAdapter(Context context, List<User> connectionList)
    {
        this.context=context;
        this.connections= connectionList;
    }
    @NonNull
    @Override
    public ConnectionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_connection,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionsAdapter.ViewHolder holder, int position) {
        AppUtils.setGlideImage(context,holder.profile_image,connections.get(position).getUser_image());
        holder.tv_name.setText(connections.get(position).getFirst_name());
        holder.tv_connect.setText(AppUtils.getConnectionStatus(context,connections.get(position).getIs_connected()));
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

        @BindView(R.id.tv_designation)
        TextView tv_designation;

        @BindView(R.id.tv_connect)
        TextView tv_connect;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        @Override
        public void onClick(View v) {

        }
    }
    //This method will filter the list
    //here we are passing the filtered data
    //and assigning it to the list with notifydatasetchanged method
    public void filterList(List<User> filterdNames) {
        this.connections = filterdNames;
        notifyDataSetChanged();
    }
}
