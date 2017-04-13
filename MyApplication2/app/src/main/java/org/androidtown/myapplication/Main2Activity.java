package org.androidtown.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Main2Activity extends AppCompatActivity {
    Button btn_up,btn_down;
    Button Fbutton;
    TextView text;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Fbutton = (Button)findViewById(R.id.finish);
        setup();
        Fbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setup() {
        btn_up = (Button) findViewById(R.id.buttonp);
        btn_down = (Button) findViewById(R.id.buttonm);
        text = (TextView) findViewById(R.id.count);

        btn_up.setOnClickListener(listener);
        btn_down.setOnClickListener(listener);
    }

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
                    if(count<0){
                        Toast.makeText(getApplicationContext(),"음수ㄴㄴ",Toast.LENGTH_SHORT).show();
                       count=0;
                        break;
                    }
                    text.setText("" + count);
                    break;
            }
        }
    };
}
