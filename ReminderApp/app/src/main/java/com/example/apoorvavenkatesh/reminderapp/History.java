package com.example.apoorvavenkatesh.reminderapp;

/**
 * Created by apoorvavenkatesh on 10/31/16.
 */
public class History {
    private int hourTaken;
    private int minuteTaken;
    private String dateString;
    private String RemindName;

    public int getHourTaken() { return hourTaken; }

    public void setHourTaken(int hourTaken) { this.hourTaken = hourTaken; }

    public int getMinuteTaken() { return minuteTaken; }

    public void setMinuteTaken(int minuteTaken) { this.minuteTaken = minuteTaken; }

    public String getAm_pmTaken() { return (hourTaken < 12) ? "am" : "pm"; }

    public String getDateString() { return dateString; }

    public void setDateString(String dateString) { this.dateString = dateString; }

    public String getPillName() {
        return RemindName;
    }

    public void setPillName(String RemindName) {
        this.RemindName = RemindName;
    }
}
