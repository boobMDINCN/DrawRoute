package com.example.xiaqi.drawroute;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {


    private MapView mMapView;
    List<Item> list;

    //    List<LatLng> positions ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        mHandler =  new MyHandler() ;
        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.onResume();
        startParser();
    }

    long beginTime;

    private void startParser() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                beginTime = System.currentTimeMillis();
                InputStream is = getResources().openRawResource(R.raw.demo);
                XmlPullParser xmlPullParser = Xml.newPullParser();
                try {
                    int count = 0;
                    xmlPullParser.setInput(is, "UTF-8");
                    int eventType = xmlPullParser.getEventType();
                    String lat = null, lon = null, date = null;
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_DOCUMENT:
                                list = new ArrayList<>();
                                break;
                            case XmlPullParser.START_TAG:

                                if (xmlPullParser.getName().equals("trkpt")) {
                                    lat = xmlPullParser.getAttributeValue(0);
                                    lon = xmlPullParser.getAttributeValue(1);
                                } else if (xmlPullParser.getName().equals("time")) {
                                    eventType = xmlPullParser.next();
                                    date = xmlPullParser.getText();
                                }
                                break;
                            case XmlPullParser.END_TAG:
                                if (xmlPullParser.getName().equals("trkpt")) {
                                    Item item = new Item(lat, lon, date);
                                    LatLng ll = item.getLL();
//                                    positions.add(ll) ;
                                    Log.i("test", item.toString() + count++);
                                    list.add(item);
                                }
                                break;
                            case XmlPullParser.END_DOCUMENT:
                                //never goes ;
                        }
                        eventType = xmlPullParser.next();
                    }
                    mHandler.sendEmptyMessage(0);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    PolylineOptions options;
    private MyHandler mHandler;

    class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast.makeText(MainActivity.this, "size of positions == " + list.size(), Toast.LENGTH_SHORT).show();
            addPointToMap();
        }
    }

    private void addPointToMap() {
        List<LatLng> points = new ArrayList<LatLng>();
        for (Item item : list) {
            LatLng ll = new LatLng(item.lat, item.lon);
            points.add(ll);
        }
        options = new PolylineOptions().points(points).color(Color.RED).width(10);
        mMapView.getMap().addOverlay(options);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}


class Item {
    double lat;
    double lon;
    String dateStr;
    LatLng position;

    public Item(String lat, String lon, String dateStr) {
        this.lat = Double.valueOf(lat);
        this.lon = Double.valueOf(lon);
        position = new LatLng(this.lat, this.lon);
        this.dateStr = dateStr;
    }


    public LatLng getLL() {
        return position;
    }

    @Override
    public String toString() {
        return "lon = " + lon + " lat = " + lat + " date= " + dateStr;
    }
}