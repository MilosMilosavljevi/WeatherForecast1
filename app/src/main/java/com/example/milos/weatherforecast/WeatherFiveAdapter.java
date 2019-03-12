package com.example.milos.weatherforecast;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class WeatherFiveAdapter extends ArrayAdapter<WeatherFiveModel> {
    private Context context;
    private ScaleAnimation scale;

    WeatherFiveAdapter(Context context, int resource, ArrayList<WeatherFiveModel> obj) {
        super(context, resource, obj);
        this.context = context;
    }

    @Nullable
    @Override
    public WeatherFiveModel getItem(int position) {
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
            convertView = inflater.inflate(R.layout.weather_five_item, null);
            viewHolder = new ViewHolder();

            viewHolder.temp = convertView.findViewById(R.id.temp_five);
            viewHolder.pressure = convertView.findViewById(R.id.pressure_five);
            viewHolder.humidity = convertView.findViewById(R.id.humidity_five);
            viewHolder.minTemp = convertView.findViewById(R.id.minTemp_five);
            viewHolder.maxTemp = convertView.findViewById(R.id.maxTemp_five);
            convertView.setTag(viewHolder);
        }
         /*
          If convertView already exists, just get the tag and set it in the viewHolder attribute
        */
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final WeatherFiveModel weatherModel = getItem(position);

        assert weatherModel != null;

        viewHolder.temp.setText(getContext().getText(R.string.temperature) + weatherModel.getTempFive());
        viewHolder.pressure.setText(getContext().getText(R.string.pressure) + weatherModel.getPressureFive());
        viewHolder.humidity.setText(getContext().getText(R.string.humidity) + weatherModel.getHumidityFive());
        viewHolder.minTemp.setText(getContext().getText(R.string.min_temperature) + weatherModel.getMinTempFive());
        viewHolder.maxTemp.setText(getContext().getText(R.string.max_temperature) + weatherModel.getMaxTempFive());


        //starting animation
        animateView();
        convertView.startAnimation(scale);
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

    private void animateView() {
        scale = new ScaleAnimation((float) 1.0, (float) 1.0, (float) 0.0, (float) 1.0);
        scale.setFillAfter(true);
        scale.setDuration(500);
    }
}
