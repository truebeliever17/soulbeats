package com.example.myapplication.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.adapters.AlbumsAdapter;
import com.example.myapplication.models.Album;
import com.example.myapplication.models.AlbumMainInfo;
import com.example.myapplication.models.AlbumRequest;
import com.example.myapplication.network.NetworkService;
import com.example.myapplication.network.SessionManager;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {
    private boolean isDetached;
    private static List<AlbumMainInfo> albums;
    private Context context;
    private RecyclerView albumsRecycler;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        context = view.getContext();
        albumsRecycler = view.findViewById(R.id.albums_rv);
        if (albums == null) {
            fetchAlbums();
        } else {
            configureAlbumsRecycler();
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.isDetached = true;
    }

    public void fetchAlbums() {
        NetworkService
            .getInstance()
            .getJSONApi()
            .getAlbumsWithMainInfo(new AlbumRequest(true))
            .enqueue(new Callback<List<AlbumMainInfo>>() {
                @Override
                public void onResponse(Call<List<AlbumMainInfo>> call, Response<List<AlbumMainInfo>> response) {
                    if (isDetached) return;
                    albums = response.body();
                    configureAlbumsRecycler();
                }

                @Override
                public void onFailure(Call<List<AlbumMainInfo>> call, Throwable t) {
                    if (isDetached) return;
                    Log.d("ERROR", Objects.requireNonNull(t.getMessage()));
                }
            });
    }

    private void configureAlbumsRecycler() {
        if (albums == null || albums.size() == 0) return;
        AlbumsAdapter albumsAdapter = new AlbumsAdapter(albums);
        albumsRecycler.setAdapter(albumsAdapter);
        albumsRecycler.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        albumsRecycler.setHasFixedSize(true);
    }
}
