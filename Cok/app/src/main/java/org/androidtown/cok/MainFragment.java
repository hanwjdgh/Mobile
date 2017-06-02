package org.androidtown.cok;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
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
    boolean isRunning =false;
    Context mainContext;
    Bundle extra;
    String mas;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main,container,false);
        bar = (ProgressBar)rootView.findViewById(R.id.progress);
        pName =(TextView)rootView.findViewById(R.id.text);
        mCount =(TextView)rootView.findViewById(R.id.text2);
        mcount = (TextView)rootView.findViewById(R.id.text3);
        day = (TextView)rootView.findViewById(R.id.day);
        btn =(Button)rootView.findViewById(R.id.btn);
        //percent = (TextView)rootView.findViewById(R.id.percent) ;

        handler = new ProgressHandler();
        extra = getArguments();
        mas = extra.getString("master").toString();
        pName.setText(extra.getString("Project").toString());
        //mCount.setText(extra.getString("mCount").toString());
        mcount.setText(extra.getString("mCount").toString());
        day.setText(extra.getString("day").toString()+"일");

        bar.setMax(Integer.parseInt(extra.getString("day")));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainContext,Main3Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("master",mas);
                bundle.putString("NAME",pName.getText().toString());
                bundle.putString("NUM",mcount.getText().toString());
                bundle.putString("Start",extra.getString("start"));
                bundle.putString("Finsih",extra.getString("finish"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return rootView;
    }

    public MainFragment(Context _context){
        mainContext = _context;
    }

    public void onStart() {
        super.onStart();
        bar.setProgress(0);
        Thread thread1 = new Thread(new Runnable(){
            public void run(){
                try{
                    for(int i=0; i<Integer.parseInt(extra.getString("day")) && isRunning; i++){
                        Thread.sleep(1000);
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                }
                }
                catch(Exception ex){
                    Log.e("MainActivity", "Exception in processing message.", ex);
                }}
        });
        isRunning = true;
        thread1.start();
    }




    public class ProgressHandler extends Handler {
        public void handleMessage(Message msg){
            bar.incrementProgressBy(1);
        }
    }
}
