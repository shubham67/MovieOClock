package com.project.bittu.movieoclock.Activities;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.Spannable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.project.bittu.movieoclock.Adapters.ListMoviesAdapter;
import com.project.bittu.movieoclock.Fragments.DetailFragment;
import com.project.bittu.movieoclock.Fragments.DisplayFavoriteMoviesFragment;
import com.project.bittu.movieoclock.Fragments.DisplayPopularMoviesFragment;
import com.project.bittu.movieoclock.Fragments.DisplayTopRatedMoviesFragment;
import com.project.bittu.movieoclock.R;
import com.project.bittu.movieoclock.Utils.TypefaceSpan;


public class MainActivity extends AppCompatActivity implements ListMoviesAdapter.OnItemSelectedListener{


    boolean isLayoutTwoPane = false;
    int selection;

    Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.RIGHT);

        View view = LayoutInflater.from(this).inflate(R.layout.action_bar_spinner, null);
        spinner = (Spinner)view.findViewById(R.id.action_bar_spinner);
        if(savedInstanceState != null)
            spinner.setSelection(savedInstanceState.getInt("selection"));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selection = position;
                if ((position + 1) == DisplayPopularMoviesFragment.TYPE_MOST_POPULAR) {

                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    DisplayPopularMoviesFragment displayMoviesFragment = DisplayPopularMoviesFragment.fragmentInstance(DisplayPopularMoviesFragment.TYPE_MOST_POPULAR);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, displayMoviesFragment);
                    fragmentTransaction.commit();
                } else if((position + 1) == DisplayTopRatedMoviesFragment.TYPE_TOP_RATED){
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    DisplayTopRatedMoviesFragment displayMoviesFragment = DisplayTopRatedMoviesFragment.fragmentInstance(DisplayTopRatedMoviesFragment.TYPE_TOP_RATED);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, displayMoviesFragment);
                    fragmentTransaction.commit();
                }else{
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    DisplayFavoriteMoviesFragment displayMoviesFragment = DisplayFavoriteMoviesFragment.fragmentInstance(DisplayFavoriteMoviesFragment.TYPE_FAVORITES);

                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, displayMoviesFragment);
                    fragmentTransaction.commit();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        actionBar.setCustomView(view, lp);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        setContentView(R.layout.activity_main);
        if(findViewById(R.id.movie_detail_container) != null){
            isLayoutTwoPane = true;
        }

        SpannableString s = new SpannableString("MovieOClock");
        s.setSpan(new TypefaceSpan(this, "MyTypeface.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        actionBar.setTitle(s);







    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("selection", spinner.getSelectedItemPosition());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onItemSelected(long id) {


        if (!isLayoutTwoPane){
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("id", id);
            startActivity(intent);
        }else{
            Bundle arguments = new Bundle();
            arguments.putLong("id", id);
            FragmentManager fragmentManager = getSupportFragmentManager();
            DetailFragment detailFragment = new DetailFragment();

            detailFragment.setArguments(arguments);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.movie_detail_container, detailFragment);
            fragmentTransaction.commit();
        }
    }
}
