package com.hypernym.evaconnect.view.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.hypernym.evaconnect.R;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


import static com.hypernym.evaconnect.utils.AppUtils.getApplicationContext;


public class VideoViewDialog extends Dialog {
    private String url;
    ImageView img_close;

    private Context context;
    ExoPlayer player;

    public VideoViewDialog(Context context, String url) {
        super(context);
        this.url=url;
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_videoview);
        player=findViewById(R.id.videoView);
        setCancelable(false);
        img_close=findViewById(R.id.img_close);
        Activity activity=(Activity)context;
       // player = new  MKPlayer(activity);




      //  MKPlayerActivity.configPlayer(activity).play(url);
        WindowManager.LayoutParams a = getWindow().getAttributes();
        a.dimAmount = 0;
        getWindow().setAttributes(a);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
