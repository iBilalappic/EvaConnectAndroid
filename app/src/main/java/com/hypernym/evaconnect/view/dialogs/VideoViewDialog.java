package com.hypernym.evaconnect.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.hypernym.evaconnect.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoViewDialog extends Dialog {
    private String url;
    VideoView videoView;
    ImageView img_close;

    private Context context;

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
        setCancelable(false);
        videoView=findViewById(R.id.videoView);
        img_close=findViewById(R.id.img_close);

        videoView.setVideoPath(url);
        WindowManager.LayoutParams a = getWindow().getAttributes();
        a.dimAmount = 0;
        getWindow().setAttributes(a);
        MediaController mediaController=new MediaController(context);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.requestFocus();
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer arg0) {

                videoView.start();
                mediaController.show();
            }
        });


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
