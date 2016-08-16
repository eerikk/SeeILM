package com.lionsandwich.seeilm.weather;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Eerik on 19-Jul-16.
 */
public class TownWeather implements Serializable{

    private String name;
    private String dayPhenomenon;
    private String nightPhenomenon;
    private int    tempMin;
    private int    tempMax;
    private Date   date;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDayPhenomenon() {
        return dayPhenomenon;
    }

    public void setDayPhenomenon(String dayPhenomenon) {
        this.dayPhenomenon = dayPhenomenon;
    }

    public String getNightPhenomenon() {
        return nightPhenomenon;
    }

    public void setNightPhenomenon(String nightPhenomenon) {
        this.nightPhenomenon = nightPhenomenon;
    }

    public int getTempMin() {
        return tempMin;
    }

    public void setTempMin(int tempMin) {
        this.tempMin = tempMin;
    }

    public int getTempMax() {
        return tempMax;
    }

    public void setTempMax(int tempMax) {
        this.tempMax = tempMax;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
