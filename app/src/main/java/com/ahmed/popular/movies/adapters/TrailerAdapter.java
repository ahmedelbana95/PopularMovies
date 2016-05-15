package com.ahmed.popular.movies.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ahmed.popular.movies.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ahmedpc on 28/12/2015.
 */
public class TrailerAdapter  extends ArrayAdapter<String> {
    private final Activity context;
    private final List<String> trailer;
   String path ;

    public TrailerAdapter(Activity context,
                       String path,  List<String> trailer) {
        super(context, R.layout.custom_trailer, trailer);
        this.context = context;
        this.trailer = trailer;
        this.path=path;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.custom_trailer, null, true);
        TextView trai = (TextView) rowView.findViewById(R.id.txt);
        ImageView img = (ImageView) rowView.findViewById(R.id.img);

        trai.setText(trailer.get(position).toString()+" :  ");
        img.setImageResource(R.drawable.youtube);
//        Picasso.with(context)
//                .load("https://image.tmdb.org/t/p/w185" + path)
//                        //     .transform(new RoundedTransformation(20, 5))
//                .error(R.drawable.noprofile)
//                .into(img);
//        // imageView.setImageResource(mThumbIds[position]);
//        img.setScaleType(ImageView.ScaleType.CENTER_CROP);
        return rowView;
    }
}