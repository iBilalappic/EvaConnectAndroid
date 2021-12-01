package com.hypernym.evaconnect.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.VideoView;

import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.hypernym.evaconnect.R;

public class LocalVideoViewDialog extends Dialog {
    private String url;
    VideoView videoView;
    ImageView img_close;
    String videoUrl="http://168.63.140.202:8003/media/assets/post/6FC2694D-E344-4360-BDFC-7F413BE78928-1602-00000302F4513407.MOV";

    VideoView exoPlayerView;
    VideoView exoPlayer;
    private Context context;

    public LocalVideoViewDialog(Context context, String url) {
        super(context);
        this.url=url;
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_localvideoview);
        setCancelable(false);
        //     videoView = findViewById(R.id.videoView);
        img_close = findViewById(R.id.img_close);
        exoPlayerView = (VideoView) findViewById(R.id.video_view);

        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            //exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            Uri videourl = Uri.parse(url);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer Video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            ProgressiveMediaSource.Factory mediaSourceFactory =
                    new ProgressiveMediaSource.Factory(dataSourceFactory);
            // Create a media source using the supplied URI
            MediaSource mediaSource1 = mediaSourceFactory.createMediaSource(videourl);

            exoPlayerView.setVideoPath(url);
            exoPlayerView.start();


        } catch (Exception E) {
            Log.d("Error", "onCreate: " + E.toString());
        }


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
