package org.androidtown.cok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {
    String s, f;
    Button btn_up, btn_down;
    Button Fbutton, sd_button, fd_button;
    Button Pup,Pdwn;
    TextView text, t,ptxt;
    EditText title;
    int count = 0,pcnt=0;
    public  static  Intent clintent;
    public static int YEAR,MON,DAY,year,mon,day1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setup();
        setDate();
        sd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clintent = new Intent(Main2Activity.this, CalendarActivity.class);
                clintent.setFlags(1);
                Bundle bundle = new Bundle();
                clintent.putExtras(bundle);
                startActivityForResult(clintent, 1);
            }
        });
        fd_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clintent = new Intent(Main2Activity.this, CalendarActivity.class);
                clintent.setFlags(2);
                Bundle bundle = new Bundle();
                clintent.putExtras(bundle);
                startActivityForResult(clintent, 2);
            }
        });
        Fbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
                bundle.putString("title", title.getText().toString());
                bundle.putString("people",pcnt+"");
                bundle.putString("number", count + "");
                bundle.putString("start", s);
                bundle.putString("finish", f);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            sd_button.setText("Start  :" + YEAR + " 년 " + MON + " 월 " + DAY + " 일");
            fd_button.setText("Finish :" + year + " 년 " + mon + " 월 " + day1 + " 일");
            if (MON < 10) {
                if (DAY < 10)
                    s = YEAR + "-" + "0" + MON + "-" + "0" + DAY;
                else
                    s = YEAR + "-" + "0" + MON + "-" + DAY;
            } else {
                if (DAY < 10)
                    s = YEAR + "-" + MON + "-" + "0" + DAY;
                else
                    s = YEAR + "-" + MON + "-" + DAY;
            }
            if (mon < 10) {
                if (day1 < 10)
                    f = year + "-" + "0" + mon + "-" + "0" + day1;
                else
                    f = year + "-" + "0" + mon + "-" + day1;
            } else {
                if (day1 < 10)
                    f = year + "-" + mon + "-" + "0" + day1;
                else
                    f = year + "-" + mon + "-" + day1;
            }
        }
    }


    private void setDate() {
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        SimpleDateFormat SettingFormat = new SimpleDateFormat("yyyy-MM-dd");
        String strCurDate = CurDateFormat.format(date);
        String setCurDate = SettingFormat.format(date);
        sd_button.setText("Start  " + "     " + strCurDate);
        fd_button.setText("Finish " + "     " + strCurDate);
        String[] arr = setCurDate.split("-");
        YEAR=year = Integer.parseInt(arr[0]);
        MON=mon = Integer.parseInt(arr[1]);
        DAY=day1= Integer.parseInt(arr[2]);
        s = f = setCurDate;
    }

    private void setup() {
        t = (TextView) findViewById(R.id.tt);
        title = (EditText) findViewById(R.id.editText3);
        btn_up = (Button) findViewById(R.id.buttonp);
        btn_down = (Button) findViewById(R.id.buttonm);
        text = (TextView) findViewById(R.id.count);
        Fbutton = (Button) findViewById(R.id.finish);
        sd_button = (Button) findViewById(R.id.sd_dutton);
        fd_button = (Button) findViewById(R.id.fd_button);
        btn_up.setOnClickListener(listener);
        btn_down.setOnClickListener(listener);
        ptxt = (TextView)findViewById(R.id.textView12);
        Pup = (Button)findViewById(R.id.button4);
        Pdwn = (Button)findViewById(R.id.button2);
        Pup.setOnClickListener(listen);
        Pdwn.setOnClickListener(listen);
    }
    View.OnClickListener listen = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.button4:
                    pcnt++;
                    ptxt.setText("" + pcnt);
                    break;
                case R.id.button2:
                    pcnt--;
                    if (pcnt < 0) {
                        Toast.makeText(getApplicationContext(), "음수ㄴㄴ", Toast.LENGTH_SHORT).show();
                        pcnt = 0;
                        break;
                    }
                    ptxt.setText("" + pcnt);
                    break;
            }
        }
    };

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonp:
                    count++;
                    text.setText("" + count);
                    break;
                case R.id.buttonm:
                    count--;
                    if (count < 0) {
                        Toast.makeText(getApplicationContext(), "음수ㄴㄴ", Toast.LENGTH_SHORT).show();
                        count = 0;
                        break;
                    }
                    text.setText("" + count);
                    break;
            }
        }
    };
}
