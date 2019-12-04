package com.marwaeltayeb.youtubedownloader.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.arch.paging.PagedList;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.marwaeltayeb.youtubedownloader.R;
import com.marwaeltayeb.youtubedownloader.adapter.ItemAdapter;
import com.marwaeltayeb.youtubedownloader.models.Item;
import com.marwaeltayeb.youtubedownloader.network.ItemViewModel;

public class PlaylistActivity extends AppCompatActivity {

    public static String keyWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        Intent intent = getIntent();
        keyWord = intent.getStringExtra(SearchActivity.KEYWORD);
        Toast.makeText(this, keyWord + "", Toast.LENGTH_SHORT).show();

        // Setting up recyclerView
        RecyclerView recyclerView = findViewById(R.id.playlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);


        // Getting our ItemViewModel
        ItemViewModel itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);

        // Creating the Adapter
        final ItemAdapter adapter = new ItemAdapter(this);


        // Observing the itemPagedList from ViewModel
        itemViewModel.itemPagedList.observe(this, new Observer<PagedList<Item>>() {
            @Override
            public void onChanged(@Nullable PagedList<Item> items) {

                Toast.makeText(PlaylistActivity.this, items.size() + "", Toast.LENGTH_SHORT).show();
                // In case of any changes, submitting the items to adapter
                adapter.submitList(items);
            }
        });

        // Setting the adapter
        recyclerView.setAdapter(adapter);


    }
}
