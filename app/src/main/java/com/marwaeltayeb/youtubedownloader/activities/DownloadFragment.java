package com.marwaeltayeb.youtubedownloader.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marwaeltayeb.youtubedownloader.R;
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

import static com.marwaeltayeb.youtubedownloader.Utility.Constant.VIDEO_ID;
import static com.marwaeltayeb.youtubedownloader.Utility.Constant.WRITE_STORAGE_PERMISSION_REQUEST_CODE;

@SuppressLint("StaticFieldLeak")
public class DownloadFragment extends Fragment {

    String idOfVideo;
    RecyclerView recyclerView;
    DownloadAdapter downloadAdapter;
    YouTubePlayerView youTubePlayerView;

    private static final String TAG = "DownloadFragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_download, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Receive the id of teh video
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            idOfVideo = bundle.getString(VIDEO_ID);
        }
        Log.d(TAG, idOfVideo);

        recyclerView = view.findViewById(R.id.downloadList);
        youTubePlayerView = view.findViewById(R.id.youtube_player_view);

        playYoutubeVideo();

        setUpRecyclerView();

        showDownloadList();
    }

    private void setUpRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void playYoutubeVideo() {
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                youTubePlayer.loadVideo(idOfVideo, 0);
            }
        });
    }

    private void showDownloadList() {
        new YouTubeExtractor(getContext()) {
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

                    downloadAdapter = new DownloadAdapter(getContext(), videoStreams, new DownloadAdapter.CallBack() {
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
                } else {
                    Toast.makeText(getContext(), "No Download Files for this video", Toast.LENGTH_SHORT).show();
                }
            }
        }.extract(getString(R.string.youtube_link) + idOfVideo, true, true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        youTubePlayerView.release();
    }

    private void download(String url) {
        DownloadManager downloadmanager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Video File");
        request.setDescription("Downloading");
        request.setDestinationInExternalFilesDir(getContext(), Environment.DIRECTORY_DOWNLOADS, createRandomImageName());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(false);

        downloadmanager.enqueue(request);
    }

    private String createRandomImageName() {
        return "video" + Math.random() + ".mp4";
    }

    public boolean checkPermissionForWriteExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED;
        }
        return false;
    }

    public void requestPermissionForWriteExternalStorage() {
        try {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_PERMISSION_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}