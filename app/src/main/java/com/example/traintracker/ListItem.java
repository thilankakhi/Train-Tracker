package com.example.traintracker;

public class ListItem {
    private String train;
    private String arrival;
    private String departure;
    private String type;
    private String frequency;
    private Boolean isRunning;

    public ListItem(String train, String arrival, String departure, String type, String frequency,Boolean isRunning) {
        this.train = train;
        this.arrival = arrival;
        this.departure = departure;
        this.type = type;
        this.frequency = frequency;
        this.isRunning = isRunning;
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

    public String getFrequency() {
        return frequency;
    }

    public Boolean getRunning() {
        return isRunning;
    }
}
