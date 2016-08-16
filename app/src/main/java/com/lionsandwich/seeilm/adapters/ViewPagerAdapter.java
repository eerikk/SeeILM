package com.lionsandwich.seeilm.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;


import com.lionsandwich.seeilm.ui.DailyWeatherFragment;
import com.lionsandwich.seeilm.utils.Constants;
import com.lionsandwich.seeilm.utils.NumberToWord;
import com.lionsandwich.seeilm.utils.Translator;
import com.lionsandwich.seeilm.weather.DailyWeather;

import java.util.ArrayList;


/**
 * Created by Eerik on 5.05.2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private int nrOfDays; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    private ArrayList<String> datesList = new ArrayList<>();
    private ArrayList<DailyWeather> dailyWeatherList = new ArrayList<>();

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapter(FragmentManager fm, ArrayList<DailyWeather> dailyWeatherList ) {
        super(fm);
        this.nrOfDays         = dailyWeatherList.size();
        this.dailyWeatherList = dailyWeatherList;

        getDates();
    }



    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        switch(position){
            default:
                return DailyWeatherFragmentWithData(position);

        }
    }






    // This method return the Number of tabs for the tabs Strip
    public CharSequence getPageTitle(int position) {
        return datesList.get(position);
    }



    @Override
    public int getCount() {
        return nrOfDays;
    }



    private DailyWeatherFragment DailyWeatherFragmentWithData(int position){
        Bundle bundle = new Bundle();
        addDataToBundle(bundle, position);

        DailyWeatherFragment dailyWeatherFragment = new DailyWeatherFragment();
        dailyWeatherFragment.setArguments(bundle);


        return dailyWeatherFragment;
    }



    private void addDataToBundle(Bundle bundle, int position) {
        DailyWeather dw = dailyWeatherList.get(position);
        //date
        String dayOfWeek  = (String) android.text.format.DateFormat.format("EEEE", dw.getDate());
        String currentDay = Translator.translateDayOfWeek(dayOfWeek);

        //day data
        String dayTemp         = Integer.toString((dw.getDayForecast().getTempmin() + dw.getDayForecast().getTempmax()) /2) + "°C";
        String dayTempNr       = dw.getDayForecast().getTempmin() + "..." + dw.getDayForecast().getTempmax() + " °C";
        String dayTempWord     = NumberToWord.converter(dw.getDayForecast().getTempmin()) + " kuni " + NumberToWord.converter(dw.getDayForecast().getTempmax()) + " kraadi";

        String dayWindText = null;
        if(dw.getDayForecast().getWindSpeedMin()> 0){
            dayWindText = dw.getDayForecast().getWindSpeedMin() + "..." + dw.getDayForecast().getWindSpeedMax() + " m/s";
        }

        String dayWeathterDesc = dw.getDayForecast().getText();
        String dayPhen         = dw.getDayForecast().getPhenomenon();

        //night data
        String nightTemp         = Integer.toString((dw.getNightForecast().getTempmin() + dw.getNightForecast().getTempmax()) /2) + "°C";
        String nightTempNr       = dw.getNightForecast().getTempmin() + "..." + dw.getNightForecast().getTempmax() + " °C";
        String nightTempWord     = NumberToWord.converter(dw.getNightForecast().getTempmin()) + " kuni " + NumberToWord.converter(dw.getNightForecast().getTempmax()) + " kraadi";

        String nightWindText = null;
        if(dw.getDayForecast().getWindSpeedMin()> 0){
            nightWindText = dw.getNightForecast().getWindSpeedMin() + "..." + dw.getNightForecast().getWindSpeedMax() + " m/s";
        }

        String nightWeathterDesc = dw.getNightForecast().getText();
        String nightPhen         = dw.getNightForecast().getPhenomenon();


        bundle.putString(Constants.DATE, currentDay);

        bundle.putString(Constants.DAY + Constants.TEMP,      dayTemp);
        bundle.putString(Constants.DAY + Constants.TEMP_NR,   dayTempNr);
        bundle.putString(Constants.DAY + Constants.TEMP_WORD, dayTempWord);
        bundle.putString(Constants.DAY + Constants.WIND_TEXT, dayWindText);
        bundle.putString(Constants.DAY + Constants.WEATHER_DESC, dayWeathterDesc);
        bundle.putString(Constants.DAY + Constants.PHENOMENON  , dayPhen);

        bundle.putString(Constants.NIGHT + Constants.TEMP,      nightTemp);
        bundle.putString(Constants.NIGHT + Constants.TEMP_NR,   nightTempNr);
        bundle.putString(Constants.NIGHT + Constants.TEMP_WORD, nightTempWord);
        bundle.putString(Constants.NIGHT + Constants.WIND_TEXT, nightWindText);
        bundle.putString(Constants.NIGHT + Constants.WEATHER_DESC, nightWeathterDesc);
        bundle.putString(Constants.NIGHT + Constants.PHENOMENON  , nightPhen);
    }


    private void getDates(){
        for(DailyWeather dw : dailyWeatherList){
            String day = (String) android.text.format.DateFormat.format("dd", dw.getDate()); //20
            String intMonth = (String) android.text.format.DateFormat.format("MM", dw.getDate()); //06
            datesList.add(day + "." + intMonth);
        }

    }





}

