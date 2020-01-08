package com.cloud.appsistant;

public class Meeting {
    private String meetingId;
    private String date;
    private String time;
    private Card card;

    public Meeting() {

    }

    public Meeting(String meetingId, String date, String time, Card card) {
        this.meetingId = meetingId;
        this.date = date;
        this.time = time;
        this.card = new Card();
        this.card = card;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Card getCard() {
        return card;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
