package com.test;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static android.os.SystemClock.sleep;

public class Bus_emulation extends ListActivity {
    static int[] distance;
    int d=0;
    ListView listView;
    LatLng[] lng=new LatLng[10];
    String[] direct=new String[10];
    // static  LatLng mylocation;
    public static String title;
    public static String[] bus_station;
     public static LatLng[] latLng;
    String[] bus_location = new String[10];
    int[] bus_distance = new int[10];

    public static int sum = 0;
    static int num = 0;
    String inflater = Context.LAYOUT_INFLATER_SERVICE;
    LayoutInflater layoutInflater;
    private RatingAdapter raAdapter;


    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_emulation);
        TextView textView1 = (TextView) findViewById(R.id.Bus_emulation_tt);
        textView1.setText(title);

        //这儿是耗时操作，完成之后更新UI；
        bus_distance = Arrays.copyOf(bus_distance, num);
        bus_location = Arrays.copyOf(bus_location, num);
        TextView textView = (TextView) findViewById(R.id.Bus_emulation_tv);

        List<View> viewList = new ArrayList<View>();

        viewList.add(getLayoutInflater().inflate(R.layout.activity_bus_emulation, null));
        raAdapter = new RatingAdapter(this);
        setListAdapter(raAdapter);

        distance=Arrays.copyOf(distance, sum);
        for(int j:distance)
        {
            Log.e("distance=", " "+j);
        }

        textView.setText("一共有" + num + "辆公交车在此路线上运行");
        Log.e("sum=", sum + " ");
        Log.e("num=",num+" ");

        for (int i = 0; i < num; i++) {


            new Thread() {
                public void run() {

                    int flag;

                    final int  s=d;
                    d++;
                    Random random=new Random();
                   int loc=random.nextInt(sum);

                    flag=random.nextInt(10);

                    while (true) {



                        if(flag%2==0) {
                            direct[s] = bus_station[sum - 1];


                            while (loc < sum - 2) {
                                lng[s]=latLng[loc];
                                bus_location[s] = bus_station[loc];
                                bus_distance[s] = distance[loc];

                                try {
                                    sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                loc++;

                                //Log.e("s=",s+" ");
                                runOnUiThread(new Runnable() {

                                    @Override
                                    public void run() {


                                        raAdapter.notify_data();
                                    }

                                });


                            }
                            flag++;
                        }else {
                            direct[s] = bus_station[0];

                            while (loc > 1) {
                                lng[s]=latLng[loc];
                                bus_location[s] = bus_station[loc];
                                bus_distance[s] = distance[loc];
                                try {
                                    sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                loc--;


                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        raAdapter.notify_data();
                                    }

                                });
                            }

                            flag++;
                        }


                    }

                }
            }.start();



        }
        //  TextView textView = (TextView) findViewById(R.id.Bus_emulation_tv);

        //   textView.setText("此路线上一共有"+num+"辆公交车正在行驶");
        listView = (ListView) findViewById(android.R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(Bus_emulation.this, Site_map.class);
                Site_map.latLng = lng[position];
                Site_map.title =(position+1)+"#公交车的位置";
                startActivity(intent);

            }
        });





    }

    public void Bus_emulation_back(View view) {
        finish();
    }


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
            return bus_location.length;
        }

        // @Override
        public Object getItem(int position) {
            return bus_location[position];
        }

        // @Override
        public long getItemId(int position) {
            return position;
        }

       public void setRating(int position, int rating)
        {
           distance[position] = rating;
            //在adapter的数据发生变化以后通知UI主线程根据新的数据重新画图
            notifyDataSetChanged();
        }


        public void notify_data()
        {
            notifyDataSetChanged();
        }
        // @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //对listview布局
            LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(
                    R.layout.bus_info_run, null);
            //分别得到3个组件

            TextView Name = ((TextView) linearLayout
                    .findViewById(R.id.bus_location));

             String str2=(position+1)+"#车在"+bus_location[position]+"附近";
            Name.setText(str2);

            TextView Direct = ((TextView) linearLayout
                    .findViewById(R.id.sitenum));


            String k="正往"+direct[position]+"方向行驶";
            Direct.setText(k);





            /*TextView Name = ((TextView) linearLayout
                    .findViewById(R.id.bus_location));


            Name.setText("第"+position+1+"辆车在"+bus_station[position]+"附近");*/


            TextView Distance = ((TextView) linearLayout
                    .findViewById(R.id.run_distance));

            String str="相距约"+bus_distance[position] + "米";
            Distance.setText(str);


            return linearLayout;
        }
    }


}
