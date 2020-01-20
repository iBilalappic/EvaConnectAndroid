package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.ChatMessage;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.UserDetails;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.LoginUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private List<ChatMessage> chatMessageList = new ArrayList<>();
    private NetworkConnection networkConnection;

    public ChatAdapter(Context context, List<ChatMessage> chatMessages, NetworkConnection networkConnection) {
        this.context = context;
        this.chatMessageList = chatMessages;
        this.networkConnection=networkConnection;
    }

    @NonNull
    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_messagechat, parent, false);
        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.ViewHolder holder, int position) {
        if (chatMessageList.get(position).getType() == 1) {
            holder.mlayout1.setVisibility(View.VISIBLE);
            holder.mlayout2.setVisibility(View.GONE);
            holder.mtextview20.setText("You:-\n" + chatMessageList.get(position).getMessage());
            if (networkConnection.getSenderId().equals(LoginUtils.getUser().getId())) {
                AppUtils.setGlideImage(context, (holder).imageView6, networkConnection.getSender().getUserImage());
                //  Toast.makeText(getContext(), "receivername" + networkConnection.getReceiver().getFirstName(), Toast.LENGTH_SHORT).show();
            } else {
                AppUtils.setGlideImage(context, (holder).imageView6, networkConnection.getReceiver().getUserImage());
                //  Toast.makeText(getContext(), "sendername" + networkConnection.getSender().getFirstName(), Toast.LENGTH_SHORT).show();
            }

        } else {
            holder.mlayout1.setVisibility(View.GONE);
            holder.mlayout2.setVisibility(View.VISIBLE);
            holder.mtextview21.setText(UserDetails.chatWith + ":-\n" + chatMessageList.get(position).getMessage());
            if (UserDetails.chatWith.contains(networkConnection.getReceiver().getFirstName())) {
                AppUtils.setGlideImage(context, (holder).imageView7, networkConnection.getReceiver().getUserImage());
                //  Toast.makeText(getContext(), "receivername" + networkConnection.getReceiver().getFirstName(), Toast.LENGTH_SHORT).show();
            } else {
                AppUtils.setGlideImage(context, (holder).imageView7, networkConnection.getSender().getUserImage());
                //  Toast.makeText(getContext(), "sendername" + networkConnection.getSender().getFirstName(), Toast.LENGTH_SHORT).show();
            }
        }


    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mtextview20, mtextview21;
        LinearLayout mlayout1, mlayout2;
        CircleImageView imageView6,imageView7;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtextview20 = itemView.findViewById(R.id.textView20);
            mtextview21 = itemView.findViewById(R.id.textView21);
            mlayout1 = itemView.findViewById(R.id.layout1);
            mlayout2 = itemView.findViewById(R.id.layout2);
            imageView6 = itemView.findViewById(R.id.imageView6);
            imageView7 = itemView.findViewById(R.id.imageView7);
        }
    }
}
