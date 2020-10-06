
package com.hypernym.evaconnect.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.facebook.login.LoginManager;
import com.google.android.material.snackbar.Snackbar;
import com.hypernym.evaconnect.R;
import com.hypernym.evaconnect.communication.RestClient;
import com.hypernym.evaconnect.constants.AppConstants;
import com.hypernym.evaconnect.models.BaseModel;
import com.hypernym.evaconnect.models.User;
import com.hypernym.evaconnect.view.dialogs.SimpleDialog;
import com.hypernym.evaconnect.view.dialogs.VideoViewDialog;
import com.hypernym.evaconnect.view.ui.activities.LoginActivity;
import com.nguyencse.URLEmbeddedData;
import com.nguyencse.URLEmbeddedTask;
import com.nguyencse.URLEmbeddedView;
import com.onesignal.OneSignal;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by hina saeed on 17/12/19.
 */

public final class AppUtils {
    public static Context applicationContext;
    public static String URL_REGEX="([a-zA-Z0-9]+://)?([a-zA-Z0-9_]+:[a-zA-Z0-9_]+@)?([a-zA-Z0-9.-]+\\.[A-Za-z]{2,4})(:[0-9]+)?(/.*)?";
    public static SimpleDialog simpleDialog;
    public final static String FOLDER = Environment.getExternalStorageDirectory() + "/PDF";
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
            snackbar.getView().setBackgroundResource(R.color.colorwhite);
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
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .apply(RequestOptions.circleCropTransform())
                .into(imageView);
    }
    public static void setGlideUrlThumbnail(Context context, ImageView imageView,String url)
    {
            Glide.with(context)
            .load(getImg(url))
            .diskCacheStrategy(DiskCacheStrategy.RESOURCE).into(imageView);
    }
    public static void setGlideImageUrl(Context context, ImageView imageView,String url)
    {
        Glide
                .with(imageView.getContext())
                .load(getImg(url))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }
    public static void setGlideVideoThumbnail(Context context, ImageView imageView,String url)
    {
        Glide.with(context)
                .load(getImg(url))
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
        String connectionStatus=context.getString(R.string.connect);
        if(status!=null)
        {
            switch (status)
            {
                case AppConstants.NOT_CONNECTED:
                    connectionStatus=context.getString(R.string.connect);
                    break;
                case AppConstants.ACTIVE:
                    connectionStatus=AppConstants.CONNECTED;
                    break;
                case AppConstants.DELETED:
                    connectionStatus=AppConstants.DELETED;
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
        }
        return connectionStatus;
    }
    public static void setLikeCount(Context context,TextView likeCount,String type,ImageView img_like)
    {
        if(type.equalsIgnoreCase(AppConstants.UNLIKE))
        {
           img_like.setBackground(context.getDrawable(R.drawable.ic_like));
           int likes=Integer.parseInt(likeCount.getText().toString());
           if(likes>0)
             likeCount.setText(String.valueOf(likes-1));
        }
        else
        {
            img_like.setBackground(context.getDrawable(R.drawable.like_selected));
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

    public static void makeTextViewResizable(final TextView tv, final int maxLine,String strtext) {
        tv.setText(strtext);
        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (tv.getLineCount() > maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine);
                    String text = tv.getText().subSequence(0, lineEndIndex - maxLine) + "...See More";
                    tv.setText(text);

                }
            }
        });

    }
    public static void makeNotification(Context context, Class<?> class_, String fragmentName,
                                        Bundle bundle, String message, boolean isUpdateCurrent,
                                        int requestCode) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationHelper helper;
            helper = new NotificationHelper(context);
            Notification.Builder nb = helper.
                    getAndroidChannelNotification(context, class_, fragmentName, bundle, message, isUpdateCurrent, requestCode);

            helper.getManager().notify(requestCode, nb.build());

        } else {
            makeNotification_default(context, class_, fragmentName, bundle, message, isUpdateCurrent, requestCode);
        }

    }

    public static void makeNotification_activity(Context context, Class<?> class_,String chat_id, String message, boolean isUpdateCurrent,
                                                int requestCode) {
        Intent intent = new Intent(context, class_);
        if (isUpdateCurrent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra("chat_room_id",chat_id);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(getNotificationIcon())
                .setTicker(message)
                // .setColor(ContextCompat.getColor(context, R.color.colorNotificationIcon))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(requestCode, notification);
    }

    public static void makeNotification_default(Context context, Class<?> class_, String fragmentName,
                                                Bundle bundle, String message, boolean isUpdateCurrent,
                                                int requestCode) {
        Intent intent = new Intent(context, class_);
        if (isUpdateCurrent) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        intent.putExtra(Constants.FRAGMENT_NAME, fragmentName);
        intent.putExtra(Constants.DATA, bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT
        );
        Notification notification = new NotificationCompat.Builder(context)
                .setSmallIcon(getNotificationIcon())
                .setTicker(message)
                // .setColor(ContextCompat.getColor(context, R.color.colorNotificationIcon))
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .build();
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(requestCode, notification);
    }
    private static int getNotificationIcon() {
        boolean useWhiteIcon = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.ic_launcher : R.mipmap.ic_launcher;
    }
    public static void logout(Context context)
    {
        Activity activity=(Activity)context;
        simpleDialog=new SimpleDialog(context, context.getString(R.string.confirmation), context.getString(R.string.msg_logout), context.getString(R.string.button_cancel), context.getString(R.string.logout), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.button_positive:
                        simpleDialog.dismiss();
                        HashMap<String,Object> body=new HashMap<>();

                        body.put("modified_by_id",LoginUtils.getLoggedinUser().getId());
                        body.put("last_online_datetime", DateUtils.GetCurrentdatetime());
                        body.put("is_online",false);
                        RestClient.get().appApi().userOnline(LoginUtils.getLoggedinUser().getId(),body).enqueue(new Callback<BaseModel<List<User>>>() {
                            @Override
                            public void onResponse(Call<BaseModel<List<User>>> call, Response<BaseModel<List<User>>> response) {

                            }

                            @Override
                            public void onFailure(Call<BaseModel<List<User>>> call, Throwable t) {

                            }
                        });
                        Intent intent = new Intent(context, LoginActivity.class);
                        activity.finish();
                        context.startActivity(intent);
                        LoginUtils.clearUser(getApplicationContext());
                        LoginUtils.removeAuthToken(getApplicationContext());
                        AppUtils.facebookLogout();
                        OneSignal.sendTag("email","null");

                        break;
                    case R.id.button_negative:
                        simpleDialog.dismiss();
                        break;
                }
            }
        });
        simpleDialog.show();
    }

    //PdfiumAndroid (https://github.com/barteksc/PdfiumAndroid)
    //https://github.com/barteksc/AndroidPdfViewer/issues/49
        public static Bitmap generateImageFromPdf(Uri pdfUri,Context context) {
            int pageNumber = 0;
            Bitmap bmp=null;
            PdfiumCore pdfiumCore = new PdfiumCore(context);
            try {
                //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
                ParcelFileDescriptor fd = context.getContentResolver().openFileDescriptor(pdfUri, "r");
                PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
                pdfiumCore.openPage(pdfDocument, pageNumber);
                int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
                int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
                bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
               // saveImage(bmp);
                pdfiumCore.closeDocument(pdfDocument); // important!

            } catch(Exception e) {
                //todo with exception
            }
            return bmp;
        }

        public static void saveImage(Bitmap bmp) {
            FileOutputStream out = null;
            try {
                File folder = new File(FOLDER);
                if(!folder.exists())
                    folder.mkdirs();
                File file = new File(folder, "PDF.png");
                out = new FileOutputStream(file);
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
            } catch (Exception e) {
                //todo with exception
            } finally {
                try {
                    if (out != null)
                        out.close();
                } catch (Exception e) {
                    //todo with exception
                }
            }
        }
    public static Bitmap getVideoThumbnail(String url)    {
       return ThumbnailUtils.createVideoThumbnail(url,MediaStore.Video.Thumbnails.MINI_KIND);
    }

    public static void facebookLogout(){
        if (LoginManager.getInstance()!=null){
            LoginManager.getInstance().logOut();
        }
    }


}
