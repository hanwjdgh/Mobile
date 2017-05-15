package org.androidtown.cok;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import static android.R.attr.data;


public class MainActivity extends AppCompatActivity {
    ImageButton mbutton;String phoneNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        phoneNum = telephonyManager.getLine1Number();
        Toast.makeText(getApplicationContext(),phoneNum,Toast.LENGTH_SHORT).show();
        //startActivity(new Intent(this,SplachActivity.class));
        mbutton = (ImageButton)findViewById(R.id.m_button);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivityForResult(intent,1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String outName = data.getStringExtra("title");
        int num = data.getIntExtra("number",1);
        Toast.makeText(getApplicationContext(), outName + " "+ num, Toast.LENGTH_LONG).show();
    }
}
