package com.example.traintracker;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class ShareLocationService extends IntentService implements LocationListener {

    LocationManager locationManager;
    android.content.res.Resources res;
    Location location;
    double latitude;
    double longitude;
    boolean isLocationChanged;

    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean isGPSTrackingEnabled = false;
    private static boolean  isRunning;
    private String provider_info;
    String travelingTrain;
    private Emitter.Listener onNewMessage;
    String dbPath;

    //String server_url= res.getString(R.string.serverURL);

    public ShareLocationService() {
        super("MyBackgroundThread");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        Log.e("server", "at onHandleIntent");

        travelingTrain = workIntent.getStringExtra("Traveling Train");

        isRunning = true;

        while (isRunning) {
            try {
                getLocation();
                Thread.sleep(1000);
                makeRequest(travelingTrain);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopUsingGPS();
    }

    protected void makeRequest(String train) {

        res = getResources();
        String server_url = res.getString(R.string.serverURL);
        String endpoint_url = server_url + "/trains/pullLocation";

        String lat = Double.toString(latitude);
        String lon = Double.toString(longitude);

        Log.i("tag", "Latitude: " + lat);
        Log.i("tag", "Longitude: " + lon);

        HashMap<String, String> params = new HashMap<>();
        params.put("latitude", lat);
        params.put("longitude", lon);
        params.put("Train", train);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, endpoint_url, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("message").equals("pass")) {

                            } else {
                                //errorMessage.setText(response.getString("message"));
                            }
                        } catch (JSONException e) {
                            //errorMessage.setText(e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //errorMessage.setText(error.toString());
                        error.printStackTrace();
                    }
                });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }

    void getLocation() {

        try {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            // Try to get location if you GPS Service is enabled
            if (isGPSEnabled) {
                this.isGPSTrackingEnabled = true;

                Log.d("isu", "Application use GPS Service");
                provider_info = LocationManager.GPS_PROVIDER;

            } else if (isNetworkEnabled) { // Try to get location if you Network Service is enabled
                this.isGPSTrackingEnabled = true;

                Log.d("isu", "Application use Network State to get GPS coordinates");
                provider_info = LocationManager.NETWORK_PROVIDER;

            }

            // Application can use GPS or Network Provider
            if (!provider_info.isEmpty()) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                locationManager.requestLocationUpdates(
                        provider_info,
                        0, 0,
                        this
                );

                if (locationManager != null) {
                    location = locationManager.getLastKnownLocation(provider_info);
                    updateGPSCoordinates();
                }
            }
        }
        catch (Exception e)
        {
            //e.printStackTrace();
            Log.e("isu", "Impossible to connect to LocationManager", e);
        }
    }

    public void updateGPSCoordinates() {
        String lat = Double.toString(latitude);
        String lon = Double.toString(longitude);

        Log.i("tag", "Latitude: " + lat);
        Log.i("tag", "Longitude: " + lon);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    static void stopService() {
        isRunning = false;
    }

    public boolean getIsGPSTrackingEnabled() {

        return this.isGPSTrackingEnabled;
    }
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(ShareLocationService.this);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        isLocationChanged = true;
        Log.i("change", "location changedddddd");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}