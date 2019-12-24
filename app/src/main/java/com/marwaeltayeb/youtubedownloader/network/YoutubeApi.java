package com.marwaeltayeb.youtubedownloader.network;

import com.marwaeltayeb.youtubedownloader.models.YoutubeApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeApi {
    @GET("search")
    Call<YoutubeApiResponse>getVideos(@Query("part") String part,@Query("type") String type ,@Query("pageToken") String token,@Query("maxResults") int resultNumbers, @Query("q") String keyword, @Query("key") String key);
}

