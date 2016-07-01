package com.example.androidzk.projectoneudacity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        long movieID = getIntent().getLongExtra(DetailFragment.MOVIE_ID, 0);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.detail_container, DetailFragment.newInstance(movieID))
            .commit();
        }
    }
}
