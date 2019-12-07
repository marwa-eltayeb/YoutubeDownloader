package com.marwaeltayeb.youtubedownloader.models;

public class Item {
    private VideoId id;
    public Snippet snippet;

    public VideoId getVideoId() {
        return id;
    }

    public Snippet getSnippet() {
        return snippet;
    }
}

