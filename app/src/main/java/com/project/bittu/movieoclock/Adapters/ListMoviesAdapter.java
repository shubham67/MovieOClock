package com.project.bittu.movieoclock.Adapters;

import android.app.Activity;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.project.bittu.movieoclock.Constants.Constants;
import com.project.bittu.movieoclock.Models.Movie;
import com.project.bittu.movieoclock.Network.VolleySingleton;
import com.project.bittu.movieoclock.R;

import java.util.List;

/**
 * Created by Bittu on 6/20/2016.
 */
public class ListMoviesAdapter extends RecyclerView.Adapter<ListMoviesAdapter.ListMoviesHolder>{




    public interface OnItemSelectedListener {
        void onItemSelected(long id);
    }

    private OnItemSelectedListener itemSelectedListener;

    List<Movie> movieDetails;
    StringBuffer buffer = new StringBuffer();
    Activity activityContext;
    VolleySingleton volleySingleton = VolleySingleton.getInstance();
    ImageLoader imageLoader = volleySingleton.getImageLoader();
    private final static int TYPE_1 = 1;
    private final static int TYPE_2 = 2;
    private final static int TYPE_3 = 3;


    public ListMoviesAdapter(Activity activityContext ,List<Movie> movieDetails) {
        this.activityContext = activityContext;
        this.movieDetails = movieDetails;
        itemSelectedListener = (OnItemSelectedListener)activityContext;
    }

    @Override
    public ListMoviesHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if(viewType == TYPE_1)
            view = layoutInflater.inflate(R.layout.grid_view_card, parent, false);
        else if(viewType == TYPE_2)
            view = layoutInflater.inflate(R.layout.grid_view_card_large_1, parent, false);
        else
            view = layoutInflater.inflate(R.layout.grid_view_card_large_2, parent, false);


        ListMoviesHolder holder = new ListMoviesHolder(view);

        return holder;

    }



    @Override
    public int getItemViewType(int position) {

        if((position + 1) % 4 != 0){
            return TYPE_1;
        }else if((position + 1) % 8 == 4){
            return TYPE_2;
        }else{
            return TYPE_3;
        }

    }

    @Override
    public void onBindViewHolder(final ListMoviesHolder holder, final int position) {






            Uri builtUri = Uri.parse(Constants.IMAGE_BASE_URL).buildUpon()
                    .appendPath("w342")
                    .build();

            holder.image.setImageUrl(builtUri.toString() + movieDetails.get(position).getImageURL(), imageLoader);
            holder.image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemSelectedListener != null)
                        itemSelectedListener.onItemSelected(movieDetails.get(position).getId());

                }
            });


        if(getItemViewType(position) == TYPE_1){
            holder.textView.setText(movieDetails.get(position).getTitle());
        }else{

            if(buffer.length() > 0)
                buffer.delete(0, buffer.length() - 1);
            buffer.insert(0, movieDetails.get(position).getOverview());
            if(buffer.length() >= 270){
                buffer.replace(0, buffer.length() - 1, buffer.substring(0, 267));
                buffer.insert(268, "...");
            }
            holder.textView.setText(buffer.substring(0, buffer.length() - 1));
        }

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemSelectedListener != null)
                    itemSelectedListener.onItemSelected(movieDetails.get(position).getId());
            }
        });

        holder.readMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemSelectedListener != null)
                    itemSelectedListener.onItemSelected(movieDetails.get(position).getId());

            }
        });



    }

    @Override
    public int getItemCount() {
        if(movieDetails != null)
            return movieDetails.size();
        else
            return 0;
    }

    public class ListMoviesHolder extends RecyclerView.ViewHolder{
        NetworkImageView image;
        TextView textView;
        Button readMoreButton;
        public ListMoviesHolder(View view){
            super(view);
            image = (NetworkImageView)view.findViewById(R.id.grid_view_card_image_view);
            textView = (TextView)view.findViewById(R.id.titleTextView);
            readMoreButton = (Button)view.findViewById(R.id.read_more_button);

        }
    }

}
