package org.androidtown.cok;

import android.app.Activity;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by GE62 on 2017-05-15.
 */

public class MainFragment extends Fragment {
    TextView pName, mCount, mcount;
    ProgressBar bar;
    ProgressHandler handler;
    Button btn;
    TextView day;
    boolean isRunning = false;
    Context mainContext;
    Bundle extra;
    String mas, fdate;
    Map<String, Integer> Alam;
    int cur;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);
        bar = (ProgressBar) rootView.findViewById(R.id.progress);
        pName = (TextView) rootView.findViewById(R.id.text);
        mCount = (TextView) rootView.findViewById(R.id.text2);
        mcount = (TextView) rootView.findViewById(R.id.text3);
        day = (TextView) rootView.findViewById(R.id.day);
        btn = (Button) rootView.findViewById(R.id.btn);

        handler = new ProgressHandler();
        extra = getArguments();
        mas = extra.getString("master").toString();
        pName.setText(extra.getString("Project").toString());
        mCount.setText(extra.getString("mCount").toString());
        mcount.setText(extra.getString("mcount").toString());
        String tm = extra.getString("day").toString();
        cur = extra.getInt("cur");
        day.setText((Integer.parseInt(tm) - cur) + "일");
        fdate = extra.getString("finish");

        bar.setMax(Integer.parseInt(extra.getString("day")));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainContext, Main3Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("master", mas);
                bundle.putString("NAME", pName.getText().toString());
                bundle.putString("NUM", mcount.getText().toString());
                bundle.putString("Start", extra.getString("start"));
                bundle.putString("Finish", fdate);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public MainFragment(Context _context, Map map) {
        mainContext = _context;
        Alam = map;
    }

    public void onStart() {
        super.onStart();
        bar.setProgress(cur);
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                try {
                    int i;
                    for (i = 0; i < Integer.parseInt(extra.getString("day")) && isRunning; i--) {
                        Thread.sleep(6000);
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                    }
                } catch (Exception ex) {
                    Log.e("MainActivity", "Exception in processing message.", ex);
                }
            }
        });
        isRunning = true;
        thread1.start();
    }


    public class ProgressHandler extends Handler {
        public void handleMessage(Message msg) {
            bar.incrementProgressBy(1);
        }

    }
}
