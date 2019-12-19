package com.marwaeltayeb.youtubedownloader.network;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.paging.PageKeyedDataSource;

import com.marwaeltayeb.youtubedownloader.Utility.YoutubeConfig;
import com.marwaeltayeb.youtubedownloader.activities.PlaylistActivity;
import com.marwaeltayeb.youtubedownloader.models.Item;
import com.marwaeltayeb.youtubedownloader.models.YoutubeApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemDataSource extends PageKeyedDataSource<String, Item> {

    private static final String PART = "snippet";
    public static final int MAX_SIZE = 25;

    @Override
    public void loadInitial(@NonNull LoadInitialParams<String> params, @NonNull final LoadInitialCallback<String, Item> callback) {

        RetrofitClient.getInstance()
                .getYoutubeService().getVideos(PART, MAX_SIZE, PlaylistActivity.keyWord, YoutubeConfig.getApiKey())
                .enqueue(new Callback<YoutubeApiResponse>() {
                    @Override
                    public void onResponse(Call<YoutubeApiResponse> call, Response<YoutubeApiResponse> response) {
                        YoutubeApiResponse youtubeApiResponse = response.body();

                        if (youtubeApiResponse != null) {
                            // Fetch data and pass the result  null for the previous page
                            callback.onResult(youtubeApiResponse.getItems(), null, youtubeApiResponse.getNextPageToken());
                        }

                        if (youtubeApiResponse.getItems() == null) {
                            Log.d("Quota", "Quota of Youtube Data Api is finished");
                            return;
                        }

                        if (!response.isSuccessful()) {
                            Log.d("Result", "Code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<YoutubeApiResponse> call, Throwable t) {
                        Log.d("onFailure", "Failed");
                    }
                });
    }

    @Override
    public void loadBefore(@NonNull final LoadParams<String> params, @NonNull final LoadCallback<String, Item> callback) {
        RetrofitClient.getInstance()
                .getYoutubeService().getVideos(PART, MAX_SIZE, PlaylistActivity.keyWord, YoutubeConfig.getApiKey())
                .enqueue(new Callback<YoutubeApiResponse>() {
                    @Override
                    public void onResponse(Call<YoutubeApiResponse> call, Response<YoutubeApiResponse> response) {
                        YoutubeApiResponse youtubeApiResponse = response.body();

                        String key = youtubeApiResponse.getPrevPageToken() != null ? params.key : null;
                        if (response.body() != null) {
                            // Passing the loaded database and the previous page key
                            callback.onResult(youtubeApiResponse.getItems(), key);
                        }
                    }

                    @Override
                    public void onFailure(Call<YoutubeApiResponse> call, Throwable t) {
                        Log.d("onFailure", "Failed");
                    }
                });

    }

    @Override
    public void loadAfter(@NonNull final LoadParams<String> params, @NonNull final LoadCallback<String, Item> callback) {
        RetrofitClient.getInstance()
                .getYoutubeService().getVideos(PART, MAX_SIZE, PlaylistActivity.keyWord, YoutubeConfig.getApiKey())
                .enqueue(new Callback<YoutubeApiResponse>() {
                    @Override
                    public void onResponse(Call<YoutubeApiResponse> call, Response<YoutubeApiResponse> response) {
                        YoutubeApiResponse youtubeApiResponse = response.body();

                        if (youtubeApiResponse != null) {
                            // If the response has next page, load it
                            String key = youtubeApiResponse.getNextPageToken() != null ? params.key : null;

                            // Passing the loaded database and next page value
                            callback.onResult(youtubeApiResponse.getItems(), key);
                        }
                    }

                    @Override
                    public void onFailure(Call<YoutubeApiResponse> call, Throwable t) {
                        Log.d("onFailure", "Failed");
                    }
                });
    }

}
