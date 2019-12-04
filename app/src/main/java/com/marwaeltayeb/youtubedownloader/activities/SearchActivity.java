package com.marwaeltayeb.youtubedownloader.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.marwaeltayeb.youtubedownloader.R;
import com.marwaeltayeb.youtubedownloader.model.YoutubeApiResponse;
import com.marwaeltayeb.youtubedownloader.network.RetrofitClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        RetrofitClient.getInstance()
                .getYoutubeService().getVideos("snippet",25,"surfing","")
                .enqueue(new Callback<YoutubeApiResponse>() {
                    @Override
                    public void onResponse(Call<YoutubeApiResponse> call, Response<YoutubeApiResponse> response) {
                        YoutubeApiResponse youtubeApiResponse = response.body();

                        if(youtubeApiResponse.getItems() != null){
                            Toast.makeText(SearchActivity.this, youtubeApiResponse.getItems().size() + " ", Toast.LENGTH_SHORT).show();
                        }

                        if (!response.isSuccessful()) {
                            Toast.makeText(SearchActivity.this, "Code: " + response.code(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Call<YoutubeApiResponse> call, Throwable t) {
                        Toast.makeText(SearchActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                });





    }



}

