package com.marwaeltayeb.youtubedownloader.Utility;

import com.marwaeltayeb.youtubedownloader.BuildConfig;

public class YoutubeConfig {

    public YoutubeConfig() {
    }

    private static final String API_KEY = BuildConfig.API_KEY;

    public static String getApiKey() {
        return API_KEY;
    }
}
