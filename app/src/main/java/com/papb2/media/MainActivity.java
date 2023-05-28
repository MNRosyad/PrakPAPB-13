package com.papb2.media;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.webkit.URLUtil;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.material.color.DynamicColors;

public class MainActivity extends AppCompatActivity {
    private static final String VIDEO_SAMPLE = "the_blue_train";
    private TextView mBufferingTextView;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DynamicColors.applyToActivitiesIfAvailable(this.getApplication());

        videoView = findViewById(R.id.mediaPlayer);
        mBufferingTextView = findViewById(R.id.text_buffer);

        MediaController controller = new MediaController(this);
        controller.setMediaPlayer(videoView);
        videoView.setMediaController(controller);

        videoView.setOnCompletionListener(mediaPlayer -> {
            Toast.makeText(MainActivity.this, "Looping", Toast.LENGTH_SHORT).show();
            videoView.seekTo(1);
            videoView.start();
        });

        videoView.setOnPreparedListener(mediaPlayer -> {
            mBufferingTextView.setVisibility(VideoView.INVISIBLE);
            videoView.seekTo(1);
            videoView.start();
        });
    }

    private Uri getMedia(String mediaName) {
        if (URLUtil.isValidUrl(mediaName)) {
            return Uri.parse(mediaName);
        } else {
            return Uri.parse("android.resource://" + getPackageName() +
                    "/raw/" + mediaName);
        }

    }

    private void initializePlayer() {
        mBufferingTextView.setVisibility(VideoView.VISIBLE);

        Uri videoUri = getMedia(VIDEO_SAMPLE);
        videoView.setVideoURI(videoUri);
        videoView.start();
    }

    private void releasePlayer() {
        videoView.stopPlayback();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initializePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            videoView.pause();
        }
    }
}