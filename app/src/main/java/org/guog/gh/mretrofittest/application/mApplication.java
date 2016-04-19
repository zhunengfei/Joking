package org.guog.gh.mretrofittest.application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by gh on 16/4/19.
 */
public class mApplication extends Application{

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(mApplication.this);
    }
}
