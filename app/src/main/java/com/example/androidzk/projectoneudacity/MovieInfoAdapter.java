package com.example.androidzk.projectoneudacity;

import android.app.Activity;
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
public class MovieInfoAdapter extends ArrayAdapter<BasicMovieInfo> {

    public MovieInfoAdapter(Activity context, List<BasicMovieInfo> baseMovieInfos) {
        super(context, 0, baseMovieInfos);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BasicMovieInfo movieInfo = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_grid_item, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.movie_image);
        Picasso.with(getContext()).load(movieInfo.getMoviePosterAddress()).into(iconView);
        iconView.setTag(movieInfo.getMovieID());
        return convertView;
    }
}
