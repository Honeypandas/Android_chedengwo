package com.test.UI;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.test.R;


/**
 * 使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 */
public class LocationTryActivity extends AppCompatActivity {
    EditText editText = null;
    String target = null;
    LatLng mylocation = null;
    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    BitmapDescriptor mCurrentMarker;
    private static final int accuracyCircleFillColor = 0xAAFFFF88;
    private static final int accuracyCircleStrokeColor = 0xAA00FF00;

    MapView mMapView;
    BaiduMap mBaiduMap;

    // UI相关
    OnCheckedChangeListener radioButtonListener;

    boolean isFirstLoc = true; // 是否首次定位

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.locationtryactivity);


        // 地图初始化

        mMapView = (MapView) findViewById(R.id.testmap);
        mBaiduMap = mMapView.getMap();

        LocationMode mCurrentMode = LocationMode.NORMAL;


        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
    }

    public void search(View view) {
        EditText ed1 = (EditText) findViewById(R.id.ed1);
        EditText ed2 = (EditText) findViewById(R.id.ed2);
        if (ed2.getText().toString().equals("") && ed1.getText().toString().equals("")) {
            Toast.makeText(LocationTryActivity.this, "请输入完整信息！", Toast.LENGTH_SHORT).show();
            return;
        }
        if (ed1.getText().toString().equals("")) {
            Toast.makeText(LocationTryActivity.this, "请输入起点！", Toast.LENGTH_SHORT).show();
            return;
        }

        //Log.e("myloc",mylocation.toString());
        if (ed2.getText().toString().equals("")) {
            Toast.makeText(LocationTryActivity.this, "请输入终点！", Toast.LENGTH_SHORT).show();
            return;
        }


        String start = ed1.getText().toString().trim();
        String finalsite = ed2.getText().toString().trim();
        Log.e("start:", start);
        Log.e("end:", finalsite);


        if (start.equals("我的位置")) {

            Log.e("status:", "1");
            Select_sites.status = 1;

            Intent intent = new Intent(this, Select_sites.class);
            Bundle bundle = new Bundle();
            //我的位置
            Select_sites.start_loc = mylocation;
            bundle.putString("终点", finalsite);
            bundle.putString("起点", start);

            intent.putExtras(bundle);

            startActivity(intent);
        }
        if (finalsite.equals("我的位置")) {
            Log.e("status:", "2");
            Select_start.status = 1;
            Intent intent = new Intent(this, Select_start.class);
            Bundle bundle = new Bundle();
            //我的位置
            Select_start.tar_loc = mylocation;
            bundle.putString("终点", finalsite);
            bundle.putString("起点", start);
            intent.putExtras(bundle);

            startActivity(intent);

        }

        if (!start.equals("我的位置") && !finalsite.equals("我的位置")) {

            Log.e("status:", "3");
            Select_sites.status = 2;

            Intent intent = new Intent(this, Select_start.class);
            Bundle bundle = new Bundle();
            //我的位置

            bundle.putString("终点", finalsite);
            bundle.putString("起点", start);

            intent.putExtras(bundle);

            startActivity(intent);


        }


    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;

                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                mylocation = ll;
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }

    public void jiaohuan(View view) {

        EditText ed1 = (EditText) findViewById(R.id.ed1);
        EditText ed2 = (EditText) findViewById(R.id.ed2);
        String str1 = ed1.getText().toString().trim();
        String str2 = ed2.getText().toString().trim();
        String temp = str1;
        str1 = str2;
        str2 = temp;
        ed1.setText(str1);
        ed2.setText(str2);

    }


    public void back(View view) {
        finish();
    }


}
