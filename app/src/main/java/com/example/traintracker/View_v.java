package com.example.traintracker;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.ActionBar.TabListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


public class View_v extends AppCompatActivity {


    private ViewPager viewPager;
    private  viewPagerAdapter adapter;
    private TabLayout tableLayout;
    private ImageView sback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_v);

        sback = findViewById(R.id.sback);

        tableLayout = findViewById(R.id.tabs);

        viewPager = findViewById(R.id.pager);
        adapter = new viewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tableLayout));
        tableLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));

        sback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent it = new Intent(View_v.this, MainMenu.class);
                startActivity(it);

            }
        });


        String intentFragment = getIntent().getStringExtra("frgToLoad");
        switch (intentFragment){
            case "ViewCurrentLocation":
                replaceFragment(new ViewCurrentLocation());
                break;
            case "ViewExpectedArrivalTimes":
                replaceFragment(new ViewExpectedTimes());
                break;
        }

    }
    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        android.support.v4.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.pager, fragment);
        fragmentTransaction.addToBackStack(fragment.toString());
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();
    }
}


