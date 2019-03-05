package com.example.traintracker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class TrainSchedule extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train_schedule);

        TextView textView = findViewById(R.id.view_options);
        registerForContextMenu(textView);
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

