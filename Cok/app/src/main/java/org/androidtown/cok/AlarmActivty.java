package org.androidtown.cok;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

/**
 * Created by LEE on 2017-06-03.
 */

public class AlarmActivty extends Activity {
    ListView listView;
    Button btn;
    Intent intent;
    Bundle bundle;
    String[] a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.alarm);
        intent= getIntent();
        listView = (ListView) findViewById(R.id.listview);
        btn = (Button)findViewById(R.id.btn);
        ArrayAdapter<CharSequence> adapterOfListViewLanguages = ArrayAdapter.createFromResource(
                this,
                R.array.strarray_languages,
                android.R.layout.simple_list_item_single_choice
        );
        listView.setAdapter(adapterOfListViewLanguages);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        if(Main3Activity.Alarm.get("7")==0)
                            Main3Activity.Alarm.put("7",1);
                        else
                            Main3Activity.Alarm.put("7",0);
                        break;
                    case 1:
                        if(Main3Activity.Alarm.get("5")==0)
                            Main3Activity.Alarm.put("5",1);
                        else
                            Main3Activity.Alarm.put("5",0);
                        break;
                    case 2:
                        if(Main3Activity.Alarm.get("3")==0)
                            Main3Activity.Alarm.put("3",1);
                        else
                            Main3Activity.Alarm.put("3",0);
                        break;
                    case 3:
                        if(Main3Activity.Alarm.get("1")==0)
                            Main3Activity.Alarm.put("1",1);
                        else
                            Main3Activity.Alarm.put("1",0);
                        break;
                }
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}
