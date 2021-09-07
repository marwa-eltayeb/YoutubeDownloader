package com.marwaeltayeb.youtubedownloader.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.marwaeltayeb.youtubedownloader.R;
import com.marwaeltayeb.youtubedownloader.Utility.DateUtils;
import com.marwaeltayeb.youtubedownloader.models.Item;

import java.util.Date;

public class ItemAdapter extends PagedListAdapter<Item, ItemAdapter.ItemViewHolder> {

    private final Context mContext;

    // Create a final private ItemAdapterOnClickHandler called mClickHandler
    private final ItemAdapterOnClickHandler clickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface ItemAdapterOnClickHandler {
        void onClick(String videoId);
    }

    public ItemAdapter(Context context, ItemAdapterOnClickHandler clickHandler) {
        super(DIFF_CALLBACK);
        this.mContext = context;
        this.clickHandler = clickHandler;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        Item item = getItem(position);

        if (item != null) {
            holder.videoTitle.setText(item.getSnippet().getTitle());
            // Load the video image into ImageView
            Glide.with(mContext)
                    .load(item.getSnippet().getThumbnail().getImageInfo().getUrl())
                    .into(holder.videoImage);

            holder.channelTitle.setText(item.getSnippet().getChannelTitle());

            Date convertedDate = DateUtils.convertStringToDate(item.getSnippet().getPublishedAt());
            String timeAgo = DateUtils.getTimeAgo(convertedDate,mContext);
            holder.publicationDate.setText(timeAgo);
        } else {
            Toast.makeText(mContext, "Item is null", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public PagedList<Item> getCurrentList() {
        return super.getCurrentList();
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

    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        // Create view instances
        TextView videoTitle;
        ImageView videoImage;
        TextView channelTitle;
        TextView publicationDate;

        private ItemViewHolder(View itemView) {
            super(itemView);
            videoTitle = itemView.findViewById(R.id.titleOfVideo);
            videoImage = itemView.findViewById(R.id.imageOfVideo);
            channelTitle = itemView.findViewById(R.id.titleOfChannel);
            publicationDate = itemView.findViewById(R.id.dataOfPublication);
            // Register a callback to be invoked when this view is clicked.
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Get the position of the view in the adapter
            int mItemSelected = getAdapterPosition();
            // Get the id of the video
            String videoId = getCurrentList().get(mItemSelected).getVideoId().getId();
            if(videoId == null){
                return;
            }
            // Send videoId through click
            clickHandler.onClick(videoId);
        }
    }
}