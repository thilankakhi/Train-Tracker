package com.example.traintracker;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<ListItem> listItems;
    private  OnNoteListener onNoteListener;

    public ResultAdapter(List<ListItem> listItems, OnNoteListener onNoteListener) {
        this.listItems = listItems;
        this.onNoteListener = onNoteListener;
    }

    @Override
    public ViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
       View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
       return  new ViewHolder(v, onNoteListener);
    }

    @Override
    public void onBindViewHolder(final ResultAdapter.ViewHolder viewHolder, int i) {
        ListItem listItem = listItems.get(i);
        viewHolder.train.setText(listItem.getTrain());
        viewHolder.arrival.setText(listItem.getArrival());
        viewHolder.departure.setText(listItem.getDeparture());
        viewHolder.type.setText(listItem.getType());
        viewHolder.state.setText(listItem.getFrequency());

    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView train;
        public TextView arrival;
        public TextView departure;
        public TextView type;
        public TextView state;
        OnNoteListener onNoteListener;

        public ViewHolder(View itemView, OnNoteListener onNoteListener) {
            super(itemView);

            train = itemView.findViewById(R.id.text_view_deatails);
            arrival = itemView.findViewById(R.id.text_view_from);
            departure = itemView.findViewById(R.id.text_view_to);
            type = itemView.findViewById(R.id.text_view_type);
            state = itemView.findViewById(R.id.train_state);
            this.onNoteListener = onNoteListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.onNoteClick(getAdapterPosition());
        }
    }

    public  interface OnNoteListener{
        void onNoteClick(int position);
    }

}
