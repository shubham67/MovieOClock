package com.project.bittu.movieoclock.Fragments;


import android.content.Context;

import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.project.bittu.movieoclock.Adapters.ListMoviesAdapter;
import com.project.bittu.movieoclock.BuildConfig;
import com.project.bittu.movieoclock.Constants.Constants;
import com.project.bittu.movieoclock.Models.Movie;
import com.project.bittu.movieoclock.Network.VolleySingleton;
import com.project.bittu.movieoclock.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bittu on 6/20/2016.
 */
public class DisplayPopularMoviesFragment extends Fragment{

    VolleySingleton volleySingleton;
    RequestQueue requestQueue;


    RecyclerView moviesList;
    ListMoviesAdapter adapter;
    List<String> imagePaths;
    static ArrayList<Movie> movieDetails;

    public final static int TYPE_MOST_POPULAR = 1;
    public final static int TYPE_TOP_RATED = 2;
    public final static int TYPE_FAVORITES = 3;



    private int type = 1;

    private int firstVisibleItemPosition, visibleItemCount, totalItemCount;
    private int previousTotal = 0;
    private boolean loading = true;
    private int visibleThreshold = 4;
    private int page = 1;

    GridLayoutManager gm;

    private static DisplayPopularMoviesFragment fragment = null;

    public static DisplayPopularMoviesFragment fragmentInstance(int type){

        if(fragment == null) {
            fragment = new DisplayPopularMoviesFragment();
            Bundle b = new Bundle();
            b.putInt("TYPE", type);
            fragment.setArguments(b);
            movieDetails = new ArrayList<Movie>();
        }

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setRetainInstance(true);
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();

        Bundle b = getArguments();
        type = b.getInt("TYPE");


        super.onCreate(savedInstanceState);
    }



    public String getRequestURL(){
        final String DISCOVER_PATH = "discover";
        final String MOVIE_PATH = "movie";
        final String SORT_PARAM = "sort_by";
        final String API_KEY_PARAM = "api_key";
        final String PAGE_PARAM = "page";
        final String CERTIFICATION_COUNTRY_PARAM = "certification_country";
        final String CERTIFICATION_PARAM = "certification";
        final String LANGUAGE_PARAM = "language";
        final String VOTE_COUNT_PARAM = "vote_count.gte";

        Uri builtUri;
        if(type == TYPE_MOST_POPULAR) {
            builtUri = Uri.parse(Constants.BASE_URL).buildUpon()
                    .appendPath(DISCOVER_PATH)
                    .appendPath(MOVIE_PATH)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .appendQueryParameter(SORT_PARAM, "popularity.desc")
                    .appendQueryParameter(PAGE_PARAM, page + "")
                    .appendQueryParameter(CERTIFICATION_COUNTRY_PARAM, "US")
                    .appendQueryParameter(LANGUAGE_PARAM, "en")
                    .build();
        }else{

            builtUri = Uri.parse(Constants.BASE_URL).buildUpon()
                    .appendPath(DISCOVER_PATH)
                    .appendPath(MOVIE_PATH)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .appendQueryParameter(SORT_PARAM, "vote_average.desc")
                    .appendQueryParameter(PAGE_PARAM, page + "")
                    .appendQueryParameter(CERTIFICATION_COUNTRY_PARAM, "US")
                    .appendQueryParameter(LANGUAGE_PARAM, "en")
                    .appendQueryParameter(VOTE_COUNT_PARAM, "250")
                    .appendQueryParameter(CERTIFICATION_PARAM, "R")
                    .build();
        }
        return builtUri.toString();
    }

    public void requestJsonData(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getRequestURL(), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                getMovieDataFromJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(request);
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        moviesList = (RecyclerView)inflater.inflate(R.layout.display_popular_movies_fragment, container, false);



            gm = new GridLayoutManager(getActivity(), 3);
            gm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if ((position + 1) % 4 == 0) {
                        return 3;
                    } else {
                        return 1;
                    }

                }
            });



            imagePaths = new ArrayList<String>();

        moviesList.setLayoutManager(gm);

            adapter = new ListMoviesAdapter(getActivity(),movieDetails);

        moviesList.setItemAnimator(new DefaultItemAnimator());

        moviesList.setAdapter(adapter);



        requestJsonData();
        moviesList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleItemCount = moviesList.getChildCount();
                totalItemCount = gm.getItemCount();
                firstVisibleItemPosition = gm.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;

                        previousTotal = totalItemCount;
                        page++;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItemPosition + visibleThreshold)) {

                    requestJsonData();
                    Snackbar.make(moviesList, "Loading...", Snackbar.LENGTH_SHORT).show();

                    loading = true;
                }
            }
        });

        return moviesList;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }


    public void getMovieDataFromJson(JSONObject response) {

        if(response == null || response.length() == 0){
            return;
        }
        try{

            JSONArray resultsArray = response.getJSONArray("results");
            for(int i = 0; i < resultsArray.length(); i++) {
                long id = -1;
                String title = "NA";
                String imageURL = "NA";
                float userRating = -1f;
                String releaseDate = "NA";
                String overview = "NA";
                JSONObject movieItem = resultsArray.getJSONObject(i);
                id = movieItem.getLong("id");
                if(movieItem.has("original_title"))
                    title = movieItem.getString("original_title");
                else
                    continue;
                if(movieItem.has("poster_path"))
                    imageURL = movieItem.getString("poster_path");
                else
                    continue;
                userRating = (float) movieItem.getDouble("vote_average");
                releaseDate = movieItem.getString("release_date");
                overview = movieItem.getString("overview");
                if((imageURL != null && title != null) && (imageURL != "NA" && title != "NA") && id != -1) {
                    boolean alreadyPresent = false;
                    Movie movie = new Movie(id, title, imageURL, userRating, releaseDate, overview, null, null, null, null, null, null, null);
                    for(int j = 0; j < movieDetails.size(); j++){
                        if(movieDetails.get(j).getId() == movie.getId()){
                            alreadyPresent = true;
                        }

                    }
                    if(!alreadyPresent)
                        movieDetails.add(movie);


                }

            }
            adapter.notifyDataSetChanged();



        }catch(JSONException e){

        }


        return;


    }


}
