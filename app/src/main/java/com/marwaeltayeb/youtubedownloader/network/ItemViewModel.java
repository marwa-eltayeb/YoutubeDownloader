package com.marwaeltayeb.youtubedownloader.network;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

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