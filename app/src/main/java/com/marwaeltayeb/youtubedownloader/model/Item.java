package com.marwaeltayeb.youtubedownloader.model;

public class Item {
    private VideoId videoId;
    private Snippet snippet;
}

class VideoId {
    private String videoId;
}

class Snippet {
    private String publishedAt;
    private String title;
    private String description;
    private Thumbnail thumbnail;
    private String channelTitle;
}

class Thumbnail {
    private Medium medium;
}

class Medium{
    private String url;
}

