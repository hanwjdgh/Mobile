package org.androidtown.cok;

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

/**
 * Created by GE62 on 2017-05-15.
 */

public class MainFragment extends Fragment {
    TextView pName;
    TextView mCount;
    TextView mcount;
    TextView percent;
    ProgressBar bar;
    ProgressHandler handler;
    Button btn;
    TextView day;
    boolean isRunning = false;
    Context mainContext;
    Bundle extra;
    String mas;

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
        //percent = (TextView)rootView.findViewById(R.id.percent) ;

        handler = new ProgressHandler();
        extra = getArguments();
        mas = extra.getString("master").toString();
        pName.setText(extra.getString("Project").toString());
        //mCount.setText(extra.getString("mCount").toString());
        mcount.setText(extra.getString("mCount").toString());
        day.setText(extra.getString("day").toString() + "일");

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
                bundle.putString("Finsih", extra.getString("finish"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public MainFragment(Context _context) {
        mainContext = _context;
    }

    public void onStart() {
        super.onStart();
        bar.setProgress(0);
        Thread thread1 = new Thread(new Runnable() {
            public void run() {
                try {
                    int i;
                    for (i = 0; i < Integer.parseInt(extra.getString("day")) && isRunning; i++) {
                        Thread.sleep(5000);
                        Message msg = handler.obtainMessage();
                        handler.sendMessage(msg);
                        handler.notifi(i);

                    }
                } catch (Exception ex) {
                    Log.e("MainActivity", "Exception in processing message.", ex);
                }
            }
        });
        isRunning = true;
        thread1.start();
    }
    public void NotificationSomethings() {


        Resources res = getResources();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(mainContext);

        builder.setContentTitle("상태바 드래그시 보이는 타이틀")
                .setContentText("상태바 드래그시 보이는 서브타이틀")
                .setTicker("상태바 한줄 메시지")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL);



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            builder.setCategory(Notification.CATEGORY_MESSAGE)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
        }

        NotificationManager nm = (NotificationManager) mainContext.getSystemService(mainContext.NOTIFICATION_SERVICE);
        nm.notify(1234, builder.build());
    }




    public class ProgressHandler extends Handler {
        public void handleMessage(Message msg) {
            bar.incrementProgressBy(1);
        }
        public void notifi(int i){
            if(i==100)
                NotificationSomethings();
        }
    }
}
