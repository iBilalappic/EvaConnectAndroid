
package com.hypernym.evaconnect.utils;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.hypernym.evaconnect.R;


/**
 * Created by hina saeed on 17/12/19.
 */

public final class AppUtils {

    public static Context applicationContext;

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
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .skipMemoryCache(true) //2
                .apply(RequestOptions.circleCropTransform())
                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                .into(imageView);
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

}
