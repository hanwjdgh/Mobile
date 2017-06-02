package org.androidtown.cok;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by LEE on 2017-05-30.
 */

public class dateFragment extends Fragment {
    TextView text,text2;
    Context mainContext;
    RelativeLayout layout;
    int ct = 0;
    Bundle bundle;
    int count;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_date, container, false);
        text = (TextView) rootView.findViewById(R.id.text);
        text2 = (TextView) rootView.findViewById(R.id.text2);
        layout = (RelativeLayout) rootView.findViewById(R.id.relative);
        bundle = getArguments();
        text2.setText(VoteActivtiy.data.get(bundle.getString("start"))+"");

        text.setText(bundle.getString("start"));

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ct++;
                if (ct % 2 == 1) {
                    layout.setBackgroundColor(Color.RED);
                    count = VoteActivtiy.data.get(bundle.getString("start"));
                    count++;
                    text2.setText(count + "");
                    VoteActivtiy.data.put(bundle.getString("start"), count);
                } else if (ct % 2 == 0) {
                    layout.setBackgroundColor(Color.WHITE);
                    count = VoteActivtiy.data.get(bundle.getString("start"));
                    count--;
                    text2.setText(count + "");
                    VoteActivtiy.data.put(bundle.getString("start"), count);
                }

            }
        });

        return rootView;
    }

    public dateFragment(Context _context) {
        mainContext = _context;
    }
}
