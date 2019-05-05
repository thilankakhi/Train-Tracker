package com.example.traintracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class TrainSchedule extends AppCompatActivity {

    String server_url= getString(R.string.serverURL);
    EditText fromStation;
    EditText toStation;
    TextView date;
    EditText startTime;
    EditText endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_train_schedule);

        //TextView textView = findViewById(R.id.view_options);
        //registerForContextMenu(textView);

        toStation = findViewById(R.id.to_station);

        fromStation = findViewById(R.id.from_station);
        if(fromStation == null){
            Log.d("create","helloooooooo");
        }
        date = findViewById(R.id.date);
        startTime = findViewById(R.id.start_time);
        endTime = findViewById(R.id.end_time);

        final String FromStation,ToStation,Date,StartTime,EndTime;
        ToStation = toStation.getText().toString();
        FromStation = fromStation.getText().toString();
        Date = date.getText().toString();
        StartTime = startTime.getText().toString();
        EndTime = endTime.getText().toString();

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("toStation", ToStation);
        params.put("fromStation", FromStation);
        params.put("date", Date);
        params.put("startTime", StartTime);
        params.put("endTime", EndTime);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.POST, server_url, new JSONObject(params), new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("Creation", response.getString("message"));
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
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

        /*ArrayList<String> data = new ArrayList<String>();
        ArrayList<String> data1 = new ArrayList<String>();
        ArrayList<String> data2 = new ArrayList<String>();
        ArrayList<String> data3 = new ArrayList<String>();
        TableLayout table;

        data.add("Samsung");
        data.add("Apple");
        data1.add("Pixle");
        data1.add("Vivo");
        data2.add(" $109");
        data2.add(" $200");
        data3.add(" $399");
        data3.add(" $290");


        table = findViewById(R.id.table);

        for(int i=0;i<data.size();i++)
        {
            TableRow row=new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row.setLayoutParams(lp);
            String a = data.get(i);
            String b = data1.get(i);
            String c = data2.get(i);
            String d = data3.get(i);
            TextView tv1=new TextView(this);
            tv1.setText(a);
            TextView tv2=new TextView(this);
            tv2.setText(b);
            TextView tv3=new TextView(this);
            tv3.setText(c);
            TextView tv4=new TextView(this);
            tv4.setText(d);
            row.addView(tv1);
            row.addView(tv2);
            row.addView(tv3);
            row.addView(tv4);
            table.addView(row);
        }*/





    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.view_menu, menu);
    }

    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.view_options1:
                Intent intent1 = new Intent(this, View.class);
                startActivity(intent1);
                return true;
            case R.id.view_options2:
                Intent intent2 = new Intent(this, View.class);
                startActivity(intent2);
                return true;
            default:
                return  super.onContextItemSelected(item);
        }
    }
}

