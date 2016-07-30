package com.project.bittu.movieoclock.Fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.bittu.movieoclock.Adapters.ListMoviesAdapter;
import com.project.bittu.movieoclock.DatabaseUtils.MoviesDBContract;
import com.project.bittu.movieoclock.DatabaseUtils.MoviesDBOpenHelper;
import com.project.bittu.movieoclock.Models.Movie;
import com.project.bittu.movieoclock.Network.VolleySingleton;
import com.project.bittu.movieoclock.R;

import java.util.ArrayList;

/**
 * Created by Bittu on 6/24/2016.
 */
public class DisplayFavoriteMoviesFragment extends Fragment{


    static ArrayList<Movie> movieDetails;

    static ListMoviesAdapter adapter;

    static SQLiteDatabase db;

    GridLayoutManager gm;

    private int type = 1;

    static RecyclerView moviesList;



    MoviesDBOpenHelper moviesDBOpenHelper;

    public final static int TYPE_MOST_POPULAR = 1;
    public final static int TYPE_TOP_RATED = 2;
    public final static int TYPE_FAVORITES = 3;



    private static DisplayFavoriteMoviesFragment fragment = null;

    public static DisplayFavoriteMoviesFragment fragmentInstance(int type){

        if(fragment == null) {
            fragment = new DisplayFavoriteMoviesFragment();
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


        Bundle b = getArguments();
        type = b.getInt("TYPE");

        moviesDBOpenHelper = new MoviesDBOpenHelper(getContext());
        db = moviesDBOpenHelper.getWritableDatabase();

        super.onCreate(savedInstanceState);
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



        moviesList.setLayoutManager(gm);

        adapter = new ListMoviesAdapter(getActivity(),movieDetails);

        moviesList.setItemAnimator(new DefaultItemAnimator());




        getData();


        return moviesList;
    }


    public static void getData(){
        movieDetails.clear();

        Cursor cursor = db.query(MoviesDBOpenHelper.FAV_MOVIES_TABLE, new String[]{"id", "title", "overview", "image_key", "backdrop_key"},
                null, null, null, null, null);


        long id;
        String title;
        String overview;
        int idIndex;
        int titleIndex;
        int overviewIndex;

        String imageURL;

        while(cursor.moveToNext()){

            idIndex = cursor.getColumnIndex("id");
            titleIndex = cursor.getColumnIndex("title");
            overviewIndex = cursor.getColumnIndex("overview");


            id = cursor.getLong(idIndex);
            title = cursor.getString(titleIndex);
            overview = cursor.getString(overviewIndex);
            imageURL = cursor.getString(cursor.getColumnIndex("image_key"));
            movieDetails.add(new Movie(id, title, imageURL, -1, null, overview, null, null, null, null, null, null, null));


        }
        adapter.notifyDataSetChanged();

        moviesList.setAdapter(adapter);


        cursor.close();
    }



}
