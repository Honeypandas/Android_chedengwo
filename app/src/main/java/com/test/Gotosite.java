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
import android.widget.Toast;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.inner.GeoPoint;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.search.core.RouteNode;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Gotosite extends ListActivity {
    String start = null;
    String target = null;


    String[] name = new String[10];
    String[] distance = new String[10];
    String[] duration = new String[10];
    String[] title = new String[10];
    private SiteAdapter raAdapter;
    String[] instruction = new String[10];
    String inflater = Context.LAYOUT_INFLATER_SERVICE;
    LayoutInflater layoutInflater;
    TextView textView = null;
    List<TransitRouteLine> transitRouteLines;
    public static LatLng start_loc;
    public static LatLng tar_loc;
    List<View> viewList = new ArrayList<View>();


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
                    R.layout.map, null);
            //分别得到2个组件

            TextView Name = ((TextView) linearLayout
                    .findViewById(R.id.map_Name));
            TextView Distance = (TextView) linearLayout.findViewById(R.id.map_distamce);

            TextView Duration = (TextView) linearLayout.findViewById(R.id.map_time);


            //3个组件分别得到内容

            Name.setText(name[position]);
            Distance.setText(distance[position]);
            Duration.setText(duration[position]);

            return linearLayout;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gotosite);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        target = bundle.getString("end");


        textView = (TextView) findViewById(R.id.textView5);

        textView.setText(bundle.getString("start") + "->" + target);

        RoutePlanSearch mSearch;

        //Log.e("GOTOSITE_start_loc",start_loc.toString());
        //Log.e("GOTOSITE_TARLOC", tar_loc.toString());

        mSearch = RoutePlanSearch.newInstance();

        mSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

                if (transitRouteResult == null || transitRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(Gotosite.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
                }
                if (transitRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
                    //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
                    //result.getSuggestAddrInfo()
                    return;
                }
                if (transitRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {

                    transitRouteResult.getSuggestAddrInfo();
                    transitRouteLines = transitRouteResult.getRouteLines();
                    for (int i = 0; i < transitRouteLines.size(); i++) {
                        int s = transitRouteLines.get(i).getDistance();
                        int v = transitRouteLines.get(i).getDuration();
                        //Log.e("iiii", );
                        int h, m, p;
                        String dis, dur, title = null, instr = "";

                        p = s % 1000;
                        p = p / 100;

                        dis = "全程约" + s / 1000 + "." + p + "公里";

                        h = v / 3600;
                        m = v % 3600;
                        m = m / 60;
                        if (h == 0) {
                            dur = "约" + m + "分钟到达";
                        } else {
                            dur = "约" + h + "小时" + m + "分钟到达";
                        }
                        distance[i] = dis;
                        duration[i] = dur;

                        int num = 0;
                        List<TransitRouteLine.TransitStep> transitSteps = transitRouteLines.get(i).getAllStep();
                        for (int k = 0; k < transitSteps.size(); k++) {
                            String js;
                            String ss = transitSteps.get(k).getInstructions();
                            //Log.e("time",""+transitSteps.get(k).getDuration());
                            int time = transitSteps.get(k).getDuration();

                            time = time / 60;


                            instr = instr + ss + "(" + time + "分钟)" + "%";
                            if (ss.contains("乘坐")) {
                                js = ss.substring(2, ss.indexOf(","));
                                if (num == 0) {
                                    title = js;
                                    num++;
                                    continue;
                                }
                                title = title + "->" + js;

                                num++;
                            }


                        }


                        instruction[i] = instr;
                        name[i] = title;
                        // Log.e("key",name[i]);
                        name = Arrays.copyOf(name, transitRouteLines.size());
                        distance = Arrays.copyOf(distance, transitRouteLines.size());
                        duration = Arrays.copyOf(duration, transitRouteLines.size());
                        instruction = Arrays.copyOf(instruction, transitRouteLines.size());
                        //Log.e("ywq:",dis+" "+dur);
                    }


                    viewList.add(getLayoutInflater().inflate(R.layout.activity_bussites, null));
                    raAdapter = new SiteAdapter(Gotosite.this);
                    setListAdapter(raAdapter);


                }


            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

            }
        });


        PlanNode stNode = PlanNode.withLocation(start_loc);
        PlanNode enNode = PlanNode.withLocation(tar_loc);

        mSearch.transitSearch((new TransitRoutePlanOption())
                .from(stNode)
                .city("沈阳")
                .to(enNode));


        ListView listView = (ListView) findViewById(android.R.id.list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Gotosite.this, Route_instruction.class);
                Bundle bundle = new Bundle();
                String title = textView.getText().toString();
                bundle.putString("title", title);
                String s = instruction[position];
                bundle.putString("instruction", s);
                intent.putExtras(bundle);

                startActivity(intent);

            }
        });


    }


    public void back2(View view) {
        finish();
    }
}
