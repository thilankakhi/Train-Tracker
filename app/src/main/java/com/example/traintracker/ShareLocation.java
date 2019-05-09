package com.example.traintracker;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;

public class ShareLocation extends AppCompatActivity {


    private ImageView sback;
    private TextView bt;
    private EditText train;
    LocationManager locationManager;
    String travelingTrain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_location);

        sback = findViewById(R.id.sback);
        bt = findViewById(R.id.share);
        train = findViewById(R.id.travelingTrain);
        travelingTrain = train.getText().toString();

        sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(ShareLocation.this, MainActivity.class);
                startActivity(it);

            }
        });
    }

    public void startShareLocation(View view) {
       Intent intent = new Intent(this,ShareLocationService.class);
       intent.putExtra("Traveling Train",travelingTrain);
       startService(intent);
    }

    public void stopShareLocation(View view) {
        ShareLocationService.stopService();
    }
}
