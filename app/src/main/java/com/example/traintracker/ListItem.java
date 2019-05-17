package com.example.traintracker;

public class ListItem {

    private String train;
    private String arrival;
    private String departure;
    private String type;

    public ListItem(String train, String arrival, String departure, String type) {
        this.train = train;
        this.arrival = arrival;
        this.departure = departure;
        this.type = type;
    }

    public String getTrain() {
        return train;
    }

    public String getArrival() {
        return arrival;
    }

    public String getDeparture() {
        return departure;
    }

    public String getType() {
        return type;
    }
}
