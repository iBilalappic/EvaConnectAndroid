package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class AttachmentsAdapter extends RecyclerView.Adapter<AttachmentsAdapter.ViewHolder> {
    private Context context;
    private List<String> images=new ArrayList<>();
    int radius = 20; // corner radius, higher value = more rounded
    int margin = 0; // crop margin, set to 0 for corners with no crop
    private AttachmentsAdapter.ItemClickListener mClickListener;
    private SimpleDialog simpleDialog;
    private static final String TAG = "AttachmentsAdapter";


    public AttachmentsAdapter(Context context, List<String> images,AttachmentsAdapter.ItemClickListener itemClickListener)
    {
        this.context=context;
        this.images=images;
        this.mClickListener=itemClickListener;
    }

    @NonNull
    @Override
    public AttachmentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_attachment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentsAdapter.ViewHolder holder, int position) {

        Log.e("image", "onBindViewHolder: " + images.get(position));

        try{
            Glide.with(context)
                    .load(images.get(position)).placeholder(R.drawable.ic_document)
                    .transform(new RoundedCornersTransformation(radius, margin))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(holder.img_attach);
        }
        catch (Exception exception){
            exception.printStackTrace();
            Log.e(TAG, "onBindViewHolder: ", exception);
        }

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.img_attach)
        ImageView img_attach;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            img_attach.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
           if (mClickListener != null)
               mClickListener.onItemClick(v, getAdapterPosition());
        }
    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
