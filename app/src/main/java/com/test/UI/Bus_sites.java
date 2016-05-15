package com.test.UI;


import android.content.DialogInterface;
import android.os.Handler;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.test.R;
import com.test.Util.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Bus_sites extends ListActivity {
    final static String ZANWU = "暂无信息";
    private Handler hander = null;
    List<View> viewList = new ArrayList<View>();
    int s = 0;
    int num = 0;
    int j = 0;

    String inflater = Context.LAYOUT_INFLATER_SERVICE;
    LayoutInflater layoutInflater;
    private String[] uid = new String[30];
    private SiteAdapter raAdapter;
    private String[] name = new String[30];
    private String[] sitenum = new String[30];
    private String[] distance = new String[30];
    private String[] time = new String[30];
    private String[] temp = new String[30];
    ListView listView = null;
    TextView textView = null;
    private PoiSearch poiSearch;
    List<PoiInfo> poiInfoList;
    String key = null;
    String[] line = new String[30];

    class SiteAdapter extends BaseAdapter {
        private Context context;

        //构造函数
        public SiteAdapter(Context context) {
            this.context = context;
            layoutInflater = (LayoutInflater) context
                    .getSystemService(inflater);
        }

        //@Override
        public int getCount() {
            return name.length;
        }

        // @Override
        public Object getItem(int position) {
            return name[position];
        }

        // @Override
        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            //对listview布局
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(
                    R.layout.realtimeroutime, null);
            //分别得到2个组件

            TextView Name = ((TextView) linearLayout
                    .findViewById(R.id.routename));

            TextView Sitenum = (TextView) linearLayout.findViewById(R.id.sitenum);

            TextView Distance = (TextView) linearLayout.findViewById(R.id.mdistance);

            TextView Reachtime = (TextView) linearLayout.findViewById(R.id.reachtime);

            //3个组件分别得到内容

            Name.setText(name[position]);
            Sitenum.setText(sitenum[position]);
            Distance.setText(distance[position]);
            Reachtime.setText(time[position]);
            return linearLayout;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_bussites);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String v = bundle.getString("site");
        String b = bundle.getString("busline");
        textView = (TextView) findViewById(R.id.siteinfo);
        textView.setText(v);
        Log.e("bus:", b);

        if (b != null) {
            line = b.split(";");
            Log.e("gggg:", line[0]);
        }




        /*Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();*/

        for (String str : line) {
            search(str);
        }


        viewList.add(getLayoutInflater().inflate(R.layout.activity_bussites, null));
        raAdapter = new SiteAdapter(Bus_sites.this);
        setListAdapter(raAdapter);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                int index = 0;
                while (!Thread.currentThread().isInterrupted()) {


                    Message m = hander.obtainMessage();
                    m.arg1 = index;
                    m.what = 0x101;
                    hander.sendMessage(m);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }

            }
        });

        hander = new Handler() {

            public void handleMessage(Message msg) {
                if (msg.what == 0x101) {
                    viewList.add(getLayoutInflater().inflate(R.layout.activity_bussites, null));
                    raAdapter = new SiteAdapter(Bus_sites.this);
                    setListAdapter(raAdapter);
                }
                super.handleMessage(msg);

            }
        };


        Toast.makeText(Bus_sites.this, "结果加载中，请稍等", Toast.LENGTH_LONG).show();


        listView = (ListView) findViewById(android.R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String v = name[position].substring(0, 3);
                String b = uid[position];
                Log.e("name", v);

                Intent intent = new Intent(Bus_sites.this, BusInfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("busline", v);
                bundle.putString("uid", b);
                intent.putExtras(bundle);

                startActivity(intent);

            }
        });


    }


    public void search(String sear) {
        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new PoiSearchResultListener());

        poiSearch.searchInCity(new PoiCitySearchOption().city(Constant.city)
                .keyword(sear).pageCapacity(15));


    }


    private class PoiSearchResultListener implements
            OnGetPoiSearchResultListener {

        @Override
        public void onGetPoiDetailResult(PoiDetailResult result) {
            if (result.error != SearchResult.ERRORNO.NO_ERROR) {


            } else {

                Toast.makeText(getApplicationContext(),
                        result.getName() + ": " + result.getAddress(), Toast.LENGTH_SHORT)
                        .show();
            }
        }


        public void onGetPoiResult(PoiResult result) {
            if ((result == null)
                    || (result.error == SearchResult.ERRORNO.RESULT_NOT_FOUND)) {

                //判断是否有误
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {


                poiInfoList = result.getAllPoi();

                Log.e("error;", "sssdfghj");

                for (int i = 0; i < 2; i++) {
                    if (!poiInfoList.get(i).name.contains("(")) {
                        continue;
                    }
                    name[s] = poiInfoList.get(i).name;
                    sitenum[s] = ZANWU;
                    distance[s] = ZANWU;
                    time[s] = ZANWU;
                    uid[s] = poiInfoList.get(i).uid;
                    // Log.e("pppppp",name[s]);
                    s++;
                }


               /* if(uid==null)
                {
                    Toast.makeText(Bus_sites.this,"未找到对象！",Toast.LENGTH_SHORT).show();

                }*/

                if (Arrays.equals(name, temp)) {
                    Toast.makeText(Bus_sites.this, "未找到对象！", Toast.LENGTH_SHORT).show();

                    new AlertDialog.Builder(Bus_sites.this).setTitle("抱歉，未找到对象!\n点击确定后返回！")
                            .setIcon(R.drawable.warn).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Bus_sites.this.finish();
                        }
                    }).show();


                }


                num++;
                Log.e("num:", num + "");
                if (num == line.length) {

                    name = Arrays.copyOf(name, s);
                    sitenum = Arrays.copyOf(sitenum, s);
                    distance = Arrays.copyOf(distance, s);
                    time = Arrays.copyOf(time, s);
                    uid = Arrays.copyOf(uid, s);
                    viewList.add(getLayoutInflater().inflate(R.layout.activity_bussites, null));
                    raAdapter = new SiteAdapter(Bus_sites.this);
                    setListAdapter(raAdapter);
                }


                return;


                /*viewList.add(getLayoutInflater().inflate(R.layout.activity_bussites, null));
                raAdapter = new SiteAdapter(Bus_sites.this);
                setListAdapter(raAdapter);*/
            }


            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
            }


        }

    }

    @Override
    protected void onDestroy() {
        poiSearch.destroy();

        super.onDestroy();
    }


    public void back8(View view) {
        finish();

    }
}
