package com.hypernym.evaconnect.view.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.utils.AppUtils;
import com.smarteist.autoimageslider.SliderView;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class SliderImageAdapter extends SliderViewAdapter<SliderImageAdapter.SliderAdapterVH> {

    private Context context;
    private List<String> attachments=new ArrayList<>();
    private SliderView sliderView;
    int prevSliderHeight=0;

    public SliderImageAdapter(Context context, List<String> attachments, SliderView sliderView) {
        this.context = context;
        this.attachments=attachments;
        this.sliderView=sliderView;
    }

    @Override
    public SliderAdapterVH onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_slider_layout_item, null);
        return new SliderAdapterVH(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterVH viewHolder, int position) {

//        Glide
//                .with(context)
//                .load(attachments.get(position))
//                .apply(new RequestOptions().override(600, 200).downsample(DownsampleStrategy.AT_LEAST))
//                .into(viewHolder.imageViewBackground);

        viewHolder.imageViewBackground.setImageDrawable(null);
        sliderView.refreshDrawableState();
        Glide.with(context)
                .load(getImg(attachments.get(position)))//
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        int width = resource.getIntrinsicWidth();
                        int height = resource.getIntrinsicHeight();


                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        Activity activity=(Activity)context;
                        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

                        int tempWidth = (int) (width / Resources.getSystem().getDisplayMetrics()
                                .density);

                        int sliderHeight = (int) (height / Resources.getSystem().getDisplayMetrics()
                                .density) * displayMetrics.widthPixels / tempWidth;
                        if(attachments.size()>0 && sliderHeight > prevSliderHeight)
                        {
                            prevSliderHeight=sliderHeight;
                        }

                        sliderView.setLayoutParams(new ConstraintLayout.LayoutParams(displayMetrics.widthPixels , prevSliderHeight));

                        viewHolder.imageViewBackground.setImageDrawable(resource);
                        viewHolder.imageViewBackground.setAdjustViewBounds(true);


                    }
                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }

                });

    }
    public static String getImg(String mItem) {
        if (mItem == null) return null;
        String url = mItem;
        return url;
    }
    @Override
    public int getCount() {
        //slider view count could be dynamic size
        return attachments.size();
    }

    class SliderAdapterVH extends SliderViewAdapter.ViewHolder {

        View itemView;
        ImageView imageViewBackground;
        TextView tv_service;


        public SliderAdapterVH(View itemView) {
            super(itemView);
            imageViewBackground = itemView.findViewById(R.id.iv_auto_image_slider);
            this.itemView = itemView;
        }
    }
}