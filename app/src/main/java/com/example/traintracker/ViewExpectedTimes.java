package com.example.traintracker;



import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewExpectedTimes extends Fragment {

    Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_expected_times,container,false);

        context = getActivity();

        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Loading data...");
        progressDialog.show();

        String end_point = "https://us-central1-train-tracker-240709.cloudfunctions.net/getExpectedArrivalTimes";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,end_point, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                      progressDialog.dismiss();
                        try {
                            Log.e("EAT", response.getString("message"));
//                           Toast.makeText(context.getApplicationContext(),response.getString("MESSAGE"),Toast.LENGTH_LONG).show();
//                            if(response.getJSONObject("RESULTS").getJSONObject("directTrains").getJSONArray("trainsList").length()!=0){
//                                JSONArray trainList = response.getJSONObject("RESULTS").getJSONObject("directTrains").getJSONArray("trainsList");
//                                for(int i=0; i<response.getInt("NOFRESULTS"); i++) {
//
//                                    String arrivalTimeAtStartStation = trainList.getJSONObject(i).getString("arrivalTime");
//                                    String arrivalTimeAtEndStation = trainList.getJSONObject(i).getString("arrivalTimeEndStation");
//                                    TableRow row = new TableRow(ct);
//                                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
//                                    row.setLayoutParams(lp);
//                                    //String train = Integer.toString(trainList.getJSONObject(i).getInt("trainID")) + " " + trainList.getJSONObject(i).getString("trainName");
//                                    String train = trainList.getJSONObject(i).getString("trainName").toUpperCase();
//                                    String arrival = "In " + fromStation + " at " + arrivalTimeAtStartStation;
//                                    String frequency = trainList.getJSONObject(i).getString("trainFrequncy");
//                                    String departure = "Out " + toStation + " at " + arrivalTimeAtEndStation;
//                                    String trainType = trainList.getJSONObject(i).getString("trainType");
//                                    ListItem item = new ListItem(train, arrival, departure, trainType, frequency);
//                                    listItems.add(item);
//                                }
//                                test();
//                            }


                        } catch (JSONException e) {
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
        });
        MySingleton.getInstance(context.getApplicationContext()).addToRequestQueue(jsonObjectRequest);

        return view;
    }

}
