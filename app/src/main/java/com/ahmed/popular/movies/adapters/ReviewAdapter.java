package com.ahmed.popular.movies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
 import android.widget.TextView;
import android.app.Activity;

import android.widget.ArrayAdapter;

import com.ahmed.popular.movies.R;

import java.util.List;


/**
 * Created by ahmedpc on 28/12/2015.
 */
public class ReviewAdapter extends ArrayAdapter<String>{
    private final Activity context;
    private final List<String> author;
    private final List<String> content;

    public ReviewAdapter(Activity context,
                         List<String> author, List<String> content) {
        super(context, R.layout.custom_review, author);
        this.context = context;
        this.author = author;
        this.content = content;

    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.custom_review, null, true);
        TextView txtAuthor = (TextView) rowView.findViewById(R.id.author);
        TextView txtContent = (TextView) rowView.findViewById(R.id.content);

        txtAuthor.setText(author.get(position).toString()+" :  ");
        txtContent.setText(content.get(position).toString());
        return rowView;
    }
}