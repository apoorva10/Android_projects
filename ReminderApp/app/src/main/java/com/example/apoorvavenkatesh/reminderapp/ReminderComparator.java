package com.example.apoorvavenkatesh.reminderapp;

import java.util.Comparator;

/**
 * Created by apoorvavenkatesh on 10/31/16.
 */
public class ReminderComparator implements Comparator<Remind> {
    @Override
    public int compare(Remind reminder1, Remind reminder2){

        String firstName = reminder1.getReminderName();
        String secondName = reminder2.getReminderName();
        return firstName.compareTo(secondName);
    }

}
