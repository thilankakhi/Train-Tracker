package com.example.traintracker;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class EATResultAdapter extends RecyclerView.Adapter<EATResultAdapter.ViewHolder> {

    private List<EATListItem> listItems;

    public EATResultAdapter(List<EATListItem> listItems) {
        this.listItems = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.eat_list_item, parent, false);
        return  new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final EATResultAdapter.ViewHolder viewHolder, int i) {
        EATListItem listItem = listItems.get(i);
        viewHolder.station.setText(listItem.getStation());
        viewHolder.scheduledTime.setText(listItem.getScheduledTime());
        viewHolder.expectedTime.setText(listItem.getExpectedTime());
        viewHolder.delay.setText(listItem.getDelay());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView station;
        public TextView scheduledTime;
        public TextView expectedTime;
        public TextView delay;

        public ViewHolder(View itemView) {
            super(itemView);

            station = itemView.findViewById(R.id.station);
            scheduledTime = itemView.findViewById(R.id.scheduled_time);
            expectedTime = itemView.findViewById(R.id.expected_time);
            delay = itemView.findViewById(R.id.delay);
        }
    }



}
