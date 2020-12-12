package com.example.testappan;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.example.testappan.MainActivity.albums;

public class ArtistFragment extends Fragment {
    RecyclerView rcView;
    AlbumAdapter albumAdapter;

    public ArtistFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);
        rcView = view.findViewById(R.id.rcView);
        rcView.setHasFixedSize(true);
        if(!(albums.size() < 1)){
            albumAdapter = new AlbumAdapter(getContext(),albums);
            rcView.setAdapter(albumAdapter);
            rcView.setLayoutManager(new GridLayoutManager(getContext(),2));
        }
        return view;
    }
}