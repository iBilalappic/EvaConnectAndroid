package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.models.AttachmentType;
import com.hypernym.evaconnect.utils.AppUtils;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context context;
    private List<AttachmentType> images = new ArrayList<>();



    public ImageAdapter(Context context, List<AttachmentType> images) {
        this.context = context;
        this.images = images;

    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_images, parent, false);
        return new ImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        if(images.get(position).getType().equalsIgnoreCase("image"))
        {
            AppUtils.setGlideUrlThumbnail(context, holder.imageSender, images.get(position).getUrl());
        }
        else
        {
           holder.imageSender.setImageResource(R.drawable.ic_document);
        }
        holder.imageSender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent browserIntent =
                        new Intent(Intent.ACTION_VIEW, Uri.parse(images.get(position).getUrl()));
                context.startActivity(browserIntent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageSender;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageSender = itemView.findViewById(R.id.senderimage);
        }

    }
    // parent activity will implement this method to respond to click events

}
