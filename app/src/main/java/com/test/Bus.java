package com.test;

import android.app.Activity;

import com.baidu.mapapi.model.LatLng;

import static android.os.SystemClock.sleep;

/**
 * Created by Administrator on 2016/5/6.
 */
public class Bus extends Activity implements Runnable {

    public  LatLng latLng;
    public  int loc=0;
    public  int sum=0;
    @Override
    public void run() {

       while(true) {
           while (loc < sum) {


               sleep(3000);
               loc++;



           }

           while (loc > 0) {


               sleep(3000);
               loc--;
           }
       }

    }

    public void setSum(int sum) {
        this.sum = sum;
    }
}
