package com.test;

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

import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import nearbysite.nearbysites;

/**
 * Created by Administrator on 2016/4/17.
 */
public class selectsites extends ListActivity {
    static private  int first=0;
    List<View> viewList = new ArrayList<View>();
    String target;
    String inflater = Context.LAYOUT_INFLATER_SERVICE;
    LayoutInflater layoutInflater;
    String uid;
    private SiteAdapter raAdapter;
    private String[] name=new String[25]   ;
    private  String[] busline=new String[25];
    private String[] temp=new String[25]   ;
    ListView listView;

    public void backselect(View view) {
        finish();
    }


    class SiteAdapter extends BaseAdapter
    {
        private Context context;
        //构造函数
        public SiteAdapter(Context context)
        {
            this.context = context;
            layoutInflater = (LayoutInflater) context
                    .getSystemService(inflater);
        }

        //@Override
        public int getCount()
        {
            return name.length;
        }

        // @Override
        public Object getItem(int position)
        {
            return name[position];
        }

        // @Override
        public long getItemId(int position)
        {
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
        public View getView(int position, View convertView, ViewGroup parent)
        {
            //对listview布局
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(
                    R.layout.selectfinalsite, null);
            //分别得到2个组件

            TextView Name = ((TextView) linearLayout
                    .findViewById(R.id.tv_sitename));
            TextView  Busline = (TextView) linearLayout
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
        setContentView(R.layout.selectsites);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        target=bundle.getString("终点");

        poiSearch = PoiSearch.newInstance();
        poiSearch.setOnGetPoiSearchResultListener(new PoiSearchResultListener());
        poiSearch.searchInCity(new PoiCitySearchOption().city("沈阳")
                .keyword(target).pageCapacity(25));

        //ITEM点击事件
        listView= (ListView) findViewById(android.R.id.list);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String v = name[position];
                    Toast.makeText(selectsites.this, "结果加载中，请稍等", Toast.LENGTH_SHORT).show();

                    Intent intent=new Intent(selectsites.this,Gotosite.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("name",v);
                    intent.putExtras(bundle);
                    startActivity(intent);


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
            if (result.error == SearchResult.ERRORNO.NO_ERROR)
            {

                poiInfoList = result.getAllPoi();
                int s=0;

                    for (int i = 0; i < poiInfoList.size(); i++)
                    {


                        if(poiInfoList.get(i).type== PoiInfo.POITYPE.BUS_STATION)
                        {
                            name[i] = poiInfoList.get(i).name+"(公交站)";
                            busline[i] = poiInfoList.get(i).address;

                            s++;
                            continue;
                        }
                        s++;
                        name[i] = poiInfoList.get(i).name;
                        busline[i] = poiInfoList.get(i).address;
                        uid = poiInfoList.get(i).uid;
                        Log.e("s", busline[i]);



                    }


                if(uid==null)
                {
                    Toast.makeText(selectsites.this,"未找到对象！",Toast.LENGTH_SHORT).show();
                    return ;
                }

                  if(Arrays.equals(name,temp))
                {
                    Toast.makeText(selectsites.this,"未找到对象！",Toast.LENGTH_SHORT).show();
                }
                name = Arrays.copyOf(name, s);
                busline = Arrays.copyOf(busline,s);



                viewList.add(getLayoutInflater().inflate(R.layout.selectfinalsite, null));
                raAdapter = new SiteAdapter(selectsites.this);
                setListAdapter(raAdapter);

                first=1;


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



}


