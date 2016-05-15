package com.ahmed.popular.movies.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.ahmed.popular.movies.activities.DetailsActivity;
import com.ahmed.popular.movies.activities.PopularMovies;
 import com.ahmed.popular.movies.models.Film;
import com.ahmed.popular.movies.adapters.FilmsAdapter;
 import com.ahmed.popular.movies.R;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class PopularMoviesFragment extends Fragment {

    String popularURL;
    String rateURL;
    StringRequest popularRequest;


    private List<Film> films;
    Context context;
    GridView gridView;
    SQLiteDatabase db;
    Cursor movies;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        gridView = (GridView) view.findViewById(R.id.grid_view);

        popularURL = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=bdc4dc5612b3be2a258f2f8c900a9b65";
        rateURL = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&api_key=bdc4dc5612b3be2a258f2f8c900a9b65";
        context = getActivity();
        return view;

   }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        movieDetails(popularURL);


        // Instance of ImageAdapter Class


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Film film = films.get(position);
                PopularMovies pop = (PopularMovies) getActivity();
                pop.call(film.getId(), film.getPosterPath(), film.getOverview(), film.getRelease_date(), film.getTitle(), film.getVote_average(), film.getBackdrop_path());


            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.sort_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menu_sort_popularity) {
            if (!isNetworkAvailable()) {
                Toast.makeText(getActivity() ,"No Connection",Toast.LENGTH_LONG).show();
            }
            else {

                movieDetails(popularURL);

            }



            return true;
        }

        if (id == R.id.menu_sort_rating) {
            if (!isNetworkAvailable()) {
                Toast.makeText(getActivity() ,"No Connection",Toast.LENGTH_LONG).show();
            }
            else {

                movieDetails(rateURL);

            }



            return true;
        }
        if (id == R.id.menu_sort_favorite) {
            db = getActivity().openOrCreateDatabase("Movies", android.content.Context.MODE_PRIVATE , null);
            movies = db.rawQuery("select * from Movi", null);
            films = new ArrayList<>();

            while (movies.moveToNext()) {
                Film film = new Film();
                film.setId(movies.getString(0));
                film.setBackdrop_path(movies.getString(6));
                film.setTitle(movies.getString(1));
                film.setPosterPath(movies.getString(4));


                film.setOverview(movies.getString(3));
                film.setRelease_date(movies.getString(2));
                film.setVote_average(movies.getString(5));

                films.add(film);

            }
            gridView.setAdapter(new FilmsAdapter(context, films));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void movieDetails(String url) {

        popularRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject results = new JSONObject(response);
                    JSONArray data = results.getJSONArray("results");
                    int dataSize = data.length();
                    films = new ArrayList<>(dataSize);
                    for (int i = 0; i < dataSize; i++) {
                        JSONObject jsonFilm = data.getJSONObject(i);

                        Film film = new Film();


                        if (jsonFilm != null) {
                            film.setId(jsonFilm.getString("id"));
                            film.setTitle(jsonFilm.getString("title"));
                            film.setPosterPath(jsonFilm.getString("poster_path"));


                            film.setOverview(jsonFilm.getString("overview"));
                            film.setRelease_date(jsonFilm.getString("release_date"));
                            film.setVote_average(jsonFilm.getString("vote_average"));

                            films.add(film);
                        }

                        else {
                            Toast.makeText(getActivity(), "No Data", Toast.LENGTH_LONG).show();
                            return;
                        }
                    }

                    gridView.setAdapter(new FilmsAdapter(getActivity(), films));



                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();

            }


        });
        Volley.newRequestQueue(getActivity()).add(popularRequest);
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }
    public interface CallBack{

        public void  call(String id , String path ,String overView ,String date ,String title ,String vote ,String backDrop);
    }

}
