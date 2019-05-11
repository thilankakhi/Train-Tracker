package com.example.traintracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Signup extends AppCompatActivity {

    private ImageView sback;
    private TextView create;
    private EditText userName;
    private EditText email;
    private EditText password;
    private EditText passwordR;
    private TextView errorMessage;
    android.content.res.Resources res;
    private String endpoint_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        sback = findViewById(R.id.sback);
        create = findViewById(R.id.create);
        userName = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordR = findViewById(R.id.password_re_entered);
        errorMessage = findViewById(R.id.error_message);
        res = getResources();
        String server_url= res.getString(R.string.serverURL);
        endpoint_url= server_url+"/users/signUp";

        sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(Signup.this, MainActivity.class);
                startActivity(it);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String UserName, Email,Password, PasswordR;
                UserName = userName.getText().toString();
                Email = email.getText().toString();
                Password = password.getText().toString();
                PasswordR = passwordR.getText().toString();

                String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
                java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
                java.util.regex.Matcher m = p.matcher(Email);

                if (UserName.isEmpty() | Email.isEmpty() | Password.isEmpty() | PasswordR.isEmpty()){
                    errorMessage.setText("Please fill every field");
                }
                else if(!m.matches()){
                    errorMessage.setText("Email address is not valid");
                }
                else if(!Password.equals(PasswordR)){
                    errorMessage.setText("Passwords are does not match");
                }
                else{
                    HashMap<String, String> params = new HashMap<String, String>();
                    params.put("name", UserName);
                    params.put("email", Email);
                    params.put("password", Password);

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.POST, endpoint_url, new JSONObject(params), new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                    try {
                                        if (response.getString("message").equals("User created successfully")){
                                            Intent it = new Intent(Signup.this, MainMenu.class);
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
    }
}
