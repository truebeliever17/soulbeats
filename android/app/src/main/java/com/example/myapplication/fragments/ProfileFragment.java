package com.example.myapplication.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.activities.LoginActivity;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.adapters.AlbumsAdapter;
import com.example.myapplication.models.AlbumMainInfo;
import com.example.myapplication.models.AlbumRequest;
import com.example.myapplication.models.User;
import com.example.myapplication.network.NetworkService;
import com.example.myapplication.network.SessionManager;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private boolean isDetached;
    private static List<AlbumMainInfo> albums;
    private static User user;
    private Context context;
    private RecyclerView albumsRecycler;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        context = view.getContext();
        albumsRecycler = view.findViewById(R.id.albums_rv);

        if (user == null) {
            fetchUser(view);
        } else {
            setUserDetails(view, user);
        }

        createOnLogoutListener(view);

        if (albums == null) {
            fetchAlbums();
        } else {
            configureAlbumsRecycler();
        }
        return view;
    }

    private void createOnLogoutListener(View view) {
        Button button = view.findViewById(R.id.logout_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager.removeToken(context);
                makeIntent(LoginActivity.class);
            }
        });
    }

    private void makeIntent(Class<?> destinationClass) {
        Intent intent = new Intent(context, destinationClass);
        startActivity(intent);
        getActivity().finishAffinity();
    }

    private void fetchUser(View view) {
        NetworkService.getInstance().getJSONApi().getUserByToken(SessionManager.fetchToken(context)).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (isDetached) return;
                    assert user != null;
                    setUserDetails(view, user);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                if (isDetached) return;
                Log.d("ERROR", Objects.requireNonNull(t.getMessage()));
            }
        });
    }
    private void setUserDetails(View view, User user) {
        TextView nameView = view.findViewById(R.id.fullname_tv);
        nameView.setText(user.getDisplayName());
    }

    private void fetchAlbums() {
        NetworkService
            .getInstance()
            .getJSONApi()
            .getUserAlbumsByToken(SessionManager.fetchToken(context))
            .enqueue(new Callback<List<AlbumMainInfo>>() {
                @Override
                public void onResponse(Call<List<AlbumMainInfo>> call, Response<List<AlbumMainInfo>> response) {
                    albums = response.body();
                    if (isDetached) return;
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

    @Override
    public void onDetach() {
        super.onDetach();
        this.isDetached = true;
    }

}
