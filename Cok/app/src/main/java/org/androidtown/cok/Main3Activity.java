package org.androidtown.cok;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.AppActionInfoBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;

/**
 * Created by LEE on 2017-05-17.
 */

public class Main3Activity extends AppCompatActivity  {
    TextView text1;
    TextView text2;
    Button btn;
    String title;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        text1 = (TextView)findViewById(R.id.textView5);
        text2 =(TextView)findViewById(R.id.textView6);
        btn = (Button)findViewById(R.id.btn);
        Intent data=getIntent();
        Bundle bundle = data.getExtras();
        title=bundle.getString("NAME").toString();
        String number =bundle.getString("NUM").toString();
        text1.setText(title);
        text2.setText(number);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void shareKakao(View v) {
        try {
            final KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder kakaoBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();

            kakaoBuilder.addText("텍스트");
            kakaoBuilder.addAppButton("제발",new AppActionBuilder().addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setExecuteParam("project"+"="+title).build()).build());
            kakaoLink.sendMessage(kakaoBuilder, this);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }
}
