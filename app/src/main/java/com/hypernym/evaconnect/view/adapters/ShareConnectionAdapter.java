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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ShareConnectionAdapter extends RecyclerView.Adapter<ShareConnectionAdapter.ViewHolder> {
    private Context context;
    private OnItemClickListener onItemClickListener;
    private List<User> connections;

    public ShareConnectionAdapter(Context context, List<User> connections, OnItemClickListener onItemClickListener) {
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
    public void onBindViewHolder(@NonNull ShareConnectionAdapter.ViewHolder holder, int position) {

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
            holder.tv_field.setText(connections.get(position).getBio_data());
        } else {
            holder.tv_field.setText("--");
        }
//
//        if (connections.get(position).isIs_shared()) {
//            holder.tv_invite.setVisibility(View.GONE);
//            holder.cancel_invite.setVisibility(View.VISIBLE);
//        } else {
//            holder.tv_invite.setVisibility(View.VISIBLE);
//            holder.cancel_invite.setVisibility(View.GONE);
//        }
        holder.tv_invite.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                holder.tv_invite.setVisibility(View.GONE);
                holder.cancel_invite.setVisibility(View.VISIBLE);

                if (onItemClickListener != null)
                    // connections.get(position).setIs_shared(true);
                    onItemClickListener.onItemClick(v, connections.indexOf(connections.get(position)), connections.get(position));
            }
        });

        holder.cancel_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.tv_invite.setVisibility(View.VISIBLE);
                holder.cancel_invite.setVisibility(View.GONE);

                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(v, connections.indexOf(connections.get(position)), connections.get(position));
            }
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

    public interface OnItemClickListener {
        void onItemClick(View view, int position, User data);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.profile_image)
        CircleImageView profile_image;

        @BindView(R.id.tv_name)
        TextView tv_name;

        @BindView(R.id.tv_field)
        TextView tv_field;

        @BindView(R.id.tv_connection_status)
        TextView tv_connection_status;

        @BindView(R.id.tv_share)
        TextView tv_invite;

        @BindView(R.id.imageView9)
        ImageView cancel_invite;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    User getItem(int position) {
        if (connections.size() > 0)
            return connections.get(position);
        else
            return null;
    }
}