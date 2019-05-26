package com.example.traintracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;

import java.util.HashMap;
import java.util.Map;

/**

 * A simple {@link Fragment} subclass.
 */
public class ViewCurrentLocation extends Fragment implements OnMapReadyCallback{

    private static final String TAG = ViewCurrentLocation.class.getSimpleName();
    GoogleMap mgoogleMap;
    MapView mapView;
    View view;
    String trainId = "4";
    String trainStartTime="1503";
    FirebaseFirestore db;

    public ViewCurrentLocation() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_current_location,container,false);
        mapView =  view.findViewById(R.id.map);
        db = FirebaseFirestore.getInstance();
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

        checkLogin();
/*
        LatLng redmond = new LatLng(latitude, longtitue);

        mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(redmond, 3));*/


    }

    private void checkLogin(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user==null){
            Toast.makeText(getActivity(),"Login first",Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().popBackStack();
        }else{
            subcribeToLocationChanges();
        }
    }

    private void subcribeToLocationChanges() {
        String date = android.text.format.DateFormat.format("yyyyMMdd", new java.util.Date()).toString();
        //String path = "train_run/" + trainId + "_" + date+"_"+trainStartTime;
        String path = "trains/" + trainId;
        //TODO:replace hardcoded path with path
        final DocumentReference docRef = db.document(path);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                Map<String,Object> data = (HashMap<String,Object>)snapshot.getData().get("current_location");
                if (data != null && snapshot.exists()) {
                    Log.e("cl", snapshot.toString());
                    setMarker(data);
                } else {
                    Toast.makeText(getActivity(),"No data available",Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });
    }

    private void setMarker(Map<String,Object> data) {
        try{

            GeoPoint geoPoint = (GeoPoint) data.get("location");
            LatLng location = new LatLng(geoPoint.getLatitude(),geoPoint.getLongitude());
            Log.d(TAG,"pointer at ["+location.latitude+","+location.longitude+"]");
            mgoogleMap.addMarker(new MarkerOptions().position(location).title("train").draggable(true).icon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.ic_tracker))));
            mgoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 75));
        }catch(ClassCastException e){
            Log.d(TAG,"current_location is not a map");
        }
    }
    private Bitmap getMarkerBitmapFromView(@DrawableRes int resId) {

        View customMarkerView = ((LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.view_custom_marker, null);
        ImageView markerImageView = (ImageView) customMarkerView.findViewById(R.id.profile_image);
        markerImageView.setImageResource(resId);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.BLACK, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);
        return returnedBitmap;
    }

}
