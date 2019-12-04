package com.marwaeltayeb.youtubedownloader.models;

import java.util.List;

public class YoutubeApiResponse {

    private String nextPageToken;
    private List<Item> items;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public List<Item> getItems() {
        return items;
    }

}
