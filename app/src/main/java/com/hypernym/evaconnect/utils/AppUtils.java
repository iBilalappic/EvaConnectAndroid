
package com.hypernym.evaconnect.utils;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.snackbar.Snackbar;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.Post;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.view.adapters.HomePostsAdapter;
import com.hypernym.evaconnect.view.dialogs.VideoViewDialog;
import com.hypernym.evaconnect.view.ui.fragments.BaseFragment;
import com.nguyencse.URLEmbeddedData;
import com.nguyencse.URLEmbeddedTask;
import com.nguyencse.URLEmbeddedView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by hina saeed on 17/12/19.
 */

public final class AppUtils {

    public static Context applicationContext;
    public static String URL_REGEX="([a-zA-Z0-9]+://)?([a-zA-Z0-9_]+:[a-zA-Z0-9_]+@)?([a-zA-Z0-9.-]+\\.[A-Za-z]{2,4})(:[0-9]+)?(/.*)?";

    private AppUtils() {
        // This class is not publicly instantiable
    }
    public static Context getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(Context applicationContext) {
        AppUtils.applicationContext = applicationContext;
    }

    public static void showSnackBar(View v, String message) {
        if (v != null && !TextUtils.isEmpty(message)) {
            Snackbar snackbar = Snackbar.make(v, message, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundResource(R.color.colorPrimary);
            View view = snackbar.getView();
            TextView tv = (TextView) view.findViewById(com.google.android.material.R.id.snackbar_text);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
                tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            else
                tv.setGravity(Gravity.CENTER_HORIZONTAL);
            tv.setTextColor(ContextCompat.getColor(view.getContext(), R.color.light_black));
            snackbar.show();
        }
    }

    public static void setGlideImage(Context context, ImageView imageView,String url)
    {
        Glide.with(context) //1
                .load(url)
                .placeholder(R.mipmap.ic_default)
                .error(R.mipmap.ic_default)
                .skipMemoryCache(true) //2
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                .into(imageView);
    }
    public static void setGlideUrlThumbnail(Context context, ImageView imageView,String url)
    {
            Glide.with(context)
            .load(getImg(url))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView);
    }
    public static void setGlideVideoThumbnail(Context context, ImageView imageView,String url)
    {
        Glide.with(context)
                .load(url)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setBackground(resource);
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

    public static String getConnectionsCount(Integer connections)
    {
        String totalConnections="0 connections";
        if(connections!=null)
        {
        return  connections+" connections";
        }
        return totalConnections;
    }

    public static String getConnectionStatus(Context context,String status,boolean isreceiver)
    {
        String connectionStatus=context.getString(R.string.connect);;
        switch (status)
        {
            case AppConstants.NOT_CONNECTED:
                connectionStatus=context.getString(R.string.connect);
                break;
            case AppConstants.ACTIVE:
                connectionStatus=AppConstants.CONNECTED;
            break;
            case AppConstants.PENDING:
                if(isreceiver)
                {
                    connectionStatus=AppConstants.REQUEST_ACCEPT;
                }
                else
                {
                    connectionStatus=AppConstants.REQUEST_SENT;
                }
                break;
        }
        return connectionStatus;
    }
    public static void setLikeCount(Context context,TextView likeCount,String type,ImageView img_like)
    {
        if(type.equalsIgnoreCase(AppConstants.UNLIKE))
        {
           img_like.setBackground(context.getDrawable(R.mipmap.ic_like));
           int likes=Integer.parseInt(likeCount.getText().toString());
           if(likes>0)
             likeCount.setText(String.valueOf(likes-1));
        }
        else
        {
            img_like.setBackground(context.getDrawable(R.mipmap.ic_like_selected));
            int likes=Integer.parseInt(likeCount.getText().toString());
            likeCount.setText(String.valueOf(likes+1));
        }
    }
    public static Bitmap decodeUri(Context c, Uri uri, final int requiredSize)
            throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }

    public static void playVideo(Context context,String url)
    {
        VideoViewDialog videoViewDialog=new VideoViewDialog(context,url);

        videoViewDialog.show();
    }

    public static ArrayList<String> containsURL(String content)
    {
        ArrayList<String> links = new ArrayList<String>();
        Pattern p = Pattern.compile(URL_REGEX);

        Matcher m = Patterns.WEB_URL.matcher(content);//replace with string to compare
        if(m.find()) {
            String urlStr = m.group();
            if (urlStr.startsWith("(") && urlStr.endsWith(")"))
            {
                urlStr = urlStr.substring(1, urlStr.length() - 1);
            }
            Log.d("url is",urlStr);
            links.add(urlStr);

            return links;

        }
      return links;
    }
    public static void showUrlEmbeddedView(String url, URLEmbeddedView urlEmbeddedView)
    {
        urlEmbeddedView.setURL(url, new URLEmbeddedView.OnLoadURLListener() {
            @Override
            public void onLoadURLCompleted(URLEmbeddedData data) {
                urlEmbeddedView.title(data.getTitle());
                urlEmbeddedView.description(data.getDescription());
                urlEmbeddedView.host(data.getHost());
                urlEmbeddedView.thumbnail(data.getThumbnailURL());
                urlEmbeddedView.favor(data.getFavorURL());
            }
        });
    }
    public static void customUrlEmbeddedView(Context context,String url, final ImageView imageView)
    {
   //    Glide.with(context).clear(imageView);
            URLEmbeddedTask urlTask = new URLEmbeddedTask(new URLEmbeddedTask.OnLoadURLListener() {
                @Override
                public void onLoadURLCompleted(URLEmbeddedData data) {
                    Log.d("THUMBNAIL",""+ data.getThumbnailURL());
                    Log.d("HOST",""+ data.getHost());
                    setGlideUrlThumbnail(context,imageView,data.getThumbnailURL());
                }
            });
            urlTask.execute(url);
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine) {
        String expandText="see more";
        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine <= 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + "..." + expandText;
                    tv.setText(text);
                } else if (tv.getLineCount() > maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + "..." + expandText;
                    tv.setText(text);
                }
            }
        });

    }

}
