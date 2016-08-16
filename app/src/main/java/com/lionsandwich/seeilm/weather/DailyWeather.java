package com.lionsandwich.seeilm.weather;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;

/**
 * Created by Eerik on 19-Jul-16.
 */
public class DailyWeather implements Serializable {

    private Date date;

    private NightForecast nightForecast;
    private DayForecast dayForecast;


    private ArrayList<TownWeather> townWeatherList = new ArrayList<>();


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public NightForecast getNightForecast() {
        return nightForecast;
    }

    public void setNightForecast(NightForecast nightForecast) {
        this.nightForecast = nightForecast;
    }

    public DayForecast getDayForecast() {
        return dayForecast;
    }

    public void setDayForecast(DayForecast dayForecast) {
        this.dayForecast = dayForecast;
    }

    public ArrayList<TownWeather> getTownWeatherList() {
        return townWeatherList;
    }

    public void setTownWeatherList(ArrayList<TownWeather> townWeatherList) {
        this.townWeatherList = townWeatherList;
    }

    public void addToTownWeatherList(TownWeather tw) {
        townWeatherList.add(tw);
    }


}
