package com.example.traintracker;



import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewExpectedTimes extends Fragment {

    Context context;
    String trainId;
    String finalStationID;
    private List<EATListItem> listItems;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_expected_times,container,false);

        context = getActivity();

        trainId = "187";
        finalStationID = "61";

        Log.e("shit", "this is ");

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        Map<String,String> params = new HashMap<String, String>();
        params.put("trainRunId",trainId);
        params.put("finalStations",finalStationID);

        recyclerView = view.findViewById(R.id.eat_my_recycler_view);
        recyclerView.setHasFixedSize(true);
        listItems = new ArrayList<>();

        JSONObject jsonObj = new JSONObject(params);

        String end_point = "https://us-central1-train-tracker-240709.cloudfunctions.net/getExpectedArrivalTimes";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,end_point,jsonObj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                      progressDialog.dismiss();
                        try {
                            Log.e("EAT", response.toString());
                            //Toast.makeText(context.getApplicationContext(),response.getString("MESSAGE"),Toast.LENGTH_LONG).show();
                            if(response.getJSONArray("RESULTS").length() !=0){
                                JSONArray stationList = response.getJSONArray("RESULTS");
                                for(int i=0; i<response.getInt("NOFRESULS"); i++) {

                                    String station_name = stationList.getJSONObject(i).getString("STATION_NAME");
                                    String arrivalTimeAtEndStation = stationList.getJSONObject(i).getString("getExpectedArrivalTime");
                                    ///String train = trainList.getJSONObject(i).getString("trainName").toUpperCase();
                                    //String arrival = "In " + fromStation + " at " + arrivalTimeAtStartStation;
                                    //String frequency = trainList.getJSONObject(i).getString("trainFrequncy");
                                    //String departure = "Out " + toStation + " at " + arrivalTimeAtEndStation;
                                    //String trainType = trainList.getJSONObject(i).getString("trainType");
                                    EATListItem item = new EATListItem(station_name, arrivalTimeAtEndStation);
                                    listItems.add(item);
                                }
                                adapter = new EATResultAdapter(listItems);
                                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                recyclerView.setAdapter(adapter);
                            }


                        } catch (JSONException e) {
                            Log.e("Error: ", e.toString());
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
                Log.e("Error: ", error.toString());
                Toast.makeText(context.getApplicationContext(),"Something went wrong. Please try again.",Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization","Bearer baa43fda-2f4b-3166-b5f3-a8086e03f173");
                return headers;
            }

        };

        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);

        return view;
    }

    public void test(){

    }

}
