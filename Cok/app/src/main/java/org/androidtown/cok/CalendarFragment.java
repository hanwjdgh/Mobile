package org.androidtown.cok;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by LEE on 2017-05-23.
 */

public class CalendarFragment extends Fragment {
    CalendarView calendarView;

    Bundle bundle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.calendar2,container,false);
        calendarView = (CalendarView)rootView.findViewById(R.id.calendar);
        long now = System.currentTimeMillis();

        final Date date = new Date(now);

        //연,월,일을 따로 저장

        final SimpleDateFormat curYearFormat = new SimpleDateFormat("yyyy", Locale.KOREA);

        final SimpleDateFormat curMonthFormat = new SimpleDateFormat("MM", Locale.KOREA);

        final SimpleDateFormat curDayFormat = new SimpleDateFormat("dd", Locale.KOREA);


        bundle =Main2Activity.clintent.getExtras();
        bundle.putInt("YEAR", Integer.parseInt(String.valueOf(curYearFormat.format(date))));
        bundle.putInt("MONTH", Integer.parseInt(String.valueOf(curMonthFormat.format(date))));
        bundle.putInt("DAY",Integer.parseInt(String.valueOf(curDayFormat.format(date))));
        Main2Activity.clintent.putExtras(bundle);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Main2Activity.YEAR=year;
                Main2Activity.MON = month+1;
                Main2Activity.DAY = dayOfMonth;
                bundle.putInt("YEAR",year);
                bundle.putInt("MONTH",month+1);
                bundle.putInt("DAY",dayOfMonth);
                Main2Activity.clintent.putExtras(bundle);
            }
        });
        return rootView;

    }
    public CalendarFragment(){
    }
}