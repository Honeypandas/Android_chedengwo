package com.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.BusLineSearchOption;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;

import java.sql.Date;
import java.text.DateFormat;
import java.util.Arrays;
import java.util.List;

public class BusInfo extends AppCompatActivity {
    BusLineSearch busLineSearch=null;
    TextView textView=null;
    boolean monthTicket;
    String start=null,end=null;
    String lineinfo=null;
    String v=null;
    String[] bus_station=new String[40];
    List<BusLineResult.BusStation> busStationList;
    StringBuffer sb=new StringBuffer(100);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_info);

        Toast.makeText(BusInfo.this,"内容加载中",Toast.LENGTH_LONG).show();
        Intent intent=getIntent();

        Bundle bundle=intent.getExtras();
        String s=bundle.getString("name");
         v=bundle.getString("busline")+"路";
        String uid=bundle.getString("uid");

        textView= (TextView) findViewById(R.id.BusInfo_info);
        textView.setText(v) ;

        busLineSearch=BusLineSearch.newInstance();
        busLineSearch.searchBusLine(new BusLineSearchOption().city("沈阳").uid(uid));


        busLineSearch.setOnGetBusLineSearchResultListener(new OnGetBusLineSearchResultListener() {
            @Override
            public void onGetBusLineResult(BusLineResult busLineResult) {
                String monthti;
               busStationList= busLineResult.getStations();

               lineinfo= busLineResult.getBusLineName();
                start =  busLineResult.getStartTime().toString().substring(11, 19);
                end =   busLineResult.getEndTime().toString().substring(11, 19);
                monthTicket = busLineResult.isMonthTicket();
                if(monthTicket==true){
                    monthti="是";
                }else {
                    monthti="否";
                }

                sb.append("\n路线名：");
                sb.append(lineinfo);
                sb.append("\n\n公交线路首班时间： ");
                sb.append(start);
                sb.append("\n\n公交线路末班时间： ");
                sb.append(end);
                sb.append("\n\n是否有月票：");
                sb.append(monthti);

               for(int i=0;i< busStationList.size();i++) {
                   bus_station[i]=busStationList.get(i).getTitle();

               }

                bus_station=Arrays.copyOf(bus_station, busStationList.size());
                TextView textView = (TextView) findViewById(R.id.textView6);
                textView.setText(sb);


            }
        });





    }















    public void BusInfo_back(View view) {
        finish();
    }


    public void tobuslist(View view) {
        Intent intent=new Intent(BusInfo.this,Bus_site_list.class);
        Bundle bundle=new Bundle();
        bundle.putString("lineInfo",lineinfo);
        bundle.putStringArray("bus_station",bus_station);
        intent.putExtras(bundle);
        startActivity(intent);



    }

    public void to_bus_map(View view) {
        Intent intent=new Intent(BusInfo.this,Bus_station_map.class);
        Bundle bundle=new Bundle();
        bundle.putString("busline", v);
        intent.putExtras(bundle);
        startActivity(intent);
    }



    @Override
    protected void onDestroy() {
        busLineSearch.destroy();

        super.onDestroy();
    }








}

