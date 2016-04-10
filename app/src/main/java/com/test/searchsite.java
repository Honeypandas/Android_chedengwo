package com.test;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class searchsite extends AppCompatActivity {
    private int select=1;
    private Button site;
    private Button route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_searchsite);
    }

    public void back3(View view) {
        finish();
    }

    public void selectsites(View view) {

        if(select!=1)
        {
            select=1;
            site= (Button) findViewById(R.id.button);
            route= (Button) findViewById(R.id.button2);
            site.setBackgroundResource(R.color.mywhite);
            site.setTextColor(getResources().getColor(R.color.colorPrimary));

            route.setBackgroundResource(R.color.colorPrimary);
            route.setTextColor(getResources().getColor(R.color.mywhite));


        }





    }

    public void selectroutes(View view) {
        if(select!=2)
        {
            select=2;
            site= (Button) findViewById(R.id.button);
            route= (Button) findViewById(R.id.button2);
            route.setBackgroundResource(R.color.mywhite);
            route.setTextColor(getResources().getColor(R.color.colorPrimary));

            site.setBackgroundResource(R.color.colorPrimary);
            site.setTextColor(getResources().getColor(R.color.mywhite));


        }


    }
}
