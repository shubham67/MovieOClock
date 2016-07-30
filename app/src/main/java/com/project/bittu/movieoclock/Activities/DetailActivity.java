package com.project.bittu.movieoclock.Activities;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;

import com.project.bittu.movieoclock.Fragments.DetailFragment;
import com.project.bittu.movieoclock.R;
import com.project.bittu.movieoclock.Utils.TypefaceSpan;

/**
 * Created by Bittu on 6/21/2016.
 */
public class DetailActivity extends AppCompatActivity{


    public long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity_layout);

        ActionBar actionBar = getSupportActionBar();
        SpannableString s = new SpannableString("MovieOClock");
        s.setSpan(new TypefaceSpan(this, "MyTypeface.otf"), 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        actionBar.setTitle(s);
        id = getIntent().getLongExtra("id", -1);
        Bundle arguments = new Bundle();
        arguments.putLong("id", getIntent().getLongExtra("id", -1));
        FragmentManager fragmentManager = getSupportFragmentManager();
        DetailFragment detailFragment = new DetailFragment();
        detailFragment.setArguments(arguments);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.movie_detail_container, detailFragment);
        fragmentTransaction.commit();


    }

}
