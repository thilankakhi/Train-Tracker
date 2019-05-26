package com.example.traintracker;

public class EATListItem {

    private String station;
    private String scheduledTime;
    private String expectedTime;
    private String delay;

    public EATListItem(String station, String scheduledTime, String expectedTime, String delay) {
        this.station = station;
        this.scheduledTime = scheduledTime;
        this.expectedTime = expectedTime;
        this.delay = delay;
    }

    public String getStation() {
        return station;
    }

    public String getScheduledTime() {
        return scheduledTime;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public String getDelay() {
        return delay;
    }
}
