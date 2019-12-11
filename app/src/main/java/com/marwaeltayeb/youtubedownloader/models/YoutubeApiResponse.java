package com.marwaeltayeb.youtubedownloader.models;

import java.util.List;

public class YoutubeApiResponse {

    private String nextPageToken;
    private String prevPageToken;
    private List<Item> items;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public String getPrevPageToken() {
        return prevPageToken;
    }

    public List<Item> getItems() {
        return items;
    }

}
