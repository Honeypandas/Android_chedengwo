package com.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class Gotosite extends AppCompatActivity {
    String target=null;
    TextView textView=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gotosite);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        target=bundle.getString("name");

        textView= (TextView) findViewById(R.id.textView5);

        textView.setText("我的位置→"+target);





    }


    public void back2(View view) {
        finish();
    }
}
