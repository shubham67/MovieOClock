package com.project.bittu.movieoclock.App;

import android.app.Application;
import android.content.Context;

/**
 * Created by Bittu on 6/21/2016.
 */
public class App extends Application {

    private static Application singletonInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        singletonInstance = this;
    }


    public static Context getContext(){
        return singletonInstance.getApplicationContext();
    }

    public static Application getInstance(){
        return singletonInstance;
    }


}
