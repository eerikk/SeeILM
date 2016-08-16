package com.lionsandwich.seeilm.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lionsandwich.seeilm.R;
import com.lionsandwich.seeilm.utils.Constants;
import com.lionsandwich.seeilm.utils.Translator;
import com.lionsandwich.seeilm.weather.DailyWeather;
import com.lionsandwich.seeilm.weather.DayForecast;
import com.lionsandwich.seeilm.weather.Forecast;
import com.lionsandwich.seeilm.weather.NightForecast;
import com.lionsandwich.seeilm.weather.TownWeather;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;

import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import butterknife.BindView;
import butterknife.ButterKnife;


public class LoadXMLActivity extends AppCompatActivity {

    @BindView(R.id.loadLayout)              LinearLayout loadLayout;
    @BindView(R.id.progressBar)             ProgressBar progressBar;
    @BindView(R.id.somethingWrongTextView)  TextView somethingWrongTextView;

    private String URL = "http://www.ilmateenistus.ee/ilma_andmed/xml/forecast.php";

    NodeList forecastList;
    Context context;
    private ArrayList<DailyWeather> dailyWeatherList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_xml);
        ButterKnife.bind(this);
        context = this;
        if(isNetworkAvailable()){
            new loadXML().execute(URL);
        }else{
            noInternet();
        }
    }


    private class loadXML extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... Url) {
            try {
                handler.postDelayed(runnable, 5000);
                URL url = new URL(Url[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory
                        .newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();
                //forecasts for every day
                forecastList = doc.getElementsByTagName(getString(R.string.tag_forecast_string));

            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void args) {
            handler.postDelayed(runnable, 5000);
            if (forecastList != null) {
                for (int day = 0; day < forecastList.getLength(); day++) {
                    DailyWeather dailyWeather = new DailyWeather();
                    Node dayForecast = forecastList.item(day);
                    //add date to dailyWeather
                    setForecastDate(dailyWeather, dayForecast);
                    //forecast of day and night
                    NodeList nightdayList = dayForecast.getChildNodes();
                    //work through day and night (day & night)
                    workThroughDayNight(nightdayList, dailyWeather);

                    mergeTownDayNightData(dailyWeather);

                    dailyWeatherList.add(dailyWeather);
                }
                startActivity(createIntent());
            }
        }

    }



    // set the date for specific forecast
    private void setForecastDate(DailyWeather dw, Node node){
        String dateString =  node.getAttributes().getNamedItem(getString(R.string.tag_forecast_attr_date_string)).getTextContent();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(dateString);
            dw.setDate(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    //inside forecast work separately thorugh night and day
    private void workThroughDayNight(NodeList nightdayList, DailyWeather dailyWeather) {
        for (int dayORnight = 0; dayORnight < nightdayList.getLength(); dayORnight++) {
            Node nodeInside = nightdayList.item(dayORnight);
            NodeList forecastInfoList = nodeInside.getChildNodes(); //
            setDailyWeather(dailyWeather, nodeInside.getNodeName(), forecastInfoList);


        }
    }

    // get all the info for a specific date, divided to day & night
    private void setDailyWeather(DailyWeather dw, String type, NodeList nodeList){
        NightForecast nightForecast = new NightForecast();
        DayForecast   dayForecast   = new DayForecast();
        switch (type) {
            case "night":
                setForecastInfo(dw, nodeList, nightForecast);
                dw.setNightForecast(nightForecast);
                break;
            case "day":
                setForecastInfo(dw, nodeList, dayForecast);
                dw.setDayForecast(dayForecast);
                break;
        }

    }

    // set  phenomenon, temperatures, and weather description to a specific date
    private void setForecastInfo(DailyWeather dw, NodeList nodeList, Forecast forecast){
        for (int el = 0; el < nodeList.getLength(); el++) {
            Node specificNode = nodeList.item(el);

            specificNode.getParentNode().getNodeName();

            String phenomenon = getNode(Constants.PHENOMENON, specificNode);
            String tempMin    = getNode(Constants.TEMP_MIN, specificNode);
            String tempMax    = getNode(Constants.TEMP_MAX, specificNode);
            String text       = getNode(Constants.TEXT, specificNode);


            if(phenomenon != null){
                forecast.setPhenomenon(Translator.translatePhen(phenomenon,context));
            }
            else if(tempMin != null){
                forecast.setTempmin(Integer.parseInt(tempMin));
            }
            else if(tempMax != null){
                forecast.setTempmax(Integer.parseInt(tempMax));
            }
            else if(tempMin != null){
                forecast.setPhenomenon(text);
            }
            else if(text != null){
                forecast.setText(text);
            }
            else if(specificNode.getChildNodes().getLength() > 1){

                setWindTownAttributes(forecast,dw, specificNode.getNodeName(),specificNode.getParentNode().getNodeName(), specificNode.getChildNodes());
            }
        }
    }


    // inside forecast start setting up towns and wind info
    private void setWindTownAttributes(Forecast forecast,DailyWeather dw, String type,String parentType, NodeList nodeList){
        switch(type){
            case "place":
                setTownAttributes(dw, parentType, nodeList);
                break;
            case "wind" :
                setWindInfo(forecast, nodeList);
        }
    }

    // add all the towns info to a specific date
    private void setTownAttributes(DailyWeather dailyWeather, String type, NodeList nodeList){
        TownWeather town = new TownWeather();
        town.setDate(dailyWeather.getDate());
        switch(type){
            case "night":
                for(int el = 0; el <nodeList.getLength() ; el++){
                    Node attributeNode = nodeList.item(el);

                    String townName = getNode(Constants.NAME, attributeNode);
                    String townPhen = getNode(Constants.PHENOMENON, attributeNode);
                    String townTempMin = getNode(Constants.TEMP_MIN, attributeNode);
                    if(townName != null){
                        town.setName(townName);
                    }
                    else if(townPhen != null){
                        town.setNightPhenomenon(townPhen);
                    }
                    else if(townTempMin != null){
                        town.setTempMin(Integer.parseInt(townTempMin));
                    }
                }
                break;
            case "day":
                for(int el = 0; el <nodeList.getLength() ; el++){
                    Node attributeNode = nodeList.item(el);
                    String townName = getNode(Constants.NAME, attributeNode);
                    String townPhen = getNode(Constants.PHENOMENON, attributeNode);
                    String townTempMax = getNode(Constants.TEMP_MAX, attributeNode);
                    if(townName != null){
                        town.setName(townName);
                    }
                    else if(townPhen != null){
                        town.setDayPhenomenon(townPhen);
                    }
                    else if(townTempMax != null){
                        town.setTempMax(Integer.parseInt(townTempMax));
                    }
                }

                break;
        }
        dailyWeather.addToTownWeatherList(town);



    }
    //add wind info to specific date
    private void setWindInfo(Forecast forecast, NodeList nodeList){
        for(int el = 0; el < nodeList.getLength() ; el ++) {
            Node attributeNode = nodeList.item(el);

            String windSpeedMin = getNode(Constants.WINDSPEED_MIN, attributeNode);
            String windSpeedMax = getNode(Constants.WINDSPEED_MAX, attributeNode);

            if(windSpeedMin != null){
                forecast.addWindSpeedToList(Integer.parseInt(windSpeedMin));
            }
            else if(windSpeedMax != null){
                forecast.addWindSpeedToList(Integer.parseInt(windSpeedMax));
            }
        }
    }

    //merge towns info gathered from day and night into one
    private void mergeTownDayNightData(DailyWeather dailyWeather){
        for(TownWeather tw : dailyWeather.getTownWeatherList()) {
            for (TownWeather tw2 : dailyWeather.getTownWeatherList()) {
                if (tw != tw2 && tw.getName().equalsIgnoreCase(tw2.getName())) {
                    tw.setTempMax(tw2.getTempMax());
                    tw.setDayPhenomenon(tw2.getDayPhenomenon());
                }
            }
        }
        Iterator<TownWeather> it = dailyWeather.getTownWeatherList().iterator();
        while (it.hasNext()) {
            TownWeather tw = it.next();
            if (tw.getNightPhenomenon() == null || tw.getDayPhenomenon()== null) {
                it.remove();
            }
        }
    }

    //create intent to display info
    private Intent createIntent(){
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Constants.DAY_FORECASTS, dailyWeatherList);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }


    //get node with specific tag
    private String getNode(String tag, Node node) {
        if (tag.equalsIgnoreCase(node.getNodeName())) {
            return node.getTextContent();
        } else {
            return null;
        }
    }
    // timer when problems fetching info
    final private Handler handler = new Handler();
    final private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(forecastList == null){
                somethingWrongTextView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                loadLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reloadPage();
                    }
                });
            }
        }
    };

    private void reloadPage(){
        somethingWrongTextView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        if(isNetworkAvailable()){
            new loadXML().execute(URL);
        }else{
            noInternet();

        }
    }

    private void noInternet(){
        somethingWrongTextView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        somethingWrongTextView.setText(R.string.no_internet_string);
        loadLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reloadPage();
            }
        });
    }

    // check if connected to internet
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }





}
