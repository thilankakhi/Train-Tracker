package com.example.traintracker;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

/**

 * A simple {@link Fragment} subclass.
 */
public class ViewCurrentLocation extends Fragment implements OnMapReadyCallback{

    GoogleMap mgoogleMap;
    MapView mapView;
    View view;
    //String server_url= getString(R.string.serverURL);
    //String endpoint_url= server_url+"/trains/currentLocation";
    String endpoint_url= "/trains/currentLocation";
    Double latitude, longtitue;

    public ViewCurrentLocation() {
        // Required empty public constructor
    }

    public static ViewCurrentLocation newInstance() {
        ViewCurrentLocation viewCurrentLocation = new ViewCurrentLocation();
        return viewCurrentLocation;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_current_location,container,false);
        mapView =  view.findViewById(R.id.map);


        //make request to get current location of the train
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, endpoint_url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            latitude = response.getDouble("latitude");
                            longtitue =  response.getDouble("longitude");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Creation", error.toString());
                        error.printStackTrace();
                    }
                });
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);
        //


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap){
        MapsInitializer.initialize(getActivity());

        mgoogleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longtitue)));

        LatLng redmond = new LatLng(latitude, longtitue);

        mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(redmond, 3));


    }

}
