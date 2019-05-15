package com.example.traintracker;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


public class MainMenu extends AppCompatActivity {


    private LinearLayout viewCurrentLocation;
    private LinearLayout viewExpectedTimes;
    private LinearLayout viewTrainSchedule;

    ImageView sback;
    ImageView home;
    ImageView share;
    TextView errorMessage;
    EditText train;
    Request  jsonObjectRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        viewCurrentLocation =  findViewById(R.id.current_location);
        viewExpectedTimes = findViewById(R.id.arrival_times);
        viewTrainSchedule = findViewById(R.id.train_shedule);

        sback = findViewById(R.id.sback);
        home = findViewById(R.id.home);
        share = findViewById(R.id.share);
        errorMessage = findViewById(R.id.error_message);
        train = findViewById(R.id.selected_train);


        viewCurrentLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final String selectedTrain;
                selectedTrain = train.getText().toString();

                if(selectedTrain.isEmpty()){
                    train.setError("Field cannot be left blank");
                }else {
                    openViewCurrentLocation();
                }
            }
        });

        viewExpectedTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String selectedTrain;
                selectedTrain = train.getText().toString();

                if(selectedTrain.isEmpty()){
                    train.setError("Field cannot be left blank");
                }else {
                    openViewExpectedTimes();
                }
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
        //open activity to view train schedule
        Intent intent = new Intent(this, ViewTrainSchedule.class);
        startActivity(intent);
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

    public void openViewExpectedTimes(){
        //open activity to view expected arrival times
        Intent intent = new Intent(this, ViewExpectedTimes.class);
        startActivity(intent);
    }
}
