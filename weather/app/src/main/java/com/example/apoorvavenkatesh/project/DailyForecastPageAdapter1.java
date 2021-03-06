package com.example.apoorvavenkatesh.project;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by apoorvavenkatesh on 11/8/16.
 */
public class DailyForecastPageAdapter1 extends FragmentPagerAdapter {
    private int numDays;
    private FragmentManager fm;
    private WeatherForecast forecast;
    private final static SimpleDateFormat sdf = new SimpleDateFormat("E, dd-MM");

    public DailyForecastPageAdapter1(int numDays, FragmentManager fm, WeatherForecast forecast) {
        super(fm);
        this.numDays = numDays;
        this.fm = fm;
        this.forecast = forecast;
    }

    // Page title
    @Override
    public CharSequence getPageTitle(int position) {
// We calculate the next days adding position to the current date
        Date d = new Date();
        Calendar gc = new GregorianCalendar();
        gc.setTime(d);
        gc.add(GregorianCalendar.DAY_OF_MONTH, position);

        return sdf.format(gc.getTime());
    }

    @Override
    public Fragment getItem(int num) {
        DayForecast dayForecast = (DayForecast) forecast.getForecast(num);
        DayForecastFragement1 f = new DayForecastFragement1();
        f.setForecast(dayForecast);
        return f;
    }
    @Override
    public int getCount() {

        return numDays;
    }
}
