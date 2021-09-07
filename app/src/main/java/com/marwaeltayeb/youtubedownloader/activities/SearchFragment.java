package com.marwaeltayeb.youtubedownloader.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.marwaeltayeb.youtubedownloader.R;

import static com.marwaeltayeb.youtubedownloader.Utility.Constant.KEYWORD;


public class SearchFragment extends Fragment {

    private EditText searchEditText;
    private Button btn_search;
    private  NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        searchEditText = view.findViewById(R.id.searchEditText);
        btn_search = view.findViewById(R.id.btn_search);

        btn_search.setOnClickListener(v -> {
            String keyword = searchEditText.getText().toString().trim();

            if(!TextUtils.isEmpty(keyword)){

                Fragment fragment = new Fragment();
                Bundle bundle = new Bundle();
                bundle.putString(KEYWORD, keyword);
                fragment.setArguments(bundle);

                navController.navigate(R.id.action_searchFragment_to_playlistFragment, bundle);
            }
        });
    }


}