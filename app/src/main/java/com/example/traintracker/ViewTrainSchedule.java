package com.example.traintracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ViewTrainSchedule extends AppCompatActivity {

    private ImageView sback;
    private TextView displayDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private TextView viewScheduleButton;
    DatabaseHelper myDb;
    EditText fromStation;
    EditText toStation;
    TextView date;
    EditText startTime;
    EditText endTime;
    String FromStation,ToStation,Date,StartTime,EndTime;
    DatabaseHelper db;
    String[] stations;
    List<String> stationlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_train_schedule);

        myDb = new DatabaseHelper(this);

        viewScheduleButton = findViewById(R.id.view_schedule);
        displayDate = findViewById(R.id.date);
        sback = findViewById(R.id.sback);
        toStation = findViewById(R.id.to_station);
        fromStation = findViewById(R.id.from_station);
        date = findViewById(R.id.date);
        endTime = findViewById(R.id.end_time);
        startTime = findViewById(R.id.start_time);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        displayDate.setText(formatter.format(date));

        db = new DatabaseHelper(this);
        SQLiteDatabase database = db.getWritableDatabase();
        Cursor cursor = db.getStations(database);
        stations = new String[cursor.getCount()];
        stationlist = Arrays.asList(stations);

        int i = 0;
        while(cursor.moveToNext()){
            stations[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_2));
            i++;
        }

        //String[] stations = getResources().getStringArray(R.array.stations);

        AutoCompleteTextView fromStation = findViewById(R.id.from_station);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,stations);
        fromStation.setAdapter(adapter1);

        AutoCompleteTextView toStation = findViewById(R.id.to_station);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,stations);
        toStation.setAdapter(adapter2);

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
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = year + "-" + (month+1) + "-" + dayOfMonth;
                displayDate.setText(date);
            }
        };

        sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(ViewTrainSchedule.this, MainMenu.class);
                startActivity(it);

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void openTrainSchedule(View view){

        ToStation = toStation.getText().toString().toUpperCase();
        FromStation = fromStation.getText().toString().toUpperCase();
        Date = date.getText().toString();
        StartTime = startTime.getText().toString();
        EndTime = endTime.getText().toString();

        Boolean cheker = true;

        if(StartTime.isEmpty()){
            StartTime = "00:00:00";
        }
        if(StartTime.isEmpty()){
            EndTime = "23:59:59";
        }
        if (FromStation.isEmpty()) {
            toStation.setError("Field cannot be left blank");
            cheker = false;
        }
        if (FromStation.isEmpty()){
            fromStation.setError("Field cannot be left blank");
            cheker = false;
        }
        if(!stationlist.contains(ToStation)){
            toStation.setError("Station not found");
            cheker = false;
        }
        if(!stationlist.contains(FromStation)){
            fromStation.setError("Station not found");
            cheker = false;
        }
        if(cheker)
        {
            Intent intent = new Intent(ViewTrainSchedule.this, TrainSchedule.class);
            intent.putExtra("fromStation",FromStation);
            intent.putExtra("toStation",ToStation);
            intent.putExtra("date",Date);
            intent.putExtra("endTime", EndTime);
            intent.putExtra("startTime", StartTime);
            startActivity(intent);
        }

    }
}

