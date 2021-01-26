package com.marwaeltayeb.youtubedownloader.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marwaeltayeb.youtubedownloader.R;
import com.marwaeltayeb.youtubedownloader.Utility.Constant;
import com.marwaeltayeb.youtubedownloader.adapter.DownloadAdapter;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

@SuppressLint("StaticFieldLeak")
public class DownloadActivity extends AppCompatActivity {

    String idOfVideo;
    RecyclerView recyclerView;
    DownloadAdapter downloadAdapter;
    YouTubePlayerView youTubePlayerView;

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
        new YouTubeExtractor(this) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {

                if (ytFiles != null) {

                    Set<String> uniqueFiles = new HashSet<String>();
                    ArrayList<YtFile> videoStreams = new ArrayList<YtFile>();

                    // Iterate over ytFiles
                    for (int i = 0, itag; i < ytFiles.size(); i++) {
                        itag = ytFiles.keyAt(i);
                        String format = ytFiles.get(itag).getFormat().getHeight() + ytFiles.get(itag).getFormat().getExt();
                        boolean isAdded = uniqueFiles.add(format);
                        if (isAdded) {
                            videoStreams.add(ytFiles.get(itag));
                        }
                    }

                    downloadAdapter = new DownloadAdapter(getBaseContext(), videoStreams);
                    recyclerView.setAdapter(downloadAdapter);
                }
            }
        }.extract(getString(R.string.youtube_link) + idOfVideo, true, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }
}
