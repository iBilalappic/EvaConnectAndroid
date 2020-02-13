package com.hypernym.evaconnect.view.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.hypernym.evaconnect.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VideoViewDialog extends Dialog {
    private String url;
    VideoView videoView;
    ImageView img_close;
    String videoUrl="http://168.63.140.202:8003/media/assets/post/6FC2694D-E344-4360-BDFC-7F413BE78928-1602-00000302F4513407.MOV";

    SimpleExoPlayerView exoPlayerView;
    SimpleExoPlayer exoPlayer;
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
   //     videoView = findViewById(R.id.videoView);
        img_close = findViewById(R.id.img_close);
        exoPlayerView = (SimpleExoPlayerView) findViewById(R.id.video_view);

        try {
            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory(bandwidthMeter));
            exoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            Uri videourl = Uri.parse(url);
            DefaultHttpDataSourceFactory dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer Video");
            ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            ProgressiveMediaSource.Factory mediaSourceFactory =
                    new ProgressiveMediaSource.Factory(dataSourceFactory);
            // Create a media source using the supplied URI
            MediaSource mediaSource1 = mediaSourceFactory.createMediaSource(videourl);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.prepare(mediaSource1);
            exoPlayer.setPlayWhenReady(true);








            exoPlayer.addListener(new ExoPlayer.EventListener(){

                @Override
                public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

                }

                @Override
                public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

                }

                @Override
                public void onLoadingChanged(boolean isLoading) {

                }

                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

                }

                @Override
                public void onRepeatModeChanged(int repeatMode) {

                }

                @Override
                public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

                }

                @Override
                public void onPlayerError(ExoPlaybackException error) {

                }

                @Override
                public void onPositionDiscontinuity(int reason) {

                }

                @Override
                public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

                }

                @Override
                public void onSeekProcessed() {

                }
            });

        } catch (Exception E) {
            Log.d("Error", "onCreate: " + E.toString());
        }


        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
/*
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
*/
    }
}
