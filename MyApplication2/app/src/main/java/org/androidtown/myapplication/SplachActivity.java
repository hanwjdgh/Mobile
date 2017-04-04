package org.androidtown.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * Created by GE62 on 2017-03-22.
 */

public class SplachActivity extends Activity{
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Handler handler = new Handler(){
            public void handleMessage(Message msg){
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0,3000);
    }
  /* @Override
    protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       try{
           Thread.sleep(3000);
       }
       catch (InterruptedException e){
           e.printStackTrace();
       }
       startActivity(new Intent(this, MainActivity.class));
       finish();
    }*/
}
