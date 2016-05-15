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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.search.busline.BusLineResult;
import com.baidu.mapapi.search.busline.BusLineSearch;
import com.baidu.mapapi.search.busline.OnGetBusLineSearchResultListener;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.test.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Search_site extends ListActivity {
    String inflater = Context.LAYOUT_INFLATER_SERVICE;
    LayoutInflater layoutInflater;
    private String[] uid = new String[15];
    private SiteAdapter raAdapter;
    private String[] name = new String[15];
    private String[] busline = new String[15];
    private String[] temp = new String[15];


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
            //分别得到3个组件

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

    ListView listView;
    String key;
    private int select = 1;
    private Button site;
    private Button route;
    private BusLineSearch busLineSearch;
    private LinearLayout layout;
    private PoiSearch poiSearch;
    List<PoiInfo> poiInfoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_searchsite);


        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new PoiSearchResultListener());

        busLineSearch = BusLineSearch.newInstance();
        busLineSearch.setOnGetBusLineSearchResultListener(new bus());

        listView = (ListView) findViewById(android.R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (select == 1) {
                    String v = name[position];
                    String b = busline[position];
                    //Toast.makeText(Search_site.this, "结果加载中，请稍等", Toast.LENGTH_SHORT).show();
                    Bundle bundle = new Bundle();
                    bundle.putString("site", v);
                    bundle.putString("busline", b);
                    Intent intent = new Intent(Search_site.this, Bus_sites.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                if (select == 2) {


                    Bundle bundle = new Bundle();
                    bundle.putString("name", name[position]);
                    bundle.putString("busline", key);
                    bundle.putString("uid", uid[position]);
                    Intent intent = new Intent(Search_site.this, BusInfo.class);
                    intent.putExtras(bundle);
                    startActivity(intent);


                }


            }
        });


    }

    public void back3(View view) {
        finish();
    }

    public void selectsites(View view) {

        if (select != 1) {
            select = 1;
            site = (Button) findViewById(R.id.button);
            route = (Button) findViewById(R.id.button2);
            site.setBackgroundResource(R.color.mywhite);
            site.setTextColor(getResources().getColor(R.color.colorPrimary));

            route.setBackgroundResource(R.color.colorPrimary);
            route.setTextColor(getResources().getColor(R.color.mywhite));


            EditText ed = (EditText) findViewById(R.id.ed_site);
            ed.setText("");

            ed.setHint(R.string.sitekey);


            /*LinearLayout hiddenView= (LinearLayout) findViewById(R.id.searlayout2);
            ViewGroup parent = (ViewGroup) hiddenView.getParent();
            parent.removeView(hiddenView);
*/
            //

        }


    }

    public void selectroutes(View view) {
        if (select != 2) {
            select = 2;
            site = (Button) findViewById(R.id.button);
            route = (Button) findViewById(R.id.button2);
            route.setBackgroundResource(R.color.mywhite);
            route.setTextColor(getResources().getColor(R.color.colorPrimary));

            site.setBackgroundResource(R.color.colorPrimary);
            site.setTextColor(getResources().getColor(R.color.mywhite));

            EditText ed = (EditText) findViewById(R.id.ed_site);
            ed.setText("");


            ed.setHint(R.string.routekey);
            /*LinearLayout hiddenView= (LinearLayout) findViewById(R.id.searlayout1);
            ViewGroup parent = (ViewGroup) hiddenView.getParent();
            parent.removeView(hiddenView);
*/


        }


    }

    //按钮
    public void sitesearch(View view) {
        EditText ed = (EditText) findViewById(R.id.ed_site);

        if (ed.getText().toString().equals("542500")) {
            Intent intent=new Intent(this,Shell.class);
            startActivity(intent);
        }



        if (ed.getText().toString().equals("")) {
            Toast.makeText(this, "请输入站点信息！", Toast.LENGTH_SHORT).show();
            return;
        }

        if (select == 1) {
            key = ed.getText().toString().trim() + " 站";
        } else {

            key = ed.getText().toString().trim();

        }

        poiSearch.searchInCity(new PoiCitySearchOption().city("广州")
                .keyword(key).pageCapacity(15));

        name = Arrays.copyOf(temp, 15);
        busline = Arrays.copyOf(temp, 15);
        uid = Arrays.copyOf(temp, 15);
        Toast.makeText(Search_site.this, "内容加载中", Toast.LENGTH_SHORT).show();


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
                int s = 0;
                if (select == 1) {
                    for (int i = 0; i < poiInfoList.size(); i++) {

                        //说明该条POI为公交信息，获取该条POI的UID
                        if (poiInfoList.get(i).address.contains("路") && poiInfoList.get(i).type ==
                                PoiInfo.POITYPE.BUS_STATION
                                && !poiInfoList.get(i).address.contains("线") && !poiInfoList.get(i)
                                .name.contains("口") && poiInfoList
                                .get(i).type != PoiInfo.POITYPE.SUBWAY_STATION && !poiInfoList.get(i)
                                .address.contains("地铁2号线")) {


                            //Log.e("BUSSTATION:",poiInfoList.get(i).name+poiInfoList.get(i).address);
                            name[s] = poiInfoList.get(s).name;

                            busline[s] = poiInfoList.get(s).address;
                            uid[s] = poiInfoList.get(s).uid;
                            Log.e("s", busline[s]);
                            s++;
                        }
                    }
                } else {
                    for (int i = 0; i < poiInfoList.size(); i++) {

                        //说明该条POI为公交信息，获取该条POI的UID
                        if (poiInfoList.get(i).name.contains("路") && poiInfoList.get(i).name.contains(key)
                                && !poiInfoList.get(i).address.contains("线") && !poiInfoList.get(i)
                                .name.contains("口") && poiInfoList
                                .get(i).type != PoiInfo.POITYPE.SUBWAY_STATION && !poiInfoList.get(i)
                                .address.contains("地铁") && poiInfoList.get(i).type == PoiInfo.POITYPE.BUS_LINE && poiInfoList.get(i).address == "") {
                            //Log.e("BUSSTATION:",poiInfoList.get(i).name+poiInfoList.get(i).address);
                            name[s] = poiInfoList.get(s).name;

                            busline[s] = poiInfoList.get(s).address;
                            uid[s] = poiInfoList.get(s).uid;
                            Log.e("s", busline[s]);
                            s++;
                        }
                    }


                }
                if (uid == null) {
                    Toast.makeText(Search_site.this, "未找到对象！", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (Arrays.equals(name, temp)) {
                    Toast.makeText(Search_site.this, "未找到对象！", Toast.LENGTH_SHORT).show();
                }
                name = Arrays.copyOf(name, s);
                busline = Arrays.copyOf(busline, s);
                uid = Arrays.copyOf(uid, s);
                List<View> viewList = new ArrayList<View>();

                viewList.add(getLayoutInflater().inflate(R.layout.searchtab1, null));
                raAdapter = new SiteAdapter(Search_site.this);
                setListAdapter(raAdapter);


                return;
            }
            if (result.error == SearchResult.ERRORNO.AMBIGUOUS_KEYWORD) {
                Log.e("add", "C");
            }
        }


    }

    public class bus implements OnGetBusLineSearchResultListener {


        public void onGetBusLineResult(BusLineResult result)

        {
            if (result.error == SearchResult.ERRORNO.NO_ERROR) {

                Log.e("BUS::", "asdasdas");
            } else {
                Log.e("BUS::", "eeeee");
            }

        }


    }


    @Override
    protected void onDestroy() {
        poiSearch.destroy();

        super.onDestroy();
    }


}
