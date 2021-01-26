package com.marwaeltayeb.youtubedownloader.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.marwaeltayeb.youtubedownloader.R;

import java.util.List;

import at.huber.youtubeExtractor.YtFile;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {

    private final Context mContext;
    private final List<YtFile> videoStreamsList;
    private int mItemSelected= -1;

    public DownloadAdapter(Context mContext, List<YtFile> videoStreamsList) {
        this.mContext = mContext;
        this.videoStreamsList = videoStreamsList;
    }

    @NonNull
    @Override
    public DownloadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_item, parent, false);
        return new DownloadViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadViewHolder holder, int position) {
        YtFile currentVideoStream = videoStreamsList.get(position);

        String resolution = String.valueOf(currentVideoStream.getFormat().getHeight()).replace("-1", "Audio") + " - ";
        String format = currentVideoStream.getFormat().getExt();

        holder.resolutionTextView.setText(resolution);
        holder.formatTextView.setText(format);
    }

    @Override
    public int getItemCount() {
        if (videoStreamsList == null) {
            return 0;
        }
        return videoStreamsList.size();
    }

    class DownloadViewHolder extends RecyclerView.ViewHolder{
        // Create view instances
        TextView resolutionTextView;
        TextView formatTextView;
        ImageView downLoadImageView;

        private DownloadViewHolder(View itemView) {
            super(itemView);
            resolutionTextView = itemView.findViewById(R.id.resolutionTextView);
            formatTextView = itemView.findViewById(R.id.formatTextView);
            downLoadImageView = itemView.findViewById(R.id.img_download);

            downLoadImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Get the position of the view in the adapter
                    mItemSelected = getAdapterPosition();
                    notifyDataSetChanged();
                    String url = videoStreamsList.get(mItemSelected).getUrl();
                    Log.d("url", url);
                    download(url);

                    Toast.makeText(mContext, "Download Started", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void download(String url){
        DownloadManager downloadmanager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);

        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Video File");
        request.setDescription("Downloading");
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, createRandomImageName());
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(false);

        downloadmanager.enqueue(request);
    }


     private String createRandomImageName() {
        return "video" + Math.random() + ".mp4";
    }
}

