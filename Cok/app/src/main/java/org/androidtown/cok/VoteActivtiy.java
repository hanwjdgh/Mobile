package org.androidtown.cok;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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
    String msg,msg1="+";
    String[] arr1,arr2;
    Server server = new Server();
    MainActivity mainActivity = new MainActivity();
    HashMap<String, Integer> data;
    Intent intent;
    Intent fintent = new Intent();
    Bundle bundle3;
    Bundle bundle2;
    Thread th;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vote);
        Intent intent = getIntent();
        Uri uri = intent.getData();
        msg = uri.getQueryParameter("project");

        arr1 = msg.split("/");
        arr2 = arr1[0].split(" ");
        msg1+=arr2[1];
       // Toast.makeText(getApplicationContext(), msg1, Toast.LENGTH_SHORT).show();
        data= new HashMap<>();
        th=new cThread();
        th.start();

        new Thread() {
            @Override
            public void run() {
                HttpURLConnection con = server.getConnection("GET", "/project/" + msg1+"/"+arr1[1]);
                try {
                    System.out.println("code" + con.getResponseCode());
                    arrayToobject(server.readJson(con));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    private void arrayToobject(JSONArray jsonArray) throws JSONException {
        JSONObject order = jsonArray.getJSONObject(0);
        String[] arr1 = order.getString("start").split("-");
        int tem = mainActivity.calculate(order.getString("start"), order.getString("finish"));
        int year = Integer.parseInt(arr1[0]), mon = Integer.parseInt(arr1[1]), day = Integer.parseInt(arr1[2]);

        for (int j = 0; j < tem; j++) {
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
            data.put(year + "-" + mon + "-" + day, 0);
            makefragment(year + "-" + mon + "-" + day);
        }
    }

    public void makefragment(String start) {
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction tr = fm.beginTransaction();
        dateFragment cf = new dateFragment(VoteActivtiy.this, data, fintent);
        bundle2 = new Bundle();

        bundle2.putString("start", start);

        cf.setArguments(bundle2);
        tr.add(R.id.sframe, cf, "co");
        tr.commit();
    }
    class cThread extends Thread {
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                    bundle3= intent.getExtras();
                    data.put(bundle3.getString("date"),bundle3.getInt("c"));
                    Toast.makeText(getApplicationContext(),bundle3.getString("date")+"  : "+bundle3.getInt("c"),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                }
            }
        }
    }

}
