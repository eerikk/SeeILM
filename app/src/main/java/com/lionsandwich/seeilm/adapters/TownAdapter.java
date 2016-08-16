package com.lionsandwich.seeilm.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lionsandwich.seeilm.R;
import com.lionsandwich.seeilm.weather.TownWeather;

import java.util.List;

/**
 * Created by Eerik on 21-Jul-16.
 */
public class TownAdapter extends ArrayAdapter<TownWeather>{

        protected Context context;
        protected List<TownWeather> towns;
        protected ViewHolder holder;

        public TownAdapter(Context context, List<TownWeather> towns){
            super(context, R.layout.item_town, towns);

            this.context = context;
            this.towns = towns;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.item_town, null);
                holder = new ViewHolder();
                holder.townName = (TextView) convertView.findViewById(R.id.townTextView);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
            holder.townName.setText(towns.get(position).getName());
            return convertView;
        }


        private static class ViewHolder {
            TextView         townName;
        }




}
