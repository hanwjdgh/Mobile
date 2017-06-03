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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;


public class MainActivity extends AppCompatActivity {
    ImageButton mbutton;
    String phoneNum;
    int[] arr = {31,28,31,30,31,30,31,31,30,31,30,31};
    private static final int REQUEST_READ_PHONE_STATE_PERMISSION = 225;
    Server server = new Server();
    public static int votenum=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, SplachActivity.class));

        phoneNum = getPhoneNum();
        //Toast.makeText(getApplicationContext(),phoneNum,Toast.LENGTH_SHORT).show();
            new Thread() {
                @Override
                public void run() {
                    System.out.println("!!!");
                    HttpURLConnection con = server.getConnection("GET","/phonenum/" + phoneNum);
                    System.out.println("Connection done");
                    try {
                        System.out.println("code" + con.getResponseCode());
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
    public String getPhoneNum(){
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String outName = data.getStringExtra("title");
            String num = data.getStringExtra("number");
            String start = data.getStringExtra("start");
            String finish = data.getStringExtra("finish");

            if(outName.length()>0&&num.length()>0) {
                InsertMap(start,finish);
                makefragment(phoneNum,outName, num, calculate(start,finish)+"");
                server.Insertproject(phoneNum,phoneNum,outName, num,start,finish,0);
                String title = phoneNum.replace("+",outName);
                server.maketable(title,VoteActivtiy.data);
            }
        }
    }
    public void InsertMap(String start, String finish) {
        String[] arr1 = start.split("-");
        int tem = calculate(start, finish);
        int year = Integer.parseInt(arr1[0]), mon = Integer.parseInt(arr1[1]), day = Integer.parseInt(arr1[2]);
        for (int j = 0; j < tem; j++) {
            VoteActivtiy.data.put(year + "-" + mon + "-" + day, 0);

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
    public int calculate(String start, String finish){
        String[] arr1 = start.split("-");
        String[] arr2 = finish.split("-");
        int stem=0,ftem=0;
        for(int i=0; i<Integer.parseInt(arr1[1])-1;i++){
            stem+=arr[i];
        }
        stem+=Integer.parseInt(arr1[2]);
        for(int i=0; i<Integer.parseInt(arr2[1])-1;i++){
            ftem+=arr[i];
        }
        ftem+=Integer.parseInt(arr2[2]);
        if(ftem-stem>=0||ftem-stem+1>=0)
                return ftem - stem;
        else
            return 0;
    }

    public void makefragment(String master, String outName, String num, String day) {
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction tr = fm.beginTransaction();
        MainFragment cf = new MainFragment(MainActivity.this);
        Bundle bundle = new Bundle();
        bundle.putString("master", master);
        bundle.putString("Project", outName);
        bundle.putString("mCount", num);
        bundle.putString("day",day);
        cf.setArguments(bundle);
        tr.add(R.id.frame, cf, "counter");
        tr.commit();
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
            makefragment(order.getString("master"),order.getString("project"), order.getInt("meeting") + "",calculate(order.getString("start"),order.getString("finish"))+"");
            Main2Activity.Alarm.put("7",order.getInt("alarm7"));
            Main2Activity.Alarm.put("5",order.getInt("alarm5"));
            Main2Activity.Alarm.put("3",order.getInt("alarm3"));
            Main2Activity.Alarm.put("1",order.getInt("alarm1"));
        }
    }
}