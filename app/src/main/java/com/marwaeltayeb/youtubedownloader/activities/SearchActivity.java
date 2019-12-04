package com.marwaeltayeb.youtubedownloader.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.marwaeltayeb.youtubedownloader.R;

public class SearchActivity extends AppCompatActivity {

    private EditText searchEditText;
    public static final String KEYWORD = "keyword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchEditText = findViewById(R.id.searchEditText);
    }


    public void Search(View view) {
        Intent intent = new Intent(SearchActivity.this, PlaylistActivity.class);
        String keyword = searchEditText.getText().toString().trim();
        if(keyword.equals("")){
            return;
        }
        intent = intent.putExtra(KEYWORD, keyword);
        startActivity(intent);
    }
}

