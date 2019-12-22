package com.marwaeltayeb.youtubedownloader.adapter;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.commit451.youtubeextractor.VideoStream;
import com.marwaeltayeb.youtubedownloader.R;

import java.util.List;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder> {

    private Context mContext;
    private List<VideoStream> videoStreamsList;
    private int mItemSelected= -1;

    public DownloadAdapter(Context mContext, List<VideoStream> videoStreamsList) {
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
        VideoStream currentVideoStream = videoStreamsList.get(position);

        String resolution = currentVideoStream.getResolution() + " - ";
        String format = currentVideoStream.getFormat().toLowerCase();

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
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setVisibleInDownloadsUi(false);

        downloadmanager.enqueue(request);
    }
}
