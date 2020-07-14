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
import com.hypernym.evaconnect.utils.DateUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageConnectionsAdapter extends RecyclerView.Adapter<MessageConnectionsAdapter.ViewHolder>{
private Context context;
private OnItemClickListener onItemClickListener;
private List<User> connections;

public MessageConnectionsAdapter(Context context, List<User> connections, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.connections = connections;
        this.onItemClickListener = onItemClickListener;
        }

@NonNull
@Override
public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message_connections, parent, false);
        return new ViewHolder(view);
        }

@Override
public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (connections.get(position).getIs_linkedin() == 1 && !TextUtils.isEmpty(connections.get(position).getLinkedin_image_url())) {
        AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getLinkedin_image_url());
        }
        else if (connections.get(position).getIs_facebook() == 1 && !TextUtils.isEmpty(connections.get(position).getFacebook_image_url())){
        AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getFacebook_image_url());
        }
        else {
        AppUtils.setGlideImage(context, holder.profile_image, connections.get(position).getUser_image());
        }

        holder.tv_name.setText(connections.get(position).getFirst_name());
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

        }


        if (connections.get(position).getBio_data() != null && !connections.get(position).getBio_data().isEmpty())
        {
        holder.tv_field.setText(connections.get(position).getBio_data());
        }
        else {
        holder.tv_field.setText("--");
        }

        holder.tv_invite.setOnClickListener(new View.OnClickListener() {
@Override
public void onClick(View v) {
      //  holder.tv_invite.setVisibility(View.GONE);
      //  holder.cancel_invite.setVisibility(View.VISIBLE);

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

    @BindView(R.id.tv_invite)
    TextView tv_invite;


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
