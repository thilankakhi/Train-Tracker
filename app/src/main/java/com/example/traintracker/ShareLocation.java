package com.example.traintracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ShareLocation extends AppCompatActivity {


    private ImageView sback;
    private TextView bt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_location);

        sback = findViewById(R.id.sback);
        bt = findViewById(R.id.share);

        sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(ShareLocation.this, MainActivity.class);
                startActivity(it);

            }
        });
    }

    public void shareLocation(View view) {
            bt.setText("Clicked");
    }
}
