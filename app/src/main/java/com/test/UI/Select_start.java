package com.test.UI;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
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

import com.baidu.mapapi.model.LatLng;
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

public class Select_start extends ListActivity {

    public static int status = 0;

    static private int first = 0;
    List<View> viewList = new ArrayList<View>();
    String target, start;
    public static LatLng start_loc;
    public static LatLng tar_loc;

    String inflater = Context.LAYOUT_INFLATER_SERVICE;
    LayoutInflater layoutInflater;
    String uid;
    private LatLng[] location = new LatLng[25];
    private SiteAdapter raAdapter;
    private String[] name = new String[25];
    private String[] busline = new String[25];
    private String[] temp = new String[25];
    ListView listView;

    public void backselect(View view) {
        finish();
    }


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
        //设置星行分数
        /*public void setRating(int position, float rating)
        {
           distance[position] = rating;
            //在adapter的数据发生变化以后通知UI主线程根据新的数据重新画图
            notifyDataSetChanged();
        }*/

        // @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //对listview布局
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(
                    R.layout.selectfinalsite, null);
            //分别得到2个组件

            TextView Name = ((TextView) linearLayout
                    .findViewById(R.id.tv_sitename));
            TextView Busline = (TextView) linearLayout
                    .findViewById(R.id.tv_busline);


            //3个组件分别得到内容

            Name.setText(name[position]);
            Busline.setText(busline[position]);


            return linearLayout;
        }
    }


    private LinearLayout layout;
    private PoiSearch poiSearch;
    List<PoiInfo> poiInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_start);

        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();

        target = bundle.getString("终点");
        start = bundle.getString("起点");

        Log.e("eessssssssss", start);
        Log.e("eeqqqqqq", target);

        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new PoiSearchResultListener());
        poiSearch.searchInCity(new PoiCitySearchOption().city(Constant.city)
                .keyword(start).pageCapacity(25));

        //ITEM点击事件
        listView = (ListView) findViewById(android.R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (status == 1) {

                    String v = name[position];
                    Log.e("v:", v);
                    Toast.makeText(Select_start.this, "结果加载中，请稍等", Toast.LENGTH_SHORT).show();
                    Gotosite.start_loc = location[position];
                    Gotosite.tar_loc = tar_loc;
                    Intent intent = new Intent(Select_start.this, Gotosite.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("start", v);
                    bundle.putString("end", "我的位置");

                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    String v = name[position];
                    Log.e("v:", v);
                    Toast.makeText(Select_start.this, "结果加载中，请稍等", Toast.LENGTH_SHORT).show();

                    Select_sites.start_loc = location[position];

                    Intent intent = new Intent(Select_start.this, Select_sites.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("起点", v);
                    bundle.putString("终点", target);

                    intent.putExtras(bundle);
                    startActivity(intent);


                }


            }
        });


    }

    //按钮
   /* public void sitesearch(View view) {

        poiSearch.searchInCity(new PoiCitySearchOption().city("沈阳")
                .keyword(key).pageCapacity(15));

        name= Arrays.copyOf(temp, 15);
        busline = Arrays.copyOf(temp,15);

    }*/


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
                int s = 0;

                for (int i = 0; i < poiInfoList.size(); i++) {


                    if (poiInfoList.get(i).type == PoiInfo.POITYPE.BUS_STATION) {
                        name[i] = poiInfoList.get(i).name + "(公交站)";
                        busline[i] = poiInfoList.get(i).address;
                        location[i] = poiInfoList.get(i).location;
                        //Log.e("print:",i+"次数："+location[i].toString());
                        s++;
                        continue;
                    }
                    s++;

                    location[i] = poiInfoList.get(i).location;
                    // Log.e("print:",i+"次数："+location[i].toString());
                    name[i] = poiInfoList.get(i).name;
                    busline[i] = poiInfoList.get(i).address;
                    uid = poiInfoList.get(i).uid;
                    //Log.e("s", busline[i]);


                }


                if (uid == null) {
                    Toast.makeText(Select_start.this, "未找到对象！", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (Arrays.equals(name, temp)) {
                    Toast.makeText(Select_start.this, "未找到对象！", Toast.LENGTH_SHORT).show();
                }
                name = Arrays.copyOf(name, s);
                busline = Arrays.copyOf(busline, s);


                viewList.add(getLayoutInflater().inflate(R.layout.selectfinalsite, null));
                raAdapter = new SiteAdapter(Select_start.this);
                setListAdapter(raAdapter);

                first = 1;


                return;
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


    public void start_back(View view) {
        finish();
    }
}
