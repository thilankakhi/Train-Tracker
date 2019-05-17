package com.example.traintracker;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainSchedule extends AppCompatActivity {

    DatabaseHelper db;
    String fromStation, toStation, date, endTime, startTime;
    String fromStationID, toStationID;
    TableLayout table;
    Context ct;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<ListItem> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_schedule);

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        ct=this;

        fromStation = getIntent().getStringExtra("fromStation");
        toStation = getIntent().getStringExtra("toStation");
        date = getIntent().getStringExtra("date");
        endTime = getIntent().getStringExtra("endTime");
        startTime = getIntent().getStringExtra("startTime");

        recyclerView = findViewById(R.id.my_recycler_view);
        DividerItemDecoration divider = new
                DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        divider.setDrawable(
                ContextCompat.getDrawable(getBaseContext(), R.drawable.divider)
        );
        recyclerView.addItemDecoration(divider);
        recyclerView.setHasFixedSize(true);
        listItems = new ArrayList<>();

        //Log.e("details ", fromStation +","+toStation+","+date+","+startTime+","+endTime);

        db = new DatabaseHelper(this);
        SQLiteDatabase database = db.getWritableDatabase();

        Cursor cursor_fromStation = db.getStationID(database, fromStation);
        Cursor cursor_toStation = db.getStationID(database, toStation);
        Cursor c_test = db.getNoOfRows(database);

        Log.e("cursor ", String.valueOf(cursor_fromStation.getCount()));
        Log.e("cursor ", String.valueOf(cursor_toStation.getCount()));

        Log.e("rows ", String.valueOf(c_test.getCount()));

        int c = 0;
        while(c<=3){
            if(cursor_fromStation.getCount()==1 && cursor_toStation.getCount()==1){
                while (cursor_fromStation.moveToNext()){
                    fromStationID = Integer.toString(cursor_fromStation.getInt(cursor_fromStation.getColumnIndex("STATION_ID")));
                }
                while (cursor_toStation.moveToNext()) {
                    toStationID = Integer.toString(cursor_toStation.getInt(cursor_toStation.getColumnIndex("STATION_ID")));
                }
                break;
            }
            else{
                if(c==3){
                    Toast.makeText(getApplicationContext(),"Something went wrong. Please try again",Toast.LENGTH_LONG).show();
                }
                db.onUpgrade(database, 1, 1);
                cursor_fromStation = db.getStationID(database, fromStation);
                cursor_toStation = db.getStationID(database, toStation);
            }
            c++;
        }

        //Log.e("details ", fromStationID +","+toStationID+","+date+","+startTime+","+endTime);

        String end_point = "http://api.lankagate.gov.lk:8280/railway/1.0/train/searchTrain?startStationID="+fromStationID+"&endStationID="+toStationID+"&searchDate="+date+"&startTime="+startTime+"&endTime="+endTime+"&lang=en";

        //Log.e("details ", end_point);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,end_point, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        try {
                            Log.e("Response trainID:", response.getJSONObject("RESULTS").getJSONObject("directTrains").getJSONArray("trainsList").toString());
                            Toast.makeText(getApplicationContext(),response.getString("MESSAGE"),Toast.LENGTH_LONG).show();
                            //Log.e("Response", response.toString());
                            if(response.getJSONObject("RESULTS").getJSONObject("directTrains").getJSONArray("trainsList").length()!=0){
                                JSONArray trainList = response.getJSONObject("RESULTS").getJSONObject("directTrains").getJSONArray("trainsList");
                                for(int i=0; i<response.getInt("NOFRESULTS"); i++){
                                    TableRow row=new TableRow(ct);
                                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                                    row.setLayoutParams(lp);
                                    String train = Integer.toString(trainList.getJSONObject(i).getInt("trainID"))+" "+trainList.getJSONObject(i).getString("trainName");
                                    String arrival = "In "+fromStation +" at "+trainList.getJSONObject(i).getString("arrivalTime");
                                    String depatureTime = trainList.getJSONObject(i).getString("depatureTime");
                                    String departure = "Out "+ toStation+" at "+ trainList.getJSONObject(i).getString("arrivalTimeEndStation");
                                    String trainType = trainList.getJSONObject(i).getString("trainType");
                                    ListItem item = new ListItem(train, arrival, departure,trainType);
                                    listItems.add(item);
                                }
                                adapter = new ResultAdapter(listItems, ct);
                                recyclerView.setLayoutManager(new LinearLayoutManager(ct));
                                recyclerView.setAdapter(adapter);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                            Log.e("Error: ", error.toString());
                            Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.view_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.view_options1:
                Intent intent1 = new Intent(this, View_v.class);
                startActivity(intent1);
                return true;
            case R.id.view_options2:
                Intent intent2 = new Intent(this, View_v.class);
                startActivity(intent2);
                return true;
            default:
                return  super.onContextItemSelected(item);
        }
    }
}

