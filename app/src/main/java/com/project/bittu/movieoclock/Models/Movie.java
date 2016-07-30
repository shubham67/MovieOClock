package com.project.bittu.movieoclock.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Bittu on 6/20/2016.
 */
public class Movie implements Parcelable{

    long id;
    String title;
    String imageURL;
    float userRating;
    String releaseDate;
    String overview;
    ArrayList<String> trailerURLs;
    ArrayList<String> trailerTitles;
    ArrayList<String> trailerKeys;
    ArrayList<String> trailerSites;
    ArrayList<String> trailerSizes;
    ArrayList<String> reviewAuthors;
    ArrayList<String> reviews;



    public long getId() {
        return id;
    }

    public Movie(long id, String title, String imageURL, float userRating, String releaseDate, String overview,
                 ArrayList<String> trailerURLs, ArrayList<String> trailerTitles, ArrayList<String> trailerKeys,
                 ArrayList<String> trailerSites, ArrayList<String> trailerSizes, ArrayList<String> reviewAuthors,
                 ArrayList<String> reviews) {
        this.id = id;
        this.title = title;
        this.imageURL = imageURL;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.overview = overview;
        this.trailerURLs = trailerURLs;
        this.trailerTitles = trailerTitles;
        this.trailerKeys = trailerKeys;
        this.trailerSites = trailerSites;
        this.trailerSizes = trailerSizes;
        this.reviewAuthors = reviewAuthors;
        this.reviews = reviews;


    }



    protected Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        imageURL = in.readString();
        userRating = in.readFloat();
        releaseDate = in.readString();
        overview = in.readString();
        trailerURLs = (ArrayList<String>) in.readSerializable();
        trailerTitles = (ArrayList<String>) in.readSerializable();
        trailerKeys = (ArrayList<String>) in.readSerializable();
        trailerSites = (ArrayList<String>) in.readSerializable();
        trailerSizes = (ArrayList<String>) in.readSerializable();
        reviewAuthors = (ArrayList<String>) in.readSerializable();
        reviews = (ArrayList<String>) in.readSerializable();
    }



    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return readFromParcel(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public void setTrailerTitles(ArrayList<String> trailerTitles) {
        this.trailerTitles = trailerTitles;
    }

    public void setTrailerKeys(ArrayList<String> trailerKeys) {
        this.trailerKeys = trailerKeys;
    }

    public void setTrailerSites(ArrayList<String> trailerSites) {
        this.trailerSites = trailerSites;
    }

    public void setTrailerSizes(ArrayList<String> trailerSizes) {
        this.trailerSizes = trailerSizes;
    }

    public String getTitle() {
        return title;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public static Creator<Movie> getCREATOR() {
        return CREATOR;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public void setUserRating(float userRating) {
        this.userRating = userRating;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getImageURL() {
        return imageURL;
    }

    public float getUserRating() {
        return userRating;
    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public ArrayList<String> getTrailerURLs() {
        return trailerURLs;
    }

    public ArrayList<String> getReviewAuthors() {
        return reviewAuthors;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public void setTrailerURLs(ArrayList<String> trailerURLs) {
        this.trailerURLs = trailerURLs;
    }

    public void setReviewAuthors(ArrayList<String> reviewAuthors) {
        this.reviewAuthors = reviewAuthors;
    }

    public void setReviews(ArrayList<String> reviews) {
        this.reviews = reviews;
    }

    public ArrayList<String> getTrailerTitles() {
        return trailerTitles;
    }

    public ArrayList<String> getTrailerSites() {
        return trailerSites;
    }

    public ArrayList<String> getTrailerKeys() {
        return trailerKeys;
    }

    public ArrayList<String> getTrailerSizes() {
        return trailerSizes;
    }

    protected static Movie readFromParcel(Parcel source){
        long id = source.readLong();
        String title = source.readString();
        String imageURL = source.readString();
        float userRating = source.readFloat();
        String releaseDate = source.readString();
        String overview = source.readString();
        ArrayList<String> trailerURLs = (ArrayList<String>) source.readSerializable();
        ArrayList<String> trailerTitles = (ArrayList<String>) source.readSerializable();
        ArrayList<String> trailerKeys = (ArrayList<String>) source.readSerializable();
        ArrayList<String> trailerSites = (ArrayList<String>) source.readSerializable();
        ArrayList<String> trailerSizes = (ArrayList<String>) source.readSerializable();
        ArrayList<String> reviewAuthors = (ArrayList<String>) source.readSerializable();
        ArrayList<String> reviews = (ArrayList<String>) source.readSerializable();
        return new Movie(id, title, imageURL, userRating, releaseDate, overview, trailerURLs,
                trailerTitles, trailerKeys, trailerSites, trailerSizes, reviewAuthors, reviews);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(imageURL);
        dest.writeFloat(userRating);
        dest.writeString(releaseDate);
        dest.writeString(overview);
        dest.writeSerializable(trailerURLs);
        dest.writeSerializable(trailerTitles);
        dest.writeSerializable(trailerKeys);
        dest.writeSerializable(trailerSites);
        dest.writeSerializable(trailerSizes);
        dest.writeSerializable(reviewAuthors);
        dest.writeSerializable(reviews);
    }
}
