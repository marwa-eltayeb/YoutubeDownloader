package com.marwaeltayeb.youtubedownloader.adapter;

import android.annotation.SuppressLint;
import android.arch.paging.PagedListAdapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.marwaeltayeb.youtubedownloader.R;
import com.marwaeltayeb.youtubedownloader.models.Item;

public class ItemAdapter extends PagedListAdapter<Item, ItemAdapter.ItemViewHolder> {


    private Context mCtx;

    public ItemAdapter(Context mCtx) {
        super(DIFF_CALLBACK);
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Item item = getItem(position);

        if (item != null) {
            holder.videoTitle.setText(item.getSnippet().getTitle());
            // Load the profile image into ImageView
            Glide.with(mCtx)
                    .load(item.getSnippet().getThumbnail().getImageInfo().getUrl())
                    .into(holder.videoImage);

            holder.channelTitle.setText(item.getSnippet().getChannelTitle());

            holder.publicationDate.setText(item.getSnippet().getPublishedAt());
        } else {
            Toast.makeText(mCtx, "Item is null", Toast.LENGTH_LONG).show();
        }
    }

    // It determine if two list objects are the same or not
    private static DiffUtil.ItemCallback<Item> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Item>() {
                @Override
                public boolean areItemsTheSame(Item oldItem, Item newItem) {
                    return oldItem.getVideoId().getId().equals(newItem.getVideoId().getId());
                }

                @SuppressLint("DiffUtilEquals")
                @Override
                public boolean areContentsTheSame(Item oldItem, Item newItem) {
                    return oldItem.equals(newItem);
                }
            };

    class ItemViewHolder extends RecyclerView.ViewHolder {

        // Create view instances
        TextView videoTitle;
        ImageView videoImage;
        TextView channelTitle;
        TextView publicationDate;

        public ItemViewHolder(View itemView) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.titleOfVideo);
            videoImage = itemView.findViewById(R.id.imageOfVideo);
            channelTitle = itemView.findViewById(R.id.titleOfChannel);
            publicationDate = itemView.findViewById(R.id.dataOfPublication);

        }
    }
}
