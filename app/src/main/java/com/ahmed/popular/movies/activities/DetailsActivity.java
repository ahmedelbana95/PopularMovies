package com.ahmed.popular.movies.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.ahmed.popular.movies.fragments.DetailsFragment;
import com.ahmed.popular.movies.R;

public class DetailsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent i = getIntent();
        String id = i.getStringExtra("id");
        String title = i.getStringExtra("title");
        String date = i.getStringExtra("date");
        String overView = i.getStringExtra("overView");
        String path = i.getStringExtra("path");
        String vote = i.getStringExtra("vote");
        String backDrop = i.getStringExtra("backDrop");

        DetailsFragment detailsFragment = DetailsFragment.newInstance(id, path, overView, date, title, vote, backDrop);
        getFragmentManager().beginTransaction().replace(R.id.container2, detailsFragment).commit();


    }

}
