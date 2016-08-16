package com.lionsandwich.seeilm.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.lionsandwich.seeilm.R;
import com.lionsandwich.seeilm.adapters.TownAdapter;
import com.lionsandwich.seeilm.utils.Constants;
import com.lionsandwich.seeilm.utils.NumberToWord;
import com.lionsandwich.seeilm.utils.Translator;
import com.lionsandwich.seeilm.weather.DailyWeather;
import com.lionsandwich.seeilm.weather.TownWeather;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Eerik on 21-Jul-16.
 */
public class TownsListDialog extends Dialog {

    private Context context;
    private static ArrayList<DailyWeather> dayForecastList;
    private int pagerPos = 0;


    private static String townName;
    private static int townTemp;
    private static String townTempWords;
    private static int townMinTemp;
    private static int townMaxTemp;
    private static String townDate;
    private static String townDayPhen;
    private static String townNightPhen;

    // come here when town is first chosen
    public TownsListDialog(Context context, ArrayList<DailyWeather> dayForecastList, int pagerPos) {
        super(context);
        this.context = context;
        this.pagerPos = pagerPos;
        this.dayForecastList = dayForecastList;
        setContentView(R.layout.dialog_choose_town);
        ListView townsListView = (ListView) findViewById(R.id.townsListView);
        TownAdapter adapter = new TownAdapter(context, dayForecastList.get(0).getTownWeatherList());
        townsListView.setAdapter(adapter);
        townsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onTownClick(position);
                dismiss();
            }
        });
        show();
    }

    //come here when you choose new town from town list
    public TownsListDialog(Context context, int pagerPos, ArrayList<DailyWeather> dayForecastList) {
        super(context);
        this.context = context;
        this.pagerPos = pagerPos;
        this.dayForecastList = dayForecastList;
        setContentView(R.layout.dialog_choose_town);
        ListView townsListView = (ListView) findViewById(R.id.townsListView);
        TownAdapter adapter = new TownAdapter(context, dayForecastList.get(0).getTownWeatherList());
        townsListView.setAdapter(adapter);
        townsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                setTownValues(position);
                dismiss();
            }
        });
        show();
    }

    //come here when app has cached town
    public TownsListDialog(Context context, int pagerPos, String cacheTown, ArrayList<DailyWeather> dayForecastList) {
        super(context);
        this.context = context;
        this.dayForecastList = dayForecastList;
        this.pagerPos = pagerPos;
        setTownValues(cacheTown);
    }

    private void onTownClick(int position) {
        Intent intent = createIntent(position);
        context.startActivity(intent);
    }

    private void setTownValues(int position) {
        DailyWeather dw = dayForecastList.get(pagerPos);
        if (dw.getTownWeatherList().size() > 0) {
            addValues(dw, position);
        } else {
            dw = dayForecastList.get(0);
            addValues(dw, position);
            Toast.makeText(context, dw.getTownWeatherList().get(position).getName() + " ilmaennustus on ainult homse kohta", Toast.LENGTH_LONG).show();
        }
    }

    private void setTownValues(String cacheTown) {
        DailyWeather dw = dayForecastList.get(pagerPos);
        if (dw.getTownWeatherList().size() > 0) {
            addValues(dw, cacheTown);
        } else {
            dw = dayForecastList.get(0);
            addValues(dw, cacheTown);

            Toast.makeText(context, cacheTown + " ilmaennustus on ainult homse kohta", Toast.LENGTH_LONG).show();
        }
    }


    private void addValues(DailyWeather dw, int position) {
        TownWeather tw = dw.getTownWeatherList().get(position);
        addValuesToVariables(tw);
    }

    private void addValues(DailyWeather dw, String cacheTown) {
        for (TownWeather tw : dw.getTownWeatherList()) {
            if (tw.getName().equalsIgnoreCase(cacheTown)) {
                addValuesToVariables(tw);
            }
        }
    }

    private void addValuesToVariables(TownWeather tw) {
        townName = tw.getName();
        townTemp = (tw.getTempMin() + tw.getTempMax()) / 2;
        townTempWords = NumberToWord.converter(tw.getTempMin()) + " kuni " + NumberToWord.converter(tw.getTempMax()) + " kraadi";
        townDate = createDate(tw.getDate());
        townMinTemp = tw.getTempMin();
        townMaxTemp = tw.getTempMax();
        townDayPhen = Translator.translatePhen(tw.getDayPhenomenon(), context);
        townNightPhen = Translator.translatePhen(tw.getNightPhenomenon(), context);
    }


    private Intent createIntent(int position) {
        Intent intent = new Intent(context, TownActivity.class);
        DailyWeather dw = dayForecastList.get(pagerPos);
        createDate(dw.getDate());
        intent.putExtra(Constants.POS, pagerPos);
        if (dw.getTownWeatherList().size() > 0) {
            putIntents(intent, dw, position);
        } else {
            dw = dayForecastList.get(0);
            putIntents(intent, dw, position);
            Toast.makeText(context, dw.getTownWeatherList().get(position).getName() + " ilmaennustus on ainult homse kohta", Toast.LENGTH_LONG).show();
        }


        return intent;
    }

    private void putIntents(Intent intent, DailyWeather dw, int position) {
        intent.putExtra(Constants.NAME, dw.getTownWeatherList().get(position).getName());
        intent.putExtra(Constants.DATE, createDate(dw.getDate()));
        intent.putExtra(Constants.DAY + Constants.PHENOMENON, Translator.translatePhen(dw.getTownWeatherList().get(position).getDayPhenomenon(), context));
        intent.putExtra(Constants.NIGHT + Constants.PHENOMENON, Translator.translatePhen(dw.getTownWeatherList().get(position).getNightPhenomenon(), context));

        int townTemp = (dw.getTownWeatherList().get(position).getTempMin() + dw.getTownWeatherList().get(position).getTempMax()) / 2;
        intent.putExtra(Constants.TEMP, townTemp);

        intent.putExtra(Constants.TEMP_MIN, dw.getTownWeatherList().get(position).getTempMin());
        intent.putExtra(Constants.TEMP_MAX, dw.getTownWeatherList().get(position).getTempMax());
    }


    private String createDate(Date date) {
        String month = (String) android.text.format.DateFormat.format("MM", date);
        String dayOfWeek = (String) android.text.format.DateFormat.format("EEEE", date);
        String day = (String) android.text.format.DateFormat.format("dd", date);
        dayOfWeek = Translator.translateDayOfWeek(dayOfWeek);

        return dayOfWeek + ", " + day + "." + month;
    }

    private String createDayOfWeek(Date date) {
        String dayOfWeek = (String) android.text.format.DateFormat.format("EEEE", date);
        dayOfWeek = Translator.translateDayOfWeek(dayOfWeek);
        return dayOfWeek;

    }


    public static String getTownName() {
        return townName;
    }

    public static int getTownMinTemp() {
        return townMinTemp;
    }

    public static int getTownMaxTemp() {
        return townMaxTemp;
    }

    public static String getTownDate() {
        return townDate;
    }

    public static String getTownDayPhen() {
        return townDayPhen;
    }

    public static String getTownNightPhen() {
        return townNightPhen;
    }

    public static int getTownTemp() {
        return townTemp;
    }

    public static String getTownTempWords() {
        return townTempWords;
    }
}
