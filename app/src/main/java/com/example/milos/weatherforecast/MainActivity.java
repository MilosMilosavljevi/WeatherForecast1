package com.example.milos.weatherforecast;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Context context;
    private ListView lvOne, lvFive;
    private EditText search;
    private TextView cityName;
    private WeatherAdapter weatherAdapter;
    private WeatherFiveAdapter weatherFiveAdapter;

    ArrayList<WeatherModel> weatherList = new ArrayList<>();
    ArrayList<WeatherFiveModel> weatherFiveModels = new ArrayList<>();

    public final String baseURL = "https://api.openweathermap.org/data/2.5/weather?q=Belgrade&APPID=f492a665bb45d06844c4ada25bba7f58";
    public final String baseURLFive = "https://api.openweathermap.org/data/2.5/forecast?q=Belgrade&APPID=f492a665bb45d06844c4ada25bba7f58";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        lvOne = (ListView) findViewById(R.id.listjsonone);
        lvFive = (ListView) findViewById(R.id.listjsonfive);
        search = (EditText) findViewById(R.id.search);
        cityName = (TextView) findViewById(R.id.city);

        final RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, baseURL, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                getJsonInfo(response);
                // Updating parsed JSON data into ListView
                weatherAdapter = new WeatherAdapter(context, R.layout.weather_one_item, weatherList);
                weatherAdapter.notifyDataSetChanged();
                lvOne.setAdapter(weatherAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(jsonObjectRequest);

        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            private Timer timer = new Timer();
            private final long DELAY = 500; // milliseconds

            @Override
            public void afterTextChanged(Editable s) {
                //if there is now text on search bar cursor is not visible
                if (s.length() > 0) {
                    search.setCursorVisible(true);
                } else {
                    search.setCursorVisible(false);
                }

                final String tags = baseURL.replace("Belgrade", s.toString());
                final String tagsFive = baseURLFive.replace("Belgrade", s.toString());

                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, tags, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                weatherList.clear();
                                getJsonInfo(response);

                                // Updating parsed JSON data into ListView
                                weatherAdapter = new WeatherAdapter(context, R.layout.weather_one_item, weatherList);
                                weatherAdapter.notifyDataSetChanged();
                                lvOne.setAdapter(weatherAdapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        requestQueue.add(jsonObjectRequest);

                        JsonObjectRequest jsonObjectRequestFive = new JsonObjectRequest(Request.Method.GET, tagsFive, null, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                weatherFiveModels.clear();
                                getJsonInfoFive(response);
                                // Updating parsed JSON data into ListView
                                weatherFiveAdapter = new WeatherFiveAdapter(context, R.layout.weather_five_item, weatherFiveModels);
                                weatherFiveAdapter.notifyDataSetChanged();
                                lvFive.setAdapter(weatherFiveAdapter);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                            }
                        });
                        requestQueue.add(jsonObjectRequestFive);
                    }
                }, DELAY);
            }
        });

        JsonObjectRequest jsonObjectRequestFive = new JsonObjectRequest(Request.Method.GET, baseURLFive, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                getJsonInfoFive(response);
                // Updating parsed JSON data into ListView
                weatherFiveAdapter = new WeatherFiveAdapter(context, R.layout.weather_five_item, weatherFiveModels);
                weatherFiveAdapter.notifyDataSetChanged();
                lvFive.setAdapter(weatherFiveAdapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(jsonObjectRequestFive);
    }

    public void getJsonInfo(JSONObject object) {
        try {

            WeatherModel model = new WeatherModel();

            JSONObject main = object.getJSONObject("main");

            String temp = main.getString("temp");
            model.setTemp(temp);

            String pressure = main.getString("pressure");
            model.setPressure(pressure);

            String humidity = main.getString("humidity");
            model.setHumidity(humidity);

            String minTemp = main.getString("temp_min");
            model.setMinTemp(minTemp);

            String maxTemp = main.getString("temp_max");
            model.setMaxTemp(maxTemp);

            weatherList.add(model);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getJsonInfoFive(JSONObject object) {
        try {

            JSONArray list = object.getJSONArray("list");
            for (int i = 0; i < list.length(); i++) {

                WeatherFiveModel model = new WeatherFiveModel();

                JSONObject jsonObject = list.getJSONObject(i);

                JSONObject main = jsonObject.getJSONObject("main");

                String temp = main.getString("temp");
                model.setTempFive(temp);

                String pressure = main.getString("pressure");
                model.setPressureFive(pressure);

                String humidity = main.getString("humidity");
                model.setHumidityFive(humidity);

                String minTemp = main.getString("temp_min");
                model.setMinTempFive(minTemp);

                String maxTemp = main.getString("temp_max");
                model.setMaxTempFive(maxTemp);
                weatherFiveModels.add(model);
            }

            JSONObject city = object.getJSONObject("city");
            String cityFive = city.getString("name");
            for (WeatherFiveModel model : weatherFiveModels) {
                model.setCityNameFive(cityFive);
                cityName.setText(model.getCityNameFive());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


