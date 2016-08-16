package com.lionsandwich.seeilm.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lb.auto_fit_textview.AutoResizeTextView;
import com.lionsandwich.seeilm.R;
import com.lionsandwich.seeilm.utils.Constants;
import com.lionsandwich.seeilm.utils.GetImage;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Eerik on 20-Jul-16.
 */
public class DailyWeatherFragment extends Fragment {

    @BindView(R.id.currentDayTextView)      TextView           currentDayTextView;

    @BindView(R.id.nightTempTextView)       TextView nightTempTextView;
    @BindView(R.id.nightTempDiffTextView)   TextView nightTempDiffTextView;
    @BindView(R.id.nightTextTempTextView)   TextView nightTextTempTextView;
    @BindView(R.id.nightWindTextView)       TextView nightWindTextView;
    @BindView(R.id.nightPhenTextView)       TextView nightPhenomenonTextView;
    @BindView(R.id.nightPhenImageView)      ImageView nightPhenImageView;
    @BindView(R.id.nightMoreInfoButton)     Button    nightMoreInfoButton;

    @BindView(R.id.dayTempTextView)       TextView dayTempTextView;
    @BindView(R.id.dayTempDiffTextView)   TextView dayTempDiffTextView;
    @BindView(R.id.dayTextTempTextView)   TextView dayTextTempTextView;
    @BindView(R.id.dayWindTextView)       TextView dayWindTextView;
    @BindView(R.id.dayPhenTextView)       TextView dayPhenomenonTextView;
    @BindView(R.id.dayPhenImageView)      ImageView dayPhenImageView;
    @BindView(R.id.dayMoreInfoButton)     Button    dayMoreInfoButton;



    @BindView(R.id.dayWindLayout)       RelativeLayout dayWindLayout;
    @BindView(R.id.nightWindLayout)     RelativeLayout nightWindLayout;


    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dailyweather, container, false);
        ButterKnife.bind(this, view);

        currentDayTextView.setText(getArguments().getString(Constants.DATE));

        setDayElements();
        setNightElements();

        return view;

    }



    private void setDayElements() {
        dayTempTextView         .setText(getArguments().getString(Constants.DAY + Constants.TEMP));
        dayTempDiffTextView     .setText(getArguments().getString(Constants.DAY + Constants.TEMP_NR));
        dayTextTempTextView     .setText(getArguments().getString(Constants.DAY + Constants.TEMP_WORD));
        dayPhenomenonTextView   .setText(getArguments().getString(Constants.DAY + Constants.PHENOMENON));
        dayMoreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMoreClick(Constants.DAY);
            }
        });

        dayPhenImageView.setBackgroundResource(GetImage.getPhenImage(getContext(), Constants.DAY, getArguments().getString(Constants.DAY + Constants.PHENOMENON)));

        if(getArguments().getString(Constants.DAY + Constants.WIND_TEXT) == null){
            dayWindLayout.setVisibility(View.GONE);
        }else{
            dayWindTextView.setText(getArguments().getString(Constants.DAY + Constants.WIND_TEXT));

        }
    }

    private void setNightElements() {
        nightTempTextView         .setText(getArguments().getString(Constants.NIGHT + Constants.TEMP));
        nightTempDiffTextView     .setText(getArguments().getString(Constants.NIGHT + Constants.TEMP_NR));
        nightTextTempTextView     .setText(getArguments().getString(Constants.NIGHT + Constants.TEMP_WORD));
        nightPhenomenonTextView   .setText(getArguments().getString(Constants.NIGHT + Constants.PHENOMENON));

        nightMoreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMoreClick(Constants.NIGHT);
            }
        });

        nightPhenImageView.setBackgroundResource(GetImage.getPhenImage(getContext(), Constants.NIGHT, getArguments().getString(Constants.NIGHT + Constants.PHENOMENON)));

        if(getArguments().getString(Constants.NIGHT + Constants.WIND_TEXT) == null){
            nightWindLayout.setVisibility(View.GONE);
        }else{
            nightWindTextView       .setText(getArguments().getString(Constants.NIGHT + Constants.WIND_TEXT));

        }
    }



    private void onMoreClick(String day) {
        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_more_info);
        TextView weatherInfoTextView   = (TextView) dialog.findViewById(R.id.weatherInfoTextView);
        ImageView weatherInfoImageView = (ImageView)dialog.findViewById(R.id.moreInfoImage);
        weatherInfoTextView.setText(getArguments().getString(day + Constants.WEATHER_DESC));
        weatherInfoImageView.setBackgroundResource(GetImage.getPhenImage(getContext(), day, getArguments().getString(day + Constants.PHENOMENON)));
        dialog.show();
    }
}
