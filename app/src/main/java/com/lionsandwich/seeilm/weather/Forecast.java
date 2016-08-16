package com.lionsandwich.seeilm.weather;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Eerik on 19-Jul-16.
 */
public class Forecast implements Serializable{

    private String phenomenon;
    private int    tempmin;
    private int    tempmax;
    private String text;
    private ArrayList<Integer> windSpeedList = new ArrayList<>();

    public String getPhenomenon() {
        return phenomenon;
    }

    public void setPhenomenon(String phenomenon) {
        this.phenomenon = phenomenon;
    }

    public int getTempmin() {
        return tempmin;
    }

    public void setTempmin(int tempmin) {
        this.tempmin = tempmin;
    }

    public int getTempmax() {
        return tempmax;
    }

    public void setTempmax(int tempmax) {
        this.tempmax = tempmax;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public void addWindSpeedToList(int speed){
        windSpeedList.add(speed);
        Collections.sort(windSpeedList);
    }



    public int getWindSpeedMin() {
        if(windSpeedList.size() > 0 ){
            return windSpeedList.get(0);
        }else{
            return -1;
        }
    }

    public int getWindSpeedMax() {
        if(windSpeedList.size() > 0){
            return windSpeedList.get(windSpeedList.size() -1 );
        }else{
            return -1;
        }
    }
}
