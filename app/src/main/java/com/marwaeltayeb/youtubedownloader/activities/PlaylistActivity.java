package com.marwaeltayeb.youtubedownloader.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marwaeltayeb.youtubedownloader.R;
import com.marwaeltayeb.youtubedownloader.Utility.Constant;
import com.marwaeltayeb.youtubedownloader.adapter.ItemAdapter;
import com.marwaeltayeb.youtubedownloader.models.Item;
import com.marwaeltayeb.youtubedownloader.network.ItemViewModel;

public class PlaylistActivity extends AppCompatActivity implements ItemAdapter.ItemAdapterOnClickHandler{

    public static String keyWord;
    private RecyclerView recyclerView;
    private ItemViewModel itemViewModel;
    private ItemAdapter adapter;
    public static Dialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        progressDialog = createProgressDialog(PlaylistActivity.this);

        Intent intent = getIntent();
        keyWord = intent.getStringExtra(SearchActivity.KEYWORD);

        SetUpRecyclerView();
    }

    private void SetUpRecyclerView(){
        recyclerView = findViewById(R.id.playlist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(this).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new ItemAdapter(this,this);

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);

        loadVideos();
    }

    @Override
    public void onClick(String videoId) {
        Intent intent = new Intent(PlaylistActivity.this, DownloadActivity.class);
        // Pass the id of the video
        intent.putExtra(Constant.ID, videoId);
        startActivity(intent);
    }


    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            progressDialog.dismiss();
            return false;
        }
    }


    public static Dialog createProgressDialog(Context context) {
        Dialog progressDialog = new Dialog(context);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setCancelable(false);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        progressDialog.show();
        return progressDialog;
    }


    private void loadVideos() {
        if (isNetworkConnected()) {
            // Observe the moviePagedList from ViewModel
            // Observing the itemPagedList from ViewModel
            itemViewModel.itemPagedList.observe(this, new Observer<PagedList<Item>>() {
                @Override
                public void onChanged(@Nullable PagedList<Item> items) {
                    // In case of any changes, submitting the items to adapter
                    adapter.submitList(items);
                    // when screen is rotated
                    if (items != null && !items.isEmpty()) {
                        progressDialog.dismiss();
                    }
                }
            });

        }

        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

}
