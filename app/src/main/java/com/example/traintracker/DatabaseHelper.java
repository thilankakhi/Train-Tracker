package com.example.traintracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "train.db";
    public static final String TABLE_NAME = "station_table";
    public static final String COL_1 = "STATION_ID";
    public static final String COL_2 = "STATION_NAME";
    JSONArray stationList;
    int noOfStations=0;
    Context c;
    SQLiteDatabase dbs;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        c = context;
        Log.d("database","database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("database", "table created1");
        db.execSQL("CREATE TABLE "+TABLE_NAME+" (STATION_ID INT PRIMARY KEY, STATION_NAME TEXT)");
        Log.d("database", "table created2");

        makeRequest(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public void makeRequest(SQLiteDatabase db){

        dbs = db;

        String end_point = "http://api.lankagate.gov.lk:8280/railway/1.0/station/getAll?lang=en";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,end_point, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.e("Response trainID: 2222", response.toString());
                        try {
                            stationList = response.getJSONObject("RESULTS").getJSONArray("stationList");
                            noOfStations = response.getInt("NOFRESULTS");
                            Log.d("Response:", stationList.getJSONObject(0).get("stationName").toString());
                            for(int i=0; i<=noOfStations; i++){

                                try {
                                    String stationName = stationList.getJSONObject(i).get("stationName").toString();
                                    int stationID = stationList.getJSONObject(i).getInt("stationID");
                                    //Log.d("stations", stationName);
                                    ContentValues contentValues = new ContentValues();
                                    contentValues.put(COL_1, stationID);
                                    contentValues.put(COL_2, stationName);
                                    dbs.insert(TABLE_NAME, null, contentValues);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            Log.e("Error: 222 ", e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: 222 ", error.toString());
            }
        }){
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer 7a69c7ee-1c5f-395b-9a84-f51b22537e04");
                return headers;
            }

        };
        MySingleton.getInstance(c.getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }

    public void insertData(SQLiteDatabase db){

        for(int i=0; i<=noOfStations; i++){

            try {
                String stationName = stationList.getJSONObject(i).get("stationName").toString();
                int stationID = stationList.getJSONObject(i).getInt("stationID");
                Log.d("stations", stationName);
                ContentValues contentValues = new ContentValues();
                contentValues.put(COL_1, stationID);
                contentValues.put(COL_2, stationName);
                db.insert(TABLE_NAME, null, contentValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public Cursor getStationID(SQLiteDatabase db, String stationName){
        String[] projection = {COL_1};
        Cursor cursor = db.rawQuery("SELECT " +COL_1+ " FROM "+TABLE_NAME+" WHERE "+ COL_2+ "=?",  new String[]{stationName});
        return cursor;
    }

    public Cursor getNoOfRows(SQLiteDatabase db){
        String[] projection = {COL_1};
        Cursor cursor = db.query(TABLE_NAME, projection,null,null,null,null,null);
        return cursor;
    }

    public Cursor getStations(SQLiteDatabase db){
        String[] projection = {COL_2};
        Cursor cursor = db.query(TABLE_NAME, projection,null,null,null,null,null);
        return cursor;
    }

}
