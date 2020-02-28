package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
            if (chatMessageList.get(position).getType_interview() != null &&
                    chatMessageList.get(position).getType_interview().equals("Interview")) {


                holder.layout3.setVisibility(View.VISIBLE);
                holder.layout_date.setVisibility(View.VISIBLE);
                holder.layout_time.setVisibility(View.VISIBLE);
               // holder.tv_interview.setText(chatMessageList.get(position).getMessage());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    holder.tv_interview.setText(Html.fromHtml(chatMessageList.get(position).getMessage(), Html.FROM_HTML_MODE_COMPACT));
                } else {
                    holder.tv_interview.setText(Html.fromHtml(chatMessageList.get(position).getMessage()));
                }
                holder.tv_day.setText(chatMessageList.get(position).getDay());
                holder.tv_month.setText(DateUtils.convertnumtocharmonths(Integer.parseInt(chatMessageList.get(position).getMonth())));
                holder.tv_year.setText(chatMessageList.get(position).getYear());
                if (Integer.parseInt(chatMessageList.get(position).getHour()) < 10) {
                    holder.tv_hour.setText("0" + chatMessageList.get(position).getHour());
                } else {
                    holder.tv_hour.setText(chatMessageList.get(position).getHour());
                }
                if (Integer.parseInt(chatMessageList.get(position).getMinutes()) < 10) {
                    holder.tv_mintues.setText("0" + chatMessageList.get(position).getMinutes());
                } else {
                    holder.tv_mintues.setText(chatMessageList.get(position).getMinutes());
                }


                AppUtils.setGlideImage(context, (holder).img_interviewSender, LoginUtils.getUser().getUser_image());
            } else {
                holder.mlayout1.setVisibility(View.GONE);
                holder.layout3.setVisibility(View.GONE);
                holder.mlayout2.setVisibility(View.VISIBLE);
                holder.tv_sendertime.setVisibility(View.GONE);
                holder.tv_receivertime.setVisibility(View.VISIBLE);
                holder.layout_date.setVisibility(View.GONE);
                holder.layout_time.setVisibility(View.GONE);

                holder.mtextview21.setText(chatMessageList.get(position).getMessage());
                if (UserDetails.receiverName.contains(networkConnection.getReceiver().getFirstName())) {
                    AppUtils.setGlideImage(context, (holder).imageView7, networkConnection.getSender().getUserImage());
                    holder.tv_receivertime.setText(DateUtils.getTimeAgo(chatMessageList.get(position).getChattime()));
                    holder.tv_sendertime.setText(DateUtils.getTimeAgo(chatMessageList.get(position).getChattime()));
                    if (chatMessageList.get(position).getImage().size() > 0) {
                        //   AppUtils.setGlideUrlThumbnail(context,holder.receiveImage,chatMessageList.get(position).getImage().get(0));
                        ImageAdapter ServicesInnerAdapter = new ImageAdapter(context, chatMessageList.get(position).getImage());
                        holder.recycler_viewReceiver.setHasFixedSize(true);
                        holder.recycler_viewReceiver.setDrawingCacheEnabled(true);
                        holder.recycler_viewReceiver.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                        LinearLayoutManager linearLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                        holder.recycler_viewReceiver.setLayoutManager(linearLayout);
                        holder.recycler_viewReceiver.setAdapter(ServicesInnerAdapter);
//                    holder.receiveImage.setVisibility(View.VISIBLE);
//                    holder.senderImage.setVisibility(View.VISIBLE);
                        holder.recycler_viewReceiver.setVisibility(View.VISIBLE);
                        holder.recycler_viewSender.setVisibility(View.VISIBLE);
                    } else {
                        holder.senderImage.setVisibility(View.GONE);
                        holder.receiveImage.setVisibility(View.GONE);
                        holder.recycler_viewReceiver.setVisibility(View.GONE);
                        holder.recycler_viewSender.setVisibility(View.GONE);
                    }

                } else {
                    AppUtils.setGlideImage(context, (holder).imageView7, networkConnection.getReceiver().getUserImage());
                    holder.tv_receivertime.setText(DateUtils.getTimeAgo(chatMessageList.get(position).getChattime()));
                    holder.tv_sendertime.setText(DateUtils.getTimeAgo(chatMessageList.get(position).getChattime()));
                    if (chatMessageList.get(position).getImage().size() > 0) {
                        //    AppUtils.setGlideUrlThumbnail(context,holder.receiveImage,chatMessageList.get(position).getImage().get(0));
//                    holder.senderImage.setVisibility(View.VISIBLE);
//                    holder.receiveImage.setVisibility(View.VISIBLE);
                        ImageAdapter ServicesInnerAdapter = new ImageAdapter(context, chatMessageList.get(position).getImage());
                        holder.recycler_viewReceiver.setHasFixedSize(true);
                        holder.recycler_viewReceiver.setDrawingCacheEnabled(true);
                        holder.recycler_viewReceiver.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                        LinearLayoutManager linearLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                        holder.recycler_viewReceiver.setLayoutManager(linearLayout);
                        holder.recycler_viewReceiver.setAdapter(ServicesInnerAdapter);
                        holder.recycler_viewReceiver.setVisibility(View.VISIBLE);
                        holder.recycler_viewSender.setVisibility(View.VISIBLE);
                    } else {
                        holder.senderImage.setVisibility(View.GONE);
                        holder.receiveImage.setVisibility(View.GONE);
                        holder.recycler_viewReceiver.setVisibility(View.GONE);
                        holder.recycler_viewSender.setVisibility(View.GONE);
                    }

                }

            }


        } else {


            holder.mlayout1.setVisibility(View.VISIBLE);
            holder.mlayout2.setVisibility(View.GONE);
            holder.layout3.setVisibility(View.GONE);
            holder.tv_sendertime.setVisibility(View.VISIBLE);
            holder.tv_receivertime.setVisibility(View.GONE);
            holder.layout_date.setVisibility(View.GONE);
            holder.layout_time.setVisibility(View.GONE);

           // holder.mtextview20.setText(chatMessageList.get(position).getMessage());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.mtextview20.setText(Html.fromHtml(chatMessageList.get(position).getMessage(), Html.FROM_HTML_MODE_COMPACT));
            } else {
                holder.mtextview20.setText(Html.fromHtml(chatMessageList.get(position).getMessage()));
            }
            if (networkConnection.getSenderId().equals(LoginUtils.getUser().getId())) {

                AppUtils.setGlideImage(context, (holder).imageView6, networkConnection.getReceiver().getUserImage());
                holder.tv_receivertime.setText(DateUtils.getTimeAgo(chatMessageList.get(position).getChattime()));
                holder.tv_sendertime.setText(DateUtils.getTimeAgo(chatMessageList.get(position).getChattime()));
                if (chatMessageList.get(position).getImage().size() > 0) {
                    //   AppUtils.setGlideUrlThumbnail(context,holder.senderImage,chatMessageList.get(position).getImage().get(0));
                    ImageAdapter ServicesInnerAdapter = new ImageAdapter(context, chatMessageList.get(position).getImage());
                    holder.recycler_viewSender.setHasFixedSize(true);
                    holder.recycler_viewSender.setDrawingCacheEnabled(true);
                    holder.recycler_viewSender.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    LinearLayoutManager linearLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    holder.recycler_viewSender.setLayoutManager(linearLayout);
                    holder.recycler_viewSender.setAdapter(ServicesInnerAdapter);

//                    holder.senderImage.setVisibility(View.VISIBLE);
//                    holder.receiveImage.setVisibility(View.VISIBLE);
                    holder.recycler_viewReceiver.setVisibility(View.VISIBLE);
                    holder.recycler_viewSender.setVisibility(View.VISIBLE);

                } else {
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
                if (chatMessageList.get(position).getImage().size() > 0) {
                    //  AppUtils.setGlideUrlThumbnail(context,holder.senderImage,chatMessageList.get(position).getImage().get(0));
                    ImageAdapter ServicesInnerAdapter = new ImageAdapter(context, chatMessageList.get(position).getImage());
                    holder.recycler_viewSender.setHasFixedSize(true);
                    holder.recycler_viewSender.setDrawingCacheEnabled(true);
                    holder.recycler_viewSender.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                    LinearLayoutManager linearLayout = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                    holder.recycler_viewSender.setLayoutManager(linearLayout);
                    holder.recycler_viewSender.setAdapter(ServicesInnerAdapter);

//                    holder.receiveImage.setVisibility(View.VISIBLE);
//                    holder.senderImage.setVisibility(View.VISIBLE);
                    holder.recycler_viewReceiver.setVisibility(View.VISIBLE);
                    holder.recycler_viewSender.setVisibility(View.VISIBLE);
                } else {
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
        TextView mtextview20, mtextview21, tv_sendertime, tv_receivertime, tv_interview,
                tv_day, tv_month, tv_year, tv_hour, tv_mintues;
        CardView mlayout1, mlayout2, layout3;
        CircleImageView imageView6, imageView7, img_interviewSender;
        ImageView senderImage, receiveImage;
        RecyclerView recycler_viewSender, recycler_viewReceiver;
        LinearLayout layout_date, layout_time;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mtextview20 = itemView.findViewById(R.id.tv_name);
            mtextview21 = itemView.findViewById(R.id.tv_lastmsg);
            mlayout1 = itemView.findViewById(R.id.layout1);
            mlayout2 = itemView.findViewById(R.id.layout2);
            imageView6 = itemView.findViewById(R.id.imageView6);
            imageView7 = itemView.findViewById(R.id.imageView7);
            tv_sendertime = itemView.findViewById(R.id.tv_sendtime);
            tv_receivertime = itemView.findViewById(R.id.tv_receivetime);
            senderImage = itemView.findViewById(R.id.senderimage);
            receiveImage = itemView.findViewById(R.id.receiveimage);
            img_interviewSender = itemView.findViewById(R.id.img_interviewSender);
            tv_interview = itemView.findViewById(R.id.tv_interview);
            layout3 = itemView.findViewById(R.id.layout3);
            layout_date = itemView.findViewById(R.id.layout_date);
            layout_time = itemView.findViewById(R.id.layout_time);
            tv_day = itemView.findViewById(R.id.tv_day);
            tv_month = itemView.findViewById(R.id.tv_month);
            tv_year = itemView.findViewById(R.id.tv_year);
            tv_hour = itemView.findViewById(R.id.tv_hours);
            tv_mintues = itemView.findViewById(R.id.tv_minutes);
            recycler_viewSender = itemView.findViewById(R.id.recycler_viewSender);
            recycler_viewReceiver = itemView.findViewById(R.id.recycler_viewReceiver);
        }
    }
}
