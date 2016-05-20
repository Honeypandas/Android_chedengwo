package com.test.UI;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BitmapDescriptor;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.test.R;

public class Site_map extends AppCompatActivity {

    static LatLng latLng;
    static String title;
    // 定位相关


    MapView mMapView;
    BaiduMap mBaiduMap;

    // UI相关


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_site_map);

        // 地图初始化
        //  Marker marker1=new Marker();
        Marker marker;

        mMapView = (MapView) findViewById(R.id.site_map);
        mBaiduMap = mMapView.getMap();

        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.myloc);

        OverlayOptions options = new MarkerOptions()//
                .position(latLng)// 设置marker的位置
                .icon(bitmap)// 设置marker的图标
                .zIndex(9)// 設置marker的所在層級
                .draggable(true).title(title);// 设置手势拖拽
        // 在地图上添加marker，并显示
        marker = (Marker) mBaiduMap.addOverlay(options);


        MapStatus.Builder builder = new MapStatus.Builder();

        builder.target(latLng).zoom(18.0f);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        TextView textView = (TextView) findViewById(R.id.site_map_tv);
        textView.setText(Site_map.title + "公交站的位置");
    }


    public void map_site_back(View view) {
        finish();
    }

    /**
     * 定位SDK监听函数
     */


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

        // 关闭定位图层

        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }





    @Override
    protected void onStop() {
        super.onStop();
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

}
