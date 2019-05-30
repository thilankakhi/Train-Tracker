package com.example.traintracker;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import customfonts.MyTextView;

public class ShareLocation extends AppCompatActivity implements View.OnClickListener {


    private ImageView sback;
    private TextView bt;
    private EditText train;
    private MyTextView shareButton;
    LocationManager locationManager;
    String travelingTrain = "1";
    String trainStartTime = "1503";
    private static final int PERMISSIONS_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_location);
        shareButton = findViewById(R.id.share);
        shareButton.setOnClickListener(this);
        // Check GPS is enabled
        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(this, "Please enable location services", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        int permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission == PackageManager.PERMISSION_GRANTED) {
            updateUI();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST);
        }
    }

    private void updateUI() {

        shareButton.setEnabled(true);
    }

    private void startTrackerService() {
        Intent intent = new Intent(this, TrackerService.class);
        String date = android.text.format.DateFormat.format("yyyyMMdd", new java.util.Date()).toString();
        //String userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
        String userId = "1";
        String path = "train/" + travelingTrain +"/locations/"+userId;
        Log.d("share-location","db_path : "+path);
        intent.putExtra("db_path",path);
        startService(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST && grantResults.length == 1
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"Permissions granted",Toast.LENGTH_LONG).show();
                updateUI();
        } else {
            Toast.makeText(this,"Permissions not granted",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.share:
                if(view.isEnabled()){
                    startTrackerService();
                    Toast.makeText(this,"Tracking Started",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(this,"Permissions not granted",Toast.LENGTH_LONG).show();
                }
        }
    }
}
