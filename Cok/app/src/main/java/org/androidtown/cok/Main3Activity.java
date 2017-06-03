package org.androidtown.cok;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.AppActionInfoBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

/**
 * Created by LEE on 2017-05-17.
 */

public class Main3Activity extends AppCompatActivity  {
    TextView text1,text2,txt;
    Button btn,btn2,btn3;
    String title,mas,title1;
    VoteActivtiy vote = new VoteActivtiy();
    String phoneNum,number;
    Server server = new Server();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        phoneNum = getPhoneNum();
        text1 = (TextView)findViewById(R.id.textView5);
        text2 =(TextView)findViewById(R.id.textView6);
        txt = (TextView)findViewById(R.id.txt);
        btn = (Button)findViewById(R.id.btn);
        btn2 = (Button)findViewById(R.id.btn2);
        btn3 = (Button)findViewById(R.id.btn3);
        Intent data=getIntent();
        final Bundle bundle = data.getExtras();
        mas =bundle.getString("master").toString();
        title=bundle.getString("NAME").toString();
        number =bundle.getString("NUM").toString();
        text1.setText(title);
        text2.setText(number);

        if(!(mas.equals(phoneNum))){
            btn2.setVisibility(View.GONE);
            btn3.setVisibility(View.VISIBLE);
        }
        else{
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);
        }

        title1 = mas.replace("+",title);
        new Thread(){
            @Override
            public void run(){
                HttpURLConnection con = server.getConnection("GET", "/gmap/"+title1+"/"+number);
                try {
                    System.out.println("code" + con.getResponseCode());
                    meetdate(server.readJson(con));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vote.setting =1;
                Intent intent = new Intent(Main3Activity.this,VoteActivtiy.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("master",mas);
                bundle1.putString("name",title);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void meetdate(JSONArray jsonArray) throws JSONException {
        String str="";
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject order = jsonArray.getJSONObject(i);
            str+=order.getString("date");
            str+="\n";
        }
        txt.setText(str);
    }
    public void shareKakao(View v) {
        try {
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            kakaoBuilder.addText("텍스트");
            kakaoBuilder.addAppButton("제발",new AppActionBuilder().addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("project"+"="+phoneNum+"/"+title).build()).build());
            kakaoLink.sendMessage(kakaoBuilder, this);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }
    public String getPhoneNum(){
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }
}
