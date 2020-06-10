package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class InvitedUsersAdapter extends RecyclerView.Adapter<InvitedUsersAdapter.ViewHolder> {
    private List<User> invitedUsers;
    private Context context;

    public InvitedUsersAdapter(Context context, List<User> invitedUsers) {
        this.context = context;
        this.invitedUsers = invitedUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invited_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = invitedUsers.get(position);

        if (user.getIs_linkedin()!=null && user.getIs_linkedin() == 1 && !TextUtils.isEmpty(user.getLinkedin_image_url())) {
            AppUtils.setGlideImage(context, holder.invitedUserImage, user.getLinkedin_image_url());

        }
        else if (user.getIs_facebook()!=null && user.getIs_facebook() == 1 && !TextUtils.isEmpty(user.getFacebook_image_url())){
            AppUtils.setGlideImage(context, holder.invitedUserImage, user.getFacebook_image_url());
        }
        else {
            AppUtils.setGlideImage(context, holder.invitedUserImage, user.getUser_image());
        }

        if (user.getFirst_name()!=null)
        {
            if (user.getFirst_name().length() > 7)
                holder.firstName.setText(user.getFirst_name().substring(0, 7));
            else
                holder.firstName.setText(user.getFirst_name());
        }

        if (user.getBio_data() != null)
        {
            if (user.getBio_data().length() > 7){
                holder.tv_designation_title.setText(user.getBio_data().substring(0, 7));
            }
            else{
                holder.tv_designation_title.setText(user.getBio_data());
            }
        }
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.invitedUserImage)
        CircleImageView invitedUserImage;

        @BindView(R.id.tv_designation_title)
        TextView tv_designation_title;

        @BindView(R.id.firstName)
        TextView firstName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
