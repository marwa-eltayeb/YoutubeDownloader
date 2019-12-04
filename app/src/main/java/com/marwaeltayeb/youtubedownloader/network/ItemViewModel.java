package com.marwaeltayeb.youtubedownloader.network;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;

import com.marwaeltayeb.youtubedownloader.models.Item;

public class ItemViewModel extends ViewModel {

    // Creating liveData for PagedList and PagedKeyedDataSource
    public LiveData<PagedList<Item>> itemPagedList;
    private LiveData<PageKeyedDataSource<String, Item>> liveDataSource;

    // Constructor
    public ItemViewModel() {

        // Getting our data source factory
        ItemDataSourceFactory itemDataSourceFactory = new ItemDataSourceFactory();

        // getting the live data source from data source factory
        liveDataSource = itemDataSourceFactory.getItemLiveDataSource();

        // Getting PagedList config
        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setPageSize(ItemDataSource.MAX_SIZE).build();

        // Building the paged list
        itemPagedList = (new LivePagedListBuilder(itemDataSourceFactory, pagedListConfig)).build();
    }
}