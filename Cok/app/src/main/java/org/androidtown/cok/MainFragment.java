package org.androidtown.cok;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

/**
 * Created by GE62 on 2017-05-15.
 */

public class MainFragment extends Fragment {
    ProgressBar bar;
    ProgressHandler handler;
    boolean isRunning =false;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_main,container,false);
        bar = (ProgressBar)rootView.findViewById(R.id.progress);
        handler = new ProgressHandler();
        return rootView;
    }
    @Override
    public void onStart() {
        super.onStart();
        bar.setProgress(0);
        Thread thread1 = new Thread(new Runnable(){
            public void run(){
                try{for(int i=0; i<20 && isRunning; i++){
                    Thread.sleep(1000);
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);            }}
                catch(Exception ex){
                    Log.e("MainActivity", "Exception in processing message.", ex);
                }}
        });
        isRunning = true;
        thread1.start();
    }
    public class ProgressHandler extends Handler {
        public void handleMessage(Message msg){
            bar.incrementProgressBy(5);
        }
    }
}
