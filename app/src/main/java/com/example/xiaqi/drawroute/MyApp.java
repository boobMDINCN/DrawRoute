package com.example.xiaqi.drawroute;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

/**
 * Created by xiaqi on 2016/3/7.
 */
public class MyApp extends Application {


//    LocationClient mLocationClient ;

    @Override
    public void onCreate() {
        super.onCreate();
//        initMapOpt() ;
        SDKInitializer.initialize(this);
    }


//    private void initMapOpt(){
//        try{
//            SDKInitializer.initialize(this );
//            mLocationClient = new LocationClient(this );
//            LocationClientOption option = new LocationClientOption();
//            option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);
//            option.setCoorType("bd09ll");
//            option.setScanSpan(5000);
//            option.setIsNeedAddress(true);
//            option.setOpenGps(true);
//            mLocationClient.setLocOption(option);
//        }catch (Throwable t){
//            t.printStackTrace();
//        }
//    }
}
