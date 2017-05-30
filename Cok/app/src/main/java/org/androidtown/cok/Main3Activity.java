package org.androidtown.cok;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        String title=bundle.getString("NAME").toString();
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
              /*메시지 추가*/
            kakaoBuilder.addText("투표하세요.");

             /*앱 실행버튼 추가*/
            kakaoBuilder.addAppButton("앱 실행 혹은 다운로드");

            /*메시지 발송*/
            kakaoLink.sendMessage(kakaoBuilder, this);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }
}
