package com.project.bittu.movieoclock.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.balysv.materialripple.MaterialRippleLayout;
import com.project.bittu.movieoclock.Constants.Constants;
import com.project.bittu.movieoclock.Models.Movie;
import com.project.bittu.movieoclock.Network.VolleySingleton;
import com.project.bittu.movieoclock.R;

import java.util.ArrayList;

/**
 * Created by Bittu on 6/21/2016.
 */
public class ListMovieDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    ArrayList<Movie> movieDetails = new ArrayList<>();
    VolleySingleton volleySingleton = VolleySingleton.getInstance();
    ImageLoader imageLoader = volleySingleton.getImageLoader();

    final static int TYPE_INTRO = 1;
    final static int TYPE_TRAILERS_FIRST_ROW = 2;
    final static int TYPE_TRAILERS = 3;
    final static int TYPE_REVIEWS_FIRST_ROW = 4;
    final static int TYPE_REVIEWS = 5;

   Activity activity;

    public ListMovieDetailsAdapter(ArrayList<Movie> movieDetails, Activity activity) {
        this.movieDetails = movieDetails;
        this.activity = activity;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_INTRO) {
            view = layoutInflater.inflate(R.layout.detail_content_layout, parent, false);
            holder = new ListMovieDetailsViewHolder(view);

        }else if(viewType == TYPE_TRAILERS_FIRST_ROW) {
            view = layoutInflater.inflate(R.layout.first_trailer_layout, parent, false);
            holder = new ListMovieTrailersViewHolder(view);

        }else if(viewType == TYPE_TRAILERS) {
            view = layoutInflater.inflate(R.layout.trailer_layout, parent, false);
            holder = new ListMovieTrailersViewHolder(view);

        }else if(viewType == TYPE_REVIEWS_FIRST_ROW){
            view = layoutInflater.inflate(R.layout.first_review_layout, parent, false);
            holder = new ListMovieReviewsViewHolder(view);

        }else{
            view = layoutInflater.inflate(R.layout.review_layout, parent, false);
            holder = new ListMovieReviewsViewHolder(view);

        }

        return holder;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        if(position == 0) {

            Uri builtUri = Uri.parse(Constants.IMAGE_BASE_URL).buildUpon()
                    .appendPath("w342")
                    .build();
            ((ListMovieDetailsViewHolder)holder).movieName.setText(movieDetails.get(position).getTitle());
            ((ListMovieDetailsViewHolder)holder).movieRating.setText("Rating: " + movieDetails.get(position).getUserRating() + "");
            ((ListMovieDetailsViewHolder)holder).movieReleaseDate.setText("Release Date: " + movieDetails.get(position).getReleaseDate());
            ((ListMovieDetailsViewHolder)holder).overview.setText(movieDetails.get(position).getOverview());
            ((ListMovieDetailsViewHolder)holder).moviePoster.setImageUrl(builtUri.toString() + movieDetails.get(position).getImageURL(), imageLoader);
        }else if(position <= movieDetails.get(0).getTrailerKeys().size()){
            ((ListMovieTrailersViewHolder)holder).trailerName.setText(movieDetails.get(0).getTrailerTitles().get(position - 1));
            ((ListMovieTrailersViewHolder)holder).trailerSite.setText(movieDetails.get(0).getTrailerSites().get(position - 1));
            ((ListMovieTrailersViewHolder)holder).trailerSize.setText(movieDetails.get(0).getTrailerSizes().get(position - 1) + "p");
            Uri builtUri = Uri.parse(Constants.BASE_YOUTUBE_URL_GET_THUMBNAIL).buildUpon()
                    .appendPath(movieDetails.get(0).getTrailerKeys().get(position - 1) + "")
                    .appendPath("mqdefault.jpg")
                    .build();
            ((ListMovieTrailersViewHolder)holder).trailerThumbnail.setImageUrl(builtUri.toString(), imageLoader);


            ((ListMovieTrailersViewHolder)holder).ripple.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(Constants.BASE_YOUTUBE_URL + movieDetails.get(0).getTrailerKeys().get(position - 1)));
                    activity.startActivity(intent);
                }
            });


        }else{


            ((ListMovieReviewsViewHolder)holder).author.setText(movieDetails.get(0).getReviewAuthors()
                    .get(position - movieDetails.get(0).getTrailerKeys().size() - 1));
            ((ListMovieReviewsViewHolder)holder).content.setText(movieDetails.get(0).getReviews()
                    .get(position - movieDetails.get(0).getTrailerKeys().size() - 1));


        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return TYPE_INTRO;
        }else if(movieDetails.get(0).getTrailerKeys().size() >= 0 && position == 1){
            return TYPE_TRAILERS_FIRST_ROW;
        }else if(position <= movieDetails.get(0).getTrailerKeys().size()){
            return TYPE_TRAILERS;
        }else if(movieDetails.get(0).getReviewAuthors().size() >= 0 && position == (movieDetails.get(0).getTrailerKeys().size() + 1)){
            return TYPE_REVIEWS_FIRST_ROW;
        }else
            return TYPE_REVIEWS;

    }

    @Override
    public int getItemCount() {
        if(movieDetails.size() > 0 && movieDetails.get(0).getReviewAuthors() != null && movieDetails.get(0).getTrailerKeys() != null) {


            return movieDetails.size() + movieDetails.get(0).getReviewAuthors().size() + movieDetails.get(0).getTrailerKeys().size();

        }
        return movieDetails.size();
    }

    public class ListMovieDetailsViewHolder extends RecyclerView.ViewHolder{

        NetworkImageView moviePoster;
        TextView movieName, movieRating, movieReleaseDate, overview;
        public ListMovieDetailsViewHolder(View itemView) {
            super(itemView);
            moviePoster = (NetworkImageView)itemView.findViewById(R.id.moviePoster);
            movieName = (TextView)itemView.findViewById(R.id.movieName);
            movieRating = (TextView)itemView.findViewById(R.id.movieRating);
            movieReleaseDate = (TextView)itemView.findViewById(R.id.movieReleaseDate);
            overview = (TextView)itemView.findViewById(R.id.overview);

        }
    }



    public class ListMovieReviewsViewHolder extends RecyclerView.ViewHolder{


        TextView author, content;
        public ListMovieReviewsViewHolder(View itemView) {
            super(itemView);

            author = (TextView)itemView.findViewById(R.id.author);
            content = (TextView)itemView.findViewById(R.id.content);


        }
    }

    public class ListMovieTrailersViewHolder extends RecyclerView.ViewHolder{

        NetworkImageView trailerThumbnail;
        TextView trailerName, trailerSite, trailerSize;
        MaterialRippleLayout ripple;
        public ListMovieTrailersViewHolder(View itemView) {
            super(itemView);
            trailerThumbnail = (NetworkImageView)itemView.findViewById(R.id.trailerThumbnail);
            trailerName = (TextView)itemView.findViewById(R.id.name);
            trailerSite = (TextView)itemView.findViewById(R.id.site);
            trailerSize = (TextView)itemView.findViewById(R.id.size);
            ripple = (MaterialRippleLayout) itemView.findViewById(R.id.ripple);


        }
    }


}
