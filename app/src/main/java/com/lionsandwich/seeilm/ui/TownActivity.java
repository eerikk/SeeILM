package com.lionsandwich.seeilm.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lionsandwich.seeilm.R;
import com.lionsandwich.seeilm.utils.Constants;
import com.lionsandwich.seeilm.utils.GetImage;
import com.lionsandwich.seeilm.utils.NumberToWord;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Eerik on 21-Jul-16.
 */
public class TownActivity extends AppCompatActivity {


    @BindView(R.id.townNameTextView)
    TextView townNameTextView;
    @BindView(R.id.townTempTextView)
    TextView townTempTextView;
    @BindView(R.id.townDayPhenTextView)
    TextView townDayPhenTextView;
    @BindView(R.id.townNightPhenTextView)
    TextView townNightPhenTextView;
    @BindView(R.id.townDateTextView)
    TextView townDateTextView;
    @BindView(R.id.townTempDiffTextView)
    TextView townTempDiffTextView;
    @BindView(R.id.townWordTempTextView)
    TextView townWordTempTextView;
    @BindView(R.id.clickLayout)
    RelativeLayout clickLayout;
    @BindView(R.id.townNightPhenImageView)
    ImageView townNightPhenImageView;
    @BindView(R.id.townDayPhenImageView)
    ImageView townDayPhenImageView;


    private String townName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_town_weather);
        ButterKnife.bind(this);
        final Intent intent = getIntent();
        final int pagerPos = intent.getIntExtra(Constants.POS, 0);

        if (intent.getBooleanExtra(Constants.CACHE, false)) {
            setCachedTownView(intent, pagerPos);
        } else {
            setTownView(intent);
        }
        townNameTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickLayout.setVisibility(View.VISIBLE);
                onTownClick(pagerPos);
            }
        });
        townNameTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                clickLayout.setVisibility(View.GONE);
                return false;
            }

        });


    }


    private void setTownView(Intent intent) {

        String townTemp = intent.getIntExtra(Constants.TEMP, 0) + "°C";
        String townTempDiffText = intent.getIntExtra(Constants.TEMP_MIN, 0) + "..." + intent.getIntExtra(Constants.TEMP_MAX, 0) + " °C";
        String tempText = NumberToWord.converter(intent.getIntExtra(Constants.TEMP_MIN, 0)) + " kuni " + NumberToWord.converter(intent.getIntExtra(Constants.TEMP_MAX, 0)) + " kraadi";
        townName = intent.getStringExtra(Constants.NAME);

        townNameTextView.setText(townName);
        townDateTextView.setText(intent.getStringExtra(Constants.DATE));
        townTempTextView.setText(townTemp);
        townTempDiffTextView.setText(townTempDiffText);
        townWordTempTextView.setText(tempText);
        townDayPhenTextView.setText(intent.getStringExtra(Constants.DAY + Constants.PHENOMENON));
        townNightPhenTextView.setText(intent.getStringExtra(Constants.NIGHT + Constants.PHENOMENON));

        townDayPhenImageView.setBackgroundResource(GetImage.getPhenImage(TownActivity.this, Constants.DAY, intent.getStringExtra(Constants.DAY + Constants.PHENOMENON)));
        townNightPhenImageView.setBackgroundResource(GetImage.getPhenImage(TownActivity.this, Constants.NIGHT, intent.getStringExtra(Constants.NIGHT + Constants.PHENOMENON)));

    }

    private void setCachedTownView(Intent intent, int pagerPos) {
        String cacheTown = intent.getStringExtra(Constants.TOWN_CACHE);
        TownsListDialog dialog = new TownsListDialog(TownActivity.this, pagerPos, cacheTown, MainActivity.getDailyWeather());
        setValues(dialog);
    }

    private void onTownClick(int pagerPos) {
        final TownsListDialog townsDialog = new TownsListDialog(TownActivity.this, pagerPos, MainActivity.getDailyWeather());
        townsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                setValues(townsDialog);
            }
        });
    }


    private void setValues(TownsListDialog townsDialog) {
        townName = TownsListDialog.getTownName();
        townNameTextView.setText(townName);

        townDateTextView.setText(townsDialog.getTownDate());

        String townTemp = townsDialog.getTownTemp() + "°C";

        townTempTextView.setText(townTemp);

        String townTempText = townsDialog.getTownMinTemp() + "..." + townsDialog.getTownMaxTemp() + " °C";
        townTempDiffTextView.setText(townTempText);

        townWordTempTextView.setText(townsDialog.getTownTempWords());

        townDayPhenTextView.setText(townsDialog.getTownDayPhen());
        townNightPhenTextView.setText(townsDialog.getTownNightPhen());

        townDayPhenImageView.setBackgroundResource(GetImage.getPhenImage(TownActivity.this, Constants.DAY, townsDialog.getTownDayPhen()));
        townNightPhenImageView.setBackgroundResource(GetImage.getPhenImage(TownActivity.this, Constants.NIGHT, townsDialog.getTownNightPhen()));
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences parsCache = getSharedPreferences(Constants.CACHE, this.MODE_PRIVATE);
        parsCache.edit().putString(Constants.TOWN_CACHE, townName).commit();
    }
}
