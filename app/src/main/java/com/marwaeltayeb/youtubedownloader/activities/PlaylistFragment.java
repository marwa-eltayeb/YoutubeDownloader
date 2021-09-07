package com.marwaeltayeb.youtubedownloader.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.paging.PagedList;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.marwaeltayeb.youtubedownloader.R;
import com.marwaeltayeb.youtubedownloader.adapter.ItemAdapter;
import com.marwaeltayeb.youtubedownloader.models.Item;
import com.marwaeltayeb.youtubedownloader.network.ItemViewModel;

import static com.marwaeltayeb.youtubedownloader.Utility.Constant.KEYWORD;
import static com.marwaeltayeb.youtubedownloader.Utility.Constant.VIDEO_ID;


public class PlaylistFragment extends Fragment implements ItemAdapter.ItemAdapterOnClickHandler{

    public static String keyWord;
    private RecyclerView recyclerView;
    private ItemViewModel itemViewModel;
    private ItemAdapter adapter;
    public static Dialog progressDialog;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_playlist, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        recyclerView = view.findViewById(R.id.playlist);

        progressDialog = createProgressDialog(getContext());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            keyWord = bundle.getString(KEYWORD);
        }

        SetUpRecyclerView();
    }

    private void SetUpRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        adapter = new ItemAdapter(getContext(),this);

        itemViewModel = ViewModelProviders.of(this).get(ItemViewModel.class);

        loadVideos();
    }

    @Override
    public void onClick(String videoId) {

        Fragment fragment = new Fragment();
        Bundle bundle = new Bundle();
        bundle.putString(VIDEO_ID, videoId);
        fragment.setArguments(bundle);

        navController.navigate(R.id.action_playlistFragment_to_downloadFragment, bundle);
    }


    private boolean isNetworkConnected() {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
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
            // Observe the itemPagedList from ViewModel
            itemViewModel.itemPagedList.observe(getViewLifecycleOwner(), new Observer<PagedList<Item>>() {
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