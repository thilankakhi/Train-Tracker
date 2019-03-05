package com.example.traintracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainMenu extends AppCompatActivity {


    private LinearLayout viewCurrentLocation;
    private LinearLayout viewExpectedTimes;
    private LinearLayout viewTrainSchedule;

    ImageView sback;
    ImageView home;
    ImageView share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);


        viewCurrentLocation = (LinearLayout) findViewById(R.id.current_location);
        viewExpectedTimes = (LinearLayout)findViewById(R.id.arrival_times);
        viewTrainSchedule = (LinearLayout)findViewById(R.id.train_shedule);

        sback = (ImageView)findViewById(R.id.sback);
        home = (ImageView)findViewById(R.id.home);
        share = (ImageView)findViewById(R.id.share);

        viewCurrentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewCurrentLocation();
            }
        });

        viewExpectedTimes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openViewExpectedTimes();
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
        Intent intent = new Intent(this, ViewTrainSchedule.class);
        startActivity(intent);
    }

    public  void openViewCurrentLocation(){
        Intent intent = new Intent(this, View_v.class);
        startActivity(intent);
    }

    public void openViewExpectedTimes(){
        Intent intent = new Intent(this, View_v.class);
        startActivity(intent);
    }
}
