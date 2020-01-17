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
import com.hypernym.evaconnect.models.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context context;
    private List<ChatMessage> chatMessageList = new ArrayList<>();

    public ChatAdapter(Context context, List<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessageList = chatMessages;
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
        } else {
            holder.mlayout1.setVisibility(View.GONE);
            holder.mlayout2.setVisibility(View.VISIBLE);
            holder.mtextview21.setText(UserDetails.chatWith + ":-\n" + chatMessageList.get(position).getMessage());
        }


    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mtextview20, mtextview21;
        LinearLayout mlayout1, mlayout2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtextview20 = itemView.findViewById(R.id.textView20);
            mtextview21 = itemView.findViewById(R.id.textView21);
            mlayout1 = itemView.findViewById(R.id.layout1);
            mlayout2 = itemView.findViewById(R.id.layout2);
        }
    }
}
