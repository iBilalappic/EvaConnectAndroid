package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.ChatMessage;
import com.hypernym.evaconnect.models.NetworkConnection;
import com.hypernym.evaconnect.models.UserDetails;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;
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
        this.networkConnection = networkConnection;
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
            holder.mlayout1.setVisibility(View.GONE);
            holder.mlayout2.setVisibility(View.VISIBLE);
            holder.tv_sendertime.setVisibility(View.GONE);
            holder.tv_receivertime.setVisibility(View.VISIBLE);
            holder.mtextview21.setText(chatMessageList.get(position).getMessage());
            if (UserDetails.receiverName.contains(networkConnection.getReceiver().getFirstName())) {
                AppUtils.setGlideImage(context, (holder).imageView7, networkConnection.getSender().getUserImage());
                holder.tv_receivertime.setText(DateUtils.getTimeAgo(chatMessageList.get(position).getChattime()));
                holder.tv_sendertime.setText(DateUtils.getTimeAgo(chatMessageList.get(position).getChattime()));
                if(chatMessageList.get(position).getImage().size()>0)
                {
                    //   AppUtils.setGlideUrlThumbnail(context,holder.receiveImage,chatMessageList.get(position).getImage().get(0));
                    ImageAdapter ServicesInnerAdapter = new ImageAdapter(context,chatMessageList.get(position).getImage());
                    holder.recycler_viewReceiver.setHasFixedSize(true);
                    holder.recycler_viewReceiver.setDrawingCacheEnabled(true);
                    holder.recycler_viewReceiver.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    LinearLayoutManager linearLayout=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
                    holder.recycler_viewReceiver.setLayoutManager(linearLayout);
                    holder.recycler_viewReceiver.setAdapter(ServicesInnerAdapter);
//                    holder.receiveImage.setVisibility(View.VISIBLE);
//                    holder.senderImage.setVisibility(View.VISIBLE);
                    holder.recycler_viewReceiver.setVisibility(View.VISIBLE);
                    holder.recycler_viewSender.setVisibility(View.VISIBLE);
                }
                else{
                    holder.senderImage.setVisibility(View.GONE);
                    holder.receiveImage.setVisibility(View.GONE);
                    holder.recycler_viewReceiver.setVisibility(View.GONE);
                    holder.recycler_viewSender.setVisibility(View.GONE);
                }

            } else {
                AppUtils.setGlideImage(context, (holder).imageView7, networkConnection.getReceiver().getUserImage());
                holder.tv_receivertime.setText(DateUtils.getTimeAgo(chatMessageList.get(position).getChattime()));
                holder.tv_sendertime.setText(DateUtils.getTimeAgo(chatMessageList.get(position).getChattime()));
                if(chatMessageList.get(position).getImage().size()>0)
                {
                    //    AppUtils.setGlideUrlThumbnail(context,holder.receiveImage,chatMessageList.get(position).getImage().get(0));
//                    holder.senderImage.setVisibility(View.VISIBLE);
//                    holder.receiveImage.setVisibility(View.VISIBLE);
                    ImageAdapter ServicesInnerAdapter = new ImageAdapter(context,chatMessageList.get(position).getImage());
                    holder.recycler_viewReceiver.setHasFixedSize(true);
                    holder.recycler_viewReceiver.setDrawingCacheEnabled(true);
                    holder.recycler_viewReceiver.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    LinearLayoutManager linearLayout=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
                    holder.recycler_viewReceiver.setLayoutManager(linearLayout);
                    holder.recycler_viewReceiver.setAdapter(ServicesInnerAdapter);
                    holder.recycler_viewReceiver.setVisibility(View.VISIBLE);
                    holder.recycler_viewSender.setVisibility(View.VISIBLE);
                }
                else{
                    holder.senderImage.setVisibility(View.GONE);
                    holder.receiveImage.setVisibility(View.GONE);
                    holder.recycler_viewReceiver.setVisibility(View.GONE);
                    holder.recycler_viewSender.setVisibility(View.GONE);
                }

            }


        } else {
            holder.mlayout1.setVisibility(View.VISIBLE);
            holder.mlayout2.setVisibility(View.GONE);
            holder.tv_sendertime.setVisibility(View.VISIBLE);
            holder.tv_receivertime.setVisibility(View.GONE);

            holder.mtextview20.setText(chatMessageList.get(position).getMessage());
            if (networkConnection.getSenderId().equals(LoginUtils.getUser().getId())) {
                AppUtils.setGlideImage(context, (holder).imageView6, networkConnection.getReceiver().getUserImage());
                holder.tv_receivertime.setText(DateUtils.getTimeAgo(chatMessageList.get(position).getChattime()));
                holder.tv_sendertime.setText(DateUtils.getTimeAgo(chatMessageList.get(position).getChattime()));
                if(chatMessageList.get(position).getImage().size()>0)
                {
                    //   AppUtils.setGlideUrlThumbnail(context,holder.senderImage,chatMessageList.get(position).getImage().get(0));
                    ImageAdapter ServicesInnerAdapter = new ImageAdapter(context,chatMessageList.get(position).getImage());
                    holder.recycler_viewSender.setHasFixedSize(true);
                    holder.recycler_viewSender.setDrawingCacheEnabled(true);
                    holder.recycler_viewSender.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    LinearLayoutManager linearLayout=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
                    holder.recycler_viewSender.setLayoutManager(linearLayout);
                    holder.recycler_viewSender.setAdapter(ServicesInnerAdapter);

//                    holder.senderImage.setVisibility(View.VISIBLE);
//                    holder.receiveImage.setVisibility(View.VISIBLE);
                    holder.recycler_viewReceiver.setVisibility(View.VISIBLE);
                    holder.recycler_viewSender.setVisibility(View.VISIBLE);

                }
                else{
                    holder.recycler_viewReceiver.setVisibility(View.GONE);
                    holder.recycler_viewSender.setVisibility(View.GONE);
                    holder.senderImage.setVisibility(View.GONE);
                    holder.receiveImage.setVisibility(View.GONE);
                }

                //  Toast.makeText(getContext(), "receivername" + networkConnection.getReceiver().getFirstName(), Toast.LENGTH_SHORT).show();
            } else {
                AppUtils.setGlideImage(context, (holder).imageView6, networkConnection.getSender().getUserImage());
                holder.tv_receivertime.setText(DateUtils.getTimeAgo(chatMessageList.get(position).getChattime()));
                holder.tv_sendertime.setText(DateUtils.getTimeAgo(chatMessageList.get(position).getChattime()));
                if(chatMessageList.get(position).getImage().size()>0)
                {
                    //  AppUtils.setGlideUrlThumbnail(context,holder.senderImage,chatMessageList.get(position).getImage().get(0));
                    ImageAdapter ServicesInnerAdapter = new ImageAdapter(context,chatMessageList.get(position).getImage());
                    holder.recycler_viewSender.setHasFixedSize(true);
                    holder.recycler_viewSender.setDrawingCacheEnabled(true);
                    holder.recycler_viewSender.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    LinearLayoutManager linearLayout=new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,false);
                    holder.recycler_viewSender.setLayoutManager(linearLayout);
                    holder.recycler_viewSender.setAdapter(ServicesInnerAdapter);

//                    holder.receiveImage.setVisibility(View.VISIBLE);
//                    holder.senderImage.setVisibility(View.VISIBLE);
                    holder.recycler_viewReceiver.setVisibility(View.VISIBLE);
                    holder.recycler_viewSender.setVisibility(View.VISIBLE);
                }
                else{
                    holder.senderImage.setVisibility(View.GONE);
                    holder.receiveImage.setVisibility(View.GONE);
                    holder.recycler_viewReceiver.setVisibility(View.GONE);
                    holder.recycler_viewSender.setVisibility(View.GONE);
                }


            }

        }


    }

    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView mtextview20, mtextview21,tv_sendertime,tv_receivertime,tv_interview;
        CardView mlayout1, mlayout2,layout3;
        CircleImageView imageView6, imageView7,imageView_interview;
        ImageView senderImage,receiveImage;
        RecyclerView recycler_viewSender,recycler_viewReceiver;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtextview20 = itemView.findViewById(R.id.tv_name);
            mtextview21 = itemView.findViewById(R.id.tv_lastmsg);
            mlayout1 = itemView.findViewById(R.id.layout1);
            mlayout2 = itemView.findViewById(R.id.layout2);
            imageView6 = itemView.findViewById(R.id.imageView6);
            imageView7 = itemView.findViewById(R.id.imageView7);
            tv_sendertime=itemView.findViewById(R.id.tv_sendtime);
            tv_receivertime=itemView.findViewById(R.id.tv_receivetime);
            senderImage=itemView.findViewById(R.id.senderimage);
            receiveImage=itemView.findViewById(R.id.receiveimage);
            recycler_viewSender=itemView.findViewById(R.id.recycler_viewSender);
            recycler_viewReceiver=itemView.findViewById(R.id.recycler_viewReceiver);

        }
    }
}
