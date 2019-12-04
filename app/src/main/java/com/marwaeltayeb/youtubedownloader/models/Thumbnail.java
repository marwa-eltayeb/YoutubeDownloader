package com.marwaeltayeb.youtubedownloader.models;

import com.google.gson.annotations.SerializedName;

public class Thumbnail {

    @SerializedName("medium")
    private ImageInfo imageInfo;

    public ImageInfo getImageInfo() {
        return imageInfo;
    }
}
