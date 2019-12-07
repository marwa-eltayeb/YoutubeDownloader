package com.marwaeltayeb.youtubedownloader.network;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;
import android.util.Log;

import com.marwaeltayeb.youtubedownloader.YoutubeConfig;
import com.marwaeltayeb.youtubedownloader.activities.PlaylistActivity;
import com.marwaeltayeb.youtubedownloader.models.Item;
import com.marwaeltayeb.youtubedownloader.models.YoutubeApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDataSource extends PageKeyedDataSource<String, Item> {

    private static final String PART = "snippet";
    public static final int MAX_SIZE = 25;
    private static final String API_KEY = "AIzaSyCLVF4cFbt7ajWqKy4qDPgcW12mcDzEGf4";

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull final LoadInitialCallback<String, Item> callback) {

        RetrofitClient.getInstance()
                .getYoutubeService().getVideos(PART,MAX_SIZE, PlaylistActivity.keyWord, YoutubeConfig.getApiKey())
                .enqueue(new Callback<YoutubeApiResponse>() {
                    @Override
                    public void onResponse(Call<YoutubeApiResponse> call, Response<YoutubeApiResponse> response) {
                        YoutubeApiResponse youtubeApiResponse = response.body();

                        if (response.body() != null) {
                            // Fetch data and pass the result  null for the previous page
                            callback.onResult(response.body().getItems(), null, response.body().getNextPageToken());
                        }

                        if(youtubeApiResponse.getItems() != null){
                            Log.d("SizeOfVideos",youtubeApiResponse.getItems().size() + " ");
                        }

                        if (!response.isSuccessful()) {
                            Log.d("Result","Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<YoutubeApiResponse> call, Throwable t) {
                        Log.d("onFailure","Failed");
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, Item> callback) {

    }

    @Override
    public void loadAfter(@NonNull LoadParams<String> params, @NonNull LoadCallback<String, Item> callback) {

    }

}
