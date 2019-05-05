package com.example.traintracker;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView signup;
    TextView signin;
    EditText email;
    EditText password;
    TextView errorMessage;
    //Resources resources = getResources();
    android.content.res.Resources res;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        res = getResources();
        String server_url= res.getString(R.string.serverURL);
        final String endpoint_url= server_url+"/users/login";

        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
        errorMessage = findViewById(R.id.error);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Email,Password;
                Email = email.getText().toString();
                Password = password.getText().toString();

                if (Email.isEmpty()){
                    errorMessage.setText("Please enter your email");
                }
                else if(Password.isEmpty()){
                    errorMessage.setText("Please enter your password");
                }
                else {
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("email", Email);
                    params.put("password", Password);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, endpoint_url, new JSONObject(params), new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("message").equals("pass")){
                                            Intent it = new Intent(MainActivity.this, MainMenu.class);
                                            startActivity(it);
                                        }
                                        else{
                                            errorMessage.setText(response.getString("message"));
                                        }
                                    } catch (JSONException e) {
                                        errorMessage.setText(e.toString());
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    errorMessage.setText(error.toString());
                                    error.printStackTrace();
                                }
                            });
                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, Signup.class);
                startActivity(it);
            }
        });
    }
}
