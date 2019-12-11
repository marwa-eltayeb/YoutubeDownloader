package com.marwaeltayeb.youtubedownloader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.commit451.youtubeextractor.VideoStream;
import com.commit451.youtubeextractor.YouTubeExtraction;
import com.commit451.youtubeextractor.YouTubeExtractor;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.marwaeltayeb.youtubedownloader.R;
import com.marwaeltayeb.youtubedownloader.Utility.YoutubeConfig;
import com.marwaeltayeb.youtubedownloader.adapter.DownloadAdapter;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DownloadActivity extends YouTubeBaseActivity {

    String idOfVideo;
    YouTubePlayerView youTubePlayerView;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    RecyclerView recyclerView;
    DownloadAdapter downloadAdapter;
    List<VideoStream> videoStreams;

    private static final String TAG = "DownloadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        // Receive the id of teh video
        Intent intent = getIntent();
        idOfVideo = intent.getStringExtra("id");

        playYoutubeVideo();

        setUpRecyclerView();

        showDownloadList();
    }

    private void setUpRecyclerView(){
        recyclerView = findViewById(R.id.downloadList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void playYoutubeVideo(){
        youTubePlayerView = findViewById(R.id.youtubePlay);

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


    private void showDownloadList(){
        YouTubeExtractor extractor1 = new YouTubeExtractor.Builder().build();
        final Disposable v = extractor1.extract(idOfVideo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<YouTubeExtraction>() {
                    @Override
                    public void accept(final YouTubeExtraction youTubeExtraction) throws Exception {
                        Log.d("Tube", youTubeExtraction + "Done");

                        videoStreams = youTubeExtraction.getVideoStreams();
                        downloadAdapter = new DownloadAdapter(getBaseContext(),videoStreams);
                        recyclerView.setAdapter(downloadAdapter);

                        //final String url = String.valueOf(youTubeExtraction.getVideoStreams().get(0).getUrl());

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("Tube", "Error");
                    }
                });
    }




}
