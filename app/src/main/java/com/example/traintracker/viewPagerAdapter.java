package com.example.traintracker;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class viewPagerAdapter extends FragmentPagerAdapter {

    public viewPagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position){
            case 0:
                fragment = new ViewCurrentLocation();
                break;
            case 1:
                fragment = new ViewExpectedTimes();
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }

    public CharSequence getPageTitle(int position){

        String m = new String();
        switch (position){
            case 0:
                m = "Current Location";
                break;
            case 1:
                m = "Expected Arrival Times";
                break;
        }
        return m;
    }
}
