package com.example.xiaqi.drawroute;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by xiaqi on 2016/3/7.
 */
public class MyApp extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
    }

}
