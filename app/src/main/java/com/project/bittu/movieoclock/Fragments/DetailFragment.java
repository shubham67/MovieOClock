package com.project.bittu.movieoclock.Fragments;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.net.ConnectivityManagerCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.project.bittu.movieoclock.Adapters.ListMovieDetailsAdapter;
import com.project.bittu.movieoclock.BuildConfig;
import com.project.bittu.movieoclock.Constants.Constants;
import com.project.bittu.movieoclock.DatabaseUtils.MoviesDBContract;
import com.project.bittu.movieoclock.DatabaseUtils.MoviesDBOpenHelper;
import com.project.bittu.movieoclock.Models.Movie;
import com.project.bittu.movieoclock.Network.VolleySingleton;
import com.project.bittu.movieoclock.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Bittu on 6/21/2016.
 */
public class DetailFragment extends Fragment {

    RecyclerView recyclerView;
    ListMovieDetailsAdapter adapter;
    long id = -1;
    ShareActionProvider shareActionProvider;
    ArrayList<Movie> movieDetails = new ArrayList<>();


    VolleySingleton volleySingleton;
    RequestQueue requestQueue;
    ImageLoader imageLoader;
    NetworkImageView backdropImageView;

    FloatingActionButton fab;
    SQLiteDatabase db;
    MoviesDBOpenHelper moviesDBOpenHelper;

    private Intent shareIntent;
    private Cursor cursor;


    @Override
    public void onResume() {
        super.onResume();
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            fab.setClickable(true);
        }else{
            fab.setClickable(false);
            Snackbar.make(recyclerView, "Check Internet Connection...", Snackbar.LENGTH_LONG).show();

        }

        cursor = db.query(MoviesDBOpenHelper.FAV_MOVIES_TABLE, new String[]{"id", "title", "rating", "release_date", "overview",
                        "image_key", "backdrop_key"},
                MoviesDBContract.MovieDetails.ID + "=?",
                new String[]{getArguments().getLong("id") + ""}, null, null, null);

