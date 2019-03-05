package com.example.traintracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

public class ViewTrainSchedule extends AppCompatActivity {

    private ImageView sback;
    private TextView displayDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TextView viewScheduleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_train_schedule);

        viewScheduleButton = (TextView) findViewById(R.id.view_schedule);
        displayDate = (TextView) findViewById(R.id.date);
        sback = (ImageView)findViewById(R.id.sback);

        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ViewTrainSchedule.this,
                        //android.R.style.Theme_Hoio_Light_Dialog_MinWidth,
                        dateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable());
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date ="Date : "+ month + "/" + dayOfMonth + "/" + year;
                displayDate.setText(date);
            }
        };

        viewScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTrainSchedule();
            }
        });
        sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(ViewTrainSchedule.this, MainMenu.class);
                startActivity(it);

            }
        });
    }

    public void openTrainSchedule(){
        Intent intent = new Intent(ViewTrainSchedule.this, TrainSchedule.class);
        startActivity(intent);
    }
}

