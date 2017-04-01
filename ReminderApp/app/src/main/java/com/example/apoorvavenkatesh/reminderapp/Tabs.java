package com.example.apoorvavenkatesh.reminderapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by apoorvavenkatesh on 11/1/16.
 */
public class Tabs extends FragmentPagerAdapter {
    private int TOTAL_TABS = 3;

    public Tabs(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {
        switch (index) {
            case 0:
                return new HistoryActivity();
            case 1:
                return new TodayReminder();
            case 2:
                return new TomorrowReminder();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TOTAL_TABS;
    }
}
