package com.hypernym.evaconnect.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class InvitedUsersAdapter extends RecyclerView.Adapter<InvitedUsersAdapter.ViewHolder> {
    private List<User> invitedUsers;
    private Context context;
    private boolean isRemoveable;
    SimpleDialog simpleDialog;

    public InvitedUsersAdapter(Context context, List<User> invitedUsers,boolean isRemoveable) {
        this.context = context;
        this.invitedUsers = invitedUsers;
        this.isRemoveable=isRemoveable;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invited_users, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        User user = invitedUsers.get(position);

        Log.d("adapter", "onBindViewHolder: ");

        if (!TextUtils.isEmpty(user.getUser_image())) {
            AppUtils.setGlideImage(context, holder.invitedUserImage, user.getUser_image());

        }
//        else if (user.getIs_facebook()!=null && user.getIs_facebook() == 1 && !TextUtils.isEmpty(user.getFacebook_image_url())){
//            AppUtils.setGlideImage(context, holder.invitedUserImage, user.getFacebook_image_url());
//        }
//        else {
//            AppUtils.setGlideImage(context, holder.invitedUserImage, user.getUser_image());
//        }

        if (user.getFirst_name()!=null) {

            holder.firstName.setText(user.getFirst_name() + " "+user.getLast_name());


        }

        if (user.getDesignation() != null) {

            holder.tv_designation_title.setText(user.getCompany_name());

        }  else {

            holder.tv_designation_title.setText(user.getBio_data());
        }
        if (isRemoveable) {
            holder.remove.setVisibility(View.VISIBLE);
            holder.remove.setOnClickListener(v -> {
                simpleDialog = new SimpleDialog(context, context.getString(R.string.confirmation), context.getString(R.string.msg_remove_invite), context.getString(R.string.button_no), context.getString(R.string.button_yes), new OnOneOffClickListener() {
                    @Override
                    public void onSingleClick(View v) {
                        switch (v.getId()) {
                            case R.id.button_positive:
                                invitedUsers.remove(position);
                                notifyDataSetChanged();
                                //  SelectedImageUri = null;
                                break;
                            case R.id.button_negative:
                                break;
                        }
                        simpleDialog.dismiss();
                    }
                });
                simpleDialog.show();
            });
        }
        else
        {
            holder.remove.setVisibility(View.GONE);
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
