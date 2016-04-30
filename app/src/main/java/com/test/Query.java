package com.test;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nearbysite.nearbysites;

public class Query extends ListActivity {

    private String[] name = new String[15];

    private String[] busline = new String[15];

    private String[] distance = new String[15];

    String inflater = Context.LAYOUT_INFLATER_SERVICE;
    LayoutInflater layoutInflater;
    private RatingAdapter raAdapter;


    private PoiSearch poiSearch;
    private int c;
    List<PoiInfo> poiInfoList;
    ListView listView;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    class RatingAdapter extends BaseAdapter {
        private Context context;

        //构造函数
        public RatingAdapter(Context context) {
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

       /* public void setRating(int position, float rating)
        {
           distance[position] = rating;
            //在adapter的数据发生变化以后通知UI主线程根据新的数据重新画图
            notifyDataSetChanged();
        }*/

        // @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //对listview布局
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(
                    R.layout.sitesinfo, null);
            //分别得到3个组件

            TextView tvApplicationName = ((TextView) linearLayout
                    .findViewById(R.id.tv_name));
            TextView tvAuthor = (TextView) linearLayout
                    .findViewById(R.id.tv_busid);
            TextView tvRating = (TextView) linearLayout
                    .findViewById(R.id.tv_distance);

            //3个组件分别得到内容

            tvApplicationName.setText(name[position]);
            tvAuthor.setText(busline[position]);
            tvRating.setText(String.valueOf(distance[position]));

            return linearLayout;
        }
    }


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.content_query);

     /*   poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new PoiSearchResultListener());
        ListView listView= (ListView) findViewById(R.id.listView2);*/

        nearbydata db = new nearbydata(this);
        List<nearbysites> site = db.findAll();
        Log.e("Long:", site.size() + "");
        for (int i = 0; i < site.size(); i++) {


            name[i] = site.get(i).getName();

            busline[i] = site.get(i).getBusid();

            distance[i] = site.get(i).getDistance();


        }
        //改变数组长度
        name = Arrays.copyOf(name, site.size());
        busline = Arrays.copyOf(busline, site.size());
        distance = Arrays.copyOf(distance, site.size());

        List<View> viewList = new ArrayList<View>();

        viewList.add(getLayoutInflater().inflate(R.layout.content_query, null));
        raAdapter = new RatingAdapter(this);
        setListAdapter(raAdapter);


        //点击事件
        listView = (ListView) findViewById(android.R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String v = name[position];
                String b = busline[position];


                Intent intent = new Intent(Query.this, bussites.class);
                Bundle bundle = new Bundle();
                bundle.putString("site", v);
                bundle.putString("busline", b);
                intent.putExtras(bundle);

                startActivity(intent);

            }
        });

        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Query Page", // TODO: Define a title for the content shown.
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
                "Query Page", // TODO: Define a title for the content shown.
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

    public void to11(View view) {

        Intent intent = new Intent(this, searchsite.class);
        startActivity(intent);
    }

    public void back2(View view) {
        finish();
    }

    //监听器
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

                Log.e("add", "A");
                return;
            }
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {

                Toast.makeText(Query.this, "查找成功！", Toast.LENGTH_SHORT).show();

                c = result.getTotalPoiNum();
                Log.e("add", "A" + c);

                poiInfoList = result.getAllPoi();
                for (int i = 0; i < poiInfoList.size(); i++) {

                    Log.e("add", poiInfoList.get(i).address + poiInfoList.get(i).name);
                    Toast.makeText(Query.this, c + "", Toast.LENGTH_SHORT).show();

                }


                // String c=result.getAllAddr().get(0).address.trim();
                // Log.e("address",c);
                return;
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
                Log.e("add", "C");
            }
        }


    }      //自定义一个Adapter继承BaseAdapter，要重写getCount(),getItem(),getItemId(),getView()四种方法


}
