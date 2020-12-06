package com.example.myapplication.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.models.Album;
import com.example.myapplication.models.AlbumMainInfo;

import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {
    List<AlbumMainInfo> albums;

    public AlbumsAdapter(List<AlbumMainInfo> albums) {
        this.albums = albums;
    }

    @NonNull
    @Override
    public AlbumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        return new AlbumViewHolder(inflater.inflate(R.layout.album_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    class AlbumViewHolder extends RecyclerView.ViewHolder {
        TextView albumNameView;
        TextView artistNameView;

        public AlbumViewHolder(@NonNull View itemView) {
            super(itemView);
            albumNameView = itemView.findViewById(R.id.album_name_tv);
            artistNameView = itemView.findViewById(R.id.artist_name_tv);
        }

        void bind(int position) {
            AlbumMainInfo album = albums.get(position);
            albumNameView.setText(album.getAlbumName());
            artistNameView.setText(album.getArtistName());
        }
    }
}
