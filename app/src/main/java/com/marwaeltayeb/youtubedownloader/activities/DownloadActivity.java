package com.marwaeltayeb.youtubedownloader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.commit451.youtubeextractor.VideoStream;
import com.commit451.youtubeextractor.YouTubeExtraction;
import com.commit451.youtubeextractor.YouTubeExtractor;
import com.marwaeltayeb.youtubedownloader.R;
import com.marwaeltayeb.youtubedownloader.Utility.Constant;
import com.marwaeltayeb.youtubedownloader.adapter.DownloadAdapter;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class DownloadActivity extends AppCompatActivity {

    String idOfVideo;
    RecyclerView recyclerView;
    DownloadAdapter downloadAdapter;
    YouTubePlayerView youTubePlayerView;
    List<VideoStream> videoStreams;

    private static final String TAG = "DownloadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        // Receive the id of teh video
        Intent intent = getIntent();
        idOfVideo = intent.getStringExtra(Constant.ID);
        Log.d(TAG,idOfVideo + "");

        playYoutubeVideo();

        setUpRecyclerView();

        showDownloadList();
    }

    private void setUpRecyclerView() {
        recyclerView = findViewById(R.id.downloadList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void playYoutubeVideo() {
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(idOfVideo, 0);
            }
        });
    }


    private void showDownloadList() {
        YouTubeExtractor extractor1 = new YouTubeExtractor.Builder().build();
        final Disposable v = extractor1.extract(idOfVideo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<YouTubeExtraction>() {
                    @Override
                    public void accept(final YouTubeExtraction youTubeExtraction) throws Exception {
                        Log.d("Tube", youTubeExtraction + "Done");

                        videoStreams = youTubeExtraction.getVideoStreams();
                        downloadAdapter = new DownloadAdapter(getBaseContext(), videoStreams);
                        recyclerView.setAdapter(downloadAdapter);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("Tube", "Error");
                    }
                });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }
}
