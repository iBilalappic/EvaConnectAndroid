package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.JobAd;
import com.hypernym.evaconnect.models.NewSources;
import com.hypernym.evaconnect.utils.AppUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    private Context context;
    public List<NewSources> newSources = new ArrayList<>();
    public List<NewSources> selected_NewsList=new ArrayList<>();
    private OnItemClickListener onItemClickListener;

    public NewsAdapter(Context context, List<NewSources> newSources, OnItemClickListener itemClickListener, List<NewSources> multiSelect) {
        this.context = context;
        this.newSources = newSources;
        this.selected_NewsList=multiSelect;
        this.onItemClickListener = itemClickListener;

    }

    @NonNull
    @Override
    public NewsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_news_sources, parent, false);
        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.ViewHolder holder, int position) {
        if (newSources.get(position).getImage() != null) {
            Glide.with(context).load(newSources.get(position).getImage()).into(holder.img_icon);
        }

        if(selected_NewsList.contains(newSources.get(position)))
            holder.layout_news.setBackgroundResource(R.drawable.border_skyblue);
        else
            holder.layout_news.setBackgroundResource(R.drawable.border_gery);
    }

    @Override
    public int getItemCount() {
        return newSources.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView img_icon;
        LinearLayout layout_news;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img_icon = itemView.findViewById(R.id.img_newsicon);
            layout_news = itemView.findViewById(R.id.layout_news);
            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            switch (v.getId()) {


            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
    // parent activity will implement this method to respond to click events

}
