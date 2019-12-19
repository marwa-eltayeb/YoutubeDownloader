package com.marwaeltayeb.youtubedownloader.network;

import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.PageKeyedDataSource;

import com.marwaeltayeb.youtubedownloader.models.Item;

public class ItemDataSourceFactory extends DataSource.Factory {

    // Creating the mutable live data
    private MutableLiveData<PageKeyedDataSource<String, Item>> itemLiveDataSource = new MutableLiveData<>();

    @Override
    public DataSource<String, Item> create() {
        // Getting our data source object
        ItemDataSource itemDataSource = new ItemDataSource();

        // Posting the data source to get the values
        itemLiveDataSource.postValue(itemDataSource);

        // Returning the data source
        return itemDataSource;
    }


    // Getter for item live data source
    public MutableLiveData<PageKeyedDataSource<String, Item>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }

}