        if(cursor.getCount() > 0){

            fab.setClickable(true);

            cursor.moveToFirst();
            fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.heart));

            int titleIndex = cursor.getColumnIndex("title");
            int overviewIndex = cursor.getColumnIndex("overview");
            id = cursor.getLong(cursor.getColumnIndex("id"));
            String title = cursor.getString(titleIndex);

            float userRating = cursor.getFloat(cursor.getColumnIndex("rating"));
            String releaseDate = cursor.getString(cursor.getColumnIndex("release_date"));
            String overview = cursor.getString(overviewIndex);

            String imageURL = cursor.getString(cursor.getColumnIndex("image_key"));
            String backdropKey = cursor.getString(cursor.getColumnIndex("backdrop_key"));
            Uri builtUri = Uri.parse(Constants.IMAGE_BASE_URL).buildUpon()
                    .appendPath("w500")
                    .build();
            backdropImageView.setImageUrl(builtUri.toString() + backdropKey, imageLoader);


            ArrayList<String> trailerTitles = new ArrayList<String>();
            ArrayList<String> trailerKeys = new ArrayList<String>();
            ArrayList<String> trailerSites = new ArrayList<String>();
            ArrayList<String> trailerSizes = new ArrayList<String>();

            cursor = db.query(MoviesDBOpenHelper.TRAILERS_TABLE, new String[]{"key", "name", "size", "site"},
                    MoviesDBContract.Trailers.ID + "=?",
                    new String[]{getArguments().getLong("id") + ""}, null, null, null);

            while(cursor.moveToNext()){
                trailerTitles.add(cursor.getString(cursor.getColumnIndex("name")));
                trailerKeys.add(cursor.getString(cursor.getColumnIndex("key")));
                trailerSites.add(cursor.getString(cursor.getColumnIndex("site")));
                trailerSizes.add(cursor.getString(cursor.getColumnIndex("size")));


            }




            ArrayList<String> reviewsAuthor = new ArrayList<String>();
            ArrayList<String> reviews = new ArrayList<String>();

            cursor = db.query(MoviesDBOpenHelper.REVIEWS_TABLE, new String[]{"author", "content"},
                    MoviesDBContract.Reviews.ID + "=?",
                    new String[]{getArguments().getLong("id") + ""}, null, null, null);



            while(cursor.moveToNext()){
                reviewsAuthor.add(cursor.getString(cursor.getColumnIndex("author")));
                reviews.add(cursor.getString(cursor.getColumnIndex("content")));
            }
            boolean alreadyPresent = false;
            Movie movie = new Movie(id, title, imageURL, userRating, releaseDate, overview, null,
                    trailerTitles, trailerKeys, trailerSites, trailerSizes, reviewsAuthor, reviews);
            for(int j = 0; j < movieDetails.size(); j++){
                if(movieDetails.get(j).getId() == movie.getId()){
                    alreadyPresent = true;
                }

            }
            if(!alreadyPresent)
                movieDetails.add(movie);


            adapter.notifyDataSetChanged();


        }else {
            requestJsonData();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(cursor != null)
            cursor.close();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        volleySingleton = VolleySingleton.getInstance();
        requestQueue = volleySingleton.getRequestQueue();
        imageLoader = volleySingleton.getImageLoader();

        setHasOptionsMenu(true);
        moviesDBOpenHelper = new MoviesDBOpenHelper(getContext());
        db = moviesDBOpenHelper.getWritableDatabase();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_fragment_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.share);
        shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if(shareIntent != null)
            shareActionProvider.setShareIntent(shareIntent);



    }

    public void requestJsonData(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, getRequestURL(getArguments().getLong("id")), new Response.Listener<JSONObject>() {
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

        View view = inflater.inflate(R.layout.detail_layout, container, false);
        fab = (FloatingActionButton)view.findViewById(R.id.fab);
        backdropImageView = (NetworkImageView)view.findViewById(R.id.moviePoster);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cursor = db.query(MoviesDBOpenHelper.FAV_MOVIES_TABLE, new String[]{"id"}, MoviesDBContract.MovieDetails.ID + "=?",
                        new String[]{getArguments().getLong("id") + ""}, null, null, null);

                if (cursor.getCount() > 0){

                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.heart_nf));
                    db.delete(MoviesDBOpenHelper.FAV_MOVIES_TABLE, "id=?", new String[]{"" + getArguments().getLong("id")});
                    db.delete(MoviesDBOpenHelper.REVIEWS_TABLE, "id=?", new String[]{"" + getArguments().getLong("id")});
                    db.delete(MoviesDBOpenHelper.TRAILERS_TABLE, "id=?", new String[]{"" + getArguments().getLong("id")});




                }else {

                    fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.heart));
                    String requestURL = getRequestURL(getArguments().getLong("id"));
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, requestURL, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {


                            try{


                                String title;
                                float userRating;
                                String releaseDate;
                                String overview;
                                String imageURL;
                                String backdropKey;

                                title = response.getString("original_title");
                                userRating = (float)response.getDouble("vote_average");
                                releaseDate = response.getString("release_date");
                                overview = response.getString("overview");
                                imageURL = response.getString("poster_path");
                                backdropKey = response.getString("backdrop_path");

                                ContentValues favMovieValues = new ContentValues();
                                favMovieValues.put(MoviesDBContract.MovieDetails.ID, getArguments().getLong("id"));
                                favMovieValues.put(MoviesDBContract.MovieDetails.TITLE, title);
                                favMovieValues.put(MoviesDBContract.MovieDetails.OVERVIEW, overview);
                                favMovieValues.put(MoviesDBContract.MovieDetails.RATING, userRating);
                                favMovieValues.put(MoviesDBContract.MovieDetails.RELEASE_DATE, releaseDate);
                                favMovieValues.put(MoviesDBContract.MovieDetails.BACKDROP_KEY, backdropKey);
                                favMovieValues.put(MoviesDBContract.MovieDetails.IMAGE_KEY, imageURL);
                                db.insert(MoviesDBOpenHelper.FAV_MOVIES_TABLE, null, favMovieValues);






                                ArrayList<String> trailerTitles = new ArrayList<String>();
                                ArrayList<String> trailerKeys = new ArrayList<String>();
                                ArrayList<String> trailerSites = new ArrayList<String>();
                                ArrayList<String> trailerSizes = new ArrayList<String>();
                                ArrayList<String> reviewsAuthor = new ArrayList<String>();
                                ArrayList<String> reviews = new ArrayList<String>();

                                JSONObject trailersObject = response.getJSONObject("videos");
                                JSONArray trailersResultArray = trailersObject.getJSONArray("results");
                                for(int i = 0; i < trailersResultArray.length(); i++) {
                                    trailerTitles.add(trailersResultArray.getJSONObject(i).getString("name"));
                                    trailerKeys.add(trailersResultArray.getJSONObject(i).getString("key"));
                                    trailerSites.add(trailersResultArray.getJSONObject(i).getString("site"));
                                    trailerSizes.add(trailersResultArray.getJSONObject(i).getString("size"));
                                }


                                for(int i = 0; i < trailerKeys.size(); i++){
                                    ContentValues trailerValues = new ContentValues();
                                    trailerValues.put(MoviesDBContract.Trailers.ID, getArguments().getLong("id"));
                                    trailerValues.put(MoviesDBContract.Trailers.KEY_ID, trailerKeys.get(i));
                                    trailerValues.put(MoviesDBContract.Trailers.QUALITY, trailerSizes.get(i));
                                    trailerValues.put(MoviesDBContract.Trailers.SITE, trailerSites.get(i));
                                    trailerValues.put(MoviesDBContract.Trailers.TRAILER_NAME, trailerTitles.get(i));
                                    db.insert(MoviesDBOpenHelper.TRAILERS_TABLE, null, trailerValues);

                                }

                                JSONObject reviewsObject = response.getJSONObject("reviews");
                                JSONArray reviewsResultArray = reviewsObject.getJSONArray("results");
                                for(int i = 0; i < reviewsResultArray.length(); i++){
                                    reviewsAuthor.add(reviewsResultArray.getJSONObject(i).getString("author"));
                                    reviews.add(reviewsResultArray.getJSONObject(i).getString("content"));
                                }



                                for(int i = 0; i < reviewsAuthor.size(); i++){
                                    ContentValues reviewValues = new ContentValues();
                                    reviewValues.put(MoviesDBContract.Reviews.ID, getArguments().getLong("id"));
                                    reviewValues.put(MoviesDBContract.Reviews.REVIEW_AUTHOR, reviewsAuthor.get(i));
                                    reviewValues.put(MoviesDBContract.Reviews.REVIEW_CONTENT, reviews.get(i));
                                    db.insert(MoviesDBOpenHelper.REVIEWS_TABLE, null, reviewValues);

                                }










                            }catch (JSONException e){

                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    requestQueue.add(request);


                }

            }
        });


        recyclerView = (RecyclerView)view.findViewById(R.id.showContents);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new ListMovieDetailsAdapter(movieDetails, getActivity());
        recyclerView.setAdapter(adapter);
        return view;
    }




    public String getRequestURL(long id){

        final String MOVIE_PATH = "movie";

        final String API_KEY_PARAM = "api_key";

        final String TRAILER_AND_REVIEWS_PARAM = "append_to_response";

        Uri builtUri;

            builtUri = Uri.parse(Constants.BASE_URL).buildUpon()
                    .appendPath(MOVIE_PATH)
                    .appendPath(id + "")
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.THE_MOVIE_DB_API_KEY)
                    .appendQueryParameter(TRAILER_AND_REVIEWS_PARAM, "videos,reviews")
                    .build();


        return builtUri.toString();
    }

    public void getMovieDataFromJson(JSONObject response) {

        if(response == null || response.length() == 0){
            return;
        }
        try{

                


                String title = "NA";
                String imageURL = "NA";
                float userRating = -1f;
                String releaseDate = "NA";
                String overview = "NA";
                ArrayList<String> trailerURLs = new ArrayList<String>();
                ArrayList<String> trailerTitles = new ArrayList<String>();
                ArrayList<String> trailerKeys = new ArrayList<String>();
                ArrayList<String> trailerSites = new ArrayList<String>();
                ArrayList<String> trailerSizes = new ArrayList<String>();
                ArrayList<String> reviewsAuthor = new ArrayList<String>();
                ArrayList<String> reviews = new ArrayList<String>();

                JSONObject trailersObject = response.getJSONObject("videos");
                JSONArray trailersResultArray = trailersObject.getJSONArray("results");
                for(int i = 0; i < trailersResultArray.length(); i++){
                    trailerTitles.add(trailersResultArray.getJSONObject(i).getString("name"));
                    trailerKeys.add(trailersResultArray.getJSONObject(i).getString("key"));
                    trailerSites.add(trailersResultArray.getJSONObject(i).getString("site"));
                    trailerSizes.add(trailersResultArray.getJSONObject(i).getString("size"));
                    Uri builtUri = Uri.parse(Constants.BASE_YOUTUBE_URL).buildUpon()
                            .appendQueryParameter("v", trailerKeys.get(i))
                            .build();
                    trailerURLs.add(builtUri.toString());
                }


                JSONObject reviewsObject = response.getJSONObject("reviews");
                JSONArray reviewsResultArray = reviewsObject.getJSONArray("results");
                for(int i = 0; i < reviewsResultArray.length(); i++){
                    reviewsAuthor.add(reviewsResultArray.getJSONObject(i).getString("author"));
                    reviews.add(reviewsResultArray.getJSONObject(i).getString("content"));
                }

                title = response.getString("original_title");

                imageURL = response.getString("poster_path");


                Uri builtUri = Uri.parse(Constants.IMAGE_BASE_URL).buildUpon()
                    .appendPath("w500")
                    .build();
                backdropImageView.setImageUrl(builtUri.toString() + response.getString("backdrop_path"), imageLoader);

            cursor= db.query(MoviesDBOpenHelper.FAV_MOVIES_TABLE, new String[]{"id"}, MoviesDBContract.MovieDetails.ID + "=?",
                    new String[]{getArguments().getLong("id") + ""}, null, null, null);

            if(cursor.getCount() > 0){
                if(fab != null) {
                    try {
                        fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.heart));
                    }catch (Exception e){

                    }

                }

            }else {
                if(fab != null) {
                    try {
                        fab.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.heart_nf));
                    }catch(Exception e){

                    }


                }
            }
            cursor.close();



                userRating = (float)response.getDouble("vote_average");
                releaseDate = response.getString("release_date");
                overview = response.getString("overview");




                if((imageURL != null && title != null) && (imageURL != "NA" && title != "NA")){
                    boolean alreadyPresent = false;
                    Movie movie = new Movie(id, title, imageURL, userRating, releaseDate, overview, trailerURLs,
                            trailerTitles, trailerKeys, trailerSites, trailerSizes, reviewsAuthor, reviews);
                    for(int j = 0; j < movieDetails.size(); j++){
                        if(movieDetails.get(j).getId() == movie.getId()){
                            alreadyPresent = true;
                        }

                    }
                    if(!alreadyPresent)
                        movieDetails.add(movie);


                    adapter.notifyDataSetChanged();
                }

            shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "A movie suggestion for your weekend!");
            if(trailerKeys != null && trailerKeys.size() != 0)
                shareIntent.putExtra(Intent.EXTRA_TEXT, Constants.BASE_YOUTUBE_URL + trailerKeys.get(0));
            else
                shareIntent.putExtra(Intent.EXTRA_TEXT, title + "#MovieOClock");
            if(shareActionProvider != null)
                shareActionProvider.setShareIntent(shareIntent);







        }catch(JSONException e){

        }


        return;


    }
}
