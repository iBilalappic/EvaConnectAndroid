package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.utils.AppUtils;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private Context context;
    private List<String> images = new ArrayList<>();


    public ImageAdapter(Context context, List<String> images) {
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
        AppUtils.setGlideUrlThumbnail(context, holder.imageSender, images.get(position));

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
