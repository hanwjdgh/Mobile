package org.androidtown.cok;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    ImageButton mbutton;
    String phoneNum;
    int[] arr = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final int REQUEST_READ_PHONE_STATE_PERMISSION = 225;
    Server server = new Server();
    public static Map<String, Integer> Ala = new HashMap<String, Integer>();
    String setCurDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, SplachActivity.class));

        phoneNum = getPhoneNum();
        setDate();
        new Thread() {
            @Override
            public void run() {
                System.out.println("!!!");
                HttpURLConnection con = server.getConnection("GET", "/phonenum/" + phoneNum);
                System.out.println("Connection done");
                try {
                    System.out.println("codeasd " + con.getResponseCode());
                    arrayToobject(server.readJson(con));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }.start();


        mbutton = (ImageButton) findViewById(R.id.m_button);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Main2Activity.class);
                Bundle bundle = new Bundle();
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
            }
        });
    }
    private void setDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat SettingFormat = new SimpleDateFormat("yyyy-MM-dd");
        setCurDate = SettingFormat.format(date);
    }
    public String getPhoneNum() {
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String outName = data.getStringExtra("title");
            String people = data.getStringExtra("people");
            String num = data.getStringExtra("number");
            String start = data.getStringExtra("start");
            String finish = data.getStringExtra("finish");
            int cur = calculate(start,setCurDate);
            System.out.print("123@@ " +cur);
            if (outName.length() > 0 && num.length() > 0 && people.length() > 0) {
                InsertMap(start, finish);
                makefragment(phoneNum, outName, people, num, calculate(start, finish) + "",cur);
                server.Insertproject(phoneNum, phoneNum, outName, num, start, finish, 0, Integer.parseInt(people));
                String title = phoneNum.replace("+", outName);
                server.maketable(title, VoteActivtiy.data);
            }
        }
    }

    public void InsertMap(String start, String finish) {
        String[] arr1 = start.split("-");
        String str;
        int tem = calculate(start, finish);
        int year = Integer.parseInt(arr1[0]), mon = Integer.parseInt(arr1[1]), day = Integer.parseInt(arr1[2]);
        for (int j = 0; j < tem; j++) {
            if (mon < 10) {
                if (day < 10)
                    str = year + "-" + "0" + mon + "-" + "0" + day;
                else
                    str = year + "-" + "0" + mon + "-" + day;
            } else {
                if (day < 10)
                    str = year + "-" + mon + "-" + "0" + day;
                else
                    str = year + "-" + mon + "-" + day;
            }
            VoteActivtiy.data.put(str, 0);

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
            } else
                day++;
        }
    }

    public int calculate(String start, String finish) {
        String[] arr1 = start.split("-");
        String[] arr2 = finish.split("-");
        int stem = 0, ftem = 0;
        for (int i = 0; i < Integer.parseInt(arr1[1]) - 1; i++) {
            stem += arr[i];
        }
        stem += Integer.parseInt(arr1[2]);
        for (int i = 0; i < Integer.parseInt(arr2[1]) - 1; i++) {
            ftem += arr[i];
        }
        ftem += Integer.parseInt(arr2[2]);
        if (ftem - stem >= 0)
            return ftem - stem;
        else
            return -1;
    }

    public void makefragment(final String master, final String outName, String peo, String num, String day, int cur) {
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection conn = server.getConnection("GET", "/galarm/" + master + "/" + phoneNum + "/" + outName);
                try {
                    System.out.println("code" + conn.getResponseCode());
                    setalarm(server.readJson(conn));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction tr = fm.beginTransaction();
        MainFragment cf = new MainFragment(MainActivity.this,Ala);
        Bundle bundle = new Bundle();
        bundle.putString("master", master);
        bundle.putString("Project", outName);
        bundle.putString("mCount", peo);
        bundle.putString("mcount", num);
        bundle.putString("day", day);
        bundle.putInt("cur",cur);
        cf.setArguments(bundle);
        tr.add(R.id.frame, cf, "counter");
        tr.commit();
    }

    private void setalarm(JSONArray jsonArray) throws JSONException {
        JSONObject order = jsonArray.getJSONObject(0);
        Ala.put("1", order.getInt("alarm1"));
        Ala.put("3", order.getInt("alarm3"));
        Ala.put("5", order.getInt("alarm5"));
        Ala.put("7", order.getInt("alarm7"));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_READ_PHONE_STATE_PERMISSION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("TAG", "Permission Granted");
                    //Proceed to next steps
                } else {
                    Log.e("TAG", "Permission Denied");
                }
                return;
            }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void arrayToobject(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject order = jsonArray.getJSONObject(i);
            makefragment(order.getString("master"), order.getString("project"), order.getInt("people") + "", order.getInt("meeting") + "", calculate(order.getString("start"), order.getString("finish")) + "",calculate(order.getString("start"),setCurDate));
        }
    }
}