package org.androidtown.cok;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by LEE on 2017-05-30.
 */

public class VoteActivtiy extends AppCompatActivity {
    String msg,msg1="+",project;
    String[] arr1,arr2;
    Server server = new Server();
    MainActivity mainActivity = new MainActivity();
    public static Map<String, Integer> data = new HashMap<>();
    Bundle bundle2;
    String s, title;
    Button btn,btn1;
    String phoneNum,meeting,start,finish;
    public static int setting=0;
    Intent intent;
    int vote,people;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote);
        btn = (Button)findViewById(R.id.button);
        btn1 = (Button)findViewById(R.id.button1);
        phoneNum = getPhoneNum();

        if(setting==0) {
            intent = getIntent();
            Uri uri = intent.getData();
            msg = uri.getQueryParameter("project");
            arr1 = msg.split("/");
            arr2 = arr1[0].split(" ");
            msg1 += arr2[1];
            project = arr1[1];
        }
        else{
            intent = getIntent();
            Bundle bundle = intent.getExtras();
            msg1 = bundle.getString("master");
            project = bundle.getString("name");
        }
        title = msg1.replace("+",project);
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection con = server.getConnection("GET", "/project/" + msg1+"/"+project);
                try {
                    System.out.println("code" + con.getResponseCode());
                    arrayToobject(server.readJson(con));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                HttpURLConnection conn = server.getConnection("GET", "/map/" + title);
                try {
                    System.out.println("code" + conn.getResponseCode());
                    settingMap(server.readJson(conn));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                server.Insertproject(msg1,phoneNum,project,meeting,start,finish,1,people);
                //mainActivity.makefragment(msg1,arr1[1],meeting,mainActivity.calculate(start,finish)+"");
                server.maketable(title,data);
                Intent inte = new Intent(VoteActivtiy.this, MainActivity.class);
                startActivity(inte);
                finish();
            }
        });
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    private void settingMap(JSONArray jsonArray)throws JSONException{
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject order = jsonArray.getJSONObject(i);
            data.put(order.getString("date"),order.getInt("votenum"));
        }
    }
    private void arrayToobject(JSONArray jsonArray) throws JSONException {
        String str;
        JSONObject order = jsonArray.getJSONObject(0);
        meeting = order.getString("meeting");
        start = order.getString("start");
        finish = order.getString("finish");
        vote = order.getInt("vote");
        people = order.getInt("people");
        if(vote==1){
            btn.setVisibility(View.GONE);
        }
        String[] arr1 = order.getString("start").split("-");
        int tem = mainActivity.calculate(order.getString("start"), order.getString("finish"));
        int year = Integer.parseInt(arr1[0]), mon = Integer.parseInt(arr1[1]), day = Integer.parseInt(arr1[2]);

        for (int j = 0; j < tem; j++) {
            if(mon<10){
                if(day<10)
                    str = year + "-" + "0"+mon + "-" + "0"+day;
                else
                    str = year + "-" + "0"+mon + "-" + day;
            }
            else{
                if(day<10)
                    str = year + "-" + mon + "-" + "0"+day;
                else
                    str = year + "-" + mon + "-" + day;
            }

            s=str;
            makefragment(str);

            if (mon == 2 && day == 28) {
                mon += 1;
                day = 1;
            } else if ((mon == 4 || mon == 6 || mon == 9 || mon == 11) && day == 30) {
                mon += 1;
                day = 1;
            } else if ((mon == 1 || mon == 3 || mon == 5 || mon == 7 || mon == 8 || mon == 10 || mon == 12) && day == 31) {
                if (mon == 12)
                    mon = 1;
                else
                    mon += 1;
                day = 1;
            }
            else
                day++;
        }
    }

    public void makefragment(String start) {
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction tr = fm.beginTransaction();
        dateFragment cf = new dateFragment(VoteActivtiy.this);
        bundle2 = new Bundle();
        bundle2.putInt("count",data.get(start));
        bundle2.putString("start", start);

        cf.setArguments(bundle2);
        tr.add(R.id.sframe, cf, "co");
        tr.commit();
    }
    public String getPhoneNum(){
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }

}
