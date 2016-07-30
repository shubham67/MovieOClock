package com.project.bittu.movieoclock.DatabaseUtils;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Bittu on 6/23/2016.
 */
public class MoviesDBContract {

    public static class MovieDetails{

        public static final String ID = "id";
        public static final String TITLE = "title";
        public static final String RATING = "rating";
        public static final String RELEASE_DATE = "release_date";
        public static final String OVERVIEW = "overview";
        public static final String IMAGE_KEY = "image_key";
        public static final String BACKDROP_KEY = "backdrop_key";

    }

    public static class Reviews{

        public static final String ID = "id";
        public static final String REVIEW_AUTHOR = "author";
        public static final String REVIEW_CONTENT = "content";

    }

    public static class Trailers{

        public static final String ID = "id";
        public static final String KEY_ID = "key";
        public static final String TRAILER_NAME = "name";
        public static final String QUALITY = "size";
        public static final String SITE = "site";

    }



}
