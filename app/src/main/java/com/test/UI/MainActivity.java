package com.test.UI;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.utils.DistanceUtil;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.test.DAO.nearbydata;
import com.test.R;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    LocationClient mLocClient;
    LatLng mylocation;
    public MyLocationListenner myListener = new MyLocationListenner();
    boolean isFirstLoc = true;
    private PoiSearch poiSearch;


    List<PoiInfo> poiInfoList;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("模拟车等我APP的设计与实现");

        setSupportActionBar(toolbar);


        ConnectivityManager con = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
        boolean internet = con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
        if (wifi | internet) {

            SDKInitializer.initialize(getApplicationContext());

            mLocClient = new LocationClient(this);
            mLocClient.registerLocationListener(myListener);
            LocationClientOption option = new LocationClientOption();
            option.setIsNeedAddress(true);
            option.setIsNeedLocationDescribe(true);

            option.setOpenGps(true); // 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setScanSpan(1000);
            mLocClient.setLocOption(option);
            mLocClient.start();


        } else {
            // exit();

            Toast.makeText(getApplicationContext(),
                    "请检查网络是否连接！", Toast.LENGTH_LONG).show();

            check();


        }

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (R.id.action_settings == id) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /*
    *点击转到查询站点界面
    *
    * */
    public void to1(View view) {
        Intent intent = new Intent(MainActivity.this, Query.class);
        startActivity(intent);

    }

    /*
    *
    * 点击转到我要去界面
    * */

    public void to2(View view) {

        // Intent intent = new Intent(MainActivity.this, LocationTryActivity.class);
        //   startActivity(intent);

        Intent intent = new Intent(MainActivity.this, LocationTryActivity.class);
        startActivity(intent);

    }

    public void to3(View view) {
        Intent intent = new Intent(MainActivity.this, Search_site.class);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.test/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }


    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.test/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();


    }


    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null) {

                return;
            }


            if (isFirstLoc) {
                isFirstLoc = false;

                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                String loc = location.getAddrStr();
                String des = location.getLocationDescribe();

                TextView loca = (TextView) findViewById(R.id.locView);
                loca.setText("\n" + "\n" + "您现在的位置：\n" + loc + "," + des);


                mylocation = ll;
                BusInfo.mylocation = mylocation;
                poiSearch = PoiSearch.newInstance();
                poiSearch.setOnGetPoiSearchResultListener(new PoiSearchResultListener());


                poiSearch.searchNearby((new PoiNearbySearchOption())
                        .location(ll).radius(1500).keyword("公交站").pageCapacity(15));


            }
        }


    }

    private class PoiSearchResultListener implements
            OnGetPoiSearchResultListener {

        @Override
        public void onGetPoiDetailResult(PoiDetailResult result) {
            if (result.error != SearchResult.ERRORNO.NO_ERROR) {
              return;
            }
            else {


                Toast.makeText(getApplicationContext(),
                        result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                        .show();
            }
        }


        public void onGetPoiResult(PoiResult result) {
            if ((result == null)
                    || (result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND)) {

                Log.e("add", "A");
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {

                nearbydata ywq = new nearbydata(MainActivity.this);
                ywq.clear();
                poiInfoList = result.getAllPoi();
                for (int i = 0; i < poiInfoList.size(); i++) {
                    int a = (int) DistanceUtil.getDistance(poiInfoList.get(i).location, mylocation);


                    String distance = a + "米";


                    ywq.add(poiInfoList.get(i).name, poiInfoList.get(i).address, distance);


                }

                return;
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
                Log.e("add", "C");
            }
        }


    }


    public void check() {

        new AlertDialog.Builder(this).setTitle("请检查网络是否连接!\n程序将关闭!")
                .setIcon(R.drawable.warn).setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.this.finish();
            }
        }).show();


    }


    @Override
    protected void onDestroy() {
        poiSearch.destroy();

        super.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }






}