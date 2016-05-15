package com.ahmed.popular.movies.activities;

import android.app.Fragment;
import android.app.FragmentTransaction;
 import android.content.Intent;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import com.ahmed.popular.movies.fragments.DetailsFragment;
import com.ahmed.popular.movies.fragments.PopularMoviesFragment;
import com.ahmed.popular.movies.R;

public class PopularMovies extends AppCompatActivity implements PopularMoviesFragment.CallBack {
    boolean m2pane;
    private FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popular_movies);
        frame = (FrameLayout) findViewById(R.id.container2);
        Fragment newFragment = new PopularMoviesFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.container, newFragment).commit();


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (frame != null) {
            m2pane = true;
        } else {
            m2pane = false;
        }
        Log.i("frame", frame == null ? "null" : "not null");
    }

    @Override
    public void call(String id, String path, String overView, String date, String title, String vote, String backDrop) {
        if (m2pane) {
            DetailsFragment newFragment = DetailsFragment.newInstance(id, path, overView, date, title, vote, backDrop);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container2, newFragment).commit();

        } else {
            Intent i = new Intent(PopularMovies.this, DetailsActivity.class);
            // passing array index
            i.putExtra("id", id);
            i.putExtra("path", path);
            i.putExtra("overView", overView);
            i.putExtra("date", date);
            i.putExtra("title", title);
            i.putExtra("vote", vote);
            i.putExtra("backDrop", backDrop);
            startActivity(i);

        }
    }


}
