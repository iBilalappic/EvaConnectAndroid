package com.hypernym.evaconnect.view.adapters;

import android.annotation.SuppressLint;
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
import com.hypernym.evaconnect.models.ConnectionModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShareConnectionAdapter extends RecyclerView.Adapter<ShareConnectionAdapter.ViewHolder> {
    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<ConnectionModel> connections;

    public ShareConnectionAdapter(Context context, List<ConnectionModel> connections, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.connections = connections;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ShareConnectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_share_connection, parent, false);
        return new ShareConnectionAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareConnectionAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {


        User user = LoginUtils.getLoggedinUser();
        if (connections.get(position).sender != null && connections.get(position).receiver != null) {
            if (user.getId().equals(connections.get(position).senderId)) {
                if (!TextUtils.isEmpty(connections.get(position).receiver.getUserImage())) {
                    AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).receiver.getUserImage());
                }
                if (connections.get(position).receiver.getFirstName() != null) {
                    holder.tv_name.setText(connections.get(position).receiver.getFirstName());
                }
                if (connections.get(position).receiver.getBioData() != null && !connections.get(position).receiver.getBioData().isEmpty()) {
                    holder.tv_field.setText(connections.get(position).receiver.getBioData());
                } else {
                    holder.tv_field.setText("--");
                }


            } else if (user.getId().equals(connections.get(position).receiverId)) {
                if (!TextUtils.isEmpty(connections.get(position).sender.getUserImage())) {
                    AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).sender.getUserImage());
                }
                if (connections.get(position).sender.getFirstName() != null) {
                    holder.tv_name.setText(connections.get(position).sender.getFirstName());
                }
                if (connections.get(position).sender.getBioData() != null && !connections.get(position).sender.getBioData().isEmpty()) {
                    holder.tv_field.setText(connections.get(position).sender.getBioData());
                } else {
                    holder.tv_field.setText("--");
                }


            }
        } else {
            if (!TextUtils.isEmpty(connections.get(position).userImage)) {
                AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).userImage);
            }
            if (connections.get(position).firstName != null) {
                holder.tv_name.setText(connections.get(position).firstName);
            }
            if (connections.get(position).bioData != null && !connections.get(position).bioData.isEmpty()) {
                holder.tv_field.setText(connections.get(position).bioData);
            } else {
                holder.tv_field.setText("--");
            }
        }


        holder.tv_invite.setOnClickListener(v -> {
            holder.tv_invite.setVisibility(View.GONE);
            holder.cancel_invite.setVisibility(View.VISIBLE);

            if (onItemClickListener != null)
                // connections.get(position).setIs_shared(true);
                onItemClickListener.onItemClick(v, connections.indexOf(connections.get(position)), connections.get(position));
        });

        holder.cancel_invite.setOnClickListener(v -> {
            holder.tv_invite.setVisibility(View.VISIBLE);
            holder.cancel_invite.setVisibility(View.GONE);

            if (onItemClickListener != null)
                onItemClickListener.onItemClick(v, connections.indexOf(connections.get(position)), connections.get(position));
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return connections.size();
    }

    public void hSetList(List<ConnectionModel> hSearchList) {
        connections = hSearchList;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, ConnectionModel data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profile_image)
        CircleImageView profile_image;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_field)
        TextView tv_field;


        @BindView(R.id.tv_share)
        TextView tv_invite;

        @BindView(R.id.imageView9)
        ImageView cancel_invite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    ConnectionModel getItem(int position) {
        if (connections.size() > 0)
            return connections.get(position);
        else
            return null;
    }
}
