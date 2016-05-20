package com.test.UI;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.mapapi.model.LatLng;
import com.test.R;

import java.util.ArrayList;
import java.util.List;

public class Bus_site_list extends ListActivity {
    private ListView listView = null;
    private String lineinfo;
    private String[] name;
    static LatLng[] latLng;
    private TextView textView = null;
    private String inflater = Context.LAYOUT_INFLATER_SERVICE;
    private LayoutInflater layoutInflater;
    private RatingAdapter raAdapter;

    class RatingAdapter extends BaseAdapter {
        Context context;

        //构造函数
        public RatingAdapter(Context context) {
            this.context = context;
            layoutInflater = (LayoutInflater) context
                    .getSystemService(inflater);
        }

        @Override
        public int getCount() {
            return name.length;
        }

        @Override
        public Object getItem(int position) {
            return name[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //对listview布局
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(
                    R.layout.station_list, null);
            //分别得到3个组件

            TextView Name = ((TextView) linearLayout
                    .findViewById(R.id.station_list_tv));

            //3个组件分别得到内容

            Name.setText(name[position]);


            return linearLayout;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_site_list);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        name = bundle.getStringArray("bus_station");
        lineinfo = bundle.getString("lineInfo");
        textView = (TextView) findViewById(R.id.Bus_site_list_info);
        textView.setText(lineinfo);


        List<View> viewList = new ArrayList<View>();

        viewList.add(getLayoutInflater().inflate(R.layout.activity_bus_site_list, null));
        raAdapter = new RatingAdapter(this);
        setListAdapter(raAdapter);


        listView = (ListView) findViewById(android.R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(Bus_site_list.this, Site_map.class);
                Site_map.latLng = latLng[position];
                Site_map.title = name[position];
                startActivity(intent);

            }
        });


    }


    public void bus_site_list_back(View view) {


        finish();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
