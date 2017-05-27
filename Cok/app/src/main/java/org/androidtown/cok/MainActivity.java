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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends AppCompatActivity {
    ImageButton mbutton;
    String phoneNum;
    String strqwe = "";

    private static final int REQUEST_READ_PHONE_STATE_PERMISSION = 225;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startActivity(new Intent(this, SplachActivity.class));

        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        phoneNum = telephonyManager.getLine1Number();
        //readJson(getConnection("/"));
        new Thread() {
            @Override
            public void run() {
                System.out.println("!!!");
                HttpURLConnection con = getConnection("GET","/phonenum/" + phoneNum);
                System.out.println("Connection done");
                try {
                    System.out.println("code" + con.getResponseCode());
                    arrayToobject(readJson(con));
                    System.out.println("Result is: " + strqwe);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String outName = data.getStringExtra("title");
        String num = data.getStringExtra("number");
       // Toast.makeText(getApplicationContext(), outName + " " + num, Toast.LENGTH_LONG).show();
        makefragment(outName, num);
        Insertproject(outName, num);
    }

    public void makefragment(String outName, String num) {
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction tr = fm.beginTransaction();
        MainFragment cf = new MainFragment(MainActivity.this);
        Bundle bundle = new Bundle();
        bundle.putString("Project", outName);
        bundle.putString("mCount", num);
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

    private HttpURLConnection getConnection(String method,String path) {
        try {
            URL url = new URL("http://192.168.219.119:3000" + path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "application/json");
            System.out.println("\nSending +request to: " + url.toString());
            return con;
        } catch (Exception e) {
            return null;
        }
    }

    private JSONArray readJson(HttpURLConnection con) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                strqwe += inputLine;
                response.append(inputLine);
            }
            in.close();
            return new JSONArray(response.toString());
        } catch (Exception e) {
            return null;
        }
    }

    private void arrayToobject(JSONArray jsonArray) throws JSONException {
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject order = jsonArray.getJSONObject(i);
//            result += "phonenum: " + order.getString("phonenum") + ", project: " + order.getString("project") +
//                    ", people: " + order.getInt("people") + ", meeting: " + order.getInt("meeting")+"\n";
            makefragment(order.getString("project"), order.getInt("meeting") + "");
        }
    }

    public void Insertproject(final String name, final String num) {
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection con = getConnection("POST","/add");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("phonenum",phoneNum);
                    jsonObject.put("project", name);
                    jsonObject.put("meeting", Integer.parseInt(num));
                } catch (Exception e) {
                }
                sendJson(con, jsonObject);
                try {
                    System.out.println("code1" + con.getResponseCode());
                    System.out.println("Result is: " + jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    private void sendJson(HttpURLConnection con, JSONObject json) {
        try {
            OutputStream out = con.getOutputStream();
            out.write(json.toString().getBytes());
            out.flush();
        } catch (Exception e) { }
    }

}
