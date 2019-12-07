package com.marwaeltayeb.youtubedownloader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.marwaeltayeb.youtubedownloader.R;

public class DownloadActivity extends AppCompatActivity {

    String idOfVideo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);

        // Receive the id of teh video
        Intent intent = getIntent();
        idOfVideo = intent.getStringExtra("id");
        Toast.makeText(this, idOfVideo + "", Toast.LENGTH_SHORT).show();

    }
}
