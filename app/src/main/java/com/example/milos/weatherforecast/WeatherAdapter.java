package com.example.milos.weatherforecast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WeatherAdapter extends ArrayAdapter<WeatherModel> {
    private Context context;

    WeatherAdapter(Context context, int resource, ArrayList<WeatherModel> obj) {
        super(context, resource, obj);
        this.context = context;
    }

    @Nullable
    @Override
    public WeatherModel getItem(int position) {
        return super.getItem(position);
    }

    @SuppressLint("SetTextI18n")
    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        //Initialize the helper class
        final ViewHolder viewHolder;
        /*
          If convertView is not set, inflate the row layout and get its views' references
          then set the helper class as a tag for the convertView
        */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.weather_one_item, null);
            viewHolder = new ViewHolder();

            viewHolder.temp = convertView.findViewById(R.id.temp);
            viewHolder.pressure = convertView.findViewById(R.id.pressure);
            viewHolder.humidity = convertView.findViewById(R.id.humidity);
            viewHolder.minTemp = convertView.findViewById(R.id.minTemp);
            viewHolder.maxTemp = convertView.findViewById(R.id.maxTemp);
            convertView.setTag(viewHolder);
        }
         /*
          If convertView already exists, just get the tag and set it in the viewHolder attribute
        */
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final WeatherModel weatherModel = getItem(position);

        assert weatherModel != null;

        viewHolder.temp.setText(getContext().getText(R.string.temperature) + weatherModel.getTemp());
        viewHolder.pressure.setText(getContext().getText(R.string.pressure) + weatherModel.getPressure());
        viewHolder.humidity.setText(getContext().getText(R.string.humidity)+ weatherModel.getHumidity());
        viewHolder.minTemp.setText(getContext().getText(R.string.min_temperature) + weatherModel.getMinTemp());
        viewHolder.maxTemp.setText(getContext().getText(R.string.max_temperature) + weatherModel.getMaxTemp());

        return convertView;
    }

    private static class ViewHolder {
        /* This is an helper class used to save
         *  each component of the listView row layout */
        TextView temp;
        TextView pressure;
        TextView humidity;
        TextView minTemp;
        TextView maxTemp;
    }
}
