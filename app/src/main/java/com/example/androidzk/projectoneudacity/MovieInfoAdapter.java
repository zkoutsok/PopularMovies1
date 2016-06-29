package com.example.androidzk.projectoneudacity;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by androidZK on 6/27/2016.
 */
public class MovieInfoAdapter extends ArrayAdapter<BaseMovieInfo> {

    public MovieInfoAdapter(Activity context, List<BaseMovieInfo> baseMovieInfos) {
        super(context, 0, baseMovieInfos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseMovieInfo movieInfo = getItem(position);

        // Adapters recycle views to AdapterViews.
        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_grid_item, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.movie_image);
        Picasso.with(getContext()).load(movieInfo.getMoviePosterAddress()).into(iconView);
        iconView.setTag(movieInfo.getMovieTitle());
        return convertView;
    }
}
