package com.example.traintracker;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private Context context;

    public ResultAdapter(List<ListItem> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
       return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ListItem listItem = listItems.get(i);
        viewHolder.train.setText(listItem.getTrain());
        viewHolder.arrival.setText(listItem.getArrival());
        viewHolder.departure.setText(listItem.getDeparture());
        viewHolder.type.setText(listItem.getType());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView train;
        public TextView arrival;
        public TextView departure;
        public TextView type;

        public ViewHolder(View itemView) {
            super(itemView);

            train = itemView.findViewById(R.id.text_view_deatails);
            arrival = itemView.findViewById(R.id.text_view_from);
            departure = itemView.findViewById(R.id.text_view_to);
            type = itemView.findViewById(R.id.text_view_type);

        }
    }

}
