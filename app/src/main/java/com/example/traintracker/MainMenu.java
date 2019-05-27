package com.example.traintracker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainMenu extends AppCompatActivity{


    private LinearLayout viewCurrentLocation;
    private LinearLayout viewExpectedTimes;
    private LinearLayout viewTrainSchedule;

    ImageView sback;
    ImageView home;
    ImageView share;
    TextView errorMessage;
    private TextView displayDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;
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
        setContentView(R.layout.activity_main_menu);

        viewCurrentLocation =  findViewById(R.id.current_location);
        viewTrainSchedule = findViewById(R.id.train_shedule);

        sback = findViewById(R.id.sback);
        home = findViewById(R.id.home);
        share = findViewById(R.id.share);
        errorMessage = findViewById(R.id.error_message);
        displayDate = findViewById(R.id.date);

        toStation = findViewById(R.id.to_station);
        fromStation = findViewById(R.id.from_station);
        date = findViewById(R.id.date);
        endTime = findViewById(R.id.end_time);
        startTime = findViewById(R.id.start_time);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = new Date();
        displayDate.setText(formatter.format(date));

        db = new DatabaseHelper(this);
        SQLiteDatabase database = db.getWritableDatabase();
        updateStationList(database);
        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MainMenu.this,
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


        viewCurrentLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        viewTrainSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewTrainSchedule();
            }
        });


        sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(MainMenu.this, MainActivity.class);
                startActivity(it);

            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(MainMenu.this, Home.class);
                startActivity(it);

            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(MainMenu.this, ShareLocation.class);
                startActivity(it);

            }
        });

    }

    public void openViewTrainSchedule(){
        ToStation = toStation.getText().toString().toUpperCase();
        FromStation = fromStation.getText().toString().toUpperCase();
        Date = date.getText().toString();
        StartTime = startTime.getText().toString();
        EndTime = endTime.getText().toString();

        Boolean cheker = true;

        if(StartTime.isEmpty()){
            StartTime = "00:00:00";
        }
        if(EndTime.isEmpty()){
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
            Intent intent = new Intent(MainMenu.this, TrainSchedule.class);
            intent.putExtra("fromStation",FromStation);
            intent.putExtra("toStation",ToStation);
            intent.putExtra("date",Date);
            intent.putExtra("endTime", EndTime);
            intent.putExtra("startTime", StartTime);
            startActivity(intent);
        }
    }

    public  void openViewCurrentLocation(){
        //open activity to view current location fo a given train
        Intent intent = new Intent(this, View_v.class);
        startActivity(intent);
        /*FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ViewCurrentLocation fragmentDemo = ViewCurrentLocation.newInstance();
        ft.replace(R.id.view_current_location, fragmentDemo);
        ft.commit();*/
    }

    public void updateStationList(SQLiteDatabase database){
        Cursor cursor = db.getStations(database);
        stations = new String[cursor.getCount()];
        stationlist = Arrays.asList(stations);

        int i = 0;
        while(cursor.moveToNext()){
            stations[i] = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_2));
            Log.e("stations", stations[i]);
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
    }
}
