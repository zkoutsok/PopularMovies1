package com.example.androidzk.projectoneudacity;

import android.content.Intent;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String stringFilter = PreferenceManager.
                getDefaultSharedPreferences(getApplicationContext()).
                getString("preference_key", "POPULAR");
        MovieFactory.MoviesFilter filter = null;
        Resources res = getResources();
        String arrVal[] = res.getStringArray(R.array.filter_default_values);
        for (int i = 0; i < arrVal.length; i++) {
            if (stringFilter.equals(arrVal[i])) {
                filter = MovieFactory.MoviesFilter.valueOf(arrVal[i]);
                break;
            }
        }
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, GridFragment.newInstance(filter))
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
}