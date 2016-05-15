package com.ahmed.popular.movies.adapters;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.ahmed.popular.movies.models.Film;
import com.ahmed.popular.movies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Create the basic adapter extending from RecyclerView.Adapter
 * note that we specify the custom ViewHolder which gives us access to our
 */
public class FilmsAdapter extends BaseAdapter {

    private List<Film> films;
    private Context context;
    private static LayoutInflater inflater = null;

    public FilmsAdapter(Context ctx, List<Film> filmsArray) {
        context = ctx;
        films = filmsArray;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return films.size();
    }

    @Override
    public Object getItem(int position) {
        return films.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Holder {

        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.gridview, null);

        holder.img = (ImageView) rowView.findViewById(R.id.grid_view_cell);

        Film film = films.get(position);


        Picasso.with(context)
                .load("https://image.tmdb.org/t/p/w185" + film.getPosterPath())

                .error(R.drawable.noprofile)
                .into(holder.img);


        holder.img.setPadding(5, 5, 5, 5);
        return holder.img;


    }
}
