package com.larry.shugo.model.bean.weather;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherList {

    @SerializedName("HeWeather data service 3.0")
    private List<WeatherData> weatherDatas;

    public List<WeatherData> getWeatherDatas() {
        return weatherDatas;
    }

    public void setWeatherDatas(List<WeatherData> weatherDatas) {
        this.weatherDatas = weatherDatas;
    }
}
