package com.lionsandwich.seeilm.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.lionsandwich.seeilm.R;
import com.lionsandwich.seeilm.adapters.ViewPagerAdapter;
import com.lionsandwich.seeilm.utils.Constants;
import com.lionsandwich.seeilm.view.SlidingTabLayout;
import com.lionsandwich.seeilm.weather.DailyWeather;
import com.lionsandwich.seeilm.weather.TownWeather;

import java.util.ArrayList;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Eerik on 20-Jul-16.
 */
public class MainActivity extends AppCompatActivity {


    @BindView(R.id.toTownsButton)
    Button toTownsButton;


    private static ViewPager pager;

    private static ArrayList<DailyWeather> dayForecastList = new ArrayList<>();
    private String cacheTown;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        dayForecastList = (ArrayList<DailyWeather>) intent.getSerializableExtra(Constants.DAY_FORECASTS);
        createDailyForecast();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences parsCache = getSharedPreferences(Constants.CACHE, this.MODE_PRIVATE);
        cacheTown = parsCache.getString(Constants.TOWN_CACHE, "");
        if (!cacheTown.isEmpty()) {
            toTownsButton.setText("ilm " + cacheTown + "s");
        }
        toTownsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onTownClick();

            }
        });
    }

    private void createDailyForecast() {
        ViewPagerAdapter adapter;
        SlidingTabLayout tabs;
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), dayForecastList);
        pager = (ViewPager) findViewById(R.id.dailyForecastViewPager);
        pager.setAdapter(adapter);
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.red);
            }
        });
        tabs.setViewPager(pager);
    }


    private void onTownClick() {
        if (cacheTown.isEmpty()) {
            createTownsDialog();
        } else {
            createCachedTown();
        }
    }


    private void createTownsDialog() {
        new TownsListDialog(MainActivity.this, dayForecastList, pager.getCurrentItem());
    }

    private void createCachedTown() {
        Intent intent = new Intent(MainActivity.this, TownActivity.class);
        for (TownWeather tw : getDailyWeather().get(0).getTownWeatherList()) {
            if (tw.getName().equalsIgnoreCase(cacheTown)) {
                intent.putExtra(Constants.TOWN_CACHE, cacheTown);
            }
        }
        intent.putExtra(Constants.POS, pager.getCurrentItem());
        intent.putExtra(Constants.CACHE, true);
        startActivity(intent);
    }

    public static ArrayList<DailyWeather> getDailyWeather() {
        return dayForecastList;
    }

}
