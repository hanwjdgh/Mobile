package org.androidtown.cok;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * Created by GE62 on 2017-05-10.
 */

public class Calendar extends AppCompatActivity {

    Button button;
    Button s_btn;
    Button e_btn;
    FragmentManager fm;
    Thread th;
    Thread th2;
    CalendarFragment cf;
    CalendarFragment2 cf2;
    Bundle bundle;
    Intent intent;
    int s_y, s_m, s_d, e_y, e_m, e_d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        intent = getIntent();
        cf = new CalendarFragment(Calendar.this, intent);
        cf2 = new CalendarFragment2(Calendar.this, intent);
        s_btn = (Button) findViewById(R.id.btn);
        e_btn = (Button) findViewById(R.id.btn2);
        th = new cThread(1);
        th2 = new cThread(2);
        fm = getFragmentManager();
        FragmentTransaction tr = fm.beginTransaction();
        tr.add(R.id.c_frame, cf, "ccc");
        tr.add(R.id.c_frame, cf2, "cccc");

        if (intent.getFlags() == 1) {
            e_btn.setBackgroundColor(Color.WHITE);
            s_btn.setBackgroundColor(Color.RED);
            tr.hide(cf2);
            th.start();
        } else if (intent.getFlags() == 2) {
            e_btn.setBackgroundColor(Color.RED);
            s_btn.setBackgroundColor(Color.WHITE);
            tr.hide(cf);
            th.start();
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
                Bundle bundle2 = intent.getExtras();
                bundle2.putInt("SY", s_y);
                bundle2.putInt("SM", s_m);
                bundle2.putInt("SD", s_d);
                bundle2.putInt("EY", e_y);
                bundle2.putInt("EM", e_m);
                bundle2.putInt("ED", e_d);
                intent.putExtras(bundle2);
                setResult(RESULT_OK, intent);
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
                    bundle = intent.getExtras();
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
