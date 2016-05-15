package com.ahmed.popular.movies.fragments;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.ahmed.popular.movies.R;
import com.ahmed.popular.movies.models.Review;
import com.ahmed.popular.movies.adapters.ReviewAdapter;
import com.ahmed.popular.movies.models.Trailer;
import com.ahmed.popular.movies.adapters.TrailerAdapter;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 */
public class DetailsFragment extends Fragment {
    TextView txtTitle, txtDate, txtVote, txtOverView, duration;
    ImageView poster;
    Button favorite;
    ListView trailers, reviews;
    private List<Trailer> trailer;
    private List<Review> review;

    String id, title, date, overView, path, vote;
    String data;
    String backDrop;
    SQLiteDatabase db;
    Context context;

    public DetailsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);

        Bundle i = getArguments();

        id = i.getString("id");
        title = i.getString("title");
        date = i.getString("date");
        overView = i.getString("overView");
        path = i.getString("path");
        vote = i.getString("vote");
        backDrop = i.getString("backDrop");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        favorite = (Button) view.findViewById(R.id.as_favorite);
        duration = (TextView) view.findViewById(R.id.duration);
        txtTitle = (TextView) view.findViewById(R.id.title);
        txtDate = (TextView) view.findViewById(R.id.release_date);
        txtVote = (TextView) view.findViewById(R.id.vote);
        txtOverView = (TextView) view.findViewById(R.id.plot_synopsis);
        poster = (ImageView) view.findViewById(R.id.poster);
        reviews = (ListView) view.findViewById(R.id.reviews_list);
        trailers = (ListView) view.findViewById(R.id.trailer_list);
        context = getActivity();
        return view;
    }

    public static DetailsFragment newInstance(String id, String path, String overView, String date, String title, String vote, String backDrop) {
        DetailsFragment detailFrag = new DetailsFragment();
        Bundle i = new Bundle();
        i.putString("id", id);
        i.putString("path", path);
        i.putString("overView", overView);
        i.putString("date", date);
        i.putString("title", title);
        i.putString("vote", vote);
        i.putString("backDrop", backDrop);
        detailFrag.setArguments(i);
        return detailFrag;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        txtTitle.setText(title);
        txtDate.setText(date);
        txtOverView.setText(overView);
        txtVote.setText(vote);
        Picasso.with(getActivity())
                .load("https://image.tmdb.org/t/p/w185" + path)

                .error(R.drawable.noprofile)
                .into(poster);


        if (!isNetworkAvailable()) {

        } else {
            getDuration();
            getTrailer();
            getReviews();

        }
        MovieBD();

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }

    private void MovieBD() {
        db = getActivity().openOrCreateDatabase("Movies", android.content.Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS `Movi` (`id` varchar(50) UNIQUE NOT NULL ,`title` varchar(50)   NOT NULL,`date` varchar(50)   NOT NULL," +
                "`overView` TEXT   NOT NULL,`path` varchar(250)   NOT NULL ,`vote` varchar(50)   NOT NULL , `backDrop`varchar(250)   NOT NULL)");

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = db.rawQuery("select 1 from Movi where id=?",
                        new String[]{id});
                boolean exists = (cursor.getCount() > 0);

                if (exists == true) {

                    Toast.makeText(getActivity(), "تمت اضافته من قبل ", Toast.LENGTH_LONG).show();
                } else if (overView.toString().contains("'")) {
                    overView = overView.replace("'", "\\");
                    db.execSQL("insert into Movi (id,title,date,overView,path,vote,backDrop) Values ('" + id + "','" + title + "','" + date + "','" + overView + "','" + path + "','" + vote + "','" + backDrop + "')");

                    Toast.makeText(getActivity(), "تم الاضافه بنجاح", Toast.LENGTH_LONG).show();
                } else {
                    db.execSQL("insert into Movi (id,title,date,overView,path,vote,backDrop) Values ('" + id + "','" + title + "','" + date + "','" + overView + "','" + path + "','" + vote + "','" + backDrop + "')");
                    Toast.makeText(getActivity(), "تمت الاضافه بنجاح", Toast.LENGTH_LONG).show();
                }
                cursor.close();
            }
        });

    }


    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, AbsoluteLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    public void getTrailer() {
        StringRequest allTrailer;
        String trilerUrl = "http://api.themoviedb.org/3/movie/" + id + "/videos?api_key=bdc4dc5612b3be2a258f2f8c900a9b65";
        allTrailer = new StringRequest(Request.Method.GET, trilerUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject results = null;
                if (response != null) {

                    try {
                        results = new JSONObject(response);
                        JSONArray data = results.getJSONArray("results");

                        int dataSize = data.length();


                        trailer = new ArrayList<>(dataSize);


                        for (int i = 0; i < dataSize; i++) {
                            JSONObject jsonTrailer = data.getJSONObject(i);

                            Trailer trl = new Trailer();


                            if (jsonTrailer != null) {
                                trl.setName(jsonTrailer.getString("name"));
                                trl.setKey(jsonTrailer.getString("key"));


                                trailer.add(trl);
                            } else {
                                Toast.makeText(getActivity(), "No Data", Toast.LENGTH_LONG).show();
                                return;
                            }

                        }


                        List<String> names = new ArrayList<String>();
                        final List<String> keys = new ArrayList<String>();

                        for (int i = 0; i < trailer.size(); i++) {
                            names.add(trailer.get(i).getName().toString());
                            keys.add(trailer.get(i).getKey().toString());
                        }


                        trailers.setAdapter(new TrailerAdapter(getActivity(), backDrop, names));

                        setListViewHeightBasedOnChildren(trailers);

                        trailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String trailerKey = keys.get(position).toString();

                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailerKey)));


                            }
                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

            }


        });
        Volley.newRequestQueue(getActivity()).add(allTrailer);
    }

    public void getReviews() {

        String ReviewsUrl = "http://api.themoviedb.org/3/movie/" + id + "/reviews?api_key=bdc4dc5612b3be2a258f2f8c900a9b65";
        StringRequest allRevews = new StringRequest(Request.Method.GET, ReviewsUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject results = null;
                if (response != null) {

                    try {
                        results = new JSONObject(response);
                        JSONArray data = results.getJSONArray("results");

                        int dataSize = data.length();


                        review = new ArrayList<>(dataSize);


                        for (int i = 0; i < dataSize; i++) {
                            JSONObject jsonReview = data.getJSONObject(i);

                            Review rev = new Review();


                            if (jsonReview != null) {
                                rev.setAuthor(jsonReview.getString("author"));
                                rev.setContent(jsonReview.getString("content"));


                                review.add(rev);
                            } else {
                                Toast.makeText(getActivity(), "No Data", Toast.LENGTH_LONG).show();
                                return;
                            }

                        }

                        List<String> author = new ArrayList<String>();
                        List<String> content = new ArrayList<String>();

                        for (int i = 0; i < review.size(); i++) {
                            author.add(review.get(i).getAuthor().toString());
                            content.add(review.get(i).getContent().toString());
                        }


                        reviews.setAdapter(new ReviewAdapter(getActivity(), author, content));

                        setListViewHeightBasedOnChildren(reviews);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

            }


        });
        Volley.newRequestQueue(getActivity()).add(allRevews);


    }

    public void getDuration() {

        String durationUrl = "http://api.themoviedb.org/3/movie/" + id + "?&api_key=bdc4dc5612b3be2a258f2f8c900a9b65";
        StringRequest durationStringRequest = new StringRequest(Request.Method.GET, durationUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject results = null;
                if (response != null) {

                    try {
                        results = new JSONObject(response);


                        if (results != null) {


                            duration.setText(results.getString("runtime") + "min");


                        } else {
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_LONG).show();
                            return;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

            }


        });
        Volley.newRequestQueue(getActivity()).add(durationStringRequest);


    }


}
