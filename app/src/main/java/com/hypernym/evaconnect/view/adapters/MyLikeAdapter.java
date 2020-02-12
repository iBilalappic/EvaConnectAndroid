package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.MyLikesModel;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

public class MyLikeAdapter extends RecyclerView.Adapter<MyLikeAdapter.ViewHolder> {
    private Context context;
    private List<MyLikesModel> myLikesModelList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public MyLikeAdapter(Context context, List<MyLikesModel> myLikesModelList, OnItemClickListener itemClickListener) {
        this.context = context;
        this.myLikesModelList = myLikesModelList;
        this.onItemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public MyLikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_mylikes, parent, false);
        return new MyLikeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyLikeAdapter.ViewHolder holder, int position) {
       // holder.mtextviewName.setText(myLikesModelList.get(position));

        if (myLikesModelList.get(position).getIsLike()!=0) {
            holder.tv_status.setText("Liked your post " + DateUtils.getTimeAgo(myLikesModelList.get(position).getCreatedDatetime()));
            holder.img_type.setImageDrawable(context.getDrawable(R.drawable.like_selected));
        } else {
            holder.tv_status.setText("Commented your post " + DateUtils.getTimeAgo(myLikesModelList.get(position).getCreatedDatetime()));
            holder.img_type.setImageDrawable(context.getDrawable(R.drawable.comment));
        }
        AppUtils.makeTextViewResizable(holder.tv_content, 3, myLikesModelList.get(position).getDetails());
    }

    @Override
    public int getItemCount() {
        return myLikesModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_Name,tv_status,tv_content;
        ImageView img_type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_Name=itemView.findViewById(R.id.tv_name);
            tv_status=itemView.findViewById(R.id.tv_status);
            tv_content=itemView.findViewById(R.id.tv_content);
            img_type=itemView.findViewById(R.id.img_type);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    // parent activity will implement this method to respond to click events

}
