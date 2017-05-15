package org.androidtown.cok;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Toast;

/**
 * Created by GE62 on 2017-05-10.
 */

public class Calendar extends AppCompatActivity{
    CalendarView calendarView;
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar);
        calendarView = (CalendarView)findViewById(R.id.calendar);
        button = (Button)findViewById(R.id.button3);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Toast.makeText(getApplicationContext(), "Selected Date:\n" + "Day = " + dayOfMonth + "\n" + "Month = " + (month+1)+ "\n" + "Year = " + year, Toast.LENGTH_LONG).show();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
