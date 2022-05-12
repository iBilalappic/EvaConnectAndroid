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
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.GetEventInterestedUsers;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class EventInterestedUserAdapter extends RecyclerView.Adapter<EventInterestedUserAdapter.ViewHolder> {
    private List<GetEventInterestedUsers> invitedUsers;
    private Context context;
    private OnItemClickListener onItemClickListener;
    SimpleDialog simpleDialog;

    public EventInterestedUserAdapter(Context context, List<GetEventInterestedUsers> invitedUsers, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.invitedUsers = invitedUsers;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public EventInterestedUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invited_users, parent, false);
        return new EventInterestedUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventInterestedUserAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User user = invitedUsers.get(position).getUser();

        if (!TextUtils.isEmpty(user.getUser_image())) {
            AppUtils.setGlideImage(context, holder.invitedUserImage, user.getUser_image());

        }
//        else if (user.getIs_facebook()!=null && user.getIs_facebook() == 1 && !TextUtils.isEmpty(user.getFacebook_image_url())){
//            AppUtils.setGlideImage(context, holder.invitedUserImage, user.getFacebook_image_url());
//        }
//        else {
//            AppUtils.setGlideImage(context, holder.invitedUserImage, user.getUser_image());
//        }

        if (user.getFirst_name() != null) {
            if (user.getFirst_name().length() > 7)
                holder.firstName.setText(user.getFirst_name().substring(0, 7));
            else
                holder.firstName.setText(user.getFirst_name());
        }
        holder.itemView.setOnClickListener(new OnOneOffClickListener() {
            @Override
            public void onSingleClick(View v) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(v, position, user);
            }
        });
        if (user.getDesignation() != null) {
            if (user.getDesignation().length() > 7) {
                holder.tv_designation_title.setText(user.getDesignation().substring(0, 7));
            } else {
                holder.tv_designation_title.setText(user.getDesignation());
            }
        } else if (user.getDesignation() != null) {
            holder.tv_designation_title.setText(user.getDesignation());
        }
        holder.remove.setVisibility(View.GONE);

    }

    public void hSetList(List<GetEventInterestedUsers> hSearchList) {
        invitedUsers = hSearchList;

        notifyDataSetChanged();

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, User data);
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
        return invitedUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.invitedUserImage)
        CircleImageView invitedUserImage;

        @BindView(R.id.tv_designation_title)
        TextView tv_designation_title;

        @BindView(R.id.remove)
        ImageView remove;

        @BindView(R.id.firstName)
        TextView firstName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

