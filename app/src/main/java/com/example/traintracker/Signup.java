package com.example.traintracker;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

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
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        sback = findViewById(R.id.sback);
        create = findViewById(R.id.create);
        userName = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordR = findViewById(R.id.password_re_entered);
        errorMessage = findViewById(R.id.error_message);
        res = getResources();

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

                if (UserName.isEmpty()){
                    userName.setError("Field cannot be left blank");
                }
                if (Email.isEmpty()){
                    email.setError("Field cannot be left blank");
                }
                if (Password.isEmpty()){
                    password.setError("Field cannot be left blank");
                }
                if (PasswordR.isEmpty()){
                    passwordR.setError("Field cannot be left blank");
                }
                else if(!m.matches()){
                    email.setError("Email address is not valid");
                }
                else if(!Password.equals(PasswordR)){
                    passwordR.setError("Password does not match");
                }
                else{
                    mAuth.createUserWithEmailAndPassword(Email, Password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Log.d("firebase", "createUserWithEmail:success");
                                Toast.makeText(Signup.this,"Auth successful",Toast.LENGTH_LONG).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            }else{
                                Log.d("firebase","createUserWithEmail:faliure");
                                Toast.makeText(Signup.this,"Auth failed",Toast.LENGTH_LONG).show();
                                updateUI(null);
                            }
                        }
                    });
                }

            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if(user==null){ return;}
        startActivity(new Intent(this,MainMenu.class));
    }
}
