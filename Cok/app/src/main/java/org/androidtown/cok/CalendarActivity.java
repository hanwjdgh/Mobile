package org.androidtown.cok;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by GE62 on 2017-06-06.
 */

public class CalendarActivity extends AppCompatActivity{
    Button button,s_btn,e_btn;
    FragmentManager fm;
    Thread th,th2;
    CalendarFragment cf;
    CalendarFragment2 cf2;
    Bundle bundle;
    int s_y, s_m, s_d, e_y, e_m, e_d;
    Intent gintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);


        cf = new CalendarFragment();
        cf2 = new CalendarFragment2();
        cf.setArguments(bundle);
        cf2.setArguments(bundle);

        s_btn = (Button) findViewById(R.id.btn);
        e_btn = (Button) findViewById(R.id.btn2);
        th = new cThread(1);
        th2 = new cThread(2);
        fm = getFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        tr.add(R.id.c_frame, cf, "ccc");
        tr.add(R.id.c_frame, cf2, "cccc");

        if (Main2Activity.clintent.getFlags() == 1) {
            e_btn.setBackgroundColor(Color.WHITE);
            s_btn.setBackgroundColor(Color.RED);
            tr.hide(cf2);
            th.start();
        } else if (Main2Activity.clintent.getFlags() == 2) {
            e_btn.setBackgroundColor(Color.RED);
            s_btn.setBackgroundColor(Color.WHITE);
            tr.hide(cf);
            th2.start();
        }
        tr.commit();


        button = (Button) findViewById(R.id.button3);

        s_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e_btn.setBackgroundColor(Color.WHITE);
                s_btn.setBackgroundColor(Color.RED);
                FragmentTransaction tr = fm.beginTransaction();
                tr.replace(R.id.c_frame, cf);
                tr.show(cf);
                if(!th.isAlive())
                    th.start();
                if (th2.isAlive())
                    th2.interrupt();
                tr.commit();
            }
        });

        e_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                e_btn.setBackgroundColor(Color.RED);
                s_btn.setBackgroundColor(Color.WHITE);
                FragmentTransaction tr = fm.beginTransaction();
                tr.replace(R.id.c_frame, cf2);
                tr.show(cf2);
                if(!th2.isAlive())
                    th2.start();
                if (th.isAlive())
                    th.interrupt();
                tr.commit();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle2 = Main2Activity.clintent.getExtras();
                bundle2.putInt("SY", s_y);
                bundle2.putInt("SM", s_m);
                bundle2.putInt("SD", s_d);
                bundle2.putInt("EY", e_y);
                bundle2.putInt("EM", e_m);
                bundle2.putInt("ED", e_d);
                Main2Activity.clintent.putExtras(bundle2);
                setResult(RESULT_OK, Main2Activity.clintent);
                th2.interrupt();
                th.interrupt();
                finish();
            }
        });

    }

    class cThread extends Thread {
        int num;

        cThread(int i) {
            num = i;
        }

        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                    bundle = Main2Activity.clintent.getExtras();
                    System.out.print("###");
                    if (num == 1) {
                        s_y = bundle.getInt("YEAR");
                        s_m = bundle.getInt("MONTH");
                        s_d = bundle.getInt("DAY");
                    } else if (num == 2) {
                        e_y = bundle.getInt("Year");
                        e_m = bundle.getInt("Month");
                        e_d = bundle.getInt("Day");
                    }
                } catch (Exception e) {
                }
            }
        }

    }
}
