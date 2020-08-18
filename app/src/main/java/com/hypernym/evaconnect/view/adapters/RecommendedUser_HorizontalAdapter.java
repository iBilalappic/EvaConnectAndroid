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
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class RecommendedUser_HorizontalAdapter extends RecyclerView.Adapter<RecommendedUser_HorizontalAdapter.ViewHolder> {
    private Context context;
    private List<User> connections, originalConnections;
    int count = 0;
    private boolean isLoaderVisible = false;

    public RecommendedUser_HorizontalAdapter(Context context, List<User> connectionList) {
        this.context = context;
        this.connections = connectionList;
        this.originalConnections = connectionList;
    }

    @NonNull
    @Override
    public RecommendedUser_HorizontalAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommended_user, parent, false);
        return new RecommendedUser_HorizontalAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendedUser_HorizontalAdapter.ViewHolder holder, int position) {
        if (!TextUtils.isEmpty(connections.get(position).getUser_image())) {
            AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getUser_image());
        }
//        else if (connections.get(position).getIs_facebook() == 1 && !TextUtils.isEmpty(connections.get(position).getFacebook_image_url())){
//            AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getFacebook_image_url());
//        }
//        else {
//            AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getUser_image());
//        }
        holder.firstName.setText(connections.get(position).getFirst_name());
        if (connections.get(position).getDesignation() != null && !connections.get(position).getDesignation().isEmpty()) {
            holder.tv_designation_title.setText(connections.get(position).getDesignation());
        } else {
            holder.tv_designation_title.setText("--");
        }

        if (connections.get(position).getCompany_name() != null && !connections.get(position).getCompany_name().isEmpty()) {
            holder.tv_company.setText(connections.get(position).getCompany_name());
        } else {
            holder.tv_company.setText("--");
        }


        //Hide connect option if post is from logged in user
        User user = LoginUtils.getLoggedinUser();
//        if (connections.get(position).getId().equals(user.getId())) {
//            holder.tv_connect.setVisibility(View.GONE);
//        } else {
//            holder.tv_connect.setVisibility(View.VISIBLE);
//            holder.tv_connect.setText(AppUtils.getConnectionStatus(context, connections.get(position).getIs_connected(), connections.get(position).isIs_receiver()));
//            String connectionstatus = AppUtils.getConnectionStatus(context, connections.get(position).getIs_connected(), connections.get(position).isIs_receiver());
//        }

    }

    @Override
    public int getItemCount() {
        return connections.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.profile_image)
        CircleImageView profile_image;

        @BindView(R.id.firstName)
        TextView firstName;

        @BindView(R.id.tv_company)
        TextView tv_company;

        @BindView(R.id.tv_designation_title)
        TextView tv_designation_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }

        @Override
        public void onClick(View v) {

        }
    }

    //This method will filter the list
    //here we are passing the filtered data
    //and assigning it to the list with notifydatasetchanged method

}
