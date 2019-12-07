package com.marwaeltayeb.youtubedownloader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.marwaeltayeb.youtubedownloader.R;
import com.marwaeltayeb.youtubedownloader.YoutubeConfig;

public class DownloadActivity extends YouTubeBaseActivity {

    String idOfVideo;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;

    private static final String TAG = "DownloadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        youTubePlayerView = findViewById(R.id.youtubePlay);

        // Receive the id of teh video
        Intent intent = getIntent();
        idOfVideo = intent.getStringExtra("id");
        Toast.makeText(this, idOfVideo + "", Toast.LENGTH_SHORT).show();

        onInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "Done initializing");
                youTubePlayer.loadVideo(idOfVideo);
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "Failed to initialize");
            }
        };



        // Play it
        youTubePlayerView.initialize(YoutubeConfig.getApiKey(), onInitializedListener);


    }


}
