package org.androidtown.cok;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.HashMap;

/**
 * Created by LEE on 2017-05-30.
 */

public class VoteActivtiy extends AppCompatActivity {
    String msg,msg1="+",project;
    String[] arr1,arr2;
    Server server = new Server();
    MainActivity mainActivity = new MainActivity();
    public static HashMap<String, Integer> data;
    Bundle bundle2;
    String s;
    Button btn;
    String phoneNum,meeting,start,finish;
    public static int setting=0;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote);
        btn = (Button)findViewById(R.id.button);
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
        data= new HashMap<>();
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
            }
        }.start();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                server.Insertproject(msg1,phoneNum,project,meeting,start,finish);
                //mainActivity.makefragment(msg1,arr1[1],meeting,mainActivity.calculate(start,finish)+"");
                Intent inte = new Intent(VoteActivtiy.this, MainActivity.class);
                startActivity(inte);
                finish();
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    private void arrayToobject(JSONArray jsonArray) throws JSONException {
        JSONObject order = jsonArray.getJSONObject(0);
        meeting = order.getString("meeting");
        start = order.getString("start");
        finish = order.getString("finish");
        String[] arr1 = order.getString("start").split("-");
        int tem = mainActivity.calculate(order.getString("start"), order.getString("finish"));
        int year = Integer.parseInt(arr1[0]), mon = Integer.parseInt(arr1[1]), day = Integer.parseInt(arr1[2]);

        for (int j = 0; j < tem; j++) {
            s=year + "-" + mon + "-" + day;
            data.put(year + "-" + mon + "-" + day, 0);
            makefragment(year + "-" + mon + "-" + day);

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
