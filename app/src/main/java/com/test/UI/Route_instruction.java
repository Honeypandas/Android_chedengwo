package com.test.UI;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.test.R;

public class Route_instruction extends AppCompatActivity {
    private  String[] step = new String[10];
    String instruction;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_instruction);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        instruction = bundle.getString("instruction");

        title = bundle.getString("title");
        RoutePlan.title=title;
        TextView textView = (TextView) findViewById(R.id.route_text);
        textView.setText(title);

        if (instruction != null) {
            step = instruction.split("%");
        }


        StringBuffer sb = new StringBuffer(200);
        for (int i = 1; i <= step.length; i++) {
            sb.append("\n" + "第" + i + "步:" + step[i - 1] + "\n");
        }

        TextView tv = (TextView) findViewById(R.id.route_instr);
        tv.setText(sb);


    }


    public void route_back(View view) {
        finish();
    }

    public void route_map(View view) {
        Intent intent=new Intent(Route_instruction.this,RoutePlan.class);
        startActivity(intent);
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
