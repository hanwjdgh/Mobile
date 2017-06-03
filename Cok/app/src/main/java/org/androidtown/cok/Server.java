package org.androidtown.cok;

import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by GE62 on 2017-05-30.
 */

public class Server {

    public void Insertproject(final String master,final String phoneNum, final String name, final String num, final String start, final String finish, final int vote) {
        new Thread() {
            @Override
            public void run() {
                HttpURLConnection con = getConnection("POST","/add");
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("master",master);
                    jsonObject.put("phonenum",phoneNum);
                    jsonObject.put("project", name);
                    jsonObject.put("meeting", Integer.parseInt(num));
                    jsonObject.put("start",start);
                    jsonObject.put("finish",finish);
                    jsonObject.put("vote",vote);
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
    public void maketable(final String title, final Map Map){
        new Thread(){
            @Override
            public void run(){
                HttpURLConnection con = getConnection("POST","/table");
                JSONObject jsonObject = new JSONObject();
                JSONObject json = new JSONObject();
                try{
                    jsonObject.put("title",title);
                    Set<Map.Entry<String, Integer>> entries = Map.entrySet();
                    Iterator<Map.Entry<String, Integer>> i = entries.iterator();

                    while(i.hasNext()) {
                        Map.Entry<String, Integer> entry = i.next();
                        json.put(entry.getKey(),entry.getValue());
                    }
                    jsonObject.put("map",json);
                }catch (Exception e){}
                sendJson(con, jsonObject);
                try {
                    System.out.println("code2" + con.getResponseCode());
                    System.out.println("Result is: " + jsonObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public HttpURLConnection getConnection(String method, String path) {
        try {
           // URL url = new URL("http://218.51.198.118:3000" + path);
            URL url = new URL("http://192.168.0.113:3000" + path);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "application/json");
            System.out.println("\nSending +request to: " + url.toString());
            return con;
        } catch (Exception e) {
            return null;
        }
    }

    public JSONArray readJson(HttpURLConnection con) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return new JSONArray(response.toString());
        } catch (Exception e) {
            return null;
        }
    }
    private void sendJson(HttpURLConnection con, JSONObject json) {
        try {
            OutputStream out = con.getOutputStream();
            out.write(json.toString().getBytes());
            out.flush();
        } catch (Exception e) { }
    }

}
