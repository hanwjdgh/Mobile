package org.androidtown.cok;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Main2Activity extends AppCompatActivity {
    Button btn_up,btn_down;
    Button Fbutton,Cbutton;
    TextView text;
    EditText title;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        setup();
        Cbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cintent = new Intent(getApplicationContext(),Calendar.class);
                startActivity(cintent);
            }
        });
        Fbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                Bundle bundle = intent.getExtras();
                bundle.putString("title",title.getText().toString());
                bundle.putInt("number",count);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    private void setup() {
        title = (EditText)findViewById(R.id.editText3);
        btn_up = (Button) findViewById(R.id.buttonp);
        btn_down = (Button) findViewById(R.id.buttonm);
        text = (TextView) findViewById(R.id.count);
        Fbutton = (Button)findViewById(R.id.finish);
        Cbutton = (Button)findViewById(R.id.button);
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
