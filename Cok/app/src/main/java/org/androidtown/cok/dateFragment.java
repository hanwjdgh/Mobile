package org.androidtown.cok;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by LEE on 2017-05-30.
 */

public class dateFragment extends Fragment {
    HashMap<String, Integer> info;
    TextView text;
    Context mainContext;
    CheckBox check;
    Intent intent;
    Bundle bundle2;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_date, container, false);
        text=(TextView)rootView.findViewById(R.id.text);
        check=(CheckBox)rootView.findViewById(R.id.check);
        final Bundle bundle = getArguments();
        text.setText(bundle.getString("start"));

        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check.isChecked()){
                    int count = info.get(bundle.getString("start"));
                    count++;
                    bundle2=new Bundle();
                    bundle2.putInt("c",count);
                    bundle2.putString("date",bundle.getString("start"));
                    intent.putExtras(bundle2);

                }
                else if(!check.isChecked()){
                    int count = info.get(bundle.getString("start"));
                    count--;
                    bundle2=new Bundle();
                    bundle2.putInt("c",count);
                    bundle2.putString("date",bundle.getString("start"));
                    intent.putExtras(bundle2);
                }

            }
        });

        return  rootView;
    }
    public dateFragment(Context _context, HashMap<String, Integer> data, Intent intent){
        mainContext = _context;
        info=data;
        this.intent=intent;
    }
}
