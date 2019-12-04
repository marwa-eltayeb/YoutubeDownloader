package com.marwaeltayeb.youtubedownloader.models;

public class Snippet {
    private String publishedAt;
    private String title;
    private String description;
    private Thumbnail thumbnails;
    private String channelTitle;

    public String getPublishedAt() {
        return publishedAt;
    }

    public String getTitle() {
        return title;
    }

    public Thumbnail getThumbnail() {
        return thumbnails;
    }

    public String getChannelTitle() {
        return channelTitle;
    }
}
