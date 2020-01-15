package com.hypernym.evaconnect.view.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.listeners.OnOneOffClickListener;
import com.hypernym.evaconnect.view.dialogs.ApplicationDialogs;
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


    public AttachmentsAdapter(Context context, List<String> images)
    {
        this.context=context;
        this.images=images;
    }

    @NonNull
    @Override
    public AttachmentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_attachment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AttachmentsAdapter.ViewHolder holder, int position) {

        Glide.with(context)
                .load(images.get(position))
                .transform(new RoundedCornersTransformation(radius, margin))
                .into(holder.img_attach);

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
            simpleDialog=new SimpleDialog(context, context.getString(R.string.confirmation), context.getString(R.string.msg_remove_attachment), context.getString(R.string.button_no), context.getString(R.string.button_yes), new OnOneOffClickListener() {
                @Override
                public void onSingleClick(View v) {
                    switch (v.getId())
                    {
                        case R.id.button_positive:
                            images.remove(getAdapterPosition());
                            notifyDataSetChanged();
                            break;
                        case R.id.button_negative:
                            break;
                    }

                    simpleDialog.dismiss();
                }
            });
                simpleDialog.show();
           // if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());
        }
    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
