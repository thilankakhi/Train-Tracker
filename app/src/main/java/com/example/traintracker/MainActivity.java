package com.example.traintracker;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    TextView signup;
    TextView signin;
    EditText email;
    EditText password;
    TextView errorMessage;
    //Resources resources = getResources();
    android.content.res.Resources res;
    static MainActivity instant;
    private CoordinatorLayout coordinatorLayout;
    String endpoint_url;
    Request  jsonObjectRequest;
    ProgressBar pb;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instant =this;

        res = getResources();
        String server_url= res.getString(R.string.serverURL);
        endpoint_url= server_url+"/authentication/";

        //FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
        errorMessage = findViewById(R.id.error);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinatorLayout);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MainActivity.this, Signup.class);
                startActivity(it);
            }
        });
    }

    public static MainActivity getInstent(){
        return instant;
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    public void signIn(View view) {

        //Intent it = new Intent(MainActivity.this, MainMenu.class);
        //startActivity(it);

        pb = findViewById(R.id.progress_circular);
        pb.setVisibility(ProgressBar.VISIBLE);
        // run a background job and once complete

        final String Email,Password;
        Email = email.getText().toString();
        Password = password.getText().toString();

        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(Email);


        if (Email.isEmpty()){
            //errorMessage.setText("Please enter your email");
            email.setError("Field cannot be left blank");
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
        else if(Password.isEmpty()){
            //errorMessage.setText("Please enter your password");
            email.setError("Field cannot be left blank");
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
        else if(!m.matches()){
            email.setError("Email address is not valid");
            pb.setVisibility(ProgressBar.INVISIBLE);
        }
        else {
            mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("firebase", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Intent it = new Intent(MainActivity.this, MainMenu.class);
                                startActivity(it);

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("firebase", "signInWithEmail:failure", task.getException());
                                Toast.makeText(MainActivity.this, "Authentication failed. Try again.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
            /*HashMap<String, String> params = new HashMap<String, String>();
            params.put("email", Email);
            params.put("password", Password);
            params.put("strategy", "local");

            jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.POST, endpoint_url, new JSONObject(params), new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {

                            Intent it = new Intent(MainActivity.this, MainMenu.class);
                            startActivity(it);
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            //errorMessage.setText(error.toString());
                            //error.printStackTrace();
                            String m;
                            if(null == error.networkResponse){
                                m="No internet connection!";
                            }
                            else {
                                m="Invalid entry!";
                            }
                            Toast.makeText(getApplicationContext(),m,Toast.LENGTH_LONG).show();
                            pb.setVisibility(ProgressBar.INVISIBLE);
                            //pb.setVisibility(ProgressBar.INVISIBLE);
                            Snackbar snackbar = Snackbar
                                    .make(coordinatorLayout, m, Snackbar.LENGTH_LONG)
                                    .setAction("RETRY", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            signIn(view);
                                        }
                                    });

                            // Changing message text color
                            snackbar.setActionTextColor(Color.RED);

                            // Changing action button text color
                            View sbView = snackbar.getView();
                            TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
                            textView.setTextColor(Color.YELLOW);

                            snackbar.show();
                        }
                    });
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
            */
        }
    }
}
