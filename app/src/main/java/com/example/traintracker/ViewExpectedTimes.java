package com.example.traintracker;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewExpectedTimes extends Fragment {

    private TextView textView;

    public ViewExpectedTimes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_expected_times,container,false);
        textView = view .findViewById(R.id.text_view);
        //String m = getArguments().getString("message");
        textView.setText("Times");
        return view;
    }

}
