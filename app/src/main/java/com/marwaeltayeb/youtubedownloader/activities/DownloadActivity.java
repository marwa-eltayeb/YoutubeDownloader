package com.marwaeltayeb.youtubedownloader.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
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

    private static final int WRITE_STORAGE_PERMISSION_REQUEST_CODE = 1000;

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

                    Log.d(TAG, "ytFiles: "+ "Hello");

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

                    Log.d(TAG, "onExtractionComplete: "+ videoStreams.size());

                    downloadAdapter = new DownloadAdapter(getBaseContext(), videoStreams, new DownloadAdapter.CallBack() {
                        @Override
                        public void onClickItem(String url) {
                            boolean granted = checkPermissionForWriteExternalStorage();
                            if (!granted) {
                                requestPermissionForWriteExternalStorage();
                            } else {
                                download(url);
                            }
                        }
                    });
                    recyclerView.setAdapter(downloadAdapter);
                }else {
                    Toast.makeText(DownloadActivity.this, "No Download Files for this video", Toast.LENGTH_SHORT).show();
                }
            }
        }.extract(getString(R.string.youtube_link) + idOfVideo, true, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        youTubePlayerView.release();
    }

    private void download(String url){
        DownloadManager downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Video File");
        request.setDescription("Downloading");
        request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_DOWNLOADS, createRandomImageName());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(false);

        downloadmanager.enqueue(request);
    }

    private String createRandomImageName() {
        return "video" + Math.random() + ".mp4";
    }

    public boolean checkPermissionForWriteExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForWriteExternalStorage() {
        try {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
