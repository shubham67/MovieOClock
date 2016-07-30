package com.project.bittu.movieoclock.DatabaseUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bittu on 6/23/2016.
 */
public class MoviesDBOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movieoclock.db";
    private static final int DATABASE_VERSION = 3;
    public static final String FAV_MOVIES_TABLE = "FavMovies";
    public static final String REVIEWS_TABLE = "Reviews";
    public static final String TRAILERS_TABLE = "Trailers";

    private static final String CREATE_FAV_MOVIES_TABLE = "create table " + FAV_MOVIES_TABLE + " ("
            + MoviesDBContract.MovieDetails.ID + " integer primary key, " + MoviesDBContract.MovieDetails.TITLE + " text not null, "
            + MoviesDBContract.MovieDetails.RATING + " float, " + MoviesDBContract.MovieDetails.RELEASE_DATE + " text, "
            + MoviesDBContract.MovieDetails.BACKDROP_KEY + " text, " + MoviesDBContract.MovieDetails.IMAGE_KEY + " text, "
            + MoviesDBContract.MovieDetails.OVERVIEW + " text);";

    private static final String CREATE_REVIEWS_TABLE = "create table " + REVIEWS_TABLE + " ("
            + MoviesDBContract.Reviews.ID + " integer, " + MoviesDBContract.Reviews.REVIEW_AUTHOR + " text, "
            + MoviesDBContract.Reviews.REVIEW_CONTENT + " text);";

    private static final String CREATE_TRAILERS_TABLE = "create table " + TRAILERS_TABLE + " ("
            + MoviesDBContract.Trailers.ID + " integer, " + MoviesDBContract.Trailers.KEY_ID + " text, "
            + MoviesDBContract.Trailers.TRAILER_NAME + " text, " + MoviesDBContract.Trailers.QUALITY + " text, "
            + MoviesDBContract.Trailers.SITE + " text);";



    public MoviesDBOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FAV_MOVIES_TABLE);
        db.execSQL(CREATE_REVIEWS_TABLE);
        db.execSQL(CREATE_TRAILERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FAV_MOVIES_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + REVIEWS_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TRAILERS_TABLE);
        onCreate(db);
    }
}
