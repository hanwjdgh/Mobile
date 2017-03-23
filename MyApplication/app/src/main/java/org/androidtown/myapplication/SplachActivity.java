package org.androidtown.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by GE62 on 2017-03-22.
 */

public class SplachActivity extends Activity{
   @Override
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
    }
}
